package org.ff4j.cassandra.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.ff4j.cassandra.driver.CassandraSchema;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

/**
 * As `ff4j_feature` table handle UDT and nested structure you may need
 * dynamic CQL queries. Mapper can be handy here.
 * 
 * CREATE TYPE IF NOT EXISTS ff4j_param (
 *   uid          text,
 *   class_name   text,
 *   value        text,
 *   fixed_values set<text>,
 *   description  text
 * );
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Entity
@CqlName(CassandraSchema.UDT_PARAM)
public class UdtParam implements CassandraSchema, Serializable {

    /** Serial. */
    private static final long serialVersionUID = -5566880842889605384L;
    
    @CqlName(UDT_PARAM_UID)
    private String uid;
    
    @CqlName(UDT_PARAM_CLASSNAME)
    private String className;
    
    @CqlName(UDT_PARAM_VALUE)
    private String value;
    
    @CqlName(UDT_PARAM_FIXEDVALUES)
    private Set<String> fixedValues = new HashSet<>();
    
    @CqlName(UDT_PARAM_DESCRIPTION)
    private String description;
    
    /** Default Constructor. */
    public UdtParam() {}

    /**
     * Getter accessor for attribute 'uid'.
     *
     * @return
     *       current value of 'uid'
     */
    public String getUid() {
        return uid;
    }

    /**
     * Setter accessor for attribute 'uid'.
     * @param uid
     * 		new value for 'uid '
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Getter accessor for attribute 'className'.
     *
     * @return
     *       current value of 'className'
     */
    public String getClassName() {
        return className;
    }

    /**
     * Setter accessor for attribute 'className'.
     * @param className
     * 		new value for 'className '
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Getter accessor for attribute 'value'.
     *
     * @return
     *       current value of 'value'
     */
    public String getValue() {
        return value;
    }

    /**
     * Setter accessor for attribute 'value'.
     * @param value
     * 		new value for 'value '
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Getter accessor for attribute 'fixedValues'.
     *
     * @return
     *       current value of 'fixedValues'
     */
    public Set<String> getFixedValues() {
        return fixedValues;
    }

    /**
     * Setter accessor for attribute 'fixedValues'.
     * @param fixedValues
     * 		new value for 'fixedValues '
     */
    public void setFixedValues(Set<String> fixedValues) {
        this.fixedValues = fixedValues;
    }

    /**
     * Getter accessor for attribute 'description'.
     *
     * @return
     *       current value of 'description'
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter accessor for attribute 'description'.
     * @param description
     * 		new value for 'description '
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void addFixedValue(String v) {
        this.fixedValues.add(v);
    }

}
