package org.ff4j.test.jdbc;

import org.ff4j.core.jdbc.JdbcQueryBuilder;
import org.junit.jupiter.api.Test;

public class JdbcQueryBuilderTest {
    
    @Test
    public void showDDL() {
        System.out.println(new JdbcQueryBuilder().sqlDropSchema());
        System.out.println(new JdbcQueryBuilder().sqlCreateSchema());
    }
    
    

}
