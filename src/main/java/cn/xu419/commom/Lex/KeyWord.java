package cn.xu419.commom.Lex;

/**
 * @author bsz
 * Created on 2018/4/2
 */
public enum KeyWord {
    //数据类型关键字
    CHAR("char",1),
    DOUBLE("double",2),
    ENUM("enum",3),
    FLOAT("float",4),
    INT("int",5),
    LONG("long",6),
    SHORT("short",7),
    SIGNED("signed",8),
    UNION("union",9),
    UNSIGNED("unsigned",10),
    VOID("void",11),
    STRUCT("struct",12),

    //控制语句关键字
    FOR("for",13),
    DO("do",14),
    WHILE("while",15),
    BREAK("break",16),
    CONTINUE("continue",17),
    IF("if",18),
    ELSE("else",19),
    GOTO("goto",20),
    SWITCH("switch",21),
    CASE("case",22),
    DEFAULT("default",23),
    RETURN("return",24),

    //储存类型关键字体
    AUTO("auto",25),
    EXTERN("extern",26),
    REGISTER("register",27),
    STATIC("static",28),

    //其他关键字
    CONST("const",29),
    SIZEOF("sizeof",30),
    TYPEDEF("typedef",31),
    VOLATILE("volatile",32);


    private String word;
    private Integer id;

    KeyWord(String word, Integer id) {
        this.word = word;
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public static Boolean isKeyWord(String s){
        for (KeyWord e : KeyWord.values()) {
            if(s.equals(e.word))
                return true;
        }
        return false;
    }
}
