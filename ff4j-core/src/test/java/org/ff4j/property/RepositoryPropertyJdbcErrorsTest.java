package org.ff4j.property;

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

import org.ff4j.feature.exception.RepositoryAccessException;
import org.ff4j.jdbc.JdbcUtils;
import org.ff4j.property.exception.PropertyAccessException;
import org.ff4j.property.repository.PropertyRepositoryJdbc;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Enhancing test coverage ration by generating error on mocked data source. 
 */
@DisplayName("RepositoryProperty_Jdbc_Test")
public class RepositoryPropertyJdbcErrorsTest {
    
    /** SQL DataSource. */
    private static DataSource sqlDataSourceMock;
    
    private static PropertyRepositoryJdbc repository;
    
    /** {@inheritDoc} */
    @BeforeAll
    public static void setUp() throws Exception {
        sqlDataSourceMock = Mockito.mock(DataSource.class);
        doThrow(new SQLException()).when(sqlDataSourceMock).getConnection();
        repository = new PropertyRepositoryJdbc(sqlDataSourceMock);
    }
    
    @Test
    public void testExecuteUpdate()  throws SQLException {
        assertThrows(RepositoryAccessException.class, () -> { 
            JdbcUtils.executeUpdate(sqlDataSourceMock, "toto"); 
        });
    }
    
    @Test
    public void should_throw_FeatureAccessException_when_invalid_datasource() {
        assertThrows(PropertyAccessException.class, repository::findAll);
        assertThrows(PropertyAccessException.class, repository::findAllIds);
        assertThrows(PropertyAccessException.class, repository::count);
        assertThrows(PropertyAccessException.class, repository::deleteAll);
        assertThrows(PropertyAccessException.class, () -> {repository.exists("ok");});
        assertThrows(PropertyAccessException.class, () -> {repository.find("f1");});
        assertThrows(PropertyAccessException.class, () -> {repository.delete("ok");});
        assertThrows(PropertyAccessException.class, () -> {repository.deleteProperty("ok");});
        assertThrows(PropertyAccessException.class, () -> {repository.saveProperty(new PropertyString("a", "b"));});
        assertThrows(PropertyAccessException.class, () -> {repository.updatePropertyValue("p1", "p2");});
    }
    
}
