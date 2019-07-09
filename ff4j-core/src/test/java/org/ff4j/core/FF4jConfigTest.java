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
import org.ff4j.parser.FF4jConfigFile;
import org.ff4j.test.TestConfigurationParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FF4jConfigTest {

    @Test
    @DisplayName(value = "")
    public void init_config_should_filled_fields() {
        // Given
        FF4j ff4j = new FF4j(new TestConfigurationParser().expectConfig());
        // When
        FF4jConfigFile ff4jConfig = new FF4jConfigFile(ff4j);
        Assertions.assertNotNull(ff4jConfig);
        
        
    }
}
