package entitybuilder.javabuilder.javaentity;

import uerr.AbsEntity;

import java.util.ArrayList;

public class MethodEntity extends AbsEntity {
    private String returnType="void";
    private int method_line =0;
    private int para_num=0;
    private int max_block_depth=0;
    private int control_block_num=1;
    private int execStmt=0;
    private int localVarDecl=0;

    // operators 和 operands 操作符和操作数
    private int operators;
    private int sumOperators;
    private int operands;
    private int sumOperands;

    //存放参数中类的名字
    public ArrayList<String> para_class_list=new ArrayList<String>();

    //存放方法体中类的名字
    public ArrayList<String> body_class_list=new ArrayList<String>();


    private String nameWithPara="";


    public MethodEntity(){this.type="method";}
    public MethodEntity(int parentId,int id,String name){
        this.parentId = parentId;
        this.id = id;
        this.name = name;
        this.type="method";
    }
    @Override
    public String toString(){
        String str = "";
        str+=   "Method:"
                +";parenId:"+parentId
                +";id:"+id
                +";name:"+name;

        return str;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }



    public int getMethod_line() { return method_line; }
    public void setMethod_line(int method_line) { this.method_line = method_line; }

    public int getPara_num() { return para_num; }
    public void setPara_num(int para_num) { this.para_num = para_num; }

    public int getMax_block_depth() {
        return max_block_depth;
    }

    public void setMax_block_depth(int max_block_depth) {
        this.max_block_depth = max_block_depth;
    }

    public int getControl_block_num() {
        return control_block_num;
    }

    public void setControl_block_num(int control_block_num) {
        this.control_block_num = control_block_num;
    }

    public int getExecStmt() { return execStmt; }

    public void setExecStmt(int execStmt) { this.execStmt = execStmt; }

    public int getLocalVarDecl() { return localVarDecl; }

    public void setLocalVarDecl(int localVarDecl) { this.localVarDecl = localVarDecl; }

    public String getNameWithPara() {
        return nameWithPara;
    }

    public void setNameWithPara(String nameWithPara) {
        this.nameWithPara = nameWithPara;
    }
    public int getOperators() {
        return operators;
    }

    public void setOperators(int operators) {
        this.operators = operators;
    }

    public int getSumOperators() {
        return sumOperators;
    }

    public void setSumOperators(int sumOperators) {
        this.sumOperators = sumOperators;
    }

    public int getOperands() {
        return operands;
    }

    public void setOperands(int operands) {
        this.operands = operands;
    }

    public int getSumOperands() {
        return sumOperands;
    }

    public void setSumOperands(int sumOperands) {
        this.sumOperands = sumOperands;
    }
}
