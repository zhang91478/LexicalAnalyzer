package cn.xu419.commom;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author bsz
 * Created on 2018/4/9
 */
class LexicalAnalyzerTest {

    @Test
    void analysis() throws IOException {
        LexicalAnalyzer.analysis("E:/main.c");
    }
}