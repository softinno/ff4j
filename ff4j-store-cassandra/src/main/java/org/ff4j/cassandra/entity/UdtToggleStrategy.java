package org.ff4j.cassandra.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.ff4j.cassandra.driver.CassandraSchema;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

/**
 * As `ff4j_feature` table handle UDT and nested structure you may need
 * dynamic CQL queries. Mapper can be handy here.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Entity
@CqlName(CassandraSchema.UDT_TOGGLESTRATEGY)
public class UdtToggleStrategy implements CassandraSchema, Serializable {
    
    /** Serial. */
    private static final long serialVersionUID = 1300445327805535855L;

    @CqlName(UDT_TOGGLESTRATEGY_CLASSNAME)
    private String classname;
    
    @CqlName(UDT_TOGGLESTRATEGY_PARAMS)
    private Map<String, UdtParam > params = new HashMap<>();

    /**
     * Default Constructor
     */
    public UdtToggleStrategy() {}
    
    /**
     * Getter accessor for attribute 'classname'.
     *
     * @return
     *       current value of 'classname'
     */
    public String getClassname() {
        return classname;
    }

    /**
     * Setter accessor for attribute 'classname'.
     * @param classname
     * 		new value for 'classname '
     */
    public void setClassname(String classname) {
        this.classname = classname;
    }

    /**
     * Getter accessor for attribute 'params'.
     *
     * @return
     *       current value of 'params'
     */
    public Map<String, UdtParam> getParams() {
        return params;
    }

    /**
     * Setter accessor for attribute 'params'.
     * @param params
     * 		new value for 'params '
     */
    public void setParams(Map<String, UdtParam> params) {
        this.params = params;
    }
    
    
}
