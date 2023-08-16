package com.opentext.itom.ucmdb.integration.odl.tools.ucmdb;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassModelConf {
    public final static String CONFIG_FILE_PATH = "data/classmodel.json";
    private Map<String, List<String>> classTypeMap;

    public Map<String, List<String>> getClassTypeMap() {
        if(classTypeMap == null){
            classTypeMap = new HashMap<String, List<String>>();
        }
        return classTypeMap;
    }

    public void setClassTypeMap(Map<String, List<String>> classTypeMap) {
        this.classTypeMap = classTypeMap;
    }
}
