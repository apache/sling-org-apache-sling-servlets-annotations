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

import java.io.UnsupportedEncodingException;
import java.util.Collections;

import org.apache.http.entity.StringEntity;
import org.apache.sling.testing.clients.ClientException;
import org.junit.Test;

/** Verify servlet selection based on our annotations. This duplicates
 *  some of the servlet resolver tests, see also {@link ServicePropertiesIT}
 *  for another testing method.
 */
public class ServletRegistrationIT extends AnnotationsTestSupport {
    
    @Test
    public void testPathBoundServlet() throws ClientException, UnsupportedEncodingException {
        CLIENT.doGet("/bin/PathBoundServlet", 555);
        CLIENT.doGet("/bin/PathBoundServlet.with.some.selector.and.extension", 555);
        CLIENT.doGet("/bin/PathBoundServlet.with.some.selector.and.extension/suffix", 555);
        // other methods should work as well
        CLIENT.doPut("/bin/PathBoundServlet", new StringEntity("some text"), Collections.emptyList(), 557);
    }
    
    @Test
    public void testPathBoundServletWithFilter() throws ClientException {
        CLIENT.doGet("/bin/PathBoundServlet.html/simplefilter", 556);
        CLIENT.doGet("/bin/PathBoundServlet.with.some.selector.and.extension/simplefilter", 556);
    }

    @Test
    public void testPathBoundServletWithPrefix() throws ClientException {
        CLIENT.doGet("/bin/PathBoundServletWithPrefix", 610);
        CLIENT.doGet("/bin/PathBoundServletWithPrefix.with.some.selector.and.extension", 610);
    }

    @Test
    public void testResourceTypeBoundServlet() throws ClientException, UnsupportedEncodingException {
        CLIENT.doGet("/content/servlettest/resourceTypeBoundServlet", 560);
        CLIENT.doGet("/content/servlettest/resourceTypeBoundServlet.html", 560);
        CLIENT.doGet("/content/servlettest/resourceTypeBoundServlet.json", 560);
        // only GET and HEAD are supposed to be working
        CLIENT.doPut("/content/servlettest/resourceTypeBoundServlet.json", new StringEntity("some text"), Collections.emptyList(), 405);
    }

    @Test
    public void testResourceTypeBoundServletWithPrefix() throws ClientException, UnsupportedEncodingException {
        CLIENT.doGet("/content/servlettest/resourceTypeBoundServletWithPrefix", 590);
        CLIENT.doGet("/content/servlettest/resourceTypeBoundServletWithPrefix.html", 590);
        CLIENT.doGet("/content/servlettest/resourceTypeBoundServletWithPrefix.json", 590);
        // only GET and HEAD are supposed to be working
        CLIENT.doPut("/content/servlettest/resourceTypeBoundServletWithPrefix.json", new StringEntity("some text"), Collections.emptyList(), 405);
    }

    @Test
    public void testResourceTypeBoundServletWithExtension() throws ClientException, UnsupportedEncodingException {
        CLIENT.doGet("/content/servlettest/resourceTypeBoundServletWithExtension", 403); // without extension is a index listing, which is forbidden by default
        CLIENT.doGet("/content/servlettest/resourceTypeBoundServletWithExtension.html", 200); // DEFAULT GET Servlet
        CLIENT.doGet("/content/servlettest/resourceTypeBoundServletWithExtension.ext1", 570);
        CLIENT.doGet("/content/servlettest/resourceTypeBoundServletWithExtension.ext2", 570);
        CLIENT.doPut("/content/servlettest/resourceTypeBoundServletWithExtension.ext2", new StringEntity("some text"), Collections.emptyList(), 571);
        // extension is considered for all methods!
        CLIENT.doPut("/content/servlettest/resourceTypeBoundServletWithExtension.someotherext", new StringEntity("some text"), Collections.emptyList(), 405); // DEFAULT servlet
    }

    @Test
    public void testResourceTypeBoundServletWithSelectors() throws ClientException, UnsupportedEncodingException {
        CLIENT.doGet("/content/servlettest/resourceTypeBoundServletWithSelectors.someext", 404);
        CLIENT.doGet("/content/servlettest/resourceTypeBoundServletWithSelectors.selector1.someext", 404);
        CLIENT.doGet("/content/servlettest/resourceTypeBoundServletWithSelectors.selector3.someext", 600);
        CLIENT.doGet("/content/servlettest/resourceTypeBoundServletWithSelectors.selector1.selector2.someext", 600);
        // some non-registered selector as first selector
        CLIENT.doGet("/content/servlettest/resourceTypeBoundServletWithSelectors.someotherselector.selector1.selector2.someext", 404);
        // some non-registered selector as last selector
        CLIENT.doGet("/content/servlettest/resourceTypeBoundServletWithSelectors.selector1.selector2.someotherselector.someext", 600);
        // only GET and HEAD are supposed to be working
        CLIENT.doPut("/content/servlettest/resourceTypeBoundServletWithSelectors.selector3.someext", new StringEntity("some text"), Collections.emptyList(), 405);
    }

    @Test
    public void testResourceTypeBoundServletWithMethods() throws ClientException, UnsupportedEncodingException {
        CLIENT.doGet("/content/servlettest/resourceTypeBoundServletWithMethods.someext", 404); // DEFAULT Get not triggered due to weird extension
        CLIENT.doPut("/content/servlettest/resourceTypeBoundServletWithMethods.someext", new StringEntity("some text"), Collections.emptyList(), 581);
        CLIENT.doPost("/content/servlettest/resourceTypeBoundServletWithMethods.someext", new StringEntity("some text"), Collections.emptyList(), 582);
    }

    @Test
    public void testInheritingServlets() throws ClientException {
        CLIENT.doGet("/content/servlettest/baseResourceTypeBoundServletWithSelectors", 403); // no selectors passed
        CLIENT.doGet("/content/servlettest/baseResourceTypeBoundServletWithSelectors.someext", 404); // no selectors passed
        CLIENT.doGet("/content/servlettest/baseResourceTypeBoundServletWithSelectors.sel1.someext", 610); // matches
        CLIENT.doGet("/content/servlettest/baseResourceTypeBoundServletWithSelectors.sel2.someext", 610); // matches
        CLIENT.doGet("/content/servlettest/inheritingResourceTypeBoundServlet", 620); // matches
        CLIENT.doGet("/content/servlettest/inheritingResourceTypeBoundServlet.someext", 620); // matches
        CLIENT.doGet("/content/servlettest/inheritingResourceTypeBoundServlet.sel.someext", 620); // matches
        CLIENT.doGet("/content/servlettest/inheritingResourceTypeBoundServlet.sel1.someext", 610); // delegated to Base
        CLIENT.doGet("/content/servlettest/inheritingResourceTypeBoundServlet.sel2.someext", 610); // delegated to Base
    }
}