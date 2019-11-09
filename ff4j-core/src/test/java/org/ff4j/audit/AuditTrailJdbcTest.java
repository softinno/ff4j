package org.ff4j.audit;

import javax.sql.DataSource;

import org.ff4j.test.jdbc.JdbcTestHelper;

/**
 * Testing live audit.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class AuditTrailJdbcTest /*extends AuditTrailTestSupport*/ {

    /** SQL DataSource. */
    private DataSource sqlDataSource;
    
    /** {@inheritDoc} */
    protected AuditTrailRepository initAuditTrailRepository() {
        sqlDataSource = JdbcTestHelper.createInMemoryHQLDataSource();
        return new AuditTrailRepositoryJdbc(sqlDataSource);
    }

}
