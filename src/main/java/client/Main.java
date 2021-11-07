package client;

import util.Configure;

import java.io.File;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args){
        if(args.length < 2) {
            System.out.println("Not enough parameters!");
            exit(1);
        }

        if(!args[0].equals(Configure.JAVA_LANG)) {
            System.out.println("Not support this language: " + args[0]);
            exit(1);
        }

        TemplateWork templateWork = new TemplateWork();
        //long startTime = System.currentTimeMillis();

        templateWork.workflow(args);
    }
}
