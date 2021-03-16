/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.servlets.annotations;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.testing.clients.osgi.OsgiConsoleClient;
import org.apache.sling.testing.paxexam.TestSupport;
import org.junit.ClassRule;
import org.junit.BeforeClass;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExamServer;
import org.ops4j.pax.exam.options.extra.VMOption;

import org.apache.sling.servlets.annotations.services.PathBoundService;

import static org.apache.sling.testing.paxexam.SlingOptions.slingQuickstartOakTar;
import static org.apache.sling.testing.paxexam.SlingOptions.versionResolver;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

import java.net.URI;

public class AnnotationsTestSupport extends TestSupport {

    private final static int STARTUP_WAIT_SECONDS = 60;
    protected static OsgiConsoleClient CLIENT;
    protected static int httpPort = findFreePort();

    @ClassRule
    public static PaxExamServer serverRule = new PaxExamServer();

    @Configuration
    public Option[] configuration() throws Exception {
        final String vmOpt = System.getProperty("pax.vm.options");
        VMOption vmOption = null;
        if (StringUtils.isNotEmpty(vmOpt)) {
            vmOption = new VMOption(vmOpt);
        }

        final String jacocoOpt = System.getProperty("jacoco.command");
        VMOption jacocoCommand = null;
        if (StringUtils.isNotEmpty(jacocoOpt)) {
            jacocoCommand = new VMOption(jacocoOpt);
        }

        return options(
            when(vmOption != null).useOptions(vmOption),
            when(jacocoCommand != null).useOptions(jacocoCommand),

            // For some reason, Jetty starts first on port 8080 without this
            systemProperty("org.osgi.service.http.port").value(String.valueOf(httpPort)),

            serverBaseConfiguration(),
            slingQuickstartOakTar(workingDirectory(), httpPort),

            testBundle("bundle.filename"),
            mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.webconsole.plugins.ds").version(versionResolver),
            mavenBundle().groupId("org.ops4j.pax.logging").artifactId("pax-logging-api").versionAsInProject(),
            mavenBundle().groupId("org.ops4j.pax.logging").artifactId("pax-logging-logback").versionAsInProject()
        );
    }

    @BeforeClass
    public static void waitForSling() throws Exception {
        final URI url = new URI(String.format("http://localhost:%d", httpPort));
        CLIENT = new OsgiConsoleClient(url, "admin", "admin");
        CLIENT.waitExists("/", STARTUP_WAIT_SECONDS * 1000, 500);
        CLIENT.waitComponentRegistered(PathBoundService.class.getName(), 10 * 1000, 500);

        // Verify stable status for a bit
        for(int i=0; i < 10 ; i++) {
            CLIENT.waitComponentRegistered(PathBoundService.class.getName(), 1000, 100);
            Thread.sleep(100);
        }
    }
}