package cn.xu419.commom.Par;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * @author bsz
 * Created on 2018/5/12
 */
class ParserTest {

    @Test
    void Pro() {
        Parser.readProgramFile("E:/progra.txt");
        System.out.println("------------->文法");
        for (Program p : Parser.getPrograms()) {
            System.out.println(p.toString());
        }



        System.out.println("------------->非终结符");
        for (NonTerminator nt:Parser.getVN()){
            System.out.println(nt.toString());
        }


        System.out.println("------------->开始推导");
        Parser.process();
    }
}