package org.ff4j.api;

/*-
 * #%L
 * ff4j-api-rest
 * %%
 * Copyright (C) 2013 - 2019 FF4J
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

/**
 * Constants used in the FF4J RESTFul Api Definition.
 *
 * @author Cedrick Lunven (@clunven)
 */
public interface FF4jApiSpecification {
    
    /** expected post parameter from POST methods. */
    String POST_PARAMNAME_FEATURE_UID = "uid";
    
    /** parameter. */
    String POST_PARAMNAME_CUSTOM_PREFIX = "PARAM_";

    /** Custom operation on resource. */
    String OPERATION_TOGGLE_ON             = "toggleOn";
    String OPERATION_TOGGLE_OFF            = "toggleOff";
    String OPERATION_TOGGLE_AUDIT_ON       = "toggleOnAudit";
    String OPERATION_TOGGLE_AUDIT_OFF      = "toggleOffAudit";
    String OPERATION_TOGGLE_FEATUREHIT_ON  = "toggleOnFeatureHit";
    String OPERATION_TOGGLE_FEATUREHIT_OFF = "toggleOffFeatureHit";
    
    /** Custom operation on resource. */
    String OPERATION_TEST = "test";

    /** Custom operation on resource. */
    String OPERATION_GRANTROLE = "grantrole";

    /** Custom operation on resource. */
    String OPERATION_REMOVEROLE = "removerole";

    /** Custom operation on resource. */
    String OPERATION_ADDGROUP = "addGroup";
    String OPERATION_REMOVEGROUP = "removeGroup";
    
    String CLEAR_CACHE = "clearCache";
    
    /** relative path for cache. */
    String STORE_CLEAR = "clear";
    
    /** relative path for cache. */
    String STORE_CREATESCHEMA = "createSchema";

    /** relative path. */
    String RESOURCE_FF4J = "ff4j";

    /** list of curves. */
    String RESOURCE_PIE = "pieChart";
    
    /** list of curves. */
    String RESOURCE_BAR = "barChart";
    
    /** filter for resource. */
    String PARAM_START = "start";
    
    /** featureID. */
    String PARAM_UID = "uid";
    
    /** filter for resource. */
    String PARAM_END = "end";
    
    /** nb of points in the curve. */
    String PARAM_NBPOINTS = "nbpoints";

    /** RBAC on API LEVEL. */
    String ROLE_READ              = "READ";
    String ROLE_VIEW_FEATURES     = "VIEW_FEATURES";
    String ROLE_VIEW_PROPERTIES   = "VIEW_PROPERTIES";
    String ROLE_VIEW_AUDITTRAIL   = "VIEW_AUDITTRAIL";
    String ROLE_VIEW_FEATUREUSAGE = "VIEW_FEATUREUSAGE";
    String ROLE_WRITE             = "WRITE";
    String ROLE_TOGGLE_FEATURES   = "TOGGLE_FEATURES";
    String ROLE_ADMIN_FEATURES    = "ADMIN_FEATURES";
    String ROLE_ADMIN_PROPERTIES  = "ADMIN_PROPERTIES";
    
    /** HTTP Parameter. */
    String PARAM_AUTHKEY = "apiKey";
    String HTTP_LOCATION        = javax.ws.rs.core.HttpHeaders.LOCATION;
    String HTTP_AUTHORIZATION   = javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
}
