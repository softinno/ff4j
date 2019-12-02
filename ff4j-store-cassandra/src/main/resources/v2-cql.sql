-- Keyspace
create keyspace ff4j WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 };
use ff4j;

-- security for feature of a group
-- Feature doSomething
-- Roles ADMIN_APP_X
-- Group BusinessFunctionX


CREATE TYPE ff4j_param (
   uid          text,
   class_name   text,
   value        text,
   fixed_values set<text>,
   description  text
)

CREATE TYPE ff4j_grantees (
   permission  text,
   roles       set<text>,
   users       set<text>
)

CREATE TABLE IF NOT EXIST permissions_by_role (
   role_name    text,
   permission   text,
   PRIMARY KEY ((role_name), permission)
);

CREATE TYPE toggle_strategy (
   class_name text,
   parameters map<text,frozen<ff4j_property>>
)

CREATE TABLE IF NOT EXISTS features (
   uid                text, 
   created            timestamp,
   last_modified      timestamp,
   owner              text,
   description        text,
   enable             boolean,
   groupname          text,
   toggle_strategies  list<frozen<toggle_strategy>>
   properties         list<frozen<ff4j_param>>
   permissions        list<frozen<ff4j_grantees>>
   PRIMARY KEY ((uid))
);

CREATE TABLE IF NOT EXISTS features_by_group (
  groupname           text,
  feature_uid         text,
  PRIMARY KEY ((groupname), feature_uid)
);

CREATE TABLE IF NOT EXISTS properties (
   uid           text, 
   created       timestamp,
   last_modified timestamp,
   owner         text,
   description   text,
   class_name    text,
   value         text,
   fixed_values  set<text>,
   PRIMARY KEY (UID)
);

CREATE TABLE IF NOT EXISTS users (
   uid           text, 
   created       timestamp,
   last_modified timestamp,
   owner         text,
   description   text,
   firstname     text,
   lastname      text,
   permissions   set<text>,
   roles         set<text>
   PRIMARY KEY ((uid))
);
-- create table audit (group per date)
CREATE TABLE ff4j.audit (
    uid varchar,
    date varchar,
    time timestamp,
    type varchar,
    name varchar,
    action varchar,
    hostName varchar,
    source varchar,
    duration int,
    user varchar,
    value varchar,
    custom map<varchar,varchar>,
    PRIMARY KEY (UID)
);




