package formator.spreadsheet;

import entitybuilder.javabuilder.javaentity.MethodEntity;
import uerr.*;
import util.Configure;
import util.Tuple;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Csvgrapher {
    Configure configure = Configure.getConfigureInstance();
    private SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();
    private ArrayList<String[]> nodes = new ArrayList<String[]>();
    private ArrayList<String[]> methodMetrics = new ArrayList<String[]>();
    private ArrayList<String[]> edges = new ArrayList<String[]>();
    private ArrayList<String[]> allDeps = new ArrayList<String[]>();

    private ArrayList<String[]> callDeps = new ArrayList<String[]>();
    private ArrayList<String[]> typeDeps = new ArrayList<String[]>();
    private ArrayList<String[]> createDeps = new ArrayList<String[]>();
    private ArrayList<String[]> extendsDeps = new ArrayList<String[]>();
    private ArrayList<String[]> importDeps = new ArrayList<String[]>();
    private ArrayList<String[]> implementDeps = new ArrayList<String[]>();
    private ArrayList<String[]> returnDeps = new ArrayList<String[]>();

    /**
     * store all nodes and deps from singlecollect into nodes and edges
     */
    public void buildProcess() {
        processNodes();
        processDeps(transDep2Map());
        processMethodMetrics();
    }

    /**
     * id, name, type, parentId
     */
    private void processNodes() {
        nodes.add(new String[] {"Id", "type", "label", "Parent"});
        for (AbsEntity entity : singleCollect.getEntities()) {
            String id = Integer.toString(entity.getId());
            String type = getEntityType(entity);
            String name = entity.getName();
            String parentId = Integer.toString(entity.getParentId());
            if("Variable".equals(type) && entity.getParentId() == -1) {
                continue;
            }
            String[] row = new String[]{id,type,name, parentId};
            nodes.add(row);
        }
    }
    private void processMethodMetrics() {
        methodMetrics.add(new String[] {"id", "name", "paraCount", "lineCount","simpleCc","maxBlockDepth","execStmt","localVarDecl",
        "halstead_vocabulary","halstead_length","halstead_difficulty","halstead_volume","halstead_effort","halstead_bugs",
                "single_operators","single_operands","operators","operands"});
        for (AbsEntity entity : singleCollect.getEntities()) {
            if(entity instanceof MethodEntity) {
                String id = Integer.toString(entity.getId());
                String name = ((MethodEntity) entity).getNameWithPara();
                int paraCount = ((MethodEntity) entity).getPara_num();
                int lineCount = ((MethodEntity) entity).getMethod_line();
                int simpleCc = ((MethodEntity) entity).getControl_block_num();
                int maxBlockDepth = ((MethodEntity) entity).getMax_block_depth();
                int execStmt = ((MethodEntity) entity).getExecStmt();
                int localVarDecl = ((MethodEntity) entity).getLocalVarDecl();

                /**
                 * 操作符种类 n1,操作符总数N1,操作数种类n2,操作数总数N2;
                 */
                int n1 = ((MethodEntity) entity).getOperators();
                int N1 = ((MethodEntity) entity).getSumOperators();
                int n2 = ((MethodEntity) entity).getOperands();
                int N2 = ((MethodEntity) entity).getSumOperands();
                double halstead_vocabulary=0.0;
                double halstead_length=0.0;
                double halstead_level = 0.0;
                double halstead_difficulty=0.0;
                double halstead_volume=0.0;
                double halstead_effort=0.0;
                double halstead_bugs=0.0;
                if(n1!=0&&n2!=0&&N1!=0&&N2!=0){
                    halstead_vocabulary=(n1+0.0)+n2;
                    halstead_length=N1+N2;
                    halstead_level = (2/(n1+0.0))*(n2+0.0)/(N2+0.0);
                    halstead_difficulty=((n1+0.0)/2)*((N2+0.0)/(n2+0.0));
                    halstead_volume=halstead_length*(Math.log(halstead_vocabulary)/Math.log(2));
                    halstead_effort=halstead_difficulty*halstead_length*halstead_vocabulary;
                    halstead_bugs=Math.pow(halstead_effort,2.0/3.0)/3000;
                }
                String[] row = new String[]{id, name, String.valueOf(paraCount), String.valueOf(lineCount),
                        String.valueOf(simpleCc), String.valueOf(maxBlockDepth), String.valueOf(execStmt),
                        String.valueOf(localVarDecl),String.valueOf(halstead_vocabulary),String.valueOf(halstead_length),
                        String.valueOf(halstead_difficulty), String.valueOf(halstead_volume),
                        String.valueOf(halstead_effort),String.valueOf(halstead_bugs),
                        String.valueOf(n1),String.valueOf(n2),String.valueOf(N1),String.valueOf(N2)};
                methodMetrics.add(row);
            }
        }
    }


    //src, dst, deptype, primitivetype, weight
    private Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>>  transDep2Map() {
        Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>> deps = new HashMap<Integer, Map<Integer, Map<String, Map<String, Integer>>>>();
        for (AbsEntity entity : singleCollect.getEntities()) {
            int id1 = entity.getId();
            for (Tuple<String, Integer> re : entity.getRelations()) {
                int id2 = re.y;
                String pritiveType = re.x;
                if(!(pritiveType.equals(Configure.RELATION_IMPORT)
                        || pritiveType.equals(Configure.RELATION_EXTEND)
                        || pritiveType.equals(Configure.RELATION_TYPE)
                        || pritiveType.equals(Configure.RELATION_CALL)
                        || pritiveType.equals(Configure.RELATION_IMPLEMENT)
                        || pritiveType.equals(Configure.RELATION_CREATE)
                        || pritiveType.equals(Configure.RELATION_RETURN)
//                        || pritiveType.equals(Configure.RELATION_USE)
//                        || pritiveType.equals(Configure.RELATION_SET)
//                        || pritiveType.equals(Configure.RELATION_IMPLICIT_INTERNAL_CALL)
//                        || pritiveType.equals(Configure.RELATION_IMPLICIT_EXTERNAL_CALL)
                        )
                    ) {
                    continue;
                }
                String visibleType= getVisibility(pritiveType);
                if(!deps.containsKey(id1)) {
                    deps.put(id1, new HashMap<Integer, Map<String, Map<String, Integer>>>());
                }
                if(!deps.get(id1).containsKey(id2)) {
                    deps.get(id1).put(id2, new HashMap<String, Map<String, Integer>>());
                }
                if(!deps.get(id1).get(id2).containsKey(visibleType)) {
                    deps.get(id1).get(id2).put(visibleType, new HashMap<String, Integer>());
                }
                if(!deps.get(id1).get(id2).get(visibleType).containsKey(pritiveType)) {
                    deps.get(id1).get(id2).get(visibleType).put(pritiveType, 0);
                }
                int oldweight = deps.get(id1).get(id2).get(visibleType).get(pritiveType);
                deps.get(id1).get(id2).get(visibleType).put(pritiveType, oldweight + 1);
            }
        }
        return deps;
    }

    private String getVisibility(String pritiveType) {
        if(pritiveType.equals(Configure.RELATION_IMPORT)
                || pritiveType.equals(Configure.RELATION_EXTEND)
                || pritiveType.equals(Configure.RELATION_TYPE)
                || pritiveType.equals(Configure.RELATION_CALL)
                || pritiveType.equals(Configure.RELATION_IMPLEMENT)
                || pritiveType.equals(Configure.RELATION_CREATE)
                || pritiveType.equals(Configure.RELATION_RETURN)
//                || pritiveType.equals(Configure.RELATION_USE)
//                || pritiveType.equals(Configure.RELATION_SET)
        ){
          return "Explicit";
        }
        if(pritiveType.equals(Configure.RELATION_IMPLICIT_INTERNAL_CALL)){
            return "Implicit internal";
        }
        if(pritiveType.equals(Configure.RELATION_IMPLICIT_EXTERNAL_CALL)) {
            return "Implicit external";
        }
        return "";
    }

    private String getEntityType(AbsEntity entity) {
        String type="";
        if(entity instanceof AbsFLDEntity) {
            return "Package";
        }
        if(entity instanceof AbsFILEntity) {
            return "File";
        }
        if(entity instanceof AbsCLSEntity) {
            return "Class";
        }
        if(entity instanceof AbsFUNEntity) {
            return "Function";
        }
        if(entity instanceof AbsVAREntity) {
            return "Variable";
        }
        if(entity instanceof MethodEntity){
            return "Method";
        }
        return type;
    }

    //src, dst, deptype, primitivetype, weight
    private void processDeps(Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>> deps) {

        allDeps.add(new String[]{"SourceType","SourceName", "TargetType","TargetName", "PrimitiveType"});
        edges.add(new String[]{"Source", "Target", "PrimitiveType", "Weight"});
        callDeps.add(new String[]{"Source", "Target", "PrimitiveType", "Weight"});
        typeDeps.add(new String[]{"Source", "Target", "PrimitiveType", "Weight"});
        createDeps.add(new String[]{"Source", "Target", "PrimitiveType", "Weight"});
        extendsDeps.add(new String[]{"Source", "Target", "PrimitiveType", "Weight"});
        importDeps.add(new String[]{"Source", "Target", "PrimitiveType", "Weight"});
        implementDeps.add(new String[]{"Source", "Target", "PrimitiveType", "Weight"});
        returnDeps.add(new String[]{"Source", "Target", "PrimitiveType", "Weight"});

        for (Map.Entry<Integer, Map<Integer, Map<String, Map<String, Integer>>>> entry1 : deps.entrySet()) {
            int src = entry1.getKey();
            String srcName = singleCollect.getEntityById(src).getName();
            String srcType = singleCollect.getEntityById(src).getType();
            for (Map.Entry<Integer, Map<String, Map<String, Integer>>> entry2 : entry1.getValue().entrySet()) {
                int dst = entry2.getKey();
                String dstName = singleCollect.getEntityById(dst).getName();
                String dstType = singleCollect.getEntityById(dst).getType();
                for (Map.Entry<String, Map<String, Integer>> entry3 : entry2.getValue().entrySet()) {
                    String depType = entry3.getKey();
                    for (Map.Entry<String, Integer> entry4 : entry3.getValue().entrySet()) {
                        String primitiveType = entry4.getKey();
                        int weight = entry4.getValue();
                        String[] arr = new String[]{Integer.toString(src),
                                Integer.toString(dst), primitiveType, Integer.toString(weight)};
                        String[] arr2 = new String[]{srcType,srcName,dstType,dstName,primitiveType};

                        allDeps.add(arr2);
                        edges.add(arr);
                        switch (primitiveType) {
                            case Configure.RELATION_CALL:
                                callDeps.add(arr);
                                break;
                            case Configure.RELATION_TYPE:
                                typeDeps.add(arr);
                                break;
                            case Configure.RELATION_CREATE:
                                createDeps.add(arr);
                                break;
                            case Configure.RELATION_EXTEND:
                                extendsDeps.add(arr);
                                break;
                            case Configure.RELATION_IMPORT:
                                importDeps.add(arr);
                                break;
                            case Configure.RELATION_IMPLEMENT:
                                implementDeps.add(arr);
                                break;
                            case Configure.RELATION_RETURN:
                                returnDeps.add(arr);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }
    public ArrayList<String[]> getMethodMetrics() {
        return methodMetrics;
    }

    public ArrayList<String[]> getCallDeps() {
        return callDeps;
    }

    public ArrayList<String[]> getTypeDeps() {
        return typeDeps;
    }

    public ArrayList<String[]> getCreateDeps() {
        return createDeps;
    }
    public ArrayList<String[]> getReTurnDeps() {return returnDeps;}
    public ArrayList<String[]> getExtendsDeps() {
        return extendsDeps;
    }

    public ArrayList<String[]> getImportDeps() {
        return importDeps;
    }

    public ArrayList<String[]> getImplementDeps() {
        return implementDeps;
    }

    public ArrayList<String[]> getNodes() {
        return nodes;
    }
    public ArrayList<String[]> getEdges() {
        return edges;
    }

    public ArrayList<String[]> getAllDeps() { return allDeps;
    }

//    public ArrayList<String[]> getInheritDeps() {return inheritDeps; }

}


