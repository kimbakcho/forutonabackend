package com.wing.forutona;

import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.spatial.SpatialFunction;
import org.hibernate.spatial.dialect.mysql.MySQL56SpatialDialect;
import org.hibernate.spatial.dialect.mysql.MySQLSpatialDialect;
import org.hibernate.type.DoubleType;
import org.hibernate.type.StandardBasicTypes;


public class CustomDialect extends MySQL56SpatialDialect {
        private MySQLSpatialDialect dialectDelegate = new MySQLSpatialDialect();

        public CustomDialect() {
            super();
            this.registerFunction("distance",new StandardSQLFunction("ST_Distance", StandardBasicTypes.DOUBLE));
            this.registerFunction("st_within",new SQLFunctionTemplate(StandardBasicTypes.INTEGER,"st_within(?1,?2) and 1"));
            this.registerFunction("match", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "match(?1) against (?2 in boolean mode) and 1"));
        }

    public String getSpatialRelateSQL(String columnName, int spatialRelation) {
        switch(spatialRelation) {
            case 0:
                return " ST_Equals(" + columnName + ", ?)";
            case 1:
                return " ST_Disjoint(" + columnName + ", ?)";
            case 2:
                return " ST_Touches(" + columnName + ", ?)";
            case 3:
                return " ST_Crosses(" + columnName + ", ?)";
            case 4:
                return " ST_Within(" + columnName + ",?)";
            case 5:
                return " ST_Overlaps(" + columnName + ", ?)";
            case 6:
                return " ST_Contains(" + columnName + ", ?)";
            case 7:
                return " ST_Intersects(" + columnName + ", ?)";
            default:
                throw new IllegalArgumentException("Spatial relation is not known by this dialect");
        }
    }

    public String getSpatialFilterExpression(String columnName) {
        return this.dialectDelegate.getSpatialFilterExpression(columnName);
    }

    public String getSpatialAggregateSQL(String columnName, int aggregation) {
        return this.dialectDelegate.getSpatialAggregateSQL(columnName, aggregation);
    }

    public String getDWithinSQL(String columnName) {
        return this.dialectDelegate.getDWithinSQL(columnName);
    }

    public String getHavingSridSQL(String columnName) {
        return this.dialectDelegate.getHavingSridSQL(columnName);
    }

    public String getIsEmptySQL(String columnName, boolean isEmpty) {
        return this.dialectDelegate.getIsEmptySQL(columnName, isEmpty);
    }

    public boolean supportsFiltering() {
        return this.dialectDelegate.supportsFiltering();
    }

    public boolean supports(SpatialFunction function) {
        return this.dialectDelegate.supports(function);
    }
}
