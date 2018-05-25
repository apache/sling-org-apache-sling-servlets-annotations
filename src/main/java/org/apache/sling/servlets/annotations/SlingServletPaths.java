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
import org.osgi.service.component.annotations.ComponentPropertyType;
/**
 * Component Property Type (as defined by OSGi DS 1.4) for Sling Servlets.
 * Takes care of writing the relevant component properties as being used by the Sling Servlet Resolver ({@link ServletResolverConstants})
 * to register the annotated servlet component as Sling servlet for a specific path.
 * Preferably register Sling servlets by resource type ({@link SlingServletResourceTypes}) though 
 * for reasons outlined at <a href="https://sling.apache.org/documentation/the-sling-engine/servlets.html#caveats-when-binding-servlets-by-path">
 * Caveats when binding servlets by path</a>.
 *
 * @see <a href="https://sling.apache.org/documentation/the-sling-engine/servlets.html">Sling Servlets</a>
 * @see ServletResolverConstants
 * @see <a href="https://github.com/apache/felix/blob/trunk/tools/org.apache.felix.scr.annotations/src/main/java/org/apache/felix/scr/annotations/sling/SlingServlet.java">Felix SCR annotation</a>
 */
@ComponentPropertyType
public @interface SlingServletPaths {
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
     * 
     * @see ServletResolverConstants#SLING_SERVLET_PATHS
     */
    String[] value();

    // TODO: does not work due to https://github.com/bndtools/bnd/issues/2445
}
