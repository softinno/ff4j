package org.ff4j.web.api.resources.domain;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ff4j.feature.Feature;
import org.ff4j.feature.togglestrategy.ToggleStrategy;
import org.ff4j.property.Property;
import org.ff4j.property.PropertyFactory;
import org.ff4j.security.FF4jGrantees;
import org.ff4j.security.FF4jPermission;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Representation of a feature within Web API.
 *
 * @author <a href="mailto:cedrick.lunven@gmail.com">Cedrick LUNVEN</a>
 */
@ApiModel( value = "featureApiBean", description = "Representation of a feature" )
@JsonInclude(Include.NON_NULL)
public class FeatureApiBean {
    
    /** unique feature identifier. */
    @JsonProperty("uid")
    @ApiModelProperty( value = "unique feature identifier", required = true )
    private String uid = null;
    
    /** status of feature. */
    @JsonProperty("enable")
    @ApiModelProperty( value = "status of feature", required = true )
    private boolean enable = false;
    
    /** status of feature. */
    @JsonProperty("description")
    @ApiModelProperty( value = "description of feature", required = false )
    private String description = null;
    
    /** status of feature. */
    @JsonProperty("group")
    @ApiModelProperty( value = "Group of the feature if exists, it's single", required = false )
    private String group = null;
     
    /** status of feature. */
    @JsonProperty("toggleStrategies")
    @ApiModelProperty( value = "List of toggle strategies", required = false )
    private List < ToggleStrategyApiBean > listOfToggleStrategies = null;
    
    @JsonProperty("properties")
    @ApiModelProperty( value = "Custom properties if they exist", required = false )
    private Map < String, PropertyApiBean > properties = new HashMap< String, PropertyApiBean >();
    
    /**
     * Default Constructor.
     */
    public FeatureApiBean() {}
  
    /**
     * Copy constructor.
     *
     * @param f
     * 
     *  target feature
     */
    public FeatureApiBean(Feature f) {
        this.enable = f.isEnabled();
        // Group
        f.getGroup().ifPresent(g -> this.group = g);
        
        // Cloning Strategies
        if (!f.getToggleStrategies().isEmpty()) {
            for (ToggleStrategy strat : f.getToggleStrategies()) {
                addToggleStrategy(ToggleStrategy.of(uid, strat.getClass().getName(), strat.getProperties()));
            }
        }
        // Cloning Properties
        for (Property<?> p : f.getProperties().values()) {
            Property<?> pTmp = PropertyFactory.createProperty(p.getUid(), p.getClass().getName(), p.getValueAsString());
            p.getDescription().ifPresent(pTmp::setDescription);
            if (p.getFixedValues().isPresent()) {
                for (Object fixValue : p.getFixedValues().get()) {
                    pTmp.add2FixedValueFromString(fixValue.toString());
                }
            }
            addProperty(pTmp);
        }
        // Cloning Permissions
        if (!f.getAccessControlList().isEmpty()) {
            Map <FF4jPermission, FF4jGrantees> currentPermissions = getAccessControlList().getPermissions();
            for (Map.Entry<FF4jPermission, FF4jGrantees> acl : f.getAccessControlList().getPermissions().entrySet()) {
                if (!currentPermissions.containsKey(acl.getKey())) {
                    currentPermissions.put(acl.getKey(), new FF4jGrantees());
                }
                FF4jGrantees currentGrantee = currentPermissions.get(acl.getKey());
                currentGrantee.grantUsers(acl.getValue().getUsers());
                currentGrantee.grantRoles(acl.getValue().getRoles());
            }
        }
    }

    /**
     * Getter accessor for attribute 'uid'.
     *
     * @return
     *       current value of 'uid'
     */
    public String getUid() {
        return uid;
    }

    /**
     * Setter accessor for attribute 'uid'.
     * @param uid
     * 		new value for 'uid '
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Getter accessor for attribute 'enable'.
     *
     * @return
     *       current value of 'enable'
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * Setter accessor for attribute 'enable'.
     * @param enable
     * 		new value for 'enable '
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * Getter accessor for attribute 'description'.
     *
     * @return
     *       current value of 'description'
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter accessor for attribute 'description'.
     * @param description
     * 		new value for 'description '
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter accessor for attribute 'group'.
     *
     * @return
     *       current value of 'group'
     */
    public String getGroup() {
        return group;
    }

    /**
     * Setter accessor for attribute 'group'.
     * @param group
     * 		new value for 'group '
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * Getter accessor for attribute 'permissions'.
     *
     * @return
     *       current value of 'permissions'
     */
    public List<String> getPermissions() {
        return permissions;
    }

    /**
     * Setter accessor for attribute 'permissions'.
     * @param permissions
     * 		new value for 'permissions '
     */
    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    /**
     * Getter accessor for attribute 'flippingStrategy'.
     *
     * @return
     *       current value of 'flippingStrategy'
     */
    public FlippingStrategyApiBean getFlippingStrategy() {
        return flippingStrategy;
    }

    /**
     * Setter accessor for attribute 'flippingStrategy'.
     * @param flippingStrategy
     * 		new value for 'flippingStrategy '
     */
    public void setFlippingStrategy(FlippingStrategyApiBean flippingStrategy) {
        this.flippingStrategy = flippingStrategy;
    }

    /**
     * Getter accessor for attribute 'customProperties'.
     *
     * @return
     *       current value of 'customProperties'
     */
    public Map<String, PropertyApiBean> getCustomProperties() {
        return customProperties;
    }

    /**
     * Setter accessor for attribute 'customProperties'.
     * @param customProperties
     * 		new value for 'customProperties '
     */
    public void setCustomProperties(Map<String, PropertyApiBean> customProperties) {
        this.customProperties = customProperties;
    }
}
