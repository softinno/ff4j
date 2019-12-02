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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.ff4j.core.FF4jEntity;
import org.ff4j.core.security.FF4jAcl;
import org.ff4j.json.utils.FF4jObjectMapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * Common properties for {@link FF4jEntity} as JSON.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public abstract class AbstractJsonEntity extends AbstractJsonBean{
    
    /** Serial. */
    private static final long serialVersionUID = -2513951185870193136L;

    @ApiModelProperty( value = "unique feature identifier", required = true )
    @JsonProperty("uid")
    @JsonInclude(Include.ALWAYS)
    protected String uid = null;

    @ApiModelProperty( value = "description of feature", required = false )
    @JsonProperty("description")
    @JsonInclude(Include.NON_NULL)
    protected String description = null;
    
    @ApiModelProperty( value = "User or Role in charge of the feature", required = false )
    @JsonProperty("owner")
    @JsonInclude(Include.NON_NULL)
    protected String owner = null;
    
    @ApiModelProperty(value = "Time when the feature has been created", required = false )
    @JsonProperty("creationDate")
    @JsonInclude(Include.NON_NULL)
    protected String creationDate = null;
    
    @ApiModelProperty(value = "Time when the feature has been updated", required = false )
    @JsonProperty("lastModifiedDate")
    @JsonInclude(Include.NON_NULL)
    protected String lastModifiedDate = null;
    
    @ApiModelProperty( value = "Custom properties if they exist", required = false )
    @JsonProperty("properties")
    @JsonInclude(Include.NON_EMPTY)
    protected List < PropertyJson > properties = new ArrayList<>();
    
    @ApiModelProperty(value = "Access Control list", required = false )
    @JsonProperty("accessControlList")
    @JsonInclude(Include.NON_NULL)
    protected FF4jAcl accessControlList = null;
    
    protected void populateTargetEntity(FF4jEntity<?> entity) {
        entity.setUid(getUid());
        if (null != creationDate) {
            entity.setCreationDate(LocalDateTime.from(FF4jObjectMapper.DF.parse(getCreationDate())));
        }
        if (null != lastModifiedDate) {
            entity.setLastModified(LocalDateTime.from(FF4jObjectMapper.DF.parse(getLastModifiedDate())));
        }
        if (null != description) {
            entity.setDescription(getDescription());
        }
        if (null != owner) {
            entity.setOwner(getOwner());
        }
        if (null != accessControlList) {
            entity.setAccessControlList(getAccessControlList());
        }
        if (null != properties && !properties.isEmpty()) {
            properties.stream().map(PropertyJson::asProperty).forEach(entity::addProperty); 
        }
    }
    
    protected void populateFromEntity(FF4jEntity<?> entity) {
        if (null != entity) {
            this.uid = entity.getUid();
            if (!entity.getCreationDate().isEmpty()) {
                this.creationDate = FF4jObjectMapper.DF.format(entity.getCreationDate().get());
            }
            if (!entity.getLastModifiedDate().isEmpty()) {
                this.lastModifiedDate = FF4jObjectMapper.DF.format(entity.getLastModifiedDate().get());
            }
            if (!entity.getDescription().isEmpty()) {
                this.description = entity.getDescription().get();
            }
            if (!entity.getOwner().isEmpty()) {
                this.owner = entity.getOwner().get();
            }
            if (null != entity.getAccessControlList()) {
                this.accessControlList = entity.getAccessControlList();
            }
            if (null != entity.getProperties() && !entity.getProperties().isEmpty()) {
                entity.getProperties().values().stream().map(PropertyJson::new).forEach(properties::add);
            }
            if (null != properties && !properties.isEmpty()) {
                properties.stream().map(PropertyJson::asProperty).forEach(entity::addProperty); 
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
     * Getter accessor for attribute 'owner'.
     *
     * @return
     *       current value of 'owner'
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Setter accessor for attribute 'owner'.
     * @param owner
     * 		new value for 'owner '
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Getter accessor for attribute 'creationDate'.
     *
     * @return
     *       current value of 'creationDate'
     */
    public String getCreationDate() {
        return creationDate;
    }
    
    /**
     * Setter accessor for attribute 'creationDate'.
     * @param creationDate
     * 		new value for 'creationDate '
     */
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Getter accessor for attribute 'lastModifiedDate'.
     *
     * @return
     *       current value of 'lastModifiedDate'
     */
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Setter accessor for attribute 'lastModifiedDate'.
     * @param lastModifiedDate
     * 		new value for 'lastModifiedDate '
     */
    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * Getter accessor for attribute 'properties'.
     *
     * @return
     *       current value of 'properties'
     */
    public List<PropertyJson> getProperties() {
        return properties;
    }

    /**
     * Setter accessor for attribute 'properties'.
     * @param properties
     * 		new value for 'properties '
     */
    public void setProperties(List<PropertyJson> properties) {
        this.properties = properties;
    }

    /**
     * Getter accessor for attribute 'accessControlList'.
     *
     * @return
     *       current value of 'accessControlList'
     */
    public FF4jAcl getAccessControlList() {
        return accessControlList;
    }

    /**
     * Setter accessor for attribute 'accessControlList'.
     * @param accessControlList
     * 		new value for 'accessControlList '
     */
    public void setAccessControlList(FF4jAcl accessControlList) {
        this.accessControlList = accessControlList;
    }
}
