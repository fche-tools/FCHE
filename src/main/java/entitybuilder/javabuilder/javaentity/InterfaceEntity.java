package entitybuilder.javabuilder.javaentity;

import uerr.AbsCLSEntity;

public class InterfaceEntity extends AbsCLSEntity {
    public InterfaceEntity(){this.type = "interface";}
    public InterfaceEntity(int id, String name, int parentId){
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.type = "interface";
    }
}
