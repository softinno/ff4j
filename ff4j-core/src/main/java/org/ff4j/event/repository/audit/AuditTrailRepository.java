package org.ff4j.event.repository.audit;

import org.ff4j.event.Event;
import org.ff4j.event.EventSeries;

/**
 * Audit Trail is READ ONLY.
 *
 * @author Cedrick LUNVEN  (@clunven)
 */
public interface AuditTrailRepository {
    
    /**
     * Create tables related to Audit Trail.
     */
    void createSchema();
    
    /**
     * Insert new event in the DB.
     *
     * @param evt
     *      current event
     */
    void log(Event evt);
    
    /**
     * Search events in the audit trail.
     *
     * @param query
     *      target query
     * @return
     */
    EventSeries search(AuditTrailQuery query);
    
    /**
     * Will delete log record matching the query. Will create a new record line to notify.
     *
     * @param query
     *      current query
     */
    void purge(AuditTrailQuery query);
}
