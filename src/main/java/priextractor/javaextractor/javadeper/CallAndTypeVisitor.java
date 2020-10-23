package priextractor.javaextractor.javadeper;

import entitybuilder.javabuilder.javaentity.ClassEntity;
import entitybuilder.javabuilder.javaentity.InterfaceEntity;
import entitybuilder.javabuilder.javaentity.MethodEntity;
import uerr.AbsEntity;
import util.Configure;
import util.Tuple;

import java.util.HashMap;

public class CallAndTypeVisitor extends DepVisitor {
    private HashMap<String, Integer> class2IdMap = new HashMap<String, Integer>();
    @Override
    public void setDep() {
        setCallDep();
        setTypeDep();
    }

    /**
     * map["classname"] = classId
     * @return
     */
    private void buildClassMap(AbsEntity entity) {
        //找出方法实体的父类的import关系中的类
        //其父类Class类
        if(!(singleCollect.getEntityById(entity.getParentId()) instanceof ClassEntity)) {
            return;
        }
        ClassEntity parentClass =(ClassEntity)singleCollect.getEntityById(entity.getParentId());

        //找到其关系Tuple<关系名，id>
        if (parentClass!=null && parentClass.getRelations() != null) {//parentClass为空时报错
            for (Tuple tuple : parentClass.getRelations()) {
                if (tuple.x.equals(Configure.RELATION_IMPORT) ||
                        tuple.x.equals(Configure.RELATION_IMPLEMENT) ||
                        tuple.x.equals(Configure.RELATION_EXTEND)) {//将import关系或者extends/implement关系的ClassName以及id放入class2IdMap
                    AbsEntity importClass = null;
                    if (singleCollect.getEntityById((Integer) tuple.y) instanceof ClassEntity) {
                        importClass = singleCollect.getEntityById((Integer) tuple.y);
                    }
                    if (singleCollect.getEntityById((Integer) tuple.y) instanceof InterfaceEntity) {
                        importClass = singleCollect.getEntityById((Integer) tuple.y);
                    }
                    //System.out.println(entity.getName());
                    String str = importClass.getName();
                    try {
                        str = str.replace('/', '.');
                        str = str.substring(str.substring(0, str.lastIndexOf('.')).lastIndexOf('.') + 1);
                    } catch (Exception e) {
                        System.out.println("no this");
                    }
                    class2IdMap.put(str, (Integer) tuple.y);
                }
            }
        }
    }

    //方法体中声明某一个类
    private void setCallDep() {
        for(AbsEntity entity : singleCollect.getEntities()) {
            if(entity instanceof MethodEntity &&((MethodEntity) entity).body_class_list.size()!=0){//找到所有的方法实体
                buildClassMap(entity);//建立了一个classMap，类名（simple）和id
                if(class2IdMap.size()>0) {
                    for(String className:((MethodEntity) entity).body_class_list){
                        if(class2IdMap.containsKey(className+".java")){
                            saveRelation(entity.getId(),class2IdMap.get(className+".java"), Configure.RELATION_CALL, Configure.RELATION_CALLED_BY);
                        }
                    }
                }
            }
        }
    }

    //方法参数中存在类名
    private void setTypeDep() {
        for(AbsEntity entity : singleCollect.getEntities()) {
            if(entity instanceof MethodEntity &&((MethodEntity) entity).para_class_list.size()!=0){//找到所有的方法实体
                if(class2IdMap.size()>0) {
                    for (String className : ((MethodEntity) entity).para_class_list) {
                        if (class2IdMap.containsKey(className + ".java")) {
                            saveRelation(entity.getId(), class2IdMap.get(className + ".java"), Configure.RELATION_TYPE, Configure.RELATION_TYPED_BY);
                        }
                    }
                }
            }
        }
    }

}
