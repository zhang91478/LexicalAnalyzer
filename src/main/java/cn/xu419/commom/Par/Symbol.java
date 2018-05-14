package cn.xu419.commom.Par;

/**
 * @author bsz
 * Created on 2018/5/10
 */
public class Symbol {
    private Boolean kind;//true 为终结符
    private String value="";//字符内容


    public Symbol() {
    }

    public void setKind(Boolean kind) {
        this.kind = kind;

    }


    public Boolean getKind() {
        return kind;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Symbol(Boolean kind, String value) {
        this.kind = kind;
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
