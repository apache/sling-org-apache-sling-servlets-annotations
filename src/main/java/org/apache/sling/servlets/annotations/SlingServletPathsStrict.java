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

import org.apache.sling.api.servlets.ServletResolverConstants;
import org.osgi.annotation.bundle.Requirement;
import org.osgi.service.component.annotations.ComponentPropertyType;
/**
 * Component Property Type (as defined by OSGi DS 1.4) for Sling Servlets.
 * 
 * Takes care of writing the relevant service properties as being used by the Sling Servlet Resolver ({@link ServletResolverConstants})
 * to register the annotated servlet component as Sling servlet for a specific path, using the strict mode
 * (defined in <a href="https://issues.apache.org/jira/browse/SLING-8110">SLING-8110</a>) which
 * optionally takes into account the request's extension, selectors and HTTP method.
 * 
 * Preferably register Sling servlets by resource type ({@link SlingServletResourceTypes}) though 
 * for reasons outlined at <a href="https://sling.apache.org/documentation/the-sling-engine/servlets.html#caveats-when-binding-servlets-by-path">
 * Caveats when binding servlets by path</a>.
 *
 * @see <a href="https://sling.apache.org/documentation/the-sling-engine/servlets.html">Sling Servlets</a>
 * @see ServletResolverConstants
 * @see <a href="https://github.com/apache/felix/blob/trunk/tools/org.apache.felix.scr.annotations/src/main/java/org/apache/felix/scr/annotations/sling/SlingServlet.java">Felix SCR annotation</a>
 * @see SlingServletPaths
 */

/** Using this requires the SLING-8110 strict mode */
@Requirement(
    namespace="osgi.extender",
    name="org.apache.sling.servlets.resolver", 
    version="1.1")

@ComponentPropertyType
public @interface SlingServletPathsStrict {
    /**
     * Prefix for every property being generated from the annotations elements (as defined in OSGi 7 Compendium, 112.8.2.1)
     */
    static final String PREFIX_ = "sling.servlet.";

    /**
     * The absolute paths under which the servlet is accessible as a resource.
     * A relative path is made absolute by prefixing it with the value set through the
     * {@link SlingServletPrefix} annotation.
     * <p>
     * This annotation or {@link SlingServletResourceTypes} should be used to properly register the servlet in Sling.
     * If both are set the servlet is registered using both ways.
     * <p>
     * A servlet using this property might be ignored unless its path is included
     * in the Execution Paths {@code servletresolver.paths} configuration setting of the
     * {@code org.apache.sling.servlets.resolver.internal.SlingServletResolver} service.
     * @return the absolute paths under which the servlet is accessible as a resource
     * @see ServletResolverConstants#SLING_SERVLET_PATHS
     */
    String[] paths();

    /** Activate the strict resolution mode. Must be set to true
     *  (which is the default) for the other options besides "paths"
     *  to be taken into account.
     * @return the "strict" option value
     */
    boolean paths_strict() default true;

    /**
     * One or more request URL selectors supported by the servlet. If specified,
     * all selectors must match for the servlet to be selected.
     * @return the selector(s)
     */
    String[] selectors() default {};

    /**
     * The request URL extensions supported by the servlet. If specified,
     * one of these must match the request for the servlet to be selected.
     * @return the extension(s)
     * @see ServletResolverConstants#SLING_SERVLET_EXTENSIONS
     */
    String[] extensions() default {};

    /**
     * The HTTP request methods supported by the servlet. If specified, the 
     * request's method must match this value for the servlet to be selected.
     * @return the methods(s)
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3">HTTP 1.1 Spec Methods</a>
     */
    String[] methods() default {};
}
