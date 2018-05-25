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
 * Takes care of writing the relevant component properties to set a name for a Sling Servlet.
 * Must be combined with either {@link SlingServletResourceTypes} or {@link SlingServletPaths}.
 *
 * @see <a href="https://github.com/apache/felix/blob/trunk/tools/org.apache.felix.scr.annotations/src/main/java/org/apache/felix/scr/annotations/sling/SlingServlet.java">Felix SCR annotations</a>
 * @see ServletResolverConstants
 */
@ComponentPropertyType
public @interface SlingServletName {
    /**
     * Prefix for every property being generated from the annotations elements (as defined in OSGi 7 Compendium, 112.8.2.1)
     */
    static final String PREFIX_ = "sling.core.";

    /**
     * Containing the name of the servlet. If this is empty (or not set), the
     * <code>component.name</code> property or the <code>service.pid</code>
     * is used. If none of the three properties is defined, the Servlet is
     * ignored.
     * @see ServletResolverConstants#SLING_SERVLET_NAME
     */
    String servletName();
}
