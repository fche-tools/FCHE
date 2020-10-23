package uerr;

import util.Configure;
import util.Tuple;

import java.util.ArrayList;

public abstract class RelationInterface {

    protected SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();

    public abstract String entityStatis();

    public abstract String dependencyStatis();

    public ArrayList<Tuple<String, String>> getDepByType(String level, String depType) {
        if(depType.equals(Configure.RELATION_IMPLEMENT)) {
            return getImplementDeps(level);
        }
        if(depType.equals(Configure.RELATION_INHERIT)) {
            return getInheritDeps(level);
        }
        if(depType.equals(Configure.RELATION_TYPE)) {
            return getTypeDeps(level);
        }
        if(depType.equals(Configure.RELATION_EXTEND)) {
            return getExtendDeps(level);
        }
        if(depType.equals(Configure.RELATION_CALL)) {
            return getFunctionCalls(level);
        }
        if(depType.equals(Configure.RELATION_IMPORT)) {
            return getImportDeps(level);
        }
        if(depType.equals(Configure.RELATION_CREATE)) {
            return getCreateDeps(level);
        }
        if(depType.equals(Configure.RELATION_IMPLICIT_EXTERNAL_CALL)) {
            return getImplicitExternalCalls(level);
        }
        return null;

    }


    public ArrayList<String> getAllFiles() {
        ArrayList<String> files = new ArrayList<String>();
        for (AbsEntity entity : singleCollect.getEntities()) {
            if(entity instanceof AbsFILEntity) {
                String fileName = entity.getName();
                files.add(fileName);
            }
        }
        //System.out.println(singleCollect.getEntities());
        return files;
    }
    public abstract ArrayList<Tuple<String, String>> getExtendDeps(String level);
    public abstract ArrayList<Tuple<String, String>> getCreateDeps(String level);
    public abstract ArrayList<Tuple<String, String>> getImportDeps(String level);
    public abstract ArrayList<Tuple<String, String>> getImplementDeps(String level);
    public abstract ArrayList<Tuple<String, String>> getInheritDeps(String level);
    public abstract ArrayList<Tuple<String, String>> getFunctionCalls(String level);
    public abstract ArrayList<Tuple<String, String>> getTypeDeps(String level);
//    public abstract ArrayList<Tuple<String, String>> getFunctionSets(String level);
//    public abstract ArrayList<Tuple<String, String>> getFunctionUses(String level);
//    public abstract ArrayList<Tuple<String, String>> getFunctionParas(String level);
//    public abstract ArrayList<Tuple<String, String>> getFunctionRets(String level);
    public abstract ArrayList<Tuple<String, String>> getImplicitExternalCalls(String level);
}
