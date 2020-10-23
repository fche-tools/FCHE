package entitybuilder.javabuilder.javavisitor.test;

import uerr.SingleCollect;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class outputTest {

    public static File file=new File("C:\\Users\\wsz\\Desktop\\name.txt");

    public static void output1() throws IOException {
        String str="";

        try {
            FileInputStream in=new FileInputStream(file);
            // size 为字串的长度 ，这里一次性读完
            int size=in.available();
            byte[] buffer=new byte[size];
            in.read(buffer);
            in.close();
            str=new String(buffer,"GB2312");

            SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();

            Pattern pattern =Pattern.compile("method(.*)[(].*");//"class(.*)"
            Matcher matcher = pattern.matcher(str);
            StringBuffer sb=new StringBuffer();
            while(matcher.find()){
//                System.out.println(matcher.group(0));//classXXX
//                System.out.println(matcher.group(1));//XXX
//                matcher.appendReplacement(sb,matcher.group(1));
                String getstr = matcher.group(1);
                System.out.println(getstr);
                if(singleCollect.method_class_map.containsKey(matcher.group(1))){
                    String classname = singleCollect.method_class_map.get(getstr).toString();
                    matcher.appendReplacement(sb,classname);
                }else{
                    System.out.println("not exsit:"+getstr);
                    matcher.appendReplacement(sb,"not exsit:"+getstr);
                }
            }
            matcher.appendTail(sb);

            //消除class
            System.out.println(sb.toString());
            Pattern pattern2 = Pattern.compile("class(.+)");//"class(.*)"method([A-Za-z_]*).*""
            Matcher matcher2 = pattern2.matcher(sb.toString());
            StringBuffer sb2=new StringBuffer();
            while(matcher2.find()){
                //System.out.println(matcher.group(0));//classXXX
                //System.out.println(matcher.group(1));//XXX
                matcher2.appendReplacement(sb2,matcher2.group(1)+".java");
            }
            matcher2.appendTail(sb2);
            System.out.println(sb2.toString());

            //导出结果
            String out = sb2.toString();

            writeTxtFile(out, new File("C:\\Users\\wsz\\Desktop\\result.txt"), "utf8");


        } catch (IOException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }

    }

    public static boolean writeTxtFile(String content, File fileName, String encoding) {
        FileOutputStream o = null;
        boolean result=false;
        try {
            o = new FileOutputStream(fileName);
            o.write(content.getBytes(encoding));
            result=true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (o != null) {
                try {
                    o.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

}
