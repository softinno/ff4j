package org.ff4j.json.domain;

/*
 * #%L
 * ff4j-webapi
 * %%
 * Copyright (C) 2013 - 2016 FF4J
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


import java.util.HashSet;
import java.util.Set;

import org.ff4j.json.utils.FF4jObjectMapper;
import org.ff4j.property.Property;
import org.ff4j.property.PropertyFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Abstract representation of {@link Property} as webbean.
 *
 * @author Cedrick Lunven (@clunven)</a>
 */
@ApiModel(value = "Property", description = "Expose FF4j Property<?> as a friendy stub for JSON" )
@JsonInclude(Include.NON_NULL)
public class PropertyJson extends AbstractJsonEntity {
  
    /** Serial. */
    private static final long serialVersionUID = 6108251653425936590L;

    /** nature of the property (classname). */
    @JsonProperty("className")
    @JsonInclude(Include.NON_NULL)
    @ApiModelProperty( value = "Classname for implementation", required = true )
    private String className;
    
    /** Value as String. */
    @JsonProperty("value")
    @JsonInclude(Include.NON_NULL)
    @ApiModelProperty( value = "value", required = true )
    private String value;
    
    /** Fixed values as String. */
    @JsonProperty("fixedValues")
    @JsonInclude(Include.NON_EMPTY)
    @ApiModelProperty( value = "fixedValues", required = false )
    private Set < String > fixedValues = new HashSet<String>();

    /**
     * Default constructor
     */
    public PropertyJson() {}
        
    /**
     * Initialization from Property.
     * 
     * (From {@link Property} to {@link PropertyJson}
     *
     * @param property
     *      target property
     */
    public PropertyJson(Property<?> prop) {
        populateFromEntity(prop);
        this.value     = prop.getValueAsString();
        this.className = prop.getClassName();
        prop.getFixedValues().ifPresent(vals -> {
           vals.stream().map(Object::toString)
               .forEach(this.fixedValues::add);
        });
    }
    
    public PropertyJson(String json) {
        PropertyJson tmp = fromJson(json);
        if (null != tmp) {
            this.uid               = tmp.getUid();
            this.creationDate      = tmp.getCreationDate();
            this.lastModifiedDate  = tmp.getLastModifiedDate();
            this.description       = tmp.getDescription();
            this.accessControlList = tmp.getAccessControlList();
            this.owner             = tmp.getOwner();
            this.properties        = tmp.getProperties();
            this.value             = tmp.getValue();
            this.className         = tmp.getClassName();
            this.fixedValues       = tmp.getFixedValues();
        }
    }
    
    /**
     * From {@link PropertyJson} to {@link Property}
     */
    public Property<?> asProperty() {
        Property<?> p = PropertyFactory.createProperty(uid, className, value, description, fixedValues);
        populateTargetEntity(p);
        return p;
    }
    
    /** From JSON to {@link PropertyJson}. */
    public static PropertyJson fromJson(String json) {
        return FF4jObjectMapper.fromJson(json, PropertyJson.class);
    }

    /**
     * Getter accessor for attribute 'value'.
     *
     * @return
     *       current value of 'value'
     */
    public String getValue() {
        return value;
    }

    /**
     * Setter accessor for attribute 'value'.
     * @param value
     * 		new value for 'value '
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Getter accessor for attribute 'fixedValues'.
     *
     * @return
     *       current value of 'fixedValues'
     */
    public Set<String> getFixedValues() {
        return fixedValues;
    }

    /**
     * Setter accessor for attribute 'fixedValues'.
     * @param fixedValues
     * 		new value for 'fixedValues '
     */
    public void setFixedValues(Set<String> fixedValues) {
        this.fixedValues = fixedValues;
    }

    /**
     * Getter accessor for attribute 'className'.
     *
     * @return
     *       current value of 'className'
     */
    public String getClassName() {
        return className;
    }

    /**
     * Setter accessor for attribute 'className'.
     * @param className
     * 		new value for 'className '
     */
    public void setClassName(String className) {
        this.className = className;
    }
    
}
