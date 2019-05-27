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

import org.mockito.Mockito;


import org.ff4j.feature.exception.FeatureAccessException;
import org.ff4j.jdbc.JdbcUtils;
import org.junit.jupiter.api.Test;

/**
 * Enhancing test coverage ration by generating error on mocked data source. 
 */
public class RepositoryFeatureJdbcErrorsTest {
    
    @Test
    public void testExecuteUpdate()  throws SQLException {
        assertThrows(FeatureAccessException.class, () -> { 
            DataSource mockDS = Mockito.mock(DataSource.class);
            doThrow(new SQLException()).when(mockDS).getConnection();
            JdbcUtils.executeUpdate(mockDS, "toto"); 
        });
       
    }
    
}
