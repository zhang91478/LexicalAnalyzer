package cn.xu419;

import cn.xu419.commom.Lex.LexicalAnalyzer;

import java.io.IOException;

/**
 * @author bsz
 * Created on 2018/4/15
 */
public class Main {

    public static void main(String[] args) throws IOException {
        for (String arg : args) {
            System.out.println(arg);
            LexicalAnalyzer.analysis(arg);
        }

    }
}
