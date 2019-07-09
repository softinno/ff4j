package org.ff4j.user;

import org.ff4j.test.TestConfigurationParser;

/*-
 * #%L
 * ff4j-core
 * %%
 * Copyright (C) 2013 - 2018 FF4J
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

import org.ff4j.user.repository.RolesAndUsersRepository;
import org.ff4j.user.repository.RolesAndUsersRepositoryInMemory;
import org.junit.jupiter.api.DisplayName;

@DisplayName("RepositoryRolesAndUsers::In Memory with expected Data set")
public class RepositoryRolesAndUsers_Test_InMemory extends RepositoryRolesAndUsersTestSupport {

    @Override
    public RolesAndUsersRepository initStore() {
        return new RolesAndUsersRepositoryInMemory(new TestConfigurationParser(), "ff4j-testDataset.xml");
    }

}
