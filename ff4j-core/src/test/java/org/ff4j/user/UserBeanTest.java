package org.ff4j.user;

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

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.ff4j.core.exception.AssertionViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Work with Grantees, roles and users.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class UserBeanTest {

    @Test
    public void should_initialize_by_name() {
        FF4jUser user = new FF4jUser("username");
        Assertions.assertEquals(user.getUid(), "username");
    }
    
    @Test
    public void should_throw_AssertionViolationException_when_null() {
        assertThrows(AssertionViolationException.class, () -> { new FF4jUser(null); });
    }
    
}
