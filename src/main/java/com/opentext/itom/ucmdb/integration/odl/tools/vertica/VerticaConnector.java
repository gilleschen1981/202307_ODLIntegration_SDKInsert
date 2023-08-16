package com.opentext.itom.ucmdb.integration.odl.tools.vertica;

import com.hp.ucmdb.api.types.TopologyCI;

import java.sql.*;
import java.util.List;

public class VerticaConnector {
    private String jdbcURL;
    private String dbuser;
    private String dbpassword;
    public void init(String verticaURL, String verticaUsername, String verticaPassword) {
        jdbcURL = verticaURL;
        dbuser = verticaUsername;
        dbpassword = verticaPassword;
    }

    public void insertCIs(String classname, List<TableColumnMeta> columnList, List<TopologyCI> ciList) {
        if(classname == null){
            System.out.println("[Vertica]Class type info wrong, classname = " + classname);
            return;
        }
        if(ciList == null || ciList.size() <= 0){
            System.out.println("[Vertica]No CI of type: " + classname);
            return;
        }
        Connection conn = null;
        try {
            Class.forName("com.vertica.jdbc.Driver");
            conn = DriverManager.getConnection(jdbcURL, dbuser,dbpassword);
            System.out.println("Connected to Vertica! Insert CIType: " + classname);
            String query = "insert into CMDB." +TableMeta.TABLE_PREFIX + classname + TableMeta.TABLE_SURFIX + " (";
            for(TableColumnMeta tableColumnMeta : columnList){
                query += tableColumnMeta.getColumnName() + ",";
            }
            query = query.substring(0, query.length() - 1) + ") values (";
            for(int i = 0; i < columnList.size(); i++){
                query += "?,";
            }
            query = query.substring(0, query.length() - 1) + ")";

            PreparedStatement pst = conn.prepareStatement(query);
            for(TopologyCI ci : ciList) {
                for (int i = 0; i < columnList.size(); i++) {
                    Object obj = ci.getPropertyValue(columnList.get(i).getColumnName());
                    // build statement
                    switch (columnList.get(i).getColumnType()){
                        case "VARCHAR":
                            pst.setString(i+1, obj==null?null:obj.toString());
                            break;
                        case "INTEGER":
                            pst.setInt(i+1, obj==null?0:Integer.valueOf(obj.toString()));
                            break;
                        case "BOOLEAN":
                            pst.setBoolean(i+1, obj==null?false:Boolean.valueOf(obj.toString()));
                            break;
                        case "DATE":
                            pst.setDate(i+1, obj==null?null:new Date(((java.util.Date)obj).getTime()));
                            break;
                        default:
                            System.out.println("[Vertica]Unknonw type, name = " + columnList.get(i).getColumnName() + ", type = " + columnList.get(0).getColumnType());
                            break;
                    }

                }
                pst.addBatch();
            }
            pst.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
