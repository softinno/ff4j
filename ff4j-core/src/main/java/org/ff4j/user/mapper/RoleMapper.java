package org.ff4j.user.mapper;

import org.ff4j.core.FF4jMapper;
import org.ff4j.user.FF4jRole;

/**
 * Specialization of the interface.
 *
 * @author Cedrick LUNVEN (@clunven)
 *
 * @param <STORE_OBJ>
 *      target driver object.
 */
public interface RoleMapper <REQ , RES> extends FF4jMapper < FF4jRole, REQ, RES > {}
