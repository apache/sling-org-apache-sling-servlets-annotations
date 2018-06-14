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
import org.osgi.service.component.propertytypes.ServiceRanking;

/** 
 * Component Property Type (as defined by OSGi DS 1.4) for Sling Servlet Filters. 
 * Takes care of writing the relevant service properties as being used by the Sling Servlet Resolver 
 * ({@link ServletResolverConstants}) to register the annotated servlet filter component as
 * Sling servlet filter. 
 * <br><br>
 * The order of the filter is determined by the property {@code service.ranking}. To set it use the annotation {@link ServiceRanking}. Its value is used to sort the filters. 
 * Filters with a higher order are executed before a filter with a lower order. If two filters have the same order, 
 * the one with the lower service id is executed first.
 * <br>
 * <br>
 * Please note that the ordering is actually depending on the used Apache Sling Engine bundle version. Version older than 2.3.4 of that
 * bundle are sorting the filters in the wrong reverse order. Make sure to run a newer version of the Sling engine to get the correct
 * ordering (see also <a href="https://issues.apache.org/jira/browse/SLING-2920">SLING-2920</a>).
 *
 * @see <a href=
 *      "https://github.com/apache/felix/blob/trunk/tools/org.apache.felix.scr.annotations/src/main/java/org/apache/felix/scr/annotations/sling/SlingFilter.java">Felix
 *      SCR annotation</a>
 * @see <a href="https://sling.apache.org/documentation/the-sling-engine/filters.html">Sling Servlet Filter</a> 
 */
@ComponentPropertyType
public @interface SlingServletFilter {

    /** 
     * Prefix for every property being generated from the annotations elements (as defined in OSGi 7 Compendium, 112.8.2.1) 
     */
    static final String PREFIX_ = "sling.filter.";

    /** 
     * Restrict the filter to paths that match the supplied regular expression. Requires Sling Engine 2.4.0.
     * @return the pattern to restrict the filter
     */
    String pattern() default "";

    /** 
     * The scopes of the filter. If the filter has request scope, it is run once for a request. If the filter has component scope, it is run
     * once for every included component (rendering). 
     * @return the scope of the filter
     */
    SlingServletFilterScope[] scope() default SlingServletFilterScope.REQUEST;
}
