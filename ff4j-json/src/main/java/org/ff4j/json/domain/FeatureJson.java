package org.ff4j.json.domain;

/*-
 * #%L
 * ff4j-json
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

import java.util.ArrayList;
import java.util.List;

import org.ff4j.feature.Feature;
import org.ff4j.json.utils.FF4jObjectMapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Representation of a feature within Web API. 
 * 
 * Having a dedicated layer for JSON help us to generate JSON as we expect. 
 * We could reuse this serialization not only to expose but REST/GraphQL 
 * but also to store into stores.
 * 
 * FEATURE <=> FEATUREJSON <=> JSON STRING
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@ApiModel(value = "Feature", description = "Expose FF4j Feature as a friendy stub for JSON" )
public class FeatureJson extends AbstractJsonEntity {
    
    /** Serial. */
    private static final long serialVersionUID = -7465366359178601114L;

    /** status of feature. */
    @JsonProperty("enable")
    @ApiModelProperty(value = "Status of feature", required = true )
    private boolean enable = false;
     
    /** status of feature. */
    @JsonProperty("group")
    @ApiModelProperty( value = "Group of the feature if exists, it's single", required = false )
    @JsonInclude(Include.NON_NULL)
    private String group = null;
    
    /** unique feature identifier. */
    @JsonProperty("ttl")
    @ApiModelProperty(value = "Time before expiration", required = false )
    @JsonInclude(Include.NON_NULL)
    private Long ttl = null;
     
    /** status of feature. */
    @JsonProperty("toggleStrategies")
    @JsonInclude(Include.NON_EMPTY)
    @ApiModelProperty( value = "List of toggle strategies", required = false )
    private List < ToggleStrategyJson > toggleStrategies = new ArrayList<>();
    
    /** Default Constructor. */
    public FeatureJson() {}
    
    /** Default Constructor. */
    public FeatureJson(Feature feat) {
        populateFromEntity(feat);
        feat.getTTL().ifPresent(val -> this.ttl = val);
        feat.getGroup().ifPresent(g -> this.group = g);
        feat.getToggleStrategies().stream()
                .map(ToggleStrategyJson::new)
                .forEach(this::addToggleStrategy);
    }
    
    public FeatureJson(String json) {
        FeatureJson tmp = fromJson(json);
        if (null != tmp) {
            this.uid               = tmp.getUid();
            this.creationDate      = tmp.getCreationDate();
            this.lastModifiedDate  = tmp.getLastModifiedDate();
            this.description       = tmp.getDescription();
            this.accessControlList = tmp.getAccessControlList();
            this.owner             = tmp.getOwner();
            
            this.properties        = tmp.getProperties();
            this.enable            = tmp.isEnable();
            this.group             = tmp.getGroup();
            this.ttl               = tmp.getTtl();
            this.toggleStrategies  = tmp.getToggleStrategies();
        }
    }
    
    /**
     * Express a {@link FeatureJson} as a {@link Feature}.
     * 
     * @return
     *      instance of Feature
     */
    public Feature asFeature() {
        Feature f = new Feature(getUid());
        populateTargetEntity(f);
        if (null != ttl) {
            f.setTTL(ttl);
        }
        if (null != group) {
            f.setGroup(group);
        }
        if (null != toggleStrategies && !toggleStrategies.isEmpty()) {
            toggleStrategies.stream()
                            .map(ts -> ts.asToggleStrategy(getUid()))
                            .forEach(f::addToggleStrategy);
        }
        return f;
    }
    
    /** Masharlling with Jackson from JSON String to {@link FeatureJson}. */
    public static FeatureJson fromJson(String json) {
        return FF4jObjectMapper.fromJson(json, FeatureJson.class);
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
     * Getter accessor for attribute 'toggleStrategies'.
     *
     * @return
     *       current value of 'toggleStrategies'
     */
    public List<ToggleStrategyJson> getToggleStrategies() {
        return toggleStrategies;
    }
    
    public void addToggleStrategy(ToggleStrategyJson toggle) {
        getToggleStrategies().add(toggle);
    }

    /**
     * Setter accessor for attribute 'toggleStrategies'.
     * @param toggleStrategies
     * 		new value for 'toggleStrategies '
     */
    public void setToggleStrategies(List<ToggleStrategyJson> toggleStrategies) {
        this.toggleStrategies = toggleStrategies;
    }

    /**
     * Getter accessor for attribute 'ttl'.
     *
     * @return
     *       current value of 'ttl'
     */
    public Long getTtl() {
        return ttl;
    }

    /**
     * Setter accessor for attribute 'ttl'.
     * @param ttl
     * 		new value for 'ttl '
     */
    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }
}
