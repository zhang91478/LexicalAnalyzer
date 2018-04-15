package cn.xu419.commom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;





/**
 * @author bsz
 * Created on 2018/4/8
 */
public class LexicalAnalyzer {
    private LexicalAnalyzer(){}//单例不允许被创建
    private static List<Token> tokens = new ArrayList<Token>();//储存结果方便打印
    private static List<Error> errors = new ArrayList<Error>();//储存错误信息

    private static List<Identifier> identifiers =new ArrayList<Identifier>();





    public static void analysis(String filePath) throws IOException {
        List<String> list = readFile(filePath);



        Character c1;//当前处理字符
        StringBuilder stringBuilder = new StringBuilder();

        int state = 0;// TODO: 2018/4/9 储存状态

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).length(); j++) {
                c1 = list.get(i).charAt(j);// TODO: 2018/4/9 获得当前字符
                stringBuilder.append(c1); // TODO: 2018/4/9 将字符存入字符串构造器



                System.out.println("当前字符-->"+c1+"当前缓存-->‘"
                        +stringBuilder.toString()+
                        "’当前状态-->"+state);

                switch (state) {
                    case 0: {
                        if (c1 == ' ' || c1 == '\t') {
                            // TODO: 2018/4/9 如果是空白或制表符
                            state = 8;
                        } else if (Character.isDigit(c1)) {
                            // TODO: 2018/4/9 如果是数字
                            state = 2;
                        } else if (Character.isLetter(c1) || c1 == '_') {
                            // TODO: 2018/4/9 如果是字母或下滑线
                            state = 1;
                        } else if (c1 == '/') {
                            // TODO: 2018/4/9 如果是单斜杠
                            state = 9;
                        } else if (c1 == '\'') {
                            // TODO: 2018/4/9 如果是单引号
                            state = 12;
                        } else if (c1 == '"') {
                            // TODO: 2018/4/9 如果是双引号
                            state = 15;
                        } else if (c1 == '<') {
                            // TODO: 2018/4/9 如果是小于号
                            state = 16;
                        } else if (c1 == '>') {
                            // TODO: 2018/4/9 如果是大于号
                            state = 17;
                        } else if (c1 == '=') {
                            // TODO: 2018/4/9 如果是等于号
                            state = 18;
                        } else if (c1 == '!') {
                            // TODO: 2018/4/9 如果是感叹号
                            state = 19;
                        } else if (c1 == '&') {
                            // TODO: 2018/4/9 如果是取地址符
                            state = 20;
                        } else if (c1 == '|') {
                            // TODO: 2018/4/9 如果是竖线
                            state = 21;

                        } else if (c1 == '%') {
                            // TODO: 2018/4/9 如果是百分号
                            state = 22;
                        } else if (c1 == '+') {
                            // TODO: 2018/4/9 如果是加号
                            state = 23;
                        } else if (c1 == '-') {
                            // TODO: 2018/4/9 如果是减号
                            state = 24;
                        } else if (c1 == '*') {
                            // TODO: 2018/4/9 如果是*
                            state = 25;
                        } else if (c1 == '[' || c1 == ']' || c1 == '(' || c1 == ')' ||c1=='.'||
                                c1 == '{' || c1 == '}' || c1 == ';' || c1 == ',' || c1 == ':'||c1=='#') {
                            // TODO: 2018/4/9 如果是界符,处理后将状态返回0
                            addTokenToList("Delimiter", stringBuilder);
                            state = 0;
                        } else {
                            // TODO: 2018/4/9 不是上述则报错
                            addErrorToList(i, "error character of " + c1);
                        }
                        break;
                    }
                    case 1:{
                        if(!Character.isDigit(c1)&&!Character.isLetter(c1)&&c1!='_'){
                            stringBuilder.deleteCharAt(stringBuilder.length()-1);
                            j--;
                            identifierOrKeyWord(stringBuilder);
                            state = 0;
                        }
                        break;
                    }
                    case 2: {
                        if (Character.isDigit(c1)) {
                            // TODO: 2018/4/9 如果是数字 2
                            break;
                        } else if (c1 == '.') {
                            // TODO: 2018/4/9 如果是小数点
                            state = 3;
                        } else if (c1 == 'E' || c1 == 'e') {
                            // TODO: 2018/4/9 如果是数字 3
                            state = 4;
                        } else {
                            // TODO: 2018/4/9 如果是别的，回退本字符
                            j--;
                            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                            addTokenToList("number", stringBuilder);
                            state = 0;
                        }
                        break;
                    }
                    case 3: {
                        if (Character.isDigit(c1)) {
                            // TODO: 2018/4/9 如果是数字 3

                            state = 4;
                        } else {
                            // TODO: 2018/4/9 如果可以进行运算，回退本字符
                            j--;
                            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                            addTokenToList("number", stringBuilder);
                            state = 0;
                        }
                        break;
                    }
                    case 4: {
                        if (Character.isDigit(c1)) {
                            break;
                        } else if (c1 == 'E' || c1 == 'e') {
                            // TODO: 2018/4/9 如果是数字 3
                            state = 5;
                        } else {
                            // TODO: 2018/4/9 如果是别的，回退本字符
                            j--;
                            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                            addTokenToList("number", stringBuilder);
                            state = 0;
                        }
                        break;
                    }
                    case 5: {
                        if (Character.isDigit(c1)) {
                            // TODO: 2018/4/9 如果是数字 2
                            break;
                        } else if (c1 == '+' || c1 == '-') {
                            // TODO: 2018/4/9 如果是数字 3
                            state = 6;
                        } else {
                            // TODO: 2018/4/9 如果是别的，回退本字符
                            j--;
                            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                            addTokenToList("number", stringBuilder);
                            state = 0;
                        }
                        break;
                    }
                    case 6: {
                        if (Character.isDigit(c1)) {
                            state = 7;
                        } else {
                            // TODO: 2018/4/9 如果是别的，回退本字符
                            j--;
                            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                            addTokenToList("number", stringBuilder);
                            state = 0;
                        }
                        break;
                    }
                    case 7: {
                        if (!Character.isDigit(c1)) {
                            // TODO: 2018/4/9 如果是别的，回退本字符
                            j--;
                            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                            addTokenToList("number", stringBuilder);
                            state = 0;
                        }
                        break;
                    }
                    // TODO: 2018/4/9 数字处理完成
                    case 8:{
                        if(c1!=' '&&c1!='\t'){
                            j--;
                            stringBuilder.deleteCharAt(stringBuilder.length()-1);
                            state = 0;
                        }
                        stringBuilder.deleteCharAt(stringBuilder.length()-1);
                        break;
                    }
                    case 9:{
                        if (c1=='/'){
                            j = list.get(i).length()+1;
                            stringBuilder.delete(0,stringBuilder.length());
                            state = 0;
                        } else if(c1 == '*'){
                            state = 10;
                        } else {
                            state = 0;
                            addErrorToList(i,"error:"+stringBuilder.toString());
                        }
                        break;
                    }
                    case 10:{
                        if(c1 =='*'){
                            state = 11;
                        }
                        break;
                    }
                    case 11:{
                        if(c1 == '/'){
                            state = 0;
                            stringBuilder.delete(0,stringBuilder.length());
                        }else {
                            state = 10;
                        }
                        break;
                    }
                    case 12:{
                        if (c1=='\\'){
                            state = 14;
                        }else {
                            state = 13;
                        }
                        break;
                    }
                    case 13:{
                        if (c1 == '\''){
                            // TODO: 2018/4/10 加入一个字符Token
                            addTokenToList("character",stringBuilder);
                            state = 0;
                        } else {
                            addErrorToList(i,"error of '");
                        }
                        break;
                    }
                    case 14:{
                        if (c1=='b'||c1=='a'||c1=='n'||c1=='t'||c1=='r'){
                            addTokenToList("character",stringBuilder);
                        } else {
                            addErrorToList(i,"error of '");
                        }
                        state = 0;
                        break;
                    }
                    case 15:{
                        if(c1=='"'){
                            // TODO: 2018/4/10 加入一个字符串
                            addTokenToList("string",stringBuilder);
                            state = 0;
                        }
                        break;
                    }
                    case 16:{
                        if(c1!='<'&&c1!='='){
                            stringBuilder.deleteCharAt(stringBuilder.length()-1);
                            j--;
                        }
                        // TODO: 2018/4/10 加入一个操作符
                        addTokenToList("operator",stringBuilder);
                        state = 0;
                        break;
                    }
                    case 17:{
                        if(c1!='>'&&c1!='='){
                            stringBuilder.deleteCharAt(stringBuilder.length()-1);
                            j--;
                        }
                        // TODO: 2018/4/10 加入一个操作符
                        addTokenToList("operator",stringBuilder);
                        state = 0;
                        break;
                    }
                    case 18:{
                        if(c1!='='){
                            stringBuilder.deleteCharAt(stringBuilder.length()-1);
                            j--;
                        }
                        // TODO: 2018/4/10 加入一个操作符
                        addTokenToList("operator",stringBuilder);
                        state = 0;
                        break;
                    }
                    case 19:{
                        if(c1!='='){
                            stringBuilder.deleteCharAt(stringBuilder.length()-1);
                            j--;
                        }
                        // TODO: 2018/4/10 加入一个操作符
                        addTokenToList("operator",stringBuilder);
                        state = 0;
                        break;
                    }
                    case 20:{
                        if(c1!='&'){
                            stringBuilder.deleteCharAt(stringBuilder.length()-1);
                            j--;
                        }
                        // TODO: 2018/4/10 加入一个操作符
                        addTokenToList("operator",stringBuilder);
                        state = 0;
                        break;
                    }
                    case 21:{
                        if(c1!='|'){
                            stringBuilder.deleteCharAt(stringBuilder.length()-1);
                            j--;
                        }
                        // TODO: 2018/4/10 加入一个操作符
                        addTokenToList("operator",stringBuilder);
                        state = 0;
                        break;
                    }
                    case 22:{
                        // TODO: 2018/4/10 如果是%回退一个字符
                        stringBuilder.deleteCharAt(stringBuilder.length());
                        j--;
                        state = 0;
                        break;
                    }
                    case 23:{
                        // TODO: 2018/4/10 加入一个操作符 ++
                        if(c1!='+'){
                            stringBuilder.deleteCharAt(stringBuilder.length()-1);
                            j--;
                        }
                        addTokenToList("operator",stringBuilder);
                        state = 0;
                        break;
                    }
                    case 24:{
                        // TODO: 2018/4/10 加入一个操作符 ++
                        if(c1!='-'){
                            stringBuilder.deleteCharAt(stringBuilder.length()-1);
                            j--;
                        }
                        addTokenToList("operator",stringBuilder);
                        state = 0;
                        break;
                    }
                    case 25:{
                        // TODO: 2018/4/10 加入一个操作符 *
                        stringBuilder.deleteCharAt(stringBuilder.length()-1);
                        j--;
                        addTokenToList("operator",stringBuilder);
                        state = 0;
                        break;
                    }



                    default:{
                        state = 0;
                        stringBuilder.delete(0,stringBuilder.length());
                        break;
                    }


                }
            }
        }
        showTokens();
        showErrors();
        showIdentifiers();
    }



    /**
     * 构造一个token加入列表,并清空缓存器
     *
     * @param key
     * The key of new token
     * @param stringBuilder
     * The value of new token
     */
    private static void addTokenToList(String key, StringBuilder stringBuilder){
        Token token = new Token(key,stringBuilder.toString());
        stringBuilder.delete(0,stringBuilder.length());
        tokens.add(token);
    }

    /**
     * 区分关键字和标识符
     *
     * @param stringBuilder
     * 字符串构造器
     *
     */
    private static void identifierOrKeyWord(StringBuilder stringBuilder){
        // TODO: 2018/4/11 区分关键字和标识符
        Token token = new Token("name",stringBuilder.toString());
        if(KeyWord.isKeyWord(stringBuilder.toString())){
            token.setKey("KeyWord");
        }else {
            Boolean sign = true;
            for (Identifier identifier1 : identifiers) {
                if (identifier1.getName().equals(stringBuilder.toString())) {
                    token.setKey("identifier");
                    token.setWord(identifier1.getId().toString());
                    sign = false;
                    break;
                }
            }
            if (sign){
                Identifier identifier = new Identifier(identifiers.size(),stringBuilder.toString());
                identifiers.add(identifier);
                token.setKey("identifier");
                token.setWord(String.valueOf(identifiers.size()));
            }

        }

        stringBuilder.delete(0,stringBuilder.length());
        tokens.add(token);
    }

    /**
     * 添加一个错误信息到list
     *
     * @param key
     * 行
     * @param value
     * 错误信息
     */
    private static void addErrorToList(Integer key, String value){
        Error error = new Error(key,value);
        errors.add(error);
    }


    /**
     * 从文件按行读入代码
     *
     * @param fileName
     * 文件名，包含路径
     * @return ArrayList
     * 按行存储的代码
     */
    private static List<String> readFile(String fileName) {
        List<String> list = new ArrayList<String>();
        File file = new File(fileName);
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String str;
            while((str = br.readLine())!=null){//使用readLine方法，一次读一行
                list.add(" "+str+" ");
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }

    /**
     *
     * 打印所有Token到控制台
     *
     */
    private static void showTokens(){
        for (Token token : tokens) {
            System.out.println("<" + token.getKey() + "," + token.word + ">");
        }
    }

    /**
     *
     * 打印所有错误到控制台
     *
     */
    private static void showErrors(){
        for (Error error : errors) {
            System.out.println("第" + error.line + "行：" + error.info);
        }
    }

    private static void showIdentifiers(){
        for (Identifier i : identifiers) {
            System.out.println(i.toString());
        }
    }
}
