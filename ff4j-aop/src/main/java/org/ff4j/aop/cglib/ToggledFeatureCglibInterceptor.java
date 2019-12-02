package org.ff4j.aop.cglib;

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

import java.lang.reflect.Method;

import javax.lang.model.type.NullType;

import org.ff4j.aop.ToggledFeature;
import org.ff4j.core.FF4j;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
public class ToggledFeatureCglibInterceptor implements MethodInterceptor {
    
    private FF4j ff4j;
    
    public ToggledFeatureCglibInterceptor(FF4j myFF4j) {
        this.ff4j = myFF4j;
    }
    
    /** {@inheritDoc} */
    @Override
    public Object intercept(Object currentObject, Method method, Object[] args, MethodProxy proxy) 
    throws Throwable {
        ToggledFeature toggleAnn = method.getAnnotation(ToggledFeature.class);
        if (null != toggleAnn) {
            
            // Read feature name
            String featureName = toggleAnn.feature();
            if (!"".equals(toggleAnn.value())) {
                featureName = toggleAnn.value();
            }
            
            // Feature Name has been provided ...or do nothing
            if (!"".equals(featureName)) {
                // Log testing
                if (ff4j.test(featureName)) {
                    // Method Name has been provided ...or do nothing
                    String targetMethodName = toggleAnn.methodName();
                    if (!"".equals(targetMethodName)) {
                        // Is same class ?
                        Class<?> targetClass = toggleAnn.className();
                        if (NullType.class.equals(targetClass)) {
                            return currentObject.getClass().getMethod(targetMethodName, method.getParameterTypes())
                                                .invoke(currentObject, args);
                        } else {
                            return targetClass.getMethod(targetMethodName, method.getParameterTypes())
                                              .invoke(targetClass.getDeclaredConstructor().newInstance(), args);
                        }
                    } 
                }
            }
        }
        // To nothing
        return proxy.invokeSuper(currentObject, args);
    }

}
