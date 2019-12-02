package org.ff4j.api.resources;

/*
 * #%L
 * ff4j-web
 * %%
 * Copyright (C) 2013 - 2014 Ff4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.ff4j.api.FF4jApiSpecification;
import org.ff4j.core.FF4j;

/**
 * SuperClass for common injections.
 */
public abstract class AbstractResource implements FF4jApiSpecification {
    
    /** Access to Features through store. */
    @Context
    protected FF4j ff4j = null;
    
    /** rest url. */
    @Context
    protected UriInfo uriInfo;

    /** current request. */
    @Context
    protected Request request;
    
    /** security context is included within resources to get permissions. */
    @Context
    protected SecurityContext securityContext;
    

}
