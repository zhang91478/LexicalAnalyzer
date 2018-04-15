package cn.xu419.commom;

/**
 * @author bsz
 * Created on 2018/4/2
 */
public enum Symbol {
    ADD("+",33),
    SUB("-",34),
    START("*",35),
    FORWARDSLASH("/",36),
    PERCENT("%",37),
    AUTOADD("++",38),
    AUTOSUB("--",39),
    EQUAL("==",40),
    NOTEQUAL("!=",41),
    BEYOND(">",42),
    BELOW("<",43),
    GRATERANDEQUAL(">=",44),
    SMALLERANDEQUAL("<=",45),

    AND("&&",46),
    OR("||",47),
    NOT("!",48),

    LEFTBIGBRACKET("{",49),
    RIGHTBIGBRACKET("}",50),

    LEFTMEDIUMBRACKET("[",51),
    RIGHTMEDIUMBRACKET("]",52),

    LEFTSMALLBRACKET("(",53),
    RIGHTSMALLBRACKET(")",54);


    private String sign;
    private Integer id;


    Symbol(String sign, Integer id) {
        this.sign = sign;
        this.id = id;
    }

    public String getSign() {
        return sign;
    }

    public Integer getId() {
        return id;
    }


}
