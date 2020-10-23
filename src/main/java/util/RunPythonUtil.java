package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public final class RunPythonUtil {
    public static String file = System.getProperty("user.dir")+"\\src\\main\\java\\util\\python\\getAllMetric.py";

    public static void run(String xmlPath, String logPath, String methodChangePath, String understandOutputPath,
                           String methodMetricPath, String writePath) throws IOException {


        String[] cmdArr = new String[]{"python", file, xmlPath, logPath, methodChangePath, understandOutputPath,
                methodMetricPath, writePath};
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
