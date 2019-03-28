package org.ff4j.web.bean;

import static org.ff4j.web.bean.WebConstants.PIC_DISABLE;

/*
 * #%L
 * ff4j-console
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

import java.io.Serializable;

import org.ff4j.FF4j;
import org.ff4j.event.repository.EventAuditTrailRepository;
import org.ff4j.event.repository.EventFeatureUsageRepository;
import org.ff4j.feature.repository.FeatureRepository;
import org.ff4j.property.repository.PropertyRepository;
import org.ff4j.user.repository.RolesAndUsersRepository;

/**
 * Webbean to display home information
 *
 * @author <a href="mailto:cedrick.lunven@gmail.com">Cedrick LUNVEN</a>
 */
public class HomeBean implements Serializable {

    /** serial. */
    private static final long serialVersionUID = 9115704270593636619L;

    /** uptime. */
    private String uptime;

    /** class of store. */
    private String featureStore = PIC_DISABLE;

    /** class of store. */
    private String propertyStore = PIC_DISABLE;

    /** class of monitoring. */
    private String userStore = PIC_DISABLE;

    /** class of monitoring. */
    private String eventFeatureUsage = PIC_DISABLE;

    /** class of monitoring. */
    private String eventAuditTrail = PIC_DISABLE;
     
    /** cmass of cache Manager if available. */
    private String caching = PIC_DISABLE;

    /** version of target ff4j. */
    private String version = PIC_DISABLE;

    /** number of features to display. */
    private int nbFeature = 0;

    /** number of features to display. */
    private int nbProperties = 0;

    /** number of groups to display. */
    private int nbGroup = 0;

    /** number of events used. */
    private int nbEvents = 0;

    /**
     * Default constructor.
     */
    public HomeBean() {
    }

    /**
     * Default constructor.
     */
    public HomeBean(FF4j ff4j) {
        this.version = ff4j.getVersion();
        setUptime(ff4j.getStartTime());
        // Feature Store
        if (ff4j.getRepositoryFeatures() != null) {
            FeatureRepository featureRepo = ff4j.getTargetRepositoryFeatures();
            this.featureStore = featureRepo.getClass().getSimpleName();
            this.nbFeature = new Long(featureRepo.count()).intValue();
            this.nbGroup   = new Long(featureRepo.listGroupNames().count()).intValue();
            featureStore = featureStore.replaceAll("FeatureRepository", "").toLowerCase();
        }
        // Property Store
        if (ff4j.getRepositoryProperties() != null) {
            PropertyRepository propsRepo = ff4j.getTargetRepositoryProperties();
            this.propertyStore = propsRepo.getClass().getSimpleName();
            this.nbProperties  = new Long(propsRepo.getPropertyNames().count()).intValue();
            propertyStore = propertyStore.replaceAll("PropertyRepository", "").toLowerCase();
        }
        // Feature Usage
        EventFeatureUsageRepository evtRepository = ff4j.getRepositoryEventFeaturesUsage();
        if (evtRepository != null) {
            this.eventFeatureUsage = evtRepository.getClass().getSimpleName();
            eventFeatureUsage = eventFeatureUsage.replaceAll("EventFeatureUsageRepository", "").toLowerCase();
        }
        // Audit Trail
        EventAuditTrailRepository auditRepository = ff4j.getAuditTrail();
        if (auditRepository != null) {
            this.eventAuditTrail = auditRepository.getClass().getSimpleName();
            eventAuditTrail = eventAuditTrail.replaceAll("EventAuditTrailRepository", "").toLowerCase();
        }
        // User and Roles
        RolesAndUsersRepository userRepository = ff4j.getRepositoryUsersRoles();
        if (userRepository != null) {
            this.userStore = auditRepository.getClass().getSimpleName();
            userStore = userStore.replaceAll("RolesAndUsersRepository", "").toLowerCase();
        }
        // Caching
        ff4j.getRepositoryFeaturesCacheProxy().ifPresent(cacheProxy -> {
            this.caching = cacheProxy.getCacheProvider();
        });
    }

    /**
     * Getter accessor for attribute 'monitoring'.
     *
     * @return current value of 'monitoring'
     */
    public String getMonitoring() {
        return eventFeatureUsage;
    }

    /**
     * Getter accessor for attribute 'version'.
     *
     * @return current value of 'version'
     */
    public String getVersion() {
        return version;
    }

    /**
     * Getter accessor for attribute 'uptime'.
     *
     * @return current value of 'uptime'
     */
    public String getUptime() {
        return uptime;
    }

    /**
     * Setter accessor for attribute 'uptime'.
     *
     * @param ff4jStartTime
     *            new value for 'uptime '
     */
    public void setUptime(long ff4jStartTime) {
        StringBuilder sb = new StringBuilder();
        long uptime = System.currentTimeMillis() - ff4jStartTime;
        long daynumber = uptime / (1000 * 3600 * 24L);
        uptime = uptime - daynumber * 1000 * 3600 * 24L;
        long hourNumber = uptime / (1000 * 3600L);
        uptime = uptime - hourNumber * 1000 * 3600L;
        long minutenumber = uptime / (1000 * 60L);
        uptime = uptime - minutenumber * 1000 * 60L;
        long secondnumber = uptime / 1000L;
        sb.append(daynumber + " days ");
        sb.append(hourNumber + " hours ");
        sb.append(minutenumber + " min ");
        sb.append(secondnumber + " sec");
        this.uptime = sb.toString();
    }

    /**
     * Getter accessor for attribute 'nbFeature'.
     *
     * @return current value of 'nbFeature'
     */
    public int getNbFeature() {
        return nbFeature;
    }

    /**
     * Getter accessor for attribute 'nbGroup'.
     *
     * @return current value of 'nbGroup'
     */
    public int getNbGroup() {
        return nbGroup;
    }

    /**
     * Getter accessor for attribute 'nbEvents'.
     *
     * @return current value of 'nbEvents'
     */
    public int getNbEvents() {
        return nbEvents;
    }

    /**
     * Getter accessor for attribute 'featureStore'.
     *
     * @return
     *       current value of 'featureStore'
     */
    public String getFeatureStore() {
        return featureStore;
    }

    /**
     * Getter accessor for attribute 'propertyStore'.
     *
     * @return
     *       current value of 'propertyStore'
     */
    public String getPropertyStore() {
        return propertyStore;
    }

    /**
     * Getter accessor for attribute 'nbProperties'.
     *
     * @return
     *       current value of 'nbProperties'
     */
    public int getNbProperties() {
        return nbProperties;
    }

    /**
     * Getter accessor for attribute 'caching'.
     *
     * @return
     *       current value of 'caching'
     */
    public String getCaching() {
        return caching;
    }
    
    /**
     * Getter accessor for attribute 'userStore'.
     *
     * @return
     *       current value of 'userStore'
     */
    public String getUserStore() {
        return userStore;
    }

    /**
     * Getter accessor for attribute 'eventFeatureUsage'.
     *
     * @return
     *       current value of 'eventFeatureUsage'
     */
    public String getEventFeatureUsage() {
        return eventFeatureUsage;
    }

    /**
     * Getter accessor for attribute 'eventAuditTrail'.
     *
     * @return
     *       current value of 'eventAuditTrail'
     */
    public String getEventAuditTrail() {
        return eventAuditTrail;
    }

}
