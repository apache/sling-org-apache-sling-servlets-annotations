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
import org.apache.sling.servlets.annotations.services.PathBoundService;
import org.apache.sling.testing.clients.osgi.OsgiConsoleClient;
import org.apache.sling.testing.paxexam.TestSupport;
import org.junit.Before;
import org.junit.ClassRule;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExamServer;
import org.ops4j.pax.exam.options.extra.VMOption;

import static org.apache.sling.testing.paxexam.SlingOptions.logback;
import static org.apache.sling.testing.paxexam.SlingOptions.slingQuickstartOakTar;
import static org.ops4j.pax.exam.CoreOptions.composite;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

import java.net.URI;

public class AnnotationsTestSupport extends TestSupport {

    private final static int STARTUP_WAIT_SECONDS = 30;

    protected OsgiConsoleClient CLIENT;
    protected static int httpPort;

    @ClassRule
    public static PaxExamServer serverRule = new PaxExamServer();

    public AnnotationsTestSupport() {
        if(httpPort == 0) {
            // findFreePort should probably be a static method
            httpPort = findFreePort();
        }
    }

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

        final String workingDirectory = workingDirectory();

        return composite(
            // TODO not sure why the below list of bundles is different from
            // running tests with PaxExam.class - but this setup works
            //super.baseConfiguration(),

            when(vmOption != null).useOptions(vmOption),
            when(jacocoCommand != null).useOptions(jacocoCommand),

            // For some reason, Jetty starts first on port 8080 without this
            systemProperty("org.osgi.service.http.port").value(String.valueOf(httpPort)),

            slingQuickstartOakTar(workingDirectory, httpPort),
            testBundle("bundle.filename"),

            logback(),
            mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.log").version("1.2.4"),
            mavenBundle().groupId("log4j").artifactId("log4j").version("1.2.17"),
            mavenBundle().groupId("org.apache.aries.spifly").artifactId("org.apache.aries.spifly.dynamic.framework.extension").version("1.3.2"),
            mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.webconsole.plugins.ds").version("2.1.0")
            
        ).getOptions();
    }

    @Before
    public void waitForSling() throws Exception {
        final URI url = new URI(String.format("http://localhost:%d", httpPort));
        CLIENT = new OsgiConsoleClient(url, "admin", "admin");
        CLIENT.waitExists("/", STARTUP_WAIT_SECONDS * 1000, 500);
        CLIENT.waitComponentRegistered(PathBoundService.class.getName(), 10 * 1000, 500);
    }
}
