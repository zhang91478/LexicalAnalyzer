package cn.xu419.commom.Lex;

/**
 * @author bsz
 * Created on 2018/4/10
 */
public class Token {
    private int keyNum;
    private String value;

    public Token(int keyNum, String value) {
        this.keyNum = keyNum;
        this.value = value;
    }

    public int getKeyNum() {
        return keyNum;
    }

    public void setKeyNum(int keyNum) {
        this.keyNum = keyNum;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
