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
import java.util.stream.Collectors;

import org.ff4j.feature.ToggleStrategy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * {@link ToggleStrategy} exposed at json layer.
 *
 * @author <a href="mailto:cedrick.lunven@gmail.com">Cedrick LUNVEN</a>
 */
@ApiModel(value = "ToggleStrategy", description = "Represents ToggleStrategy realted to feature" )
@JsonInclude(Include.NON_NULL)
public class ToggleStrategyJson {
    
    /** Implementation class. */
    @ApiModelProperty( value = "implementation class", required = true )
    @JsonProperty("className")
    private String className = null;
    
    /**
     * Init params.
     */
    @ApiModelProperty( value = "init parameters", required = false )
    @JsonProperty("initParams")
    private List <PropertyJson > initParams = new ArrayList<>();

    /**
     * Target init parameters.
     *
     * @param fs
     *      current flipping strategy
     */
    public ToggleStrategyJson() {}
    
    /**
     * From {@link ToggleStrategyJson} to {@link ToggleStrategy}.
     *
     * @param ts
     */
    public ToggleStrategyJson(ToggleStrategy ts) {
        this.className  = ts.getClassName();
        this.initParams = ts.getProperties().map(PropertyJson::new)
                            .collect(Collectors.toList());
    }
    
    /**
     * Instanciate a ToggleStrategy, for a feature.
     *
     * @return
     *      instanciation for the feature
     */
    public ToggleStrategy asToggleStrategy(String uid) {
        return ToggleStrategy.of(uid, className, initParams.stream().map(PropertyJson::asProperty));
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
     * Getter accessor for attribute 'initParams'.
     *
     * @return
     *       current value of 'initParams'
     */
    public List<PropertyJson> getInitParams() {
        return initParams;
    }

    /**
     * Setter accessor for attribute 'initParams'.
     * @param initParams
     * 		new value for 'initParams '
     */
    public void setInitParams(List<PropertyJson> initParams) {
        this.initParams = initParams;
    }    
}
