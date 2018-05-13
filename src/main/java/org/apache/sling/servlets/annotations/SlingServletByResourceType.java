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
 * to register the annotated servlet component as Sling servlet for a specific resource type.
 * 
 * @see <a href="https://github.com/apache/felix/blob/trunk/tools/org.apache.felix.scr.annotations/src/main/java/org/apache/felix/scr/annotations/sling/SlingServlet.java">Felix SCR annotations</a>
 * @see ServletResolverConstants
 */
@ComponentPropertyType
public @interface SlingServletByResourceType {

    /**
     * The resource type(s) supported by the servlet (value
     * is "sling.servlet.resourceTypes").
     * A relative resource type is made absolute by prefixing it with the value set through the
     * {@link #sling_servlet_prefix()} property.
     * <p>
     */
    String[] sling_servlet_resourceTypes();
    
    /**
     * One ore more request URL selectors supported by the servlet. The
     * selectors must be configured in the order as they would be specified in the URL that
     * is as a list of dot-separated strings such as <em>print.a4</em>.
     * In case this is not empty all selectors must match, otherwise the servlet is not executed.
     * @see ServletResolverConstants#SLING_SERVLET_SELECTORS
     */
    String[] sling_servlet_selectors() default {};
    
    /**
     * The request URL extensions supported by the servlet
     * for GET requests.
     * <p>
     * It this is not set, the servlet is not limited to certain extensions.
     * @see ServletResolverConstants#SLING_SERVLET_EXTENSIONS
     */
    String[] sling_servlet_extensions() default {};
    
    /**
     * The request methods supported by the servlet. The value may be one of the HTTP 
     * methods or "*" for all methods.
     * <p>
     * If this is not set (i.e. empty array) it is assumed to be {@code GET} and {@code HEAD}.
     * @see ServletResolverConstants#SLING_SERVLET_METHODS
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3">HTTP 1.1 Spec Methods</a>
     */
    String[] sling_servlet_methods() default {};
    
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
