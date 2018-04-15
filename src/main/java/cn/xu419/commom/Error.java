package cn.xu419.commom;

/**
 * @author bsz
 * Created on 2018/4/10
 */
public class Error {
    Integer line;
    String info;

    public Error(Integer line, String info) {
        this.line = line;
        this.info = info;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
