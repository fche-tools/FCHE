package priextractor.javaextractor.javadeper;

import entitybuilder.javabuilder.javaentity.ClassEntity;
import uerr.AbsEntity;
import uerr.AbsFLDEntity;
import util.Configure;

import java.util.HashMap;

public class ExtendsVistor extends DepVisitor {

    public ExtendsVistor(){
        buildClassMap();
    }
    private HashMap<String, Integer> class2IdMap = new HashMap<String, Integer>();
    @Override
    public void setDep() {
        setExtendsDep();
    }

    private void setExtendsDep() {
        for(AbsEntity entity : singleCollect.getEntities()) {
            if (entity instanceof ClassEntity)  {
                String extendsFile=((ClassEntity)entity).getExtendsFile();

                if(extendsFile=="")
                    continue;
                //XXXX;XXX.XXX.XXXX
                //取最后的一项
                if(extendsFile.contains(".")){
                    extendsFile = extendsFile.substring(extendsFile.lastIndexOf(".")+1);
                }
                if(class2IdMap.containsKey(extendsFile)){
                    saveRelation(entity.getId(), class2IdMap.get(extendsFile), Configure.RELATION_EXTEND, Configure.RELATION_EXTENDED_BY);
                }
            }
        }

    }

    private void buildClassMap() {
        for(AbsEntity entity : singleCollect.getEntities()) {
            if(entity instanceof ClassEntity) {
                AbsFLDEntity parent =((AbsFLDEntity)singleCollect.getEntityById(entity.getParentId()));
                String parent_fullpath = parent.getFullPath();
                // System.out.println(str);
                String str = "";
                str = entity.getName().replaceAll(parent_fullpath,"");
                str = str.replace('/','.');
                class2IdMap.put(str.substring(1,str.lastIndexOf('.')),entity.getId());
            }
        }
    }




}
