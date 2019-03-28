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

import java.util.HashMap;
import java.util.Map;

import org.ff4j.feature.togglestrategy.ToggleStrategy;
import org.ff4j.property.Property;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Bean for flipping strategy
 *
 * @author <a href="mailto:cedrick.lunven@gmail.com">Cedrick LUNVEN</a>
 */
@ApiModel(value = "ToggleStrategyApiBean", description = "Represents the ToggleStrategy for API" )
@JsonInclude(Include.NON_NULL)
public class ToggleStrategyApiBean {
    
    /** Implementation class. */
    @ApiModelProperty( value = "implementation class", required = true )
    @JsonProperty("className")
    private String className = null;
    
    /**
     * Init params.
     */
    @ApiModelProperty( value = "init parameters", required = false )
    @JsonProperty("initParams")
    private Map < String, Property<?> > properties = new HashMap<String, Property<?> >();

    /**
     * Target init parameters.
     *
     * @param fs
     *      current flipping strategy
     */
    public ToggleStrategyApiBean() {
    }
    
    /**
     * Target init parameters.
     *
     * @param fs
     *      current flipping strategy
     */
    public ToggleStrategyApiBean(ToggleStrategy fs) {
        this.className  = fs.getClass().getName();
        this.properties = fs.getPropertiesAsMap();
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

    /**
     * Getter accessor for attribute 'properties'.
     *
     * @return
     *       current value of 'properties'
     */
    public Map<String, Property<?>> getProperties() {
        return properties;
    }

    /**
     * Setter accessor for attribute 'properties'.
     * @param properties
     * 		new value for 'properties '
     */
    public void setProperties(Map<String, Property<?>> properties) {
        this.properties = properties;
    }
    
}
