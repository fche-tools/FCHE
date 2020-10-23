package priextractor;


import priextractor.javaextractor.JavaDepLyzer;

import util.Configure;

public class AnayzerIntf {

    public void run() {
        Configure configure = Configure.getConfigureInstance();
        String lang = configure.getLang();


        if(lang.equals(configure.JAVA_LANG)){
            JavaDepLyzer javaDepLyzer = new JavaDepLyzer();
            javaDepLyzer.identifyDeps();
        }
    }
}
