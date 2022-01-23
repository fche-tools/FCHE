package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


public final class RunPythonUtil {

//    public static String file = new ClassPathResource("/python/getAllMetric.py").getPath();
    public static String path = RunPythonUtil.class.getClassLoader().getResource("getAllMetric.py").getPath();
    public static String file = path.substring(path.indexOf("/")+1);
    public static void run(String xmlPath, String logPath, String methodChangePath, String understandOutputPath,
                           String methodMetricPath, String writePath, String printMod) throws IOException {

        System.out.println("文件路径："+file);
        if(!new File(file).isFile()){
            file = "python\\getAllMetric.py";
            System.out.println("not found："+file);
            if(!new File(file).isFile()){
                System.out.println("not found:"+file);
            }else{
                System.out.println("find:"+file);
            }
        }else {
            System.out.println("find:"+file);
        }
        String[] cmdArr = new String[]{"python", file, xmlPath, logPath, methodChangePath, understandOutputPath,
                methodMetricPath, writePath, printMod};
        Process process = Runtime.getRuntime().exec(cmdArr);

        // 获取python输出流并输出
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
        in.close();
    }

}
