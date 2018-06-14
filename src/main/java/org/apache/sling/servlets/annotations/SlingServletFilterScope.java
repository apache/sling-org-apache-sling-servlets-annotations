/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.servlets.annotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletResponse;
/**
 * The possible scopes for the {@link SlingServletFilter#scope()} annotation.
 */
public enum SlingServletFilterScope {

    /**
     * Filters are called once per request hitting Sling from the outside. 
     * These filters are called after the resource addressed by the request URL and the Servlet or script to process the request has been resolved 
     * before the COMPONENT filters (if any) and the Servlet or script are called.
     * <p>Servlet API Correspondence: {@code REQUEST}</p>
     */
    REQUEST("REQUEST"),

    /**
     * Filters are called upon calling the {@link RequestDispatcher#include(javax.servlet.ServletRequest, javax.servlet.ServletResponse)} 
     * method after the included resource and the Servlet or script to process the include have been resolved before the Servlet or script is called.
     * <p>Servlet API Correspondence: {@code REQUEST},{@code INCLUDE},{@code FORWARD}</p>
     */
    COMPONENT("COMPONENT"),

    /**
     * Filters are called upon {@link HttpServletResponse#sendError(int)} or {@link HttpServletResponse#sendError(int, String)}
     * or any uncaught Throwable before resolving the error handler Servlet or script.
     * <p>Servlet API Correspondence: {@code ERROR}</p>
     */
    ERROR("ERROR"),

    /**
     * Filters are called upon calling the RequestDispatcher.include method after the included resource and the Servlet 
     * or script to process the include have been resolved before the Servlet or script is called.
     * <p>Servlet API Correspondence: {@code INCLUDE}</p>
     */
    INCLUDE("INCLUDE"),

    /**
     * Filters are called upon calling the RequestDispatcher.forward method after the included resource and the Servlet
     * or script to process the include have been resolved before the Servlet or script is called.
     * <p>Servlet API Correspondence: {@code FORWARD}</p>
     */
    FORWARD("FORWARD");

    private final String scope;

    private SlingServletFilterScope(final String scope) {
        this.scope = scope;
    }

    /**
     * @return String representation of the scope
     */
    public String getScope() {
        return this.scope;
    }

    @Override
    public String toString() {
        return this.getScope();
    }

}