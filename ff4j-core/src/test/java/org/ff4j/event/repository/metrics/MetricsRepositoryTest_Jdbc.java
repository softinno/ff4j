package org.ff4j.event.repository.metrics;

import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.sql.DataSource;

import org.ff4j.event.repository.hit.FeatureHitRepositoryJdbc;
import org.ff4j.event.repository.hit.FeatureHitRepositorySupport;
import org.ff4j.feature.repository.FeatureRepositoryJdbc;
import org.ff4j.test.jdbc.JdbcTestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MetricsRepository_Jdbc_Test")
public class MetricsRepositoryTest_Jdbc extends AbstractTestSupportMetricsRepository {

    /** SQL DataSource. */
    private DataSource sqlDataSource;

    /** {@inheritDoc} */
    @Override
    protected FeatureHitRepositorySupport initRepository() {
        sqlDataSource = JdbcTestHelper.createInMemoryHQLDataSource();
        return new FeatureHitRepositoryJdbc(sqlDataSource);
    }

    /** {@inheritDoc} */
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        JdbcTestHelper.initDBSchema(sqlDataSource);
    }

    @Test
    public void should_throw_IllegalStateException_if_no_datasource() {
        assertThrows(IllegalStateException.class, () -> {
            new FeatureRepositoryJdbc().getDataSource();
        });
    }

    @Test
    public void should_init_ok() {
        FeatureRepositoryJdbc store = new FeatureRepositoryJdbc();
        sqlDataSource = JdbcTestHelper.createInMemoryHQLDataSource();
        store.setDataSource(sqlDataSource);
        Assertions.assertTrue(store.count() > 0);
    }

   

}
