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

import org.ff4j.FF4j;
import org.ff4j.aop.GreetingService;
import org.ff4j.feature.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit test some aspect.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
public class ToggleAspectTest {

    @Test
    @DisplayName("Unit testing of aspects")
    public void testYourAspect() {
        FF4j ff4j = new FF4j().withFeatureAutoCreate()
                              .withoutFeatureUsageTracking()
                              .withoutAudit()
                              .addFeature(new Feature("french", false));
        ToggledFeatureAspect.ff4j = ff4j;
        
        GreetingService service = new GreetingService();
        System.out.println(service.greetings("Cedrick"));
        
        ff4j.toggleOn("french");
        System.out.println(service.greetings("Cedrick"));
    }
    
}
