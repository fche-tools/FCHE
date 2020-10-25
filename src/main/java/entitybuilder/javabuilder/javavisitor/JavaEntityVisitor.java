package entitybuilder.javabuilder.javavisitor;


import entitybuilder.javabuilder.javaentity.ClassEntity;
import entitybuilder.javabuilder.javaentity.InterfaceEntity;
import entitybuilder.javabuilder.javaentity.MethodEntity;
import entitybuilder.javabuilder.javaentity.VarEntity;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ErrorNodeImpl;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import parser.parsejava.JavaBaseVisitor;
import parser.parsejava.JavaParser;
import sun.misc.Queue;
import uerr.SingleCollect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author wsz
 */
public class JavaEntityVisitor extends JavaBaseVisitor {
    private String fileFullPath;
    public JavaProcessTask processTask = new JavaProcessTask();
    private ClassEntity classEntity = new ClassEntity();
    public JavaEntityVisitor(String fileFullPath){
        this.fileFullPath = fileFullPath;

    }

    /**
     * find import
     * @param ctx
     * @return
     */
    @Override
    public String visitImportDeclaration(JavaParser.ImportDeclarationContext ctx) {
        String str;
        str = ctx.children.get(1).getText();
        classEntity.addToImportList(str);
        return str;
    }

    /**
     * find interface
     * @param ctx
     * @return
     */
    @Override
    public String visitInterfaceDeclaration(JavaParser.InterfaceDeclarationContext ctx) {
        SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();
        String str;
        if(ctx==null){
            return "";
        }
        str = ctx.children.get(1).getText();
        int parentId = processTask.processPackage(fileFullPath);
        String name = fileFullPath;
        int id = singleCollect.getCurrentIndex();
        InterfaceEntity interfaceEntity = new InterfaceEntity(id,name,parentId);
        singleCollect.addEntity(interfaceEntity);
        return str;
    }

    /**
     * find Class
     * @param ctx
     * @return
     */
    @Override
    public String visitClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
        SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();
        String str;
        if (ctx==null){
            return "";
        }

        //创建package实体返回id
        int parentId = processTask.processPackage(fileFullPath);
        //包含包名和文件名
        str = fileFullPath;

        //发现implements
        if(ctx.children.size()>3 && ctx.children.get(2).getText().equals("implements")) {
            classEntity.setImplementFile(ctx.children.get(3).getChild(0).getText());
        }
        //发现extends
        if(ctx.children.size()>3 && ctx.children.get(2).getText().equals("extends")) {
           classEntity.setExtendsFile(ctx.children.get(3).getChild(0).getText());

        }
        classEntity.setName(str);
        classEntity.setId(processTask.singleCollect.getCurrentIndex());
        classEntity.setParentId(parentId);
        singleCollect.addEntity(classEntity);

        RuleContext ctx2;
        // 排除错误节点
        if (ctx.children.get(ctx.children.size()-1) instanceof ErrorNodeImpl) {
            return "";
        }

        try {
            ctx2 = (RuleContext) ctx.children.get(ctx.children.size()-1);
            visitClassBody((JavaParser.ClassBodyContext) ctx2);
        }catch (Exception e){
            e.printStackTrace();
        }

        //创建类实体，添加入singleCollect
        singleCollect.class_method_map.put(classEntity.getName(),classEntity.getMethodList());
        return str;
    }


    /**
     * find class body
     * @param ctx
     * @return
     */
    @Override
    public String visitClassBody(JavaParser.ClassBodyContext ctx) {
        String str = "";
        if(ctx==null){
            return "";
        }

        //0:'{';1-size-2:'ClassBodyDeclarationContext';size-1:'}';
        for (int i =1;i<ctx.children.size()-1;i++){
            RuleContext ctx2;
            //出现错误节点，进行排除
            if(ctx.children.get(i) instanceof ErrorNodeImpl) {
                System.out.println("error node");
                continue;
            }
            ctx2 = (RuleContext) ctx.children.get(i);
            visitClassBodyDeclaration((JavaParser.ClassBodyDeclarationContext) ctx2);
        }
        return str;
    }

    /**
     * find class declaration
     */
    @Override
    public String visitClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx) {
        String str = "";
        if(ctx==null){
            return "";
        }
        RuleContext ctx2;
        //last:MemberDeclarationContext
        for (int i =0;i<ctx.children.size();i++) {
            if(ctx.children.get(i) instanceof JavaParser.MemberDeclarationContext) {
                ctx2 = (RuleContext) ctx.children.get(i);
                visitMemberDeclaration((JavaParser.MemberDeclarationContext) ctx2);
                return str;
            }
        }
        return "";
    }

    @Override
    public String visitMemberDeclaration(JavaParser.MemberDeclarationContext ctx) {
        String str =  "";
        if(ctx==null || ctx.children==null){
            return "";
        }
        RuleContext ctx2;
        for(int i=0;i<ctx.children.size();i++){
            if(ctx.children.get(i) instanceof JavaParser.FieldDeclarationContext){
                ctx2 = (RuleContext) ctx.children.get(i);
                visitFieldDeclaration((JavaParser.FieldDeclarationContext) ctx2);
            }else if(ctx.children.get(i) instanceof JavaParser.ConstructorDeclarationContext){

            }else if(ctx.children.get(i) instanceof JavaParser.MethodDeclarationContext){
                ctx2 = (RuleContext) ctx.children.get(i);
                visitMethodDeclaration((JavaParser.MethodDeclarationContext) ctx2);
            }

        }
        return str;
    }

    /**
     * find var declaration
     * @param ctx
     * @return
     */
    @Override
    public String visitFieldDeclaration(JavaParser.FieldDeclarationContext ctx) {
        if(ctx==null){
            return "";
        }
        String str="";

        SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();
        VarEntity varEntity = new VarEntity();
        for (int i=0;i<ctx.children.size();i++){
            if(ctx.children.get(i) instanceof JavaParser.TypeContext){
                varEntity.setType(((JavaParser.TypeContext) ctx.children.get(i)).children.get(0).getChild(0).getText());
            }
            if(ctx.children.get(i) instanceof JavaParser.VariableDeclaratorsContext){
                varEntity.setName(fileFullPath.replace(".java","")+"."+ctx.children.get(i).getChild(0).getText().split("=")[0]);
            }
        }
        //设置变量列表
        classEntity.getVarList().add(varEntity.getVarType());

        varEntity.setId(singleCollect.getCurrentIndex());
        varEntity.setParentId(classEntity.getId());
        singleCollect.addEntity(varEntity);

        return str;
    }

    /**
     * find Constructor
     * @param ctx
     * @return
     */
    @Override
    public String visitConstructorDeclaration(JavaParser.ConstructorDeclarationContext ctx) {
        String str;
        str = ctx.children.get(0).getText();
        str = "Constructor:"+str;
        classEntity.setConstructor(true);
        return str;
    }

    /**
     * find method
     * @param ctx
     * @return
     */
    @Override
    public String visitMethodDeclaration(JavaParser.MethodDeclarationContext ctx)  {
        SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();
        String methodName;
        methodName=fileFullPath.replace(".java","")+"."+ctx.children.get(1).getText();
        MethodEntity methodEntity = new MethodEntity(classEntity.getId(),singleCollect.getCurrentIndex(),methodName);

        //最大块深度,inQueue/outQueue用于计算最大深度，分别表示当前进入队列数与当次退出队列数，
        int max_block_depth=0;
        int thisQ=0;
        int inQueue=1;
        int outQueue=0;

        //临时变量个数
        int localVarDecl=0;

        //可执行语句
        int execStmt=0;

        //操作数，操作符
        Map<String,Integer> operands = new HashMap<>();
        Map<String,Integer> operators = new HashMap<>();

        //控制语句块个数
        int control_block_num=1;

        //获取方法的总行数setMethod_line
        methodEntity.setMethod_line(ctx.stop.getLine()-ctx.start.getLine()+1);

        //添加方法_类hashmap method_class_map
        if(!singleCollect.method_class_map.containsKey(methodName)){
            Set<String> methodList = new HashSet<String>();
            methodList.add(classEntity.getName());
            singleCollect.method_class_map.put(methodName,methodList);
        }else{
            singleCollect.method_class_map.get(methodName).add(classEntity.getName());
        }

        //找出返回类型setReturnType
        if(ctx.children.get(0) instanceof JavaParser.TypeContext){
            methodEntity.setReturnType(ctx.children.get(0).getChild(0).getText());
        }

        //获取参数的个数setPara_num
        //找出参数列中存在的类形存入para_class_list
        //是否有返回参数
        if(ctx.children.size()>=3) {
            int child_count = ctx.children.get(2).getChild(1).getChildCount();
            methodEntity.setPara_num((child_count+1)/2);
            if(ctx.children.get(2).getChildCount()>2) {
                for (int i =0; i<child_count;i++) {
                    String typeName=ctx.children.get(2).getChild(1).getChild(i).getText();
                    if (!typeName.contains(",")){
                        methodEntity.para_class_list.add(ctx.children.get(2).getChild(1).getChild(i).getChild(0).getText());
                    }
                }
            }
        }


        //找出方法体中的类,存在抽象方法（没函数体）
        if (ctx.children.size() == 4) {
            //排除抽象方法(无函数体情况)abstract XXX();
            if(!";".equals(ctx.children.get(3).getText())) {
                //广度优先遍历函数体,访问其中的所有块，声明块中存在函数，其他块则继续访问
                Queue queue = new Queue();
                queue.enqueue(ctx.children.get(3).getChild(0));
                while(!queue.isEmpty()){
                    //出队
                    try {
                        //计算max_block_depth最大深度深度
                        if(inQueue==outQueue) {
                            inQueue += thisQ;
                            thisQ = 0;
                            max_block_depth++;
                        }
                        outQueue++;
                        ParserRuleContext ctx1 = (ParserRuleContext) queue.dequeue();
                        ParserRuleContext ctx2 = null;
                        //后续入队
                        if(ctx1 instanceof JavaParser.StatementContext ){
                            //ifelse语句块导致ctx1.child中存在StatementContext
                            if(ctx1.getChildCount()>1) {
                                for (int i = 0; i < ctx1.getChildCount(); i++) {
                                    if (ctx1.getChild(i) instanceof JavaParser.StatementContext &&
                                            ctx1.getChild(i).getChild(0) instanceof JavaParser.BlockContext) {
                                        ctx2 = (ParserRuleContext) ctx1.getChild(i).getChild(0);
                                    }
                                }
                            } else if(ctx1.getChild(0) instanceof JavaParser.BlockContext){
                                ctx2 = (ParserRuleContext) ctx1.getChild(0);
                            }
                        }else if(ctx1 instanceof JavaParser.BlockContext){
                            //第一次入队
                            ctx2 = ctx1;
                        }

                        if(ctx2!=null){
                            //方法体里面的块的个数(包含两个括号)
                        int blocksize = ctx2.getChildCount();
                            //排除空函数体情况XXX(){}
                            if(blocksize>=3){
                                for (int i=1;i<blocksize-1;i++) {
                                    //声明语句块（LocalVariableDeclarationStatementContext）：可检测出类的声明、可执行语句
                                    ParserRuleContext ctx3 = (ParserRuleContext) ctx2.getChild(i).getChild(0);
                                    // 声明语句
                                    if(ctx3 instanceof JavaParser.LocalVariableDeclarationStatementContext){
                                        localVarDecl++;
                                        if(ctx3.getChild(0) instanceof JavaParser.LocalVariableDeclarationContext){
                                            methodEntity.body_class_list.add(ctx3.getChild(0).getChild(0).getText());
                                        }
                                    // 其他语句
                                    }else{
                                        //非声明语句块：if，else,while，for,try，switch等(return)
                                        if(ctx3 instanceof JavaParser.StatementContext){
                                            if("if".equals(ctx3.getChild(0).getText())||
                                                    "do".equals(ctx3.getChild(0).getText())||
                                                    "while".equals(ctx3.getChild(0).getText())||
                                                    "for".equals(ctx3.getChild(0).getText())||
                                                    "switch".equals(ctx3.getChild(0).getText())||
                                                    "try".equals(ctx3.getChild(0).getText())||
                                                    "return".equals(ctx3.getChild(0).getText())
                                            ) {
                                                //记录本次加入块数
                                                int addnum = 0;
                                                //将{}的statementContext 继续放入队列
                                                for (int j = 0; j < ctx3.getChildCount(); j++) {
                                                    if (ctx3.getChild(j) instanceof
                                                            JavaParser.StatementContext) {//for while do
                                                        queue.enqueue(ctx3.getChild(j));
                                                        control_block_num++;
                                                        addnum++;
                                                    }else if(ctx3.getChild(j) instanceof
                                                            JavaParser.BlockContext){ //try
                                                        for(int k=1;k<ctx3.getChild(j).getChildCount()-1;k++){
                                                            if(ctx3.getChild(j).getChild(k).getText().contains("{")){
                                                                queue.enqueue(ctx3.getChild(j).getChild(k).getChild(0));
                                                                control_block_num++;
                                                                addnum++;
                                                            }
                                                        }

                                                    }else if(ctx3.getChild(j) instanceof
                                                            JavaParser.FinallyBlockContext){ //finally
                                                            for(int k=0;k<ctx3.getChild(j).getChild(1).getChildCount();k++){
                                                                if(ctx3.getChild(j).getChild(1).getChild(k).getChild(0)
                                                                        instanceof JavaParser.StatementContext &&
                                                                        ctx3.getChild(j).getChild(1).getChild(k).getChild(0).getText().contains("{")){
                                                                    queue.enqueue(ctx3.getChild(j).getChild(1).getChild(k).getChild(0));
                                                                    control_block_num++;
                                                                    addnum++;
                                                                }
                                                            }

                                                    }else if(ctx3.getChild(j) instanceof
                                                            JavaParser.SwitchBlockStatementGroupContext){//switch
                                                            for(int k=0;k<ctx3.getChild(j).getChildCount();k++){
                                                                if (ctx3.getChild(j).getChild(k) instanceof
                                                                        JavaParser.BlockStatementContext &&
                                                                        (ctx3.getChild(j).getChild(k).getChild(0).getText()).contains("{")){
                                                                    queue.enqueue(ctx3.getChild(j).getChild(k).getChild(0));
                                                                    control_block_num++;
                                                                    addnum++;
                                                                }
                                                            }
                                                    }
                                                }
                                                thisQ += addnum;
                                            }else{//普通语句，用于检测调用其他类方法否
                                                execStmt++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        } else {
            // System.out.println("lengths too low");
        }

        if (ctx.children.size() == 4) {
            //排除抽象方法(无函数体情况)abstract XXX();
            if (!";".equals(ctx.children.get(3).getText())) {
                //广度优先遍历函数体,访问其中的所有块，声明块中存在函数，其他块则继续访问
                Queue queue = new Queue();
                queue.enqueue(ctx.children.get(3).getChild(0));
                while (!queue.isEmpty()) {
                    //出队
                    try {
                        ParserRuleContext ctx1 = (ParserRuleContext) queue.dequeue();
                        ParserRuleContext ctx2 = null;
                        if (ctx1 instanceof JavaParser.StatementContext) {
                            //后续入队
                            //ifelse语句块导致ctx1.child中存在StatementContext
                            if (ctx1.getChildCount() > 1) {
                                for (int i = 0; i < ctx1.getChildCount(); i++) {
                                    if (ctx1.getChild(i) instanceof JavaParser.StatementContext &&
                                            ctx1.getChild(i).getChild(0) instanceof JavaParser.BlockContext) {
                                        ctx2 = (ParserRuleContext) ctx1.getChild(i).getChild(0);
                                    }
                                }
                            } else if (ctx1.getChild(0) instanceof JavaParser.BlockContext) {
                                ctx2 = (ParserRuleContext) ctx1.getChild(0);
                            }
                        } else if (ctx1 instanceof JavaParser.BlockContext) {
                            //第一次入队
                            ctx2 = ctx1;
                        }

                        if (ctx2 != null) {
                            //方法体里面的块的个数(包含两个括号)
                            int blocksize = ctx2.getChildCount();
                            if (blocksize >= 3) {
                                //排除空函数体情况XXX(){}
                                boolean setMethodName = false;
                                for (int i = 1; i < blocksize - 1; i++) {

                                    //声明语句块（LocalVariableDeclarationStatementContext）：可检测出类的声明、可执行语句
                                    ParserRuleContext ctx3 = (ParserRuleContext) ctx2.getChild(i).getChild(0);

                                    // 解析ctx3里面的各个节点
                                    Queue queue1 = new Queue();
                                    queue1.enqueue(ctx3);


                                    //将方法名等信息加入map(只能加一次)
                                    if(!setMethodName) {
                                        for (int k = 0; k < ctx.children.get(2).getChildCount() - 1; k++) {
                                            queue1.enqueue(ctx.children.get(2).getChild(k));
                                        }
                                        setMethodName = true;
                                    }

                                    while (!queue1.isEmpty()){
                                        Object obj=queue1.dequeue();
                                        if(obj instanceof TerminalNode || obj instanceof JavaParser.TypeContext){
                                            String objString ="";
                                            if(obj instanceof TerminalNode){
                                                objString = obj.toString();
                                            }else{
                                                JavaParser.TypeContext typeContext = (JavaParser.TypeContext)obj;
                                                objString = typeContext.getText();
                                            }
                                            // 已经存在该操作符
                                            if(operators.containsKey(objString)) {
                                                operators.put(objString,operators.get(objString)+1);
                                            }else {
                                                // 不存在该操作符
                                                operators.put(objString,1);
                                            }
                                        }else if (obj instanceof JavaParser.PrimaryContext || obj instanceof JavaParser.VariableDeclaratorIdContext){
                                            String objString="";
                                            if(obj instanceof JavaParser.PrimaryContext){
                                                JavaParser.PrimaryContext primaryContext = (JavaParser.PrimaryContext)obj;
                                                objString = primaryContext.getText();
                                            }else{
                                                JavaParser.VariableDeclaratorIdContext var = (JavaParser.VariableDeclaratorIdContext)obj;
                                                objString = var.getText();
                                            }

                                            // 已经存在该操作数
                                            if(operands.containsKey(objString)) {
                                                operands.put(objString,operands.get(objString)+1);
                                            }else {
                                                // 不存在该操作数
                                                operands.put(objString,1);
                                            }
                                        }else{
                                            // 不属于输出节点时，将其孩子放入队列
                                            ParserRuleContext child = (ParserRuleContext)obj;
                                            for(int j=0;j<child.getChildCount();j++) {
                                                queue1.enqueue(child.getChild(j));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }

        //添加方法实体
        methodEntity.setControl_block_num(control_block_num);
        methodEntity.setMax_block_depth(max_block_depth);
        methodEntity.setExecStmt(execStmt);
        methodEntity.setLocalVarDecl(localVarDecl);

        // 操作符，操作数 计算
        // 去除重复的三元运算符，重复的()[]{}
        operators.remove("?");
        operators.remove("(");
        operators.remove("[");
        operators.remove("{");
        // 去除点(点会被计算到操作符和操作数) 操作符. 操作数.
        operands.remove(".");
        operators.remove(".");

        int sumOperators=0;
        for (Map.Entry<String, Integer> entry : operators.entrySet()) {
            sumOperators+=operators.get(entry.getKey());
        }
        methodEntity.setOperators(operators.size());
        methodEntity.setSumOperators(sumOperators);

        int sumOperands=0;
        for (Map.Entry<String, Integer> entry : operands.entrySet()) {
            sumOperands+=operands.get(entry.getKey());
        }
        methodEntity.setOperands(operands.size());
        methodEntity.setSumOperands(sumOperands);
        //计算带参方法名
        String nameWithPara ;

        int count=0;
        int paraNum=methodEntity.getPara_num();

        nameWithPara = methodName+'(';
        if(paraNum!=0) {
            for(String paraType:methodEntity.para_class_list){
                nameWithPara+=paraType;

                if(count==paraNum-1) {
                    break;
                }
                count++;
                nameWithPara+=' ';
            }
        }
        nameWithPara+=')';
        methodEntity.setNameWithPara(nameWithPara);
        singleCollect.addEntity(methodEntity);
        classEntity.addToMethodList(methodName);

        return methodName;
    }

}
