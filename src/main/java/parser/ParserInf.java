package parser;


import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import parser.parsejava.JavaLexer;
import parser.parsejava.JavaParser;

import util.Configure;

import java.io.IOException;

public class ParserInf {

    public ParseTree rootEntry(String fileFullPath) throws IOException {
        CharStream input = CharStreams.fromFileName(fileFullPath);
        Configure configure = Configure.getConfigureInstance();
        String lang = configure.getLang();

        ParseTree tree = null;

        if(lang.equals(Configure.JAVA_LANG)){
            JavaLexer lexer = new JavaLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JavaParser parser = new JavaParser(tokens);
            tree = parser.compilationUnit();

        }

        return tree;
    }

}
