package entitybuilder.javabuilder.javaentity;

import uerr.AbsVAREntity;

public class VarEntity extends AbsVAREntity {
    private String varType;
    public VarEntity(){this.type="var";}
    public VarEntity(int id, String name, String varType){
        this.id = id;
        this.name = name;
        this.varType = varType;
        this.type="var";
    }

    public void setType(String varType){
        this.varType = varType;
    }
    public String getVarType(){
        return this.varType;
    }
    public String toString(){
        String str="";
        str+=   "Var:"+
                ";parentId:"+parentId
                +";id:"+id
                +";name:"+name
                +";varType:"+varType;

        return str;
    }
}
