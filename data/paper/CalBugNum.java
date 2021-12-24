//
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.stream.Collectors;
//
//import static java.lang.System.exit;
//
///**
// * @author wsz
// * @create 2021-12-22 21:40
// */
//public class CalBugNum {
//    // discuss 4.5:Table16,discuss4.7:Table17
//    // input readPath: avro_result
//    public static String readPath = "D:\\LearningFile\\projects\\FCHE\\avro-out\\avro_result.csv";
//
//    public static void main(String[] args) {
//        if(args.length<1){
//            System.out.println("Not enough parameters! Please set (Run-Edit Configuration-Program arguments)");
//            exit(1);
//        }
//        if(args[0]==null || "".equals(args[0])){
//            System.out.println("null file");
//        }
//        readPath = args[0];
//        try{
//            calBug(readPath);
//        }catch (Exception e){
//            e.printStackTrace();
//            exit(1);
//        }
//
//    }
//    /**
//     * cal bug-prone method line，not bug-prone method line
//     * @throws Exception
//     */
//    public static void calBug(String readPath)  {
//
//        CsvReader csvReader = null;
//        try {
//            csvReader = new CsvReader(readPath);
//        } catch (Exception e) {
//            System.out.println("fail to read file");
//        }
//
//        ArrayList<String> lines = csvReader.getLines();
//        int bugProneMethodNum=0;
//        int notBugProneMethodNum=0;
//
//        int bugProneCodeLine=0;
//        int notBugProneCodeLine=0;
//
//        // 1-10,11-25,26-50,51-51+
//        int[] proneRange = new int[4];
//        int[] notProneRange = new int[4];
//        for (int i = 0; i < lines.size(); i++) {
//            if(i!=0){
//                String[] line = lines.get(i).split(",");
//                String bugProne = line[line.length - 1];
//                int code_line = Integer.parseInt(line[4]);
//
//                if("bug-prone".equals(bugProne)){
//                    bugProneCodeLine += code_line;
//                    bugProneMethodNum+=1;
//                    getRange(proneRange, code_line);
//
//                }else{
//                    notBugProneCodeLine += code_line;
//                    notBugProneMethodNum+=1;
//                    getRange(notProneRange, code_line);
//
//                }
//            }
//        }
//
//
//        System.out.println("readPath,bugProneMethodNum,bugProneCodeLine,notBugProneMethodNum,notBugProneCodeLine");
//        System.out.print(readPath+","+bugProneMethodNum +","+ bugProneCodeLine + ",");
//        System.out.println( notBugProneMethodNum+","+ notBugProneCodeLine);
//
//        System.out.println("bugProne line(1-10,11-25,26-50,50+),");
//        ListHelper.printList(Arrays.stream(proneRange).boxed().collect(Collectors.toList()));
//    }
//
//    private static void getRange(int[] range, int code_line) {
//        if(code_line<=10){
//            range[0]+=1;
//        }else if(code_line<=25){
//            range[1]+=1;
//        }else if(code_line<=50){
//            range[2]+=1;
//        }else{
//            range[3]+=1;
//        }
//    }
//    public static  <T> void printList(List<T> list){
//        for (T t : list) {
//            System.out.print(t+",");
//        }
//        System.out.println();
//    }
//
//
//}
//class CsvReader {
//    private String filename;
//    private FileReader f;
//    private BufferedReader reader;
//    /**
//     * store all lines
//     */
//    ArrayList<String> lines = new ArrayList<>();
//
//    public CsvReader(String filename)  {
//        this.filename = filename;
//        try {
//            f  = new FileReader(filename);
//        } catch (FileNotFoundException e) {
//            System.out.println("fail to read csv file !");
//            e.printStackTrace();
//        }
//        reader = new BufferedReader(f);
//
//
//        while(true){String line = null;
//            try {
//                if (!((line=reader.readLine())!=null)) break;
//            } catch (IOException e) {
//                System.out.println("fail to read line !");
//                e.printStackTrace();
//            }
//            lines.add(line);
//        }
//
//    }
//
//    /**
//     * return all lines
//     * @return
//     */
//    public ArrayList<String> getLines() {
//        return lines;
//    }
//}
//
