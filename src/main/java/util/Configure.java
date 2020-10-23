package util;

import java.io.File;

public class Configure {

    private Configure(){}

    private static Configure configure = new Configure();
    public static Configure getConfigureInstance() {
        return configure;
    }


    private String inputSrcPath;
    private String usageSrcPath;
    private String analyzedProjectName = "default";
    private String lang = "java";
    private String curr_pro_suffix = ".java";

    public static final String RELATION_LEVEL_FILE = "File";

    private String outputDotFile = analyzedProjectName + ".dot";

    private String outputCsvNodeFile = analyzedProjectName + "_node.csv";
    private String outputCsvEdgeFile = analyzedProjectName + "_edge.csv";
    private String outputCsvAllDepsFile = analyzedProjectName + "_allDeps.csv";
    private String outputMethodMetricsFile = analyzedProjectName + "_mehtodMetrics.csv";
    private String outputCsvDeps = analyzedProjectName+ "_deps.csv";

    private String outputJsonFile = analyzedProjectName  + "_dep.json";
    private String outputXmlFile = analyzedProjectName + "_dep.xml";
    private String attributeName = analyzedProjectName + "-sdsm";
    private String schemaVersion = "1.0";

    public static final String BASIC_ENTITY_FUNCTION = "function";
    public static final String BASIC_ENTITY_CLASS = "class";
    public static final String BASIC_ENTITY_FILE = "file";
    public static final String BASIC_ENTITY_FOLDER = "folder";
    public static final String IMPLICIT_DEPENDENCY = "Implicit";
    public static final String EXPLICIT_DEPENDENCY = "Explicit";

    public static final String JAVA_LANG = "java";
    public static final String JAVA_PRO_SUFFIX =".java" ;
    public static final String OS_DOT_NAME = "os.name";
    public static final String WINDOWS = "windows";
    public static final String LINUX = "linux";
    public static final String MAC = "mac";
    public static final String NULL_STRING = "";
    public static final String COMMA = ",";
    public static final String LEFT_PARENTHESES = "(";
    public static final String RIGHT_PARENTHESES = ")";
    public static final String DOT = ".";
    public static final String SEMICOLON = ";";
    public static final String STAR = "*";
    public static final String POINTER = "*";
    public static final String ONE_SPACE_STRING = " ";
    public static final String SEMI_COLON = ";";
    public static final String BLANK_IDENTIFIER = "_";
    public static final String SQUARE_BRACKETS = "[]";
    public static final String LEFT_SQUARE_BRACKET = "[";
    public static final String RIGHT_SQUARE_BRACKET = "]";
    public static final String ELLIPSIS = "...";
    public static final String LEFT_CURLY_BRACE = "{";
    public static final String RIGHT_CURLY_BRACE = "}";
    public static final String STRING_COLON = ":";
    public static final String EQUAL = "=";

    //class->interface
    public static final String RELATION_IMPLEMENT = "Implement";
    public static final String RELATION_IMPLEMENTED_BY = "Implemented by";

    public static final String RELATION_EXTEND = "Extend";
    public static final String RELATION_EXTENDED_BY = "Extended by";
    //file->package
    public static final String RELATION_IMPORT = "Import";
    public static final String RELATION_IMPORTED_BY = "Imported by";

    //class->class
    public static final String RELATION_INHERIT = "Inherit";
    public static final String RELATION_INHERITED_BY = "Inherited by";

    // struct->method
//    public static final String RELATION_RECEIVED_BY = "Received by";
//    public static final String RELATION_RECEIVE = "Receive";

    // method->method
    public static final String RELATION_CALL = "Call";
    public static final String RELATION_CALLED_BY = "Called by";

    //method->OperandVar   method->class
    public static final String RELATION_USE = "Use";
    public static final String RELATION_USED_BY = "Used by";

    //method->OperandVar
    public static final String RELATION_SET = "Set";
    public static final String RELATION_SETED_BY = "Seted by";

    //method->var
    public static final String RELATION_RETURN = "Return";
    public static final String RELATION_RETURNED_BY = "Returned by";

    //method->var
    public static final String RELATION_PARAMETER = "Parameter";
    public static final String RELATION_PARAMETERED_BY = "Parametered by";

    //method->class
    public static final String RELATION_TYPE = "type";
    public static final String RELATION_TYPED_BY = "typed by";

    //var->class
    public static final String RELATION_CREATE = "create";
    public static final String RELATION_CREATED_BY = "created by";

    public static final String RELATION_IMPLICIT_INTERNAL_CALL = "Nternal Implicit Call";
    public static final String RELATION_IMPLICIT_INTERNAL_CALLED_BY = "Nternal Implicit Call by";
    public static final String RELATION_IMPLICIT_EXTERNAL_CALL = "External Implicit Call";
    public static final String RELATION_IMPLICIT_EXTERNAL_CALLED_BY = "External Implicit Call by";

    public String getOutputJsonFile() {
        return outputJsonFile;
    }
    public String getOutputXmlFile() {
        return outputXmlFile;
    }

    public String getOutputCsvAllDepsFile() { return outputCsvAllDepsFile;
    }
    public String getOutputCsvMethodMetricsFile() {return outputMethodMetricsFile;
    }
    public String getOutputCsvEdgeFile() {
        return outputCsvEdgeFile;
    }
    public String getOutputCsvNodeFile() {
        return outputCsvNodeFile;
    }

    public String getOutputCsvDeps(String depType) {
        return analyzedProjectName + "_dep_"+depType+".csv";
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public String getAttributeName() {
        return attributeName;
    }
    public String getLang() {
        return lang;
    }
    public String getInputSrcPath() {
        return inputSrcPath;
    }
    public String getUnifiedInputSrcpath() {
        return StringUtil.unifyPath(inputSrcPath);
    }
    public String getUsageSrcPath() {
        return usageSrcPath;
    }
    public String getAnalyzedProjectName() {
        return analyzedProjectName;
    }
    public void setLang(String lang) {
        this.lang = lang;

        if(lang.equals(JAVA_LANG)) {
            curr_pro_suffix = JAVA_PRO_SUFFIX;
        }
    }
    public void setInputSrcPath(String inputSrcPath) {
        this.inputSrcPath = inputSrcPath;
    }
    public void setUsageSrcPath(String usageSrcPath) {
        this.usageSrcPath = usageSrcPath;
    }
    public void setAnalyzedProjectName(String analyzedProjectName) {
        new File(analyzedProjectName + "-out").mkdir();
        if(OsUtil.isWindows()) {
            this.analyzedProjectName = analyzedProjectName + "-out\\" + analyzedProjectName;
        }
        if(OsUtil.isMac() || OsUtil.isLinux()) {
            this.analyzedProjectName = analyzedProjectName + "-out/" + analyzedProjectName;
        }
    }
    public void setDefault() {
        outputJsonFile = analyzedProjectName  + "_dep.json";
        outputDotFile = analyzedProjectName + ".dot";
        outputXmlFile = analyzedProjectName + "_dep.xml";
        outputCsvNodeFile = analyzedProjectName + "_node.csv";
        outputCsvEdgeFile = analyzedProjectName + "_edge.csv";
        outputCsvAllDepsFile = analyzedProjectName + "_allDeps.csv";
        outputMethodMetricsFile =analyzedProjectName +"_methodMetric.csv";

        outputCsvDeps = analyzedProjectName + "_dep.csv";
        attributeName = analyzedProjectName + "-sdsm";
    }

    public String getCurr_pro_suffix() {
        return curr_pro_suffix;
    }

}
