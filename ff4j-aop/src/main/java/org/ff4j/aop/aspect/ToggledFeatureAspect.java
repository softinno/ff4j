package org.ff4j.aop.aspect;

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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.ff4j.FF4j;
import org.ff4j.aop.ToggledFeature;

/**
 * Aspect defining Pointcut for FF4j to change default behaviour and operate
 * with features statuses.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Aspect
public class ToggledFeatureAspect {

    /**
     * Hold instance of Aspect
     */
    public static FF4j ff4j = new FF4j().withFeatureAutoCreate();
    
    /**
     * Defines a pointcut that we can use in any of moment :
     * @Before, @After, @AfterThrowing, @AfterReturning, @Around
     * 
     * But only when the annotation @ToggledFeature is present on method.
     */
    @Pointcut("@annotation(org.ff4j.aop.ToggledFeature)")
    public void toggledFeaturePointCutDefinition() {}

    /**
     * Defines a pointcut that we can use in any of moment :
     * @Before, @After, @AfterThrowing, @AfterReturning, @Around
     * 
     * The pointcut is a catch-all pointcut with the scope of execution
     */
    @Pointcut("execution(* *(..))")
    public void atFeatureEvaluation() {}

    /**
     * Pre-action before feature toggling. Could be use to log
     * but can add some overhead ?. 
     * 
     * @param pointcut
     *       parameters and method name
     */
    //@Before("toggledFeaturePointCutDefinition() && atFeatureEvaluation()")
    //public void preActionFeature(JoinPoint pointcut) {}
    
    /**
     * Change implementation at runtime.
     * 
     * @param joinPoint
     *    the reference of the call to the method.
     * @return
     *    returned value
     * @throws Throwable
     *    an error can occured and will be propagate
     */
    @Around("@annotation(org.ff4j.aop.ToggledFeature) && execution(* *(..))")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint)
    throws Throwable {
        try {
            // Parsing incoming call
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method          method    = signature.getMethod();
            ToggledFeature  toggleAnn = method.getAnnotation(ToggledFeature.class);
            Object          currentObject = joinPoint.getTarget();
            // Read feature name
            String featureName = toggleAnn.feature();
            if (!"".equals(toggleAnn.value())) {
                featureName = toggleAnn.value();
            }
            //System.out.println("+ Processing " + featureName);

            // Feature Name has been provided ...or do nothing
            if (!"".equals(featureName)) {
                String targetMethodName = toggleAnn.methodName();
                //System.out.println("+ Invoking " + targetMethodName);
                // Method Name has been provided ...or do nothing
                if (!"".equals(targetMethodName)) {
                    Class<?> targetClass = toggleAnn.className();
                    // From this point FF4j can be checked
                    //System.out.println("+ FF4j evaluation " + featureName);
                    if (ff4j.test(featureName)) {
                        if (NullType.class.equals(targetClass)) {
                            //System.out.println("+ In same bean");
                            return currentObject.getClass().getMethod(targetMethodName, signature.getParameterTypes())
                                                .invoke(currentObject, joinPoint.getArgs());
                        } else {
                            return targetClass.getMethod(targetMethodName, signature.getParameterTypes())
                                              .invoke(targetClass.getDeclaredConstructor().newInstance(), joinPoint.getArgs());
                        }
                    }
                }
            }
            // Do nothing
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        }
    }
   
}
