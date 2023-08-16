package com.opentext.itom.ucmdb.integration.odl.tools.vertica;

import com.hp.ucmdb.api.classmodel.Attribute;
import com.hp.ucmdb.api.classmodel.ClassDefinition;
import com.hp.ucmdb.api.types.Type;
import com.opentext.itom.ucmdb.integration.odl.tools.ucmdb.ClassModelConf;

import java.util.List;

public class ModelConverter {

    public static String convertColumnMeta2TableType(TableColumnMeta columnMeta) {
        String rlt = columnMeta.getColumnName() + "\t";
        switch (columnMeta.getColumnType()){
            case "VARCHAR":
                rlt += columnMeta.getColumnType() + "(" + String.valueOf(columnMeta.getColumnSize()) + ")";
                break;
            case "INTEGER":
            case "BOOLEAN":
            case "DATE":
                rlt += columnMeta.getColumnType();
                break;
            default:
                rlt = ""; System.out.println("[CONVERT]Unrecognized table column type: " + columnMeta.getColumnType());
                break;
        }
        return rlt;
    }

    public static TableMeta convertClassDefinition2TableMeta(ClassModelConf classModelConf, ClassDefinition classDefinition) {
        TableMeta rlt = new TableMeta(classDefinition.getName());
        for(Attribute attr : classDefinition.getAllAttributes().values()){
            List<String> attrList = classModelConf.getClassTypeMap().get(classDefinition.getName());
            if(attrList != null && attrList.contains(attr.getName())){
                TableColumnMeta columnMeta = new TableColumnMeta(attr.getName(), convertAttrType2String(attr.getType()), attr.getSize() == null?0:attr.getSize());
                rlt.getColumnList().add(columnMeta);
            }
        }
        return rlt;
    }

    private static String convertAttrType2String(Type type) {
        String rlt = null;
        switch (type){
            case INTEGER:
                rlt = "INTEGER";
                break;
            case BOOLEAN:
                rlt = "BOOLEAN";
                break;
            case DATE:
                rlt = "DATE";
                break;
            case STRING:
                rlt = "VARCHAR";
                break;
            case ENUM:
                rlt = "VARCHAR";
                break;
            case LIST:
                rlt = "VARCHAR";
                break;
            case STRING_LIST:
                rlt = "VARCHAR";
                break;
            default:
                System.out.println("[UCMDB]Unrecognized Type: " + type);
        }
        return rlt;
    }
}
