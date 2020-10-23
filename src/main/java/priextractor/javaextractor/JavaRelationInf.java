package priextractor.javaextractor;

import entitybuilder.javabuilder.javaentity.ClassEntity;
import entitybuilder.javabuilder.javaentity.InterfaceEntity;
import entitybuilder.javabuilder.javaentity.MethodEntity;
import uerr.AbsEntity;
import uerr.AbsFLDEntity;
import uerr.AbsVAREntity;
import uerr.RelationInterface;
import util.Tuple;

import java.util.ArrayList;

public class JavaRelationInf extends RelationInterface {
    //用于判断关系
    @Override
    public String entityStatis(){
        String str="";
        int packageCount = 0;
        int classCount = 0;
        int methodCount = 0;
        int varCount=0;
        int interfaceCount=0;
        for(AbsEntity entity : singleCollect.getEntities()) {
            if (entity instanceof AbsFLDEntity) {
                packageCount++;
            } else if (entity instanceof ClassEntity) {
                classCount++;
            } else if (entity instanceof InterfaceEntity) {
                interfaceCount++;
            }else if (entity instanceof MethodEntity) {
                methodCount++;
            } else if (entity instanceof AbsVAREntity){
                varCount++;
            }
            str += ("Package:      " + packageCount + "\n");
            str += ("Interface:       " + interfaceCount + "\n");
            str += ("Class:        " + classCount + "\n");
            str += ("Method:       " + methodCount + "\n");
            str += ("Varies:       " + varCount + "\n");

        }

        return str;
    }
    @Override
    public String dependencyStatis() {
        String str = "";
        return str;
    }

    @Override
    public ArrayList<Tuple<String, String>> getImportDeps(String level) {
        return null;
    }

    @Override
    public ArrayList<Tuple<String, String>> getImplementDeps(String level) {
        return null;
    }

    @Override
    public ArrayList<Tuple<String, String>> getInheritDeps(String level) {
        return null;
    }

    @Override
    public ArrayList<Tuple<String, String>> getFunctionCalls(String level) {
        return null;
    }

    @Override
    public ArrayList<Tuple<String, String>> getTypeDeps(String level) {
        return null;
    }

    @Override
    public ArrayList<Tuple<String, String>> getExtendDeps(String level) {
        return null;
    }

    @Override
    public ArrayList<Tuple<String, String>> getCreateDeps(String level) {
        return null;
    }

//    public ArrayList<Tuple<String, String>> getFunctionSets(String level) {
//        return null;
//    }
//
//    @Override
//    public ArrayList<Tuple<String, String>> getFunctionUses(String level) {
//        return null;
//    }
//
//    @Override
//    public ArrayList<Tuple<String, String>> getFunctionParas(String level) {
//        return null;
//    }
//
//    @Override
//    public ArrayList<Tuple<String, String>> getFunctionRets(String level) {
//        return null;
//    }

    @Override
    public ArrayList<Tuple<String, String>> getImplicitExternalCalls(String level) {
        return null;
    }
}
