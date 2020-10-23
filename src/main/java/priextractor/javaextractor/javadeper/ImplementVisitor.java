package priextractor.javaextractor.javadeper;


import entitybuilder.javabuilder.javaentity.ClassEntity;
import entitybuilder.javabuilder.javaentity.InterfaceEntity;
import uerr.AbsEntity;
import uerr.AbsFLDEntity;
import util.Configure;

import java.util.HashMap;

public class ImplementVisitor extends DepVisitor {

    private HashMap<String, Integer> interface2IdMap = new HashMap<String, Integer>();
    public ImplementVisitor(){
        buildInterfaceMap();
    }
    @Override
    public void setDep() {
        setImplementDep();
    }

    private void buildInterfaceMap() {
        for(AbsEntity entity : singleCollect.getEntities()) {
            if(entity instanceof InterfaceEntity) {
                AbsFLDEntity parent =((AbsFLDEntity)singleCollect.getEntityById(entity.getParentId()));
                String parent_fullpath = parent.getFullPath();
                // System.out.println(str);
                String str = "";
                str = entity.getName().replaceAll(parent_fullpath,"");
                str = str.replace('/','.');
                interface2IdMap.put(str.substring(1,str.lastIndexOf('.')),entity.getId());
            }
        }
    }

    private void setImplementDep() {
        for(AbsEntity entity : singleCollect.getEntities()) {
            if (entity instanceof ClassEntity)  {
                String implementFile=((ClassEntity)entity).getImplementFile();

                if(implementFile=="")
                    continue;
                //XXXX;XXX.XXX.XXXX
                //取最后的一项
                if(implementFile.contains(".")){
                    implementFile = implementFile.substring(implementFile.lastIndexOf(".")+1);
                }
                if(interface2IdMap.containsKey(implementFile)){
                    saveRelation(entity.getId(), interface2IdMap.get(implementFile), Configure.RELATION_IMPLEMENT, Configure.RELATION_IMPLEMENTED_BY);
                }
            }
        }
    }
}
