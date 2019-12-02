package org.ff4j.cassandra;

/*-
 * #%L
 * ff4j-store-cassandra
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

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createType;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.core.type.UserDefinedType;

/**
 * Group constants and schema for FF4j.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public interface FF4jCassandraRepository {
    
    /**
     * Reservation Keyspace constants (tables names, columns names)
     */
    CqlIdentifier TYPE_ADDRESS               = CqlIdentifier.fromCql("address");
    CqlIdentifier TABLE_RESERVATION_BY_HOTEL = CqlIdentifier.fromCql("reservations_by_hotel_date");
    CqlIdentifier TABLE_RESERVATION_BY_CONFI = CqlIdentifier.fromCql("reservations_by_confirmation");
    CqlIdentifier TABLE_RESERVATION_BY_GUEST = CqlIdentifier.fromCql("reservations_by_guest");
    CqlIdentifier TABLE_GUESTS               = CqlIdentifier.fromCql("guests");
    CqlIdentifier STREET                     = CqlIdentifier.fromCql("street");
    CqlIdentifier CITY                       = CqlIdentifier.fromCql("city");
    CqlIdentifier STATE_PROVINCE             = CqlIdentifier.fromCql("state_or_province");
    CqlIdentifier POSTAL_CODE                = CqlIdentifier.fromCql("postal_code");
    CqlIdentifier COUNTRY                    = CqlIdentifier.fromCql("country");
    CqlIdentifier HOTEL_ID                   = CqlIdentifier.fromCql("hotel_id");
    CqlIdentifier START_DATE                 = CqlIdentifier.fromCql("start_date");
    CqlIdentifier END_DATE                   = CqlIdentifier.fromCql("end_date");
    CqlIdentifier ROOM_NUMBER                = CqlIdentifier.fromCql("room_number");
    CqlIdentifier CONFIRMATION_NUMBER        = CqlIdentifier.fromCql("confirmation_number");
    CqlIdentifier GUEST_ID                   = CqlIdentifier.fromCql("guest_id");
    CqlIdentifier GUEST_LAST_NAME            = CqlIdentifier.fromCql("guest_last_name");
    CqlIdentifier FIRSTNAME                  = CqlIdentifier.fromCql("first_name");
    CqlIdentifier LASTNAME                   = CqlIdentifier.fromCql("last_name");
    CqlIdentifier TITLE                      = CqlIdentifier.fromCql("title");
    CqlIdentifier EMAILS                     = CqlIdentifier.fromCql("emails");
    CqlIdentifier PHONE_NUMBERS              = CqlIdentifier.fromCql("phone_numbers");
    CqlIdentifier ADDRESSES                  = CqlIdentifier.fromCql("addresses");
    
    
    /**
     * Create Keyspace and relevant tables as per defined in 'reservation.cql'.
     *
     * @param cqlSession
     *          connectivity to Cassandra
     * @param keyspaceName
     *          keyspace name
     */
     default void createTables(CqlSession cqlSession, CqlIdentifier keyspaceName) {
        
        /**
         * Create TYPE 'Address' if not exists
         * 
         * CREATE TYPE reservation.address (
         *   street text,
         *   city text,
         *   state_or_province text,
         *   postal_code text,
         *   country text
         * );
         */
        cqlSession.execute(
                createType(keyspaceName, TYPE_ADDRESS)
                .ifNotExists()
                .withField(STREET, DataTypes.TEXT)
                .withField(CITY, DataTypes.TEXT)
                .withField(STATE_PROVINCE, DataTypes.TEXT)
                .withField(POSTAL_CODE, DataTypes.TEXT)
                .withField(COUNTRY, DataTypes.TEXT)
                .build());
        
        /** 
         * CREATE TABLE reservation.reservations_by_hotel_date (
         *  hotel_id text,
         *  start_date date,
         *  end_date date,
         *  room_number smallint,
         *  confirmation_number text,
         *  guest_id uuid,
         *  PRIMARY KEY ((hotel_id, start_date), room_number)
         * ) WITH comment = 'Q7. Find reservations by hotel and date';
         */
        cqlSession.execute(createTable(keyspaceName, TABLE_RESERVATION_BY_HOTEL)
                        .ifNotExists()
                        .withPartitionKey(HOTEL_ID, DataTypes.TEXT)
                        .withPartitionKey(START_DATE, DataTypes.DATE)
                        .withClusteringColumn(ROOM_NUMBER, DataTypes.SMALLINT)
                        .withColumn(END_DATE, DataTypes.DATE)
                        .withColumn(CONFIRMATION_NUMBER, DataTypes.TEXT)
                        .withColumn(GUEST_ID, DataTypes.UUID)
                        .withClusteringOrder(ROOM_NUMBER, ClusteringOrder.ASC)
                        .withComment("Q7. Find reservations by hotel and date")
                        .build());
        
        /**
         * CREATE TABLE reservation.reservations_by_confirmation (
         *   confirmation_number text PRIMARY KEY,
         *   hotel_id text,
         *   start_date date,
         *   end_date date,
         *   room_number smallint,
         *   guest_id uuid
         * );
         */
        cqlSession.execute(createTable(keyspaceName, TABLE_RESERVATION_BY_CONFI)
                .ifNotExists()
                .withPartitionKey(CONFIRMATION_NUMBER, DataTypes.TEXT)
                .withColumn(HOTEL_ID, DataTypes.TEXT)
                .withColumn(START_DATE, DataTypes.DATE)
                .withColumn(END_DATE, DataTypes.DATE)
                .withColumn(ROOM_NUMBER, DataTypes.SMALLINT)
                .withColumn(GUEST_ID, DataTypes.UUID)
                .build());
         
         /**
          * CREATE TABLE reservation.reservations_by_guest (
          *  guest_last_name text,
          *  hotel_id text,
          *  start_date date,
          *  end_date date,
          *  room_number smallint,
          *  confirmation_number text,
          *  guest_id uuid,
          *  PRIMARY KEY ((guest_last_name), hotel_id)
          * ) WITH comment = 'Q8. Find reservations by guest name';
          */
         cqlSession.execute(createTable(keyspaceName, TABLE_RESERVATION_BY_GUEST)
                 .ifNotExists()
                 .withPartitionKey(GUEST_LAST_NAME, DataTypes.TEXT)
                 .withClusteringColumn(HOTEL_ID, DataTypes.TEXT)
                 .withColumn(START_DATE, DataTypes.DATE)
                 .withColumn(END_DATE, DataTypes.DATE)
                 .withColumn(ROOM_NUMBER, DataTypes.SMALLINT)
                 .withColumn(CONFIRMATION_NUMBER, DataTypes.TEXT)
                 .withColumn(GUEST_ID, DataTypes.UUID)
                 .withComment("Q8. Find reservations by guest name")
                 .build());
          
          /**
           * CREATE TABLE reservation.guests (
           *   guest_id uuid PRIMARY KEY,
           *   first_name text,
           *   last_name text,
           *   title text,
           *   emails set<text>,
           *   phone_numbers list<text>,
           *   addresses map<text, frozen<address>>,
           *   confirmation_number text
           * ) WITH comment = 'Q9. Find guest by ID';
           */
          UserDefinedType  udtAddressType = 
                  cqlSession.getMetadata().getKeyspace(keyspaceName).get() // Retrieving KeySpaceMetadata
                            .getUserDefinedType(TYPE_ADDRESS).get();        // Looking for UDT (extending DataType)
          cqlSession.execute(createTable(keyspaceName, TABLE_GUESTS)
                  .ifNotExists()
                  .withPartitionKey(GUEST_ID, DataTypes.UUID)
                  .withColumn(FIRSTNAME, DataTypes.TEXT)
                  .withColumn(LASTNAME, DataTypes.TEXT)
                  .withColumn(TITLE, DataTypes.TEXT)
                  .withColumn(EMAILS, DataTypes.setOf(DataTypes.TEXT))
                  .withColumn(PHONE_NUMBERS, DataTypes.listOf(DataTypes.TEXT))
                  .withColumn(ADDRESSES, DataTypes.mapOf(DataTypes.TEXT, udtAddressType, true))
                  .withColumn(CONFIRMATION_NUMBER, DataTypes.TEXT)
                  .withComment("Q9. Find guest by ID")
                  .build());
    }

}
