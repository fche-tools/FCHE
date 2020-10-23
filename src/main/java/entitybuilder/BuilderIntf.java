package entitybuilder;

import entitybuilder.javabuilder.javavisitor.JavaEntityVisitor;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import parser.ParserInf;
import uerr.SingleCollect;
import util.Configure;
import util.FileUtil;
import util.StringUtil;

import java.io.IOException;

public class BuilderIntf {

    private Configure configure = Configure.getConfigureInstance();
    private AbstractParseTreeVisitor visitor = null; //from this package
    private ParseTree tree = null;

    /**
     * get visitor from current package
     * @param fileFullPath
     */
    private void setVisitor(String fileFullPath) {

        if (configure.getLang().equals(Configure.JAVA_LANG)) {
            if(!fileFullPath.endsWith("_test.java")) {
                visitor = new JavaEntityVisitor(fileFullPath);
            }
        }
    }

    /**
     * get tree from parser package (antlr4 package)
     * @param fileFullPath
     */
    private void setTree(String fileFullPath) {
        ParserInf parserInterface = new ParserInf();
        try {
            tree = parserInterface.rootEntry(fileFullPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void run(){
        FileUtil fileUtil = new FileUtil(configure.getInputSrcPath());
        for (String fileFullPath : fileUtil.getFileNameList(configure.getCurr_pro_suffix())) {

            setTree(fileFullPath); //use the original filepath, Antlr Parser will read the content of the file.
            setVisitor(StringUtil.unifyPath(fileFullPath)); //our customizer visitor, use the unified path

            if(tree != null && visitor != null) {
                System.out.println(fileFullPath);
                visitor.visit(tree);
            }
            tree = null;
            visitor = null;
        }
        SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();
        singleCollect.method_class_map.size();
//        try {
//            outputTest.output1();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.out.println("Identify entities successfully...");


    }


}
