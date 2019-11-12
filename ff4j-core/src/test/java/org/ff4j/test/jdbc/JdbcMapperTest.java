package org.ff4j.test.jdbc;

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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.ff4j.core.jdbc.JdbcQueryBuilder;
import org.ff4j.core.jdbc.JdbcSchema;
import org.ff4j.feature.mapper.FeatureMapperJdbc;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class JdbcMapperTest implements JdbcSchema{
    
    public static JdbcQueryBuilder qBuilder = new JdbcQueryBuilder();
    
    @Test
    public void mapEntity_should_map_exception_sql_to_IllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> { 
            Connection sqlConn = Mockito.mock(Connection.class);
            ResultSet rs = Mockito.mock(ResultSet.class);
            doThrow(new SQLException()).when(rs).getTimestamp(COLUMN_CREATED);
            new FeatureMapperJdbc(sqlConn, qBuilder).mapEntity(rs, null);
        });
    }
    
    @Test
    public void mapEntity_sql_error_should_be_map_as_IllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> { 
            Connection sqlConn = Mockito.mock(Connection.class);
            ResultSet rs = Mockito.mock(ResultSet.class);
            doThrow(new SQLException()).when(rs).getTimestamp(COLUMN_CREATED);
            new FeatureMapperJdbc(sqlConn, qBuilder).mapEntity(rs, null);
        });
    }
}
