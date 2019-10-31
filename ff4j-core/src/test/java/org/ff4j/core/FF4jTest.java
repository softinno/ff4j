package org.ff4j.core;

/*-
 * #%L
 * ff4j-core
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
import org.ff4j.feature.Feature;
import org.ff4j.test.AssertFF4j;
import org.ff4j.test.FF4jTestDataSet;
import org.ff4j.test.TestConfigurationParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test operations over {@link FF4j}
 * 
 * @author Cedrick Lunven (@clunven)
 */
@DisplayName("TMain FF4j Test Class")
public class FF4jTest implements FF4jTestDataSet {

    /** FF4J Instance for testings. */
    protected FF4j ff4j = null;

    /** Test Values */
    protected AssertFF4j assertFf4j = null;
    
    /** {@inheritDoc} */
    @BeforeEach
    public void init() {
        ff4j = new FF4j(new TestConfigurationParser().expectConfig());
        this.assertFf4j = new AssertFF4j(ff4j);
    }
    
    @Test
    public void should_enable_feature_when_toggle_on() {
        FF4j ff4j = new FF4j();
        Assertions.assertFalse(ff4j.getRepositoryFeatures().exists(FEATURE_FOR_TEST));
        ff4j.getRepositoryFeatures().save(new Feature(FEATURE_FOR_TEST).toggleOff());
        Assertions.assertFalse(ff4j.test(FEATURE_FOR_TEST));
        ff4j.toggleOn(FEATURE_FOR_TEST);
        Assertions.assertTrue(ff4j.test(FEATURE_FOR_TEST));
    }
    
    @Test
    public void should_disable_feature_when_toggle_off() {
        FF4j ff4j = new FF4j();
        Assertions.assertFalse(ff4j.getRepositoryFeatures().exists(FEATURE_FOR_TEST));
        ff4j.getRepositoryFeatures().save(new Feature(FEATURE_FOR_TEST).toggleOn());
        Assertions.assertTrue(ff4j.getRepositoryFeatures().exists(FEATURE_FOR_TEST));
        Assertions.assertTrue(ff4j.test(FEATURE_FOR_TEST));
        ff4j.toggleOff(FEATURE_FOR_TEST);
        Assertions.assertFalse(ff4j.test(FEATURE_FOR_TEST));
    }
 
}
