package com.opentext.itom.ucmdb.integration.odl.tools.vertica;

import java.util.ArrayList;
import java.util.List;

public class TableMeta {
    public static final String TABLE_PREFIX = "cmdb_";
    public static final String TABLE_SURFIX = "";
    private String className;
    private List<TableColumnMeta> columnList;

    public TableMeta(String tableName) {
        this.className = tableName;
        columnList = new ArrayList<TableColumnMeta>();
    }

    public String getClassName() {
        return className;
    }

    public String getTableName() {
        return TABLE_PREFIX + className + TABLE_SURFIX;
    }

    public List<TableColumnMeta> getColumnList() {
        if(columnList == null){
            columnList = new ArrayList<TableColumnMeta>();
        }
        return columnList;
    }

    public List<String> getColumnListForTable() {
        List<String> rlt = new ArrayList<String>();
        for(TableColumnMeta columnMeta : getColumnList()){
            rlt.add(ModelConverter.convertColumnMeta2TableType(columnMeta));
        }
        return rlt;
    }
}