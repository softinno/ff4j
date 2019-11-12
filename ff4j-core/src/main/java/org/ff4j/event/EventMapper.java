package org.ff4j.event;

import org.ff4j.core.FF4jMapper;

/**
 * Specialization of mapper for Events
 * 
 * @author Cedrick LUNVEN (@clunven)
 *
 * @param <STORE_OBJ>
 */
public interface EventMapper < REQ, RES > extends FF4jMapper<Event, REQ, RES> {}
