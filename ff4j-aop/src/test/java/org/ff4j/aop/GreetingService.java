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

public class GreetingService {

    @ToggledFeature // valid but do nothinh
    public void done() {}
    
    @ToggledFeature("sayHello") // valid but only usage tracking
    public void doSomething() {}
    
    // Default behaviour
    @ToggledFeature(feature = "french", methodName="greetingsFR")
    public String greetings(String people) {
        return "Hello " + people;
    }
    
    // You can also provide name of external classes
    @ToggledFeature(feature = "french", methodName="greetingsFR", className = GreetingService.class) 
    public String extraClassSample(String people) {
        return "Hello " + people;
    }
    
    public String greetingsFR(String people) {
        return "Bonjour " + people;
    }

}

