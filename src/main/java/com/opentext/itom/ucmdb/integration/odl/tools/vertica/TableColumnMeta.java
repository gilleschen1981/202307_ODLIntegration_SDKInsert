package com.opentext.itom.ucmdb.integration.odl.tools.vertica;

public class TableColumnMeta {
    public static final String RELATION_TABLE_ENDA_COLUMNNAME = "end1";
    public static final String RELATION_TABLE_ENDB_COLUMNNAME = "end2";
    private String columnName;
    private String columnType;
    private int columnSize;

    public TableColumnMeta(String columnName, String columnType, int columnSize) {
        if("VARCHAR".equals(columnType) && columnSize <= 0){
            System.out.println("[CONVERT]Missing size information, use default value. AttrName = " + columnName);
            columnSize = 100;
        }
        this.columnName = columnName;
        this.columnType = columnType;
        this.columnSize = columnSize;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public int getColumnSize() {
        return columnSize;
    }
}
