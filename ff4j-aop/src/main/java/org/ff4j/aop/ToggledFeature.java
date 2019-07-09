package org.ff4j.aop;

/*-
 * #%L
 * ff4j-aop
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.lang.model.type.NullType;

/**
 * Feature Toggling Annotation.
 * 
 * By annotating the target method the advisor could intercept method call 
 * and subsitute with alter class or mock class.
 * 
 * @since v2
 * @author <a href="mailto:cedrick.lunven@gmail.com">Cedrick LUNVEN</a>
 * @author <a href="https://github.com/ghostd">Vincent Ricard</a>
 */
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ToggledFeature {
    
    /**
     * Unique identifier for target feature.
     *
     * @return feature identifier.
     */
    String feature() default "";
    
    /**
     * Unique identifier for target feature.
     *
     * @return feature identifier.
     */
    String value() default "";
    
    /**
     * [Optional] define method name to be invoked if related feature is `ON`
     * 
     * @return existing method name
     */
    String methodName() default "";
    
    /**
     * [Optional] define method name to be invoked if related feature is `ON`
     * 
     * @return existing method name
     */
    Class<?> className() default NullType.class;
    
}

