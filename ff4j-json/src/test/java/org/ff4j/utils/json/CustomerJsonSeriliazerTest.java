package org.ff4j.utils.json;

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

import org.ff4j.core.test.FF4jTestDataSet;
import org.ff4j.feature.Feature;
import org.ff4j.json.domain.FeatureJson;
import org.ff4j.json.domain.PropertyJson;
import org.ff4j.property.Property;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Testing Json Serialization.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class CustomerJsonSeriliazerTest implements FF4jTestDataSet {
    
    @Test
    public void marshall_feature_json_should_not_loss_info() {
        for (Feature tmpFeat : expectedFeatures().values()) {
            FeatureJson fj      = new FeatureJson(tmpFeat);
            FeatureJson after   = new FeatureJson(fj.toJson());
            System.out.println(after.toJson());
            Assertions.assertEquals(fj.toJson(), after.toJson());
        }
    }
    
    @Test
    public void marshall_properties_json_should_not_loss_info() {
        for (Property<?> tmpProp : expectedProperties().values()) {
            PropertyJson pj      = new PropertyJson(tmpProp);
            PropertyJson after   = new PropertyJson(pj.toJson());
            System.out.println(after.toJson());
            Assertions.assertEquals(pj.toJson(), after.toJson());
        }
    }

}
