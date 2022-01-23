package util.paper;


import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.System.exit;

/**
 * @author wsz
 * @create 2021-12-22 21:46
 */
public class Cal80Bug{
    // discuss 4.6:fig 2
    // input readPath:bugnum file path
//    public static String readPath = "D:\\LearningFile\\projects\\FCHE\\avro-out\\avro_result.csv";
    public static String readPath = "";

    public static void main(String[] args) {
        if(args.length<1){
            System.out.println("Not enough parameters! Please set (Run-Edit Configuration-Program arguments) path");
            exit(1);
        }
        if(args[0]==null || "".equals(args[0])){
            System.out.println("error path");
            exit(1);
        }
        readPath = args[0];

        File file = new File(readPath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.out.println("read file error");
            e.printStackTrace();
            exit(1);
        }

        int sumBug=0;
        int bugMethodNum=0;
        int methodNum=0;
        List<Integer> bugs = new ArrayList<>();

        String tempString = null;
        while(true){
            try {
                if (!((tempString = reader.readLine())!=null)) break;
            } catch (IOException e) {
                System.out.println("read line error");
                e.printStackTrace();
                exit(1);
            }
            String[] split = tempString.split(",");
            if("Method name".equals(split[0])){
                continue;
            }
            int bugNum =0;
            try {
                bugNum = Integer.parseInt(split[split.length-1]);
            }catch (Exception e){
                System.out.println("please offer the bugnum file(mod 2)");
                e.printStackTrace();
                exit(1);
            }
            if(bugNum>0){
                sumBug+=bugNum;
                bugMethodNum++;
                bugs.add(bugNum);
            }
            methodNum++;
        }
        Collections.sort(bugs);
        Collections.reverse(bugs);
        int methodNum_80=0;
        int bugs_80 = (int) Math.round(sumBug*0.8);
        for (Integer bug : bugs) {
            methodNum_80++;
            bugs_80-=bug;
            if(bugs_80<=0){
                break;
            }
        }
        // output projectPath,Top80%bug method num
        System.out.println("path,Top80%bug method num");
        System.out.println(readPath+","+methodNum_80);
    }
}

