package com.wing.forutona.FcubeDto;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class FcubeStateTypeHandler extends BaseTypeHandler<FcubeState> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, FcubeState fcubeState, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i,fcubeState.getValue());
    }

    @Override
    public FcubeState getNullableResult(ResultSet resultSet, String s) throws SQLException {

        return FcubeState.getEnum(resultSet.getInt(s));
    }

    @Override
    public FcubeState getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return FcubeState.getEnum(resultSet.getInt(i));
    }

    @Override
    public FcubeState getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return FcubeState.getEnum(callableStatement.getInt(i));
    }
}
