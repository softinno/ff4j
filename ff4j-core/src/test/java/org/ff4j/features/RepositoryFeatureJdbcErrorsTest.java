package org.ff4j.features;

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
import static org.mockito.Mockito.doThrow;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.ff4j.feature.Feature;
import org.ff4j.feature.exception.FeatureAccessException;
import org.ff4j.feature.exception.RepositoryAccessException;
import org.ff4j.feature.repository.FeatureRepository;
import org.ff4j.feature.repository.FeatureRepositoryJdbc;
import org.ff4j.jdbc.JdbcUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Enhancing test coverage ration by generating error on mocked data source. 
 */
@DisplayName("RepositoryFeature_Jdbc_TestErrors")
public class RepositoryFeatureJdbcErrorsTest {
    
    /** SQL DataSource. */
    private static DataSource sqlDataSourceMock;
    
    private static FeatureRepository repository;
    
    /** {@inheritDoc} */
    @BeforeAll
    public static void setUp() throws Exception {
        sqlDataSourceMock = Mockito.mock(DataSource.class);
        doThrow(new SQLException()).when(sqlDataSourceMock).getConnection();
        repository = new FeatureRepositoryJdbc(sqlDataSourceMock);
    }
    
    @Test
    public void testExecuteUpdate()  throws SQLException {
        assertThrows(RepositoryAccessException.class, () -> { 
            JdbcUtils.executeUpdate(sqlDataSourceMock, "toto"); 
        });
    }
    
    @Test
    public void should_throw_FeatureAccessException_when_invalid_datasource() {
        assertThrows(FeatureAccessException.class, repository::findAll);
        assertThrows(FeatureAccessException.class, repository::findAllIds);
        assertThrows(FeatureAccessException.class, repository::count);
        assertThrows(FeatureAccessException.class, repository::deleteAll);
        assertThrows(FeatureAccessException.class, repository::listGroupNames);
        assertThrows(FeatureAccessException.class, () -> {repository.exists("ok");});
        assertThrows(FeatureAccessException.class, () -> {repository.find("f1");});
        assertThrows(FeatureAccessException.class, () -> {repository.delete("ok");});
        assertThrows(FeatureAccessException.class, () -> {repository.existGroup("g1");});
        assertThrows(FeatureAccessException.class, () -> {repository.readGroup("g1");});
        assertThrows(FeatureAccessException.class, () -> {repository.saveFeature(new Feature("fx", true));});
        assertThrows(FeatureAccessException.class, () -> {repository.toggleOnGroup("g1");});
    }
    
}
