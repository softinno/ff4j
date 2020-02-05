INSERT INTO ff4j_settings(param_name, param)  VALUES ('autocreate', { uid: 'autocreate', class_name: 'java.lang.Boolean' , value: 'true'} );
INSERT INTO ff4j_settings(param_name, param)  VALUES ('audit',      { uid: 'audit', class_name: 'java.lang.Boolean' , value: 'true'} );

INSERT INTO ff4j_role(role_name, permissions)  VALUES ('EVERYONE', {'VIEW_FEATURES', 'VIEW_PROPERTIES'});
INSERT INTO ff4j_role(role_name, permissions)  VALUES ('USER', {'VIEW_FEATURES', 'VIEW_PROPERTIES', 'VIEW_AUDITTRAIL', 'VIEW_FEATUREUSAGE'});
INSERT INTO ff4j_role(role_name, permissions)  VALUES ('SUPERUSER', {'TOGGLE_FEATURES',  'VIEW_FEATURES', 'VIEW_PROPERTIES', 'VIEW_AUDITTRAIL', 'VIEW_FEATUREUSAGE'});
INSERT INTO ff4j_role(role_name, permissions)  VALUES ('ADMIN_FEATURES', { 'ADMIN_PROPERTIES', 'ADMINISTRATOR', 'TOGGLE_FEATURES',  'VIEW_FEATURES', 'VIEW_PROPERTIES', 'VIEW_AUDITTRAIL', 'VIEW_FEATUREUSAGE'});

INSERT INTO ff4j_user(uid, description, firstname, lastname, permissions, roles) 
VALUES ('john', 'sample description if OK', 'John', 'Connor', {'ADMIN_FEATURES'}, {'ADMINISTRATOR'});
INSERT INTO ff4j_user(uid, description, firstname, lastname, permissions, roles) 
VALUES ('sarah', 'sample description if OK', 'Sarah', 'Connor', {'FEATURE_VIEW'}, {'USER'});

INSERT INTO ff4j_feature(uid, enable, description) VALUES ('f1', false, 'some desc');
INSERT INTO ff4j_feature(uid, enable, description, group_name, permissions_roles, permissions_users, properties, toggle_strategies) 
VALUES ('f2', true, 'description', 'GRP1', 
    { 'FEATURE_VIEW':   {'EVERYONE'}}, -- roles permissions
    { 'FEATURE_TOGGLE': {'john'} },    -- users permissions
    -- properties
    { 'ppint':      {uid:'ppint',      class_name:'int', value:'12'    },
      'ppdouble':   {uid:'ppdouble',   class_name:'double',  value:'12.5'  },
      'ppboolean':  {uid:'ppboolean',  class_name:'boolean', value:'true'  },
      'ppstring':   {uid:'ppstring',   class_name:'string',  value:'hello' },
      'myLogLevel': {uid:'myLogLevel', class_name:'logLevel',          value:'DEBUG' },
      'ppListInt':  {uid:'ppListInt',  class_name:'listInt',           value:'12,13,14' },
      'digitValue': {uid:'digitValue', class_name:'org.ff4j.property.PropertyInteger',  value:'1',  fixed_values:{'0','1','2','3'} },
      'regionIdentifier': {uid:'regionIdentifier', class_name:'string',       value:'NA', fixed_values:{'NA','EMEA','APAC'} }
    },
    -- toggle Strategies
    [{class_name:'org.ff4j.feature.togglestrategy.PonderationToggleStrategy', params:{
        'weight': {uid:'weight', class_name:'double', value:'1'}
    }}] 
);
INSERT INTO ff4j_feature(uid, enable, description, group_name, permissions_roles) 
VALUES ('f3', true, 'description', 'GRP0',  { 'FEATURE_VIEW':   {'USER'}});
INSERT INTO ff4j_feature(uid, enable, description, group_name, toggle_strategies) 
VALUES ('f4', true, 'description', 'GRP1',
    -- toggle Strategies
    [{class_name:'org.ff4j.feature.togglestrategy.expression.ExpressionToggleStrategy', params:{
        'expression': {uid:'expression', class_name:'string', value:' f3 | f2'}
    }}] 
);


INSERT INTO ff4j_property(uid, param) VALUES ('pBigDecimal', 	 { uid:'pBigDecimal', 	  class_name:'bigDecimal',     value:'1.5'});
INSERT INTO ff4j_property(uid, param) VALUES ('pBigInteger', 	 { uid:'pBigInteger', 	  class_name:'bigInteger',     value:'123456'});
INSERT INTO ff4j_property(uid, param) VALUES ('pBoolean',    	 { uid:'pBoolean',    	  class_name:'boolean',        value:'true'});
INSERT INTO ff4j_property(uid, param) VALUES ('pByte',       	 { uid:'pByte',       	  class_name:'byte',           value:'p'});
INSERT INTO ff4j_property(uid, param) VALUES ('pCalendar',   	 { uid:'pCalendar',   	  class_name:'calendar',       value:' 2018-12-24 23:00:00'});
INSERT INTO ff4j_property(uid, param) VALUES ('pDate',       	 { uid:'pDate',       	  class_name:'date',           value:' 2018-12-24 23:00:00'});
INSERT INTO ff4j_property(uid, param) VALUES ('pClass',      	 { uid:'pClass',      	  class_name:'class',          value:' 123456'});
INSERT INTO ff4j_property(uid, param) VALUES ('pDouble',    	 { uid:'pDouble',     	  class_name:'double',         value:'20.0'});
INSERT INTO ff4j_property(uid, param) VALUES ('pFloat',      	 { uid:'pFloat',      	  class_name:'float',          value:'20.0'});
INSERT INTO ff4j_property(uid, param) VALUES ('pInstant',    	 { uid:'pInstant',    	  class_name:'instant',        value:'2018-12-24 23:00:00'});
INSERT INTO ff4j_property(uid, param) VALUES ('pLocal',      	 { uid:'pLocal',      	  class_name:'localDateTime',  value:'2018-12-24 23:00:00'});
INSERT INTO ff4j_property(uid, param) VALUES ('pInt',        	 { uid:'pInt',        	  class_name:'int',            value:'10'});
INSERT INTO ff4j_property(uid, param) VALUES ('pLogLevel',   	 { uid:'pLogLevel',   	  class_name:'logLevel',       value:'INFO'});
INSERT INTO ff4j_property(uid, param) VALUES ('pLong',       	 { uid:'pLong',       	  class_name:'long',           value:'123'});
INSERT INTO ff4j_property(uid, param) VALUES ('pShort',      	 { uid:'pShort',      	  class_name:'short',          value:'5'});
INSERT INTO ff4j_property(uid, param) VALUES ('pString',     	 { uid:'pString',     	  class_name:'string',         value:'pString'});
INSERT INTO ff4j_property(uid, param) VALUES ('pListBigDecimal', { uid:'pListBigDecimal', class_name:'listBigDecimal', value:'1.5,2.5'});
INSERT INTO ff4j_property(uid, param) VALUES ('pListBigInteger', { uid:'pListBigInteger', class_name:'listBigInteger', value:'123456,789012'});
INSERT INTO ff4j_property(uid, param) VALUES ('pListBoolean',    { uid:'pListBoolean',    class_name:'listBoolean',    value:'true,false'});
INSERT INTO ff4j_property(uid, param) VALUES ('pListByte',       { uid:'pListByte',       class_name:'listByte',       value:'p,l'});
INSERT INTO ff4j_property(uid, param) VALUES ('pListCalendar',   { uid:'pListCalendar',   class_name:'listCalendar',   value:'018-12-24 23:00:00,2019-01-01 23:00:00'});
INSERT INTO ff4j_property(uid, param) VALUES ('pListDate',       { uid:'pListDate',       class_name:'listDate',       value:'018-12-24 23:00:00,2019-01-01 23:00:00'});
INSERT INTO ff4j_property(uid, param) VALUES ('pListClass',      { uid:'pListClass',      class_name:'listClass',      value:'java.lang.String,java.lang.Integer'});
INSERT INTO ff4j_property(uid, param) VALUES ('pListDouble',     { uid:'pListDouble',     class_name:'listDouble',     value:'20.0,30.0'});
INSERT INTO ff4j_property(uid, param) VALUES ('pListFloat',      { uid:'pListFloat',      class_name:'listFloat',      value:'20.0,30.0'});
INSERT INTO ff4j_property(uid, param) VALUES ('plistInstant',    { uid:'plistInstant',    class_name:'listInstant',    value:'2018-12-24 23:00:00,2019-01-01 23:00:00'});
INSERT INTO ff4j_property(uid, param) VALUES ('pListInt',        { uid:'pListInt',        class_name:'listInt',        value:'10,20'});
INSERT INTO ff4j_property(uid, param) VALUES ('pListLogLevel',   { uid:'pListLogLevel',   class_name:'listLogLevel',   value:'INFO,DEBUG'});
INSERT INTO ff4j_property(uid, param) VALUES ('pListLong',       { uid:'pListLong',       class_name:'listLong',       value:'123,456'});
INSERT INTO ff4j_property(uid, param) VALUES ('pListShort',      { uid:'pListShort',      class_name:'listShort',      value:'5,6'});
INSERT INTO ff4j_property(uid, param) VALUES ('pListString',     { uid:'pListString',     class_name:'listString',     value:'pString,listString'});
INSERT INTO ff4j_property(uid, param) VALUES ('pListLocal',      { uid:'pListLocal',      class_name:'listLocalDateTime', value:'2018-12-24 23:00:00,2019-01-01 23:00:00'});
