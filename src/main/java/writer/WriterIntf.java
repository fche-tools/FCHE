package writer;

import formator.fjson.JDepObject;
import formator.fxml.XDepObject;
import util.Configure;

import java.util.ArrayList;

public class WriterIntf<allDeps> {

    Configure configure = Configure.getConfigureInstance();

    public void run(ArrayList<String[]> nodes, ArrayList<String[]> edges, ArrayList<String[]> callDeps,
                    ArrayList<String[]> typeDeps, ArrayList<String[]> createDeps, ArrayList<String[]> extendsDeps,
                    ArrayList<String[]> importDeps, ArrayList<String[]> implementDeps, ArrayList<String[]> returnDeps,
                    ArrayList<String[]> allDeps,ArrayList<String[]> methodMetrics) {

        CsvWriter csvWriter = new CsvWriter();
        csvWriter.writeCsv(nodes, configure.getOutputCsvNodeFile());
        System.out.println("Export "+configure.getOutputCsvNodeFile());

        csvWriter.writeCsv(methodMetrics, configure.getOutputCsvMethodMetricsFile());
        System.out.println("Export "+configure.getOutputCsvMethodMetricsFile());

//        csvWriter.writeCsv(edges, configure.getOutputCsvEdgeFile());
//        System.out.println("Export "+configure.getOutputCsvEdgeFile());
//
//        csvWriter.writeCsv(allDeps, configure.getOutputCsvAllDepsFile());
//        System.out.println("Export "+configure.getOutputCsvAllDepsFile());
//
//
//        csvWriter.writeCsv(callDeps, configure.getOutputCsvDeps("call"));
//        System.out.println("Export "+configure.getOutputCsvDeps("call"));
//
//        csvWriter.writeCsv(typeDeps, configure.getOutputCsvDeps("type"));
//        System.out.println("Export "+configure.getOutputCsvDeps("type"));
//
//        csvWriter.writeCsv(createDeps, configure.getOutputCsvDeps("create"));
//        System.out.println("Export "+configure.getOutputCsvDeps("create"));
//
//        csvWriter.writeCsv(extendsDeps, configure.getOutputCsvDeps("extends"));
//        System.out.println("Export "+configure.getOutputCsvDeps("extends"));
//
//        csvWriter.writeCsv(importDeps, configure.getOutputCsvDeps("import"));
//        System.out.println("Export "+configure.getOutputCsvDeps("import"));
//
//        csvWriter.writeCsv(implementDeps, configure.getOutputCsvDeps("implement"));
//        System.out.println("Export "+configure.getOutputCsvDeps("implement"));
//
//        csvWriter.writeCsv(returnDeps, configure.getOutputCsvDeps("return"));
//        System.out.println("Export "+configure.getOutputCsvDeps("return"));

//        csvWriter.writeCsv(inheritDeps, configure.getOutputCsvDeps("inherit"));
//        System.out.println("Export "+configure.getOutputCsvDeps("inherit"));

        //output data by writers

//        JsonWriter jsonWriter = new JsonWriter();
//        jsonWriter.toJson(jDepObject, configure.getOutputJsonFile());
//        System.out.println("Export "+ configure.getOutputJsonFile());

//        XmlWriter xmlWriter = new XmlWriter();
//        xmlWriter.toXml(xDepObject, configure.getOutputXmlFile() );
//        System.out.println("Export "+ configure.getOutputXmlFile());

//        DotWriter dotWriter = new DotWriter();
//        String fileName1 = Configure.getConfigureInstance().getAnalyzedProjectName() + "_" + DotUtil.FILTER_NO_DEP + ".dot";
//        String fileName2 = Configure.getConfigureInstance().getAnalyzedProjectName() + "_" + DotUtil.FILTER_FILE_FOLDER_DEP + ".dot";
//        String fileName3 = Configure.getConfigureInstance().getAnalyzedProjectName() + "_" + DotUtil.FILTER_CLASS_DEP + ".dot";
//        String fileName4 = Configure.getConfigureInstance().getAnalyzedProjectName() + "_" + DotUtil.FILTER_FUNC_CLASS_DEP + ".dot";
//        String fileName5 = Configure.getConfigureInstance().getAnalyzedProjectName() + "_" + DotUtil.FILTER_FUNCTION_DEP + ".dot";
//        String fileName6 = Configure.getConfigureInstance().getAnalyzedProjectName() + "_" + DotUtil.FILTER_DEFAULT_DEP + ".dot";

//        dotWriter.writeDot(DotUtil.FILTER_NO_DEP, fileName1);
//        dotWriter.writeDot(DotUtil.FILTER_FILE_FOLDER_DEP, fileName2);
//        dotWriter.writeDot(DotUtil.FILTER_CLASS_DEP, fileName3);
//        dotWriter.writeDot(DotUtil.FILTER_FUNC_CLASS_DEP, fileName4);
//        dotWriter.writeDot(DotUtil.FILTER_FUNCTION_DEP, fileName5);
//        dotWriter.writeDot(DotUtil.FILTER_DEFAULT_DEP, fileName6);

        /*Render render = new Render();
        String destFileName1 = Configure.getConfigureInstance().getAnalyzedProjectName() + "_" + DotUtil.FILTER_NO_DEP + ".svg";
        String destFileName2 = Configure.getConfigureInstance().getAnalyzedProjectName() + "_" + DotUtil.FILTER_FILE_FOLDER_DEP + ".svg";
        String destFileName3 = Configure.getConfigureInstance().getAnalyzedProjectName() + "_" + DotUtil.FILTER_CLASS_DEP + ".svg";
        String destFileName4 = Configure.getConfigureInstance().getAnalyzedProjectName() + "_" + DotUtil.FILTER_FUNC_CLASS_DEP + ".svg";
        String destFileName5 = Configure.getConfigureInstance().getAnalyzedProjectName() + "_" + DotUtil.FILTER_FUNCTION_DEP + ".svg";
        String destFileName6 = Configure.getConfigureInstance().getAnalyzedProjectName() + "_" + DotUtil.FILTER_DEFAULT_DEP + ".svg";

        render.run(fileName1, "fdp", destFileName1, "svg");
        render.run(fileName1, "fdp", destFileName2, "svg");
        render.run(fileName1, "fdp", destFileName3, "svg");
        render.run(fileName1, "fdp", destFileName4, "svg");
        render.run(fileName1, "fdp", destFileName5, "svg");
        render.run(fileName1, "fdp", destFileName6, "svg");*/
    }

    public void undTest() {
        UndWriter undWriter = new UndWriter();
        undWriter.writeUnd();
    }


    public void exportImplicitExternalAtFileLevel() {
        ImplicitCallWriter implicitCallWriter = new ImplicitCallWriter();
        implicitCallWriter.writeImplicitCalls();
    }


}
