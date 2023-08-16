package com.opentext.itom.ucmdb.integration.odl.tools.ucmdb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.ucmdb.api.*;
import com.hp.ucmdb.api.classmodel.ClassDefinition;
import com.hp.ucmdb.api.classmodel.ClassModelService;
import com.hp.ucmdb.api.topology.*;
import com.hp.ucmdb.api.types.TopologyCI;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class UCMDBConnector {
    private ClassModelConf classModelConf;

    private UcmdbService ucmdbService;

    public ClassModelConf getClassModelConf() {
        return classModelConf;
    }

    public void setClassModelConf(ClassModelConf classModelConf) {
        this.classModelConf = classModelConf;
    }

    public void loadClassmodelConf() {
        File configFile = new File(ClassModelConf.CONFIG_FILE_PATH);
        ObjectMapper mapper = new ObjectMapper();
        try {
            classModelConf = mapper.readValue(configFile, ClassModelConf.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(String ucmdbHostname, String ucmdbPort, String ucmdbUsername, String ucmdbPassword) {
        loadClassmodelConf();
        UcmdbServiceProvider serviceProvider = null;
        try {
            serviceProvider = UcmdbServiceFactory.getServiceProvider("https", ucmdbHostname, Integer.valueOf(ucmdbPort));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Creating a client context according to the name of this integration (for auditing)
        ClientContext clientContext = serviceProvider.createClientContext("Sync");
        //Creating the credentials for authentication
        Credentials credentials = serviceProvider.createCredentials(ucmdbUsername, ucmdbPassword);
        CustomerContext customerContext = serviceProvider.createCustomerContext("Default Client");
        // Creating the connection object
        ucmdbService = serviceProvider.connect(customerContext, credentials,clientContext);
    }

    public boolean isRelation(String classname) {
        boolean rlt = false;
        if("containment".equals(classname)){
            rlt = true;
        }
        return rlt;
    }

    public List<TopologyCI> queryCIbyType(String classname) {
        TopologyQueryService queryService = ucmdbService.getTopologyQueryService();
        // Get the query factory
        TopologyQueryFactory queryFactory = queryService.getFactory();
        // Create the query definition
        QueryDefinition queryDefinition = queryFactory.createQueryDefinition("Get all CI of a given type");
        String elementName = "CIs";
        QueryNode node = queryDefinition.addNode(elementName).ofType(classname);
        for(String property : getClassModelConf().getClassTypeMap().get(classname)){
            node.queryProperty(property);
        }

        Topology topology = queryService.executeQuery(queryDefinition);

        Collection<TopologyCI> nodes = topology.getCIsByName(elementName);
        return new ArrayList<TopologyCI>(nodes);
    }

    public ClassDefinition getClassDefinition(String classname) {
        if(classname == null || classname.length() <=0){
            return null;
        }
        if(ucmdbService == null){
            return null;
        }
        ClassModelService classModelService = ucmdbService.getClassModelService();
        Collection<ClassDefinition> collection = classModelService.getAllClasses();
        Iterator<ClassDefinition> it = collection.iterator();
        while(it.hasNext()){
            ClassDefinition cd = it.next();
            if(classname.equals(cd.getName())){
                return cd;
            }
        }
        return null;
    }
}
