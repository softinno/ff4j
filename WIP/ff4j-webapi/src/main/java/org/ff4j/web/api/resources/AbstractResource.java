package org.ff4j.web.api.resources;

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

import org.ff4j.FF4j;
import org.ff4j.event.repository.EventAuditTrailRepository;
import org.ff4j.event.repository.EventFeatureUsageRepository;
import org.ff4j.feature.repository.FeatureRepository;
import org.ff4j.property.repository.PropertyRepository;

/**
 * SuperClass for common injections.
 */
public abstract class AbstractResource {
    
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
    
    /** Access to Features through store. */
    private FeatureRepository featureRepo;
    
    /** Access to Features through store. */
    private PropertyRepository propertyStore;
    
    /** Access to event repository. */
    private EventAuditTrailRepository auditTrailRepo;
    
    /** Access to event repository. */
    private EventFeatureUsageRepository featureUsageRepo;
     
    /**
     * Getter accessor for attribute 'repo'.
     *
     * @return
     *       current value of 'repo'
     */
    public EventAuditTrailRepository getAuditTrailRepo() {
        if (auditTrailRepo == null) {
            auditTrailRepo = ff4j.getAuditTrail();
        }
        return auditTrailRepo;
    }
    
    /**
     * Getter accessor for attribute 'repo'.
     *
     * @return
     *       current value of 'repo'
     */
    public EventFeatureUsageRepository getFeatureUsageRepo() {
        if (featureUsageRepo == null) {
            featureUsageRepo = ff4j.getRepositoryEventFeaturesUsage();
        }
        return featureUsageRepo;
    }
    
    
    /**
     * Getter accessor for attribute 'store'.
     * 
     * @return current value of 'store'
     */
    public FeatureRepository getFeatureRepository() {
        if (featureRepo == null) {
            featureRepo = ff4j.getRepositoryFeatures();
        }
        return featureRepo;
    }

    /**
     * Getter accessor for attribute 'propertyStore'.
     *
     * @return
     *       current value of 'propertyStore'
     */
    public PropertyRepository getPropertyRepository() {
        if (propertyStore == null) {
            propertyStore = ff4j.getRepositoryProperties();
        }
        return propertyStore;
    }

}
