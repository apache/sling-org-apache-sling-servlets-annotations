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
 * Preferably register Sling servlets by resource type ({@link SlingServletByResourceType}) though 
 * for reasons outlined at <a href="https://sling.apache.org/documentation/the-sling-engine/servlets.html#caveats-when-binding-servlets-by-path">
 * Caveats when binding servlets by path</a>
 *
 * @see <a href="https://github.com/apache/felix/blob/trunk/tools/org.apache.felix.scr.annotations/src/main/java/org/apache/felix/scr/annotations/sling/SlingServlet.java">Felix SCR annotations</a>
 * @see ServletResolverConstants
 */
@ComponentPropertyType
public @interface SlingServletByPath {

    /**
     * The absolute paths under which the servlet is accessible as a resource.
     * A relative path is made absolute by prefixing it with the value set through the
     * {@link #sling_servlet_prefix()} element.
     * <p>
     * Either this property or the {@link #SLING_SERVLET_RESOURCE_TYPES}
     * property must be set or the servlet is ignored.
     * If both are set the servlet is registered using both ways.
     * <p>
     * A servlet using this property might be ignored unless its path is included
     * in the Execution Paths {@code servletresolver.paths} configuration setting of the
     * {@code org.apache.sling.servlets.resolver.internal.SlingServletResolver} service.
     * 
     * @see ServletResolverConstants#SLING_SERVLET_PATHS
     */
    String[] sling_servlet_paths();
    
    /**
     * The prefix/index to be used to register this servlet.
     * It only is applied as prefix to {@link #sling_servlet_paths()} and 
     * in case they do not start with a "/".
     * </p>
     * <ul>
     * <li>If the value of this element is a number, it defines the index of the search
     * path entries from the resource resolver. The defined search path is used as
     * a prefix to mount this servlet. The number can be -1 which always points to the
     * last search entry. If the specified value is higher than than the highest index
     * of the search paths, the last entry is used. The index starts with 0.
     * If the value of this property is a string and parseable as a number, the above
     * logic is used.</li>
     * <li>If the value of this element is a string starting with "/", this value is applied
     * as a prefix, regardless of the configured search paths!</li>
     * <li>If the value is anything else, it is ignored.</li>
     * </ul>
     * If this property is empty, the configuration of the {@code org.apache.sling.servlets.resolver.internal.SlingServletResolver}
     * service is used.
     * In case even that one is not set "/" is used as prefix.
     * @see ServletResolverConstants#SLING_SERVLET_PREFIX
     */
    String sling_servlet_prefix() default "";
    
    /**
     * Containing the name of the servlet. If this is empty, the
     * <code>component.name</code> property or the <code>service.pid</code>
     * is used. If none of the three properties is defined, the Servlet is
     * ignored.
     * @see ServletResolverConstants#SLING_SERVLET_NAME
     */
    String sling_core_servletName() default "";
}
