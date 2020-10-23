package entitybuilder.javabuilder.javaentity;

import uerr.AbsCLSEntity;

import java.util.ArrayList;

public class ClassEntity extends AbsCLSEntity {
    private ArrayList<String> VarList = new ArrayList<>();
    private ArrayList<String> MethodList = new ArrayList<>();
    private ArrayList<String> importList = new ArrayList<>();
    private String implementFile = "";
    private String extendsFile = "";

    public ClassEntity(){this.type="class";}
    public ClassEntity(int parentId,int id,String name,ArrayList<String> varList, ArrayList<String> methodlist){
        this.parentId = parentId;
        this.id = id;
        this.name = name;
        this.VarList = (ArrayList<String>) varList.clone();
        this.MethodList = (ArrayList<String>) methodlist.clone();
        this.type="class";

    }
    public String toString(){
        String str="";
        str+=   "Class:"
                +"parenId:"+parentId
                +";id:"+id
                +";ClassName:"+name
                + ";VarList:" + VarList
                + ";MethodList:" + MethodList;
        return str;
    }
    public void clear(){
        this.name="";
        this.setConstructor(false);
        this.VarList.clear();
        this.MethodList.clear();

    }

    public void setExtendsFile(String extendsFile1){extendsFile=extendsFile1;}
    public String getExtendsFile(){return extendsFile;}

    public void setImplementFile(String implementFile1){implementFile=implementFile1;}
    public String getImplementFile(){return implementFile;}

    public void addToImportList(String importPac){importList.add(importPac);}
    public ArrayList<String> getImportList(){return importList;}

    public void addToVarList(String var){
        this.VarList.add(var);
    }
    public void addToMethodList(String method){
        this.MethodList.add(method);
    }
    private boolean hasConstuctor = false;

    public void setConstructor(boolean bool){
        this.hasConstuctor = bool;
    }
    public void setName(String str){
        this.name = str;
    }
    public String getName(){return this.name;}
    public Boolean getHasConstructor(){return this.hasConstuctor;}
    public ArrayList<String> getVarList(){
        return this.VarList;
    }
    public void setVarList(ArrayList varList){this.VarList = varList;}
    public ArrayList<String> getMethodList() {
        return this.MethodList;
    }
    public void setMethodList(ArrayList methodList){this.MethodList = methodList;}

}
