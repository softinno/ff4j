package org.ff4j.store.xml;

import org.ff4j.parser.xml.XmlParserV2;
import org.ff4j.test.RepositoryRolesAndUsersTestSupport;

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

@DisplayName("RepositoryRolesAndUsers::InMemory with XML")
public class RepositoryRolesAndUsersInMemoryTest extends RepositoryRolesAndUsersTestSupport {

    @Override
    public RolesAndUsersRepository initStore() {
        return new RolesAndUsersRepositoryInMemory(new XmlParserV2(), "ff4j-testDataset.xml");
    }

}
