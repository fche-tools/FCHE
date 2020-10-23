package priextractor.javaextractor.javadeper;

import entitybuilder.javabuilder.javaentity.ClassEntity;
import uerr.AbsEntity;
import uerr.AbsFLDEntity;
import util.Configure;

import java.util.ArrayList;
import java.util.HashMap;


public class ImportVisitor extends DepVisitor {


    private HashMap<String, Integer> class2IdMap = new HashMap<String, Integer>();

    public ImportVisitor() {
        buildClassMap(); //fullpathname->id

    }
    @Override
    public void setDep() {

        setImportDep(); //import

    }



    /**
     * map["classname"] = classId
     * @return
     */
    private void buildClassMap() {
        for(AbsEntity entity : singleCollect.getEntities()) {
            if(entity instanceof ClassEntity) {
                AbsFLDEntity parent =((AbsFLDEntity)singleCollect.getEntityById(entity.getParentId()));
                String parent_fullpath = parent.getFullPath();
               // System.out.println(str);
                String str = "";
                str = parent.getName()+entity.getName().replaceAll(parent_fullpath,"");
                class2IdMap.put(str.replace('/','.'),entity.getId());
            }
        }
    }

    private void setImportDep() {
        ArrayList<String> importList = null;
        for(AbsEntity entity : singleCollect.getEntities()) {
            if (entity instanceof ClassEntity){
                importList = ((ClassEntity)entity).getImportList();
                int size = importList.size();
                if(size==0)
                    continue;
                for(int i=0;i<size;i++){
                    //map中存在importList中元素时，添加import依赖
                    //System.out.println(importList.get(i).replaceAll("/","."));
                    String str = importList.get(i);
                    System.out.println(str);

                    try {
                        str =str.substring(str.substring(0,str.lastIndexOf('.')).lastIndexOf('.')+1)+".java";
                    }catch (Exception e){
                        System.out.println("no this");
                    }

                    //System.out.println(str);

                    if(class2IdMap.containsKey(str)){
                       // System.out.println("success");
                        saveRelation(entity.getId(), class2IdMap.get(str), Configure.RELATION_IMPORT, Configure.RELATION_IMPORTED_BY);
                    }
                }

            }else {
                continue;
            }
        }
    }
}
