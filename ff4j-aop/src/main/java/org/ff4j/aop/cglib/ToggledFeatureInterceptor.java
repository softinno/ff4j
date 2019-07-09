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

import org.ff4j.FF4j;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
public class ToggledFeatureInterceptor implements MethodInterceptor {
    
    private FF4j ff4j;
    
    public ToggledFeatureInterceptor(FF4j myFF4j) {
        this.ff4j = myFF4j;
    }
    
    /** {@inheritDoc} */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) 
    throws Throwable {
        // Intercept only if annotation presents
        
        System.out.println("OK");
        Object targetReturn = proxy.invokeSuper(obj, args);
        return targetReturn;
    }

}
