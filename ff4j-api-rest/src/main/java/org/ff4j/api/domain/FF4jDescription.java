package org.ff4j.api.domain;

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

import org.ff4j.core.FF4j;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * API Bean to represent ff4j.
 * 
 * @author <a href="mailto:cedrick.lunven@gmail.com">Cedrick LUNVEN</a>
 */
@ApiModel(value = "ff4jStatus", description = "ff4j resource representation" )
@JsonInclude(Include.NON_NULL)
public final class FF4jDescription {
    
    @ApiModelProperty(value = "Last restart time", required = true )
    @JsonProperty("startTime")
    private String startTime;
    
    /** uptime. */
    @ApiModelProperty(value = "uptime of the application", required = true )
    @JsonProperty("uptime")
    private String uptime;
    
    /** autocreate. */
    @ApiModelProperty( value = "feature are created if not exist in store", required = true )
    @JsonProperty("autocreate")
    private boolean autocreate = false;
    
    /** version. */
    @ApiModelProperty( value = "current version of ff4j library", required = true )
    @JsonProperty("version")
    private String version = "N/A";
    
    @ApiModelProperty( value = "Source for ff4j", required = true )
    @JsonProperty("source")
    private String source;
    
    // --- Repositories Definitions ---
    
    @ApiModelProperty( value = "Meta information for FeatureRepository", required = true )
    @JsonProperty(value = "featureRepositorySettings")
    @JsonInclude(Include.NON_NULL)
    private FeatureRepositorySettings featureRepositorySettings = null;
    
    @ApiModelProperty( value = "Meta information for FeatureHitRepository", required = true )
    @JsonProperty(value = "featureHitRepositorySettings")
    @JsonInclude(Include.NON_NULL)
    private FeatureHitRepositorySettings featureHitRepositorySettings = null;
    
    @ApiModelProperty( value = "Meta information for PropertyRepository", required = true )
    @JsonProperty(value = "propertyRepositorySettings")
    @JsonInclude(Include.NON_NULL)
    private PropertyRepositorySettings propertyRepositorySettings = null;
    
    @ApiModelProperty( value = "Meta information for RolesAndUsersRepository", required = true )
    @JsonProperty(value = "rolesAndUsersRepositorySettings")
    @JsonInclude(Include.NON_NULL)
    private RolesAndUsersRepositorySettings rolesAndUsersRepositorySettings = null;
    
    /** store. */
    @ApiModelProperty( value = "Meta information for PropertyRepository", required = true )
    @JsonProperty(value = "auditTrailDefinition")
    @JsonInclude(Include.NON_NULL)
    private AuditTrailSettings auditTrailDefinition = null;
    
    /**
     * Default constructor.
     *
     * @param ff4j
     *      target ff4j.
     */
    public FF4jDescription() {}
            
    /**
     * Parameterized Constructor.
     *
     * @param ff4j
     *      target ff4j.
     */
    public FF4jDescription(FF4j ff4j) {
        // UpTime
        long up = System.currentTimeMillis() - ff4j.getStartTime();
        long daynumber = up / (1000 * 3600 * 24);
        up = up - (daynumber * 1000 * 3600 * 24);
        long hourNumber = up / (1000 * 3600);
        up = up - (hourNumber * 1000 * 3600);
        long minutenumber = up / (1000 * 60);
        up = up - (minutenumber * 1000 * 60);
        long secondnumber = up / 1000;
        uptime =  daynumber + " day(s) ";
        uptime += hourNumber + " hours(s) ";
        uptime += minutenumber + " minute(s) ";
        uptime += secondnumber + " seconds\"";
        autocreate = ff4j.isAutoCreateFeatures();
        version    = ff4j.getVersion();
        if (null != ff4j.getRepositoryFeatures()) {
            featureRepositorySettings = new FeatureRepositorySettings(ff4j.getRepositoryFeatures());
        }
        if (null != ff4j.getRepositoryProperties()) {
            propertyRepositorySettings = new PropertyRepositorySettings(ff4j.getRepositoryProperties());
        }
        if (null != ff4j.getRepositoryUsersRoles()) {
            rolesAndUsersRepositorySettings = new RolesAndUsersRepositorySettings(ff4j.getRepositoryUsersRoles());
        }
        if (ff4j.getRepositoryFeaturesHit().isPresent()) {
            featureHitRepositorySettings = new FeatureHitRepositorySettings(ff4j.getRepositoryFeaturesHit().get());
        }
        if (ff4j.getAuditTrail().isPresent()) {
            auditTrailDefinition = new AuditTrailSettings(ff4j.getAuditTrail().get());
        }
    }

    /**
     * Getter accessor for attribute 'startTime'.
     *
     * @return
     *       current value of 'startTime'
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Setter accessor for attribute 'startTime'.
     * @param startTime
     * 		new value for 'startTime '
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Getter accessor for attribute 'uptime'.
     *
     * @return
     *       current value of 'uptime'
     */
    public String getUptime() {
        return uptime;
    }

    /**
     * Setter accessor for attribute 'uptime'.
     * @param uptime
     * 		new value for 'uptime '
     */
    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    /**
     * Getter accessor for attribute 'autocreate'.
     *
     * @return
     *       current value of 'autocreate'
     */
    public boolean isAutocreate() {
        return autocreate;
    }

    /**
     * Setter accessor for attribute 'autocreate'.
     * @param autocreate
     * 		new value for 'autocreate '
     */
    public void setAutocreate(boolean autocreate) {
        this.autocreate = autocreate;
    }

    /**
     * Getter accessor for attribute 'version'.
     *
     * @return
     *       current value of 'version'
     */
    public String getVersion() {
        return version;
    }

    /**
     * Setter accessor for attribute 'version'.
     * @param version
     * 		new value for 'version '
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Getter accessor for attribute 'source'.
     *
     * @return
     *       current value of 'source'
     */
    public String getSource() {
        return source;
    }

    /**
     * Setter accessor for attribute 'source'.
     * @param source
     * 		new value for 'source '
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Getter accessor for attribute 'featureRepositorySettings'.
     *
     * @return
     *       current value of 'featureRepositorySettings'
     */
    public FeatureRepositorySettings getFeatureRepositorySettings() {
        return featureRepositorySettings;
    }

    /**
     * Setter accessor for attribute 'featureRepositorySettings'.
     * @param featureRepositorySettings
     * 		new value for 'featureRepositorySettings '
     */
    public void setFeatureRepositorySettings(FeatureRepositorySettings featureRepositorySettings) {
        this.featureRepositorySettings = featureRepositorySettings;
    }

    /**
     * Getter accessor for attribute 'propertyRepositorySettings'.
     *
     * @return
     *       current value of 'propertyRepositorySettings'
     */
    public PropertyRepositorySettings getPropertyRepositorySettings() {
        return propertyRepositorySettings;
    }

    /**
     * Setter accessor for attribute 'propertyRepositorySettings'.
     * @param propertyRepositorySettings
     * 		new value for 'propertyRepositorySettings '
     */
    public void setPropertyRepositorySettings(PropertyRepositorySettings propertyRepositorySettings) {
        this.propertyRepositorySettings = propertyRepositorySettings;
    }

    /**
     * Getter accessor for attribute 'featureHitRepositorySettings'.
     *
     * @return
     *       current value of 'featureHitRepositorySettings'
     */
    public FeatureHitRepositorySettings getFeatureHitRepositorySettings() {
        return featureHitRepositorySettings;
    }

    /**
     * Setter accessor for attribute 'featureHitRepositorySettings'.
     * @param featureHitRepositorySettings
     * 		new value for 'featureHitRepositorySettings '
     */
    public void setFeatureHitRepositorySettings(FeatureHitRepositorySettings featureHitRepositorySettings) {
        this.featureHitRepositorySettings = featureHitRepositorySettings;
    }

    /**
     * Getter accessor for attribute 'audittrailDefinition'.
     *
     * @return
     *       current value of 'audittrailDefinition'
     */
    public AuditTrailSettings getAuditTrailDefinition() {
        return auditTrailDefinition;
    }

    /**
     * Setter accessor for attribute 'audittrailDefinition'.
     * @param audittrailDefinition
     * 		new value for 'audittrailDefinition '
     */
    public void setAuditTrailDefinition(AuditTrailSettings audittrailDefinition) {
        this.auditTrailDefinition = audittrailDefinition;
    }

}
