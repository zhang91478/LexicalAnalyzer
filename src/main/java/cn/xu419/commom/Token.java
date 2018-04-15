package cn.xu419.commom;

/**
 * @author bsz
 * Created on 2018/4/10
 */
public class Token {
    String key;
    String word;

    public Token(String key, String word) {
        this.key = key;
        this.word = word;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
