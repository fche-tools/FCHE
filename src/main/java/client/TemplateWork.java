package client;

import entitybuilder.BuilderIntf;
import formator.Formator;
import formator.fjson.JDepObject;
import formator.fxml.XDepObject;
import formator.spreadsheet.Csvgrapher;
import hianalyzer.HiDepData;
import hianalyzer.HiDeper;
import priextractor.AnayzerIntf;
import priextractor.javaextractor.JavaRelationInf;
import uerr.RelationInterface;
import util.Configure;
import util.RunPythonUtil;
import writer.UndWriter;
import writer.WriterIntf;

import java.util.ArrayList;

public class TemplateWork {
    protected static Configure configure = Configure.getConfigureInstance();

    public final void workflow(String[] args) {
        String lang = args[0];
        String inputDir = args[1];
        String usageDir = null;
        String projectName = usageDir;
        String depMask = "111111111";
        if (args.length > 2) {
            projectName = args[2];
        }

//        if (args.length > 4) {
//            depMask = args[4];
//        }
        config(lang, inputDir, usageDir, projectName);
        String[] depTypes = getDepType(depMask);

        long startTime = System.currentTimeMillis();

        //identify Entities
        BuilderIntf entityTreeBuilder = new BuilderIntf();
        entityTreeBuilder.run();

        //extract Deps
        AnayzerIntf entityDepAnalyzer = new AnayzerIntf();
        entityDepAnalyzer.run();

        long endTime = System.currentTimeMillis();
        System.out.println("\nConsumed time: " + (float) ((endTime - startTime) / 1000.00) + " s,  or " + (float) ((endTime - startTime) / 60000.00) + " min.\n");

        //build hierarchical dependencies
        HiDeper hiDeper = new HiDeper();
        hiDeper.run();

        //hiDeper.tmpOutput();
        HiDepData hiDepData = HiDepData.getInstance();

//        Formator formator = new Formator(depTypes);
//        JDepObject jDepObject = formator.getfJsonDataModel();
//        XDepObject xDepObject = formator.getfXmlDataModel();

        Csvgrapher csvgrapher = new Csvgrapher();
        csvgrapher.buildProcess();
        ArrayList<String[]> allNodes = csvgrapher.getNodes();
        ArrayList<String[]> allEdges = csvgrapher.getEdges();
        ArrayList<String[]> allDeps = csvgrapher.getAllDeps();
        ArrayList<String[]> methodMetrics = csvgrapher.getMethodMetrics();

        ArrayList<String[]> callDeps = csvgrapher.getCallDeps();
        ArrayList<String[]> typeDeps = csvgrapher.getTypeDeps();
        ArrayList<String[]> createDeps = csvgrapher.getCreateDeps();
        ArrayList<String[]> extendsDeps = csvgrapher.getExtendsDeps();
        ArrayList<String[]> importDeps = csvgrapher.getImportDeps();
        ArrayList<String[]> implementDeps = csvgrapher.getImplementDeps();
        ArrayList<String[]> returnDeps = csvgrapher.getReTurnDeps();

        WriterIntf writer = new WriterIntf();
        writer.run(allNodes, allEdges,callDeps,typeDeps,createDeps,extendsDeps,importDeps,implementDeps,returnDeps,allDeps,methodMetrics);
        try {
            String resultPath = configure.getAnalyzedProjectName()+"_result.csv";
            String methodMetricPath = configure.getAnalyzedProjectName()+"_methodMetric.csv";
            if(args.length > 3){
                RunPythonUtil.run(args[3],args[4],args[5],args[6],methodMetricPath,resultPath);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //output the summary of the acquired results.
//        summary();

    }

    private void config(String lang, String inputDir, String usageDir, String projectName) {
        configure.setLang(lang);
        configure.setInputSrcPath(inputDir);
        configure.setUsageSrcPath(usageDir);
        configure.setAnalyzedProjectName(projectName);
        configure.setDefault();
    }

    private String[] getDepType(String depMask) {
        ArrayList<String> depStrs = new ArrayList<String>();
        for (int i = 0; i < depMask.toCharArray().length; i++) {
            if (depMask.toCharArray()[i] == '1') {
                if (i == 0) {
                    depStrs.add(Configure.RELATION_IMPORT);
                } else if (i == 1) {
                    depStrs.add(Configure.RELATION_INHERIT);
                } else if (i == 2) {
                    depStrs.add(Configure.RELATION_IMPLEMENT);
                } else if (i == 3) {
                    depStrs.add(Configure.RELATION_CREATE);//Receive
                } else if (i == 4) {
                    depStrs.add(Configure.RELATION_CALL);
                } else if (i == 5) {
                    depStrs.add(Configure.RELATION_EXTEND);//Set
                } else if (i == 6) {
//                    depStrs.add(Configure.RELATION_USE);
                } else if (i == 7) {
                    depStrs.add(Configure.RELATION_TYPE); //Parameter
                } else if (i == 8) {
//                    depStrs.add(Configure.RELATION_RETURN);
                }
            }
        }
        String[] depStrArr = depStrs.toArray(new String[depStrs.size()]);
        return depStrArr;
    }

    private void summary() {
        Configure configure = Configure.getConfigureInstance();
        RelationInterface relationInterface = null;

        if (configure.getLang().equals(Configure.JAVA_LANG)) {
            relationInterface = new JavaRelationInf();
        }
        if(relationInterface !=  null) {
//            System.out.println("\nSummarize the entity's results:");
//            System.out.println(relationInterface.entityStatis());
//            System.out.println("\nSummarize the dependency's results:");
//            System.out.println(relationInterface.dependencyStatis());
            UndWriter undWriter = new UndWriter();
            //System.out.println(undWriter.priDepStatis()+ "\n");
        }
    }
}
