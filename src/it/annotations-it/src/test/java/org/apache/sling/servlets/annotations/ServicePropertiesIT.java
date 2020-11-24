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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.servlets.annotations.services.PathBoundService;
import org.apache.sling.servlets.annotations.services.PathBoundStrictAllOptionsService;
import org.apache.sling.servlets.annotations.services.PathBoundStrictJustPathService;
import org.apache.sling.testing.clients.ClientException;
import org.apache.sling.testing.clients.util.JsonUtils;
import org.codehaus.jackson.JsonNode;
import org.junit.Test;

/** Assuming servlet selection is tested in the servlets resolver module,
 *  it's good enough to verify that the expected OSGi service properties
 *  are set by our annotations - this is what this class does for some of
 *  our test services.
 */
public class ServicePropertiesIT extends AnnotationsTestSupport {
    
    private static String componentPath(Object nameOrId) {
        return "/system/console/components/" + nameOrId + ".json";
    }

    /** Getting service properties is not directly supported by the OsgiConsoleClient */
    private List<String> getServiceProperties(String serviceName) throws ClientException {
        // Need to get the component ID first, then its details
        final List<String> result = new ArrayList<>();
        final JsonNode idInfo = JsonUtils.getJsonNodeFromString(CLIENT.doGet(componentPath(serviceName)).getContent());
        final String id = idInfo.get("data").get(0).get("id").getTextValue();
        assertTrue("Expecting non-null component ID", !id.equals("null"));
        final JsonNode details = JsonUtils.getJsonNodeFromString(CLIENT.doGet(componentPath(id)).getContent());
        final JsonNode data = details.get("data").get(0);
        final JsonNode props = data.get("props");
        for(JsonNode prop : props) {
            if("Properties".equals(prop.get("key").getTextValue())) {
                for(JsonNode value : prop.get("value")) {
                    result.add(value.getTextValue().replaceAll(" ", "").trim());
                }
            }
        }
        return result;
    }

    private void assertProperty(List<String> props, String key, String value) {
        final String pattern = key + "=" + value;
        assertTrue(
            "Expecting " + pattern + " in " + props,
            props.stream().anyMatch(line -> line.equals(pattern))
        );
    }

    private void assertAbsentProperties(List<String> props, String ... keys) {
        for(String key : keys) {
            assertFalse(
                "Expecting " + key + " to be absent in " + props,
                props.stream().anyMatch(line -> line.startsWith(key))
            );
        }
    }

    @Test
    public void testPathBoundService() throws ClientException {
        final List<String> props = getServiceProperties(PathBoundService.class.getName());
        assertProperty(props, "sling.servlet.paths", "[PathBoundServicePath]");
        assertAbsentProperties(
            props,
            "sling.servlet.extensions",
            "sling.servlet.selectors",
            "sling.servlet.methods",
            "sling.servlet.paths.strict"
        );
    }

    @Test
    public void testPathBoundStrictAllOptionsService() throws ClientException {
        final List<String> props = getServiceProperties(PathBoundStrictAllOptionsService.class.getName());
        assertProperty(props, "sling.servlet.paths.strict", "true");
        assertProperty(props, "sling.servlet.paths", "[P1,P2]");
        assertProperty(props, "sling.servlet.extensions", "[E1,E2]");
        assertProperty(props, "sling.servlet.selectors", "[S1,S2]");
        assertProperty(props, "sling.servlet.methods", "[M1,M2]");
    }

    @Test
    public void testPathBoundStrictJustPathService() throws ClientException {
        final List<String> props = getServiceProperties(PathBoundStrictJustPathService.class.getName());
        assertProperty(props, "sling.servlet.paths.strict", "true");
        assertProperty(props, "sling.servlet.paths", "[JustThePath]");
        assertAbsentProperties(
            props,
            "sling.servlet.extensions",
            "sling.servlet.selectors",
            "sling.servlet.methods"
        );
    }
}
