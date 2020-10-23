package entitybuilder.javabuilder.javavisitor;

import uerr.AbsFLDEntity;
import uerr.SingleCollect;
import util.StringUtil;

import java.util.Map;

//用于生成实体，并返回实体id（包括class、module、method等）
public class JavaProcessTask {
    SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();

    //创建package实体
    public int processPackage(String fileName) {
        String dirStr = StringUtil.deleteLastStrByPathDelimiter(fileName);
        String packageName = StringUtil.getLastStrByPathDelimiter(dirStr);

        //判断是否存在package
        if(!singleCollect.allPackage.containsValue(dirStr)) {
            singleCollect.allPackage.put(singleCollect.getCurrentIndex(),dirStr);
            // new packageEntity
            int packageId = singleCollect.getCurrentIndex();
            AbsFLDEntity packageEntity = new AbsFLDEntity(packageId, dirStr, packageName);
            singleCollect.addEntity(packageEntity);
            return packageId;
        }else {
            return Integer.parseInt(getKey(singleCollect.allPackage,dirStr).toString());
        }
    }
    public static Object getKey(Map map, Object value){
        int i=-1;
        for(Object key: map.keySet()){
            if(map.get(key).equals(value)){
                return key;
            }
        }
        return i;

    }
    //创建class实体
    public void processClass(){}

    //创建var实体

    //创建method实体


}
