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
 * Takes care of writing the relevant service properties as being used by the Sling Servlet Resolver ({@link ServletResolverConstants})
 * to register the annotated servlet component as Sling servlet for a specific resource type.
 * 
 * @see <a href="https://sling.apache.org/documentation/the-sling-engine/servlets.html">Sling Servlets</a>
 * @see ServletResolverConstants
 * @see <a href="https://github.com/apache/felix/blob/trunk/tools/org.apache.felix.scr.annotations/src/main/java/org/apache/felix/scr/annotations/sling/SlingServlet.java">Felix SCR annotation</a>
 */
@ComponentPropertyType
public @interface SlingServletResourceTypes {

    /**
     * Prefix for every property being generated from the annotations elements (as defined in OSGi 7 Compendium, 112.8.2.1)
     */
    static final String PREFIX_ = "sling.servlet.";

    /**
     * The resource type(s) supported by the servlet (value
     * is "sling.servlet.resourceTypes").
     * A relative resource type is made absolute by prefixing it with the value set through the
     * {@link SlingServletPrefix} annotation.
     * <p>
     */
    String[] resourceTypes();
    
    /**
     * One ore more request URL selectors supported by the servlet. The
     * selectors must be configured in the order as they would be specified in the URL that
     * is as a list of dot-separated strings such as <em>print.a4</em>.
     * In case this is not empty the first selector(s) (i.e. the most right-hand ones in the URL) must match, 
     * otherwise the servlet is not executed. After that may follow arbitrarily many non-registered selectors.
     * @see ServletResolverConstants#SLING_SERVLET_SELECTORS
     */
    String[] selectors() default {};
    
    /**
     * The request URL extensions supported by the servlet
     * for GET requests.
     * <p>
     * It this is not set, the servlet is not limited to certain extensions.
     * @see ServletResolverConstants#SLING_SERVLET_EXTENSIONS
     */
    String[] extensions() default {};
    
    /**
     * The request methods supported by the servlet. The value may be one of the HTTP 
     * methods or "*" for all methods.
     * <p>
     * If this is not set (i.e. empty array) it is assumed to be {@code GET} and {@code HEAD}.
     * @see ServletResolverConstants#SLING_SERVLET_METHODS
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3">HTTP 1.1 Spec Methods</a>
     */
    String[] methods() default {};
}
