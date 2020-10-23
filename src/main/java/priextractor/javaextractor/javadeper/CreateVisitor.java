package priextractor.javaextractor.javadeper;

import entitybuilder.javabuilder.javaentity.ClassEntity;
import entitybuilder.javabuilder.javaentity.InterfaceEntity;
import entitybuilder.javabuilder.javaentity.VarEntity;
import uerr.AbsEntity;
import uerr.AbsFLDEntity;
import util.Configure;
import util.Tuple;

import java.util.HashMap;

public class CreateVisitor extends DepVisitor {
    private HashMap<String, Integer> class2IdMap = new HashMap<String, Integer>();

    @Override
    public void setDep() {
        setCreatetDep();
    }

    private void buildClassMap(AbsEntity entity) {
        //找出方法实体的父类的import关系中的类
        if(singleCollect.getEntityById(entity.getParentId()) instanceof AbsFLDEntity){//排除父类为文件夹的错误实体
            return;
        }

        //其父类Class类
        ClassEntity parentClass =(ClassEntity)singleCollect.getEntityById(entity.getParentId());

        //找到其关系Tuple<关系名，id>
        if (parentClass!=null && parentClass.getRelations() != null)//parentClass为空时报错
            for (Tuple tuple : parentClass.getRelations()) {
                if (tuple.x.equals(Configure.RELATION_IMPORT)||
                        tuple.x.equals(Configure.RELATION_IMPLEMENT)||
                        tuple.x.equals(Configure.RELATION_EXTEND)) {//将import关系或者extends/implement关系的ClassName以及id放入class2IdMap
                    AbsEntity importClass=null;
                    if(singleCollect.getEntityById((Integer) tuple.y) instanceof ClassEntity) {
                        importClass= singleCollect.getEntityById((Integer) tuple.y);
                    }
                    if(singleCollect.getEntityById((Integer) tuple.y) instanceof InterfaceEntity) {
                        importClass= singleCollect.getEntityById((Integer) tuple.y);
                    }
                    //System.out.println(entity.getName());
                    try {
                        String str = importClass.getName();
                        str = str.replace('/', '.');
                        str = str.substring(str.substring(0, str.lastIndexOf('.')).lastIndexOf('.') + 1);
                        class2IdMap.put(str, (Integer) tuple.y);
                    } catch (Exception e) {
                        System.out.println("no this file");
                    }

                }
            }
    }

//当一个类的成员的类型为另一个类时，定义为Create关系
    private void setCreatetDep() {
        for(AbsEntity entity : singleCollect.getEntities()) {
            if(entity instanceof VarEntity){//找到所有的变量实体
                buildClassMap(entity);//建立了一个classMap，类名（simple）和id

                if(class2IdMap.size()>0 && class2IdMap.containsKey(((VarEntity) entity).getVarType()+".java")){
                        saveRelation(entity.getId(),class2IdMap.get(((VarEntity) entity).getVarType()+".java"),
                                Configure.RELATION_CREATE, Configure.RELATION_CREATED_BY);
                    }
            }
        }
    }

}
