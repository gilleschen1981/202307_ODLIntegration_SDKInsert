package com.opentext.itom.ucmdb.integration.odl.tools;

import com.hp.ucmdb.api.types.TopologyCI;
import com.opentext.itom.ucmdb.integration.odl.tools.ucmdb.ClassModelConf;
import com.opentext.itom.ucmdb.integration.odl.tools.ucmdb.UCMDBConnector;
import com.opentext.itom.ucmdb.integration.odl.tools.vertica.ModelConverter;
import com.opentext.itom.ucmdb.integration.odl.tools.vertica.TableMeta;
import com.opentext.itom.ucmdb.integration.odl.tools.vertica.VerticaConnector;

import java.util.List;

public class InsertVerticaBySDK {
    public static void main(String[] args) {
        if (args.length != 7) {
            System.out.println("Usage: java MyClass ucmdbhost ucmdbport ucmdbuser ucmdbpassword verticajdbc verticauser verticapassword");
            System.exit(0);
        }

        String ucmdbHostname = args[0];
        String ucmdbPort = args[1];
        String ucmdbUsername = args[2];
        String ucmdbPassword = args[3];

        String verticaURL = args[4];
        String verticaUsername = args[5];
        String verticaPassword = args[6];

        UCMDBConnector ucmdbConnector = new UCMDBConnector();
        ucmdbConnector.init(ucmdbHostname, ucmdbPort, ucmdbUsername, ucmdbPassword);
        VerticaConnector verticaConnector = new VerticaConnector();
        verticaConnector.init(verticaURL, verticaUsername, verticaPassword);

        for(String classname : ucmdbConnector.getClassModelConf().getClassTypeMap().keySet()){
            if(!ucmdbConnector.isRelation(classname)){
                // class
                TableMeta tableMeta = ModelConverter.convertClassDefinition2TableMeta(ucmdbConnector.getClassModelConf(), ucmdbConnector.getClassDefinition(classname));
                List<TopologyCI> ciList = ucmdbConnector.queryCIbyType(classname);
                verticaConnector.insertCIs(classname, tableMeta.getColumnList(), ciList);
            } else{
                // relation
            }
        }
    }

}