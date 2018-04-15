package cn.xu419.util;



import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author bsz
 * Created on 2018/4/7
 */
public  class LexicalAnalyzer {


    private static String SplitToken = "$";//用于分割单词的符号
    private static Map<String,String> KeyWordToken;//kw表示关键字
    private static String NumberToken = "num";//表示字符
    private static Map<String,String> IdentifyToken;//表示是标识符


    private LexicalAnalyzer() {
    }

    /**
     * 过滤掉代码中的空格、Tab、换行，将代码按行存储便于显示出错行。
     *
     * @param code
     * 一段程序代码
     * @return 一个ArrayList,每个元素储存一行代码
     *
     */
    public static List<String> preProcess(List<String> code){
        List<String> list = new ArrayList<String>();
        Boolean blankSign;//用于控制多个空格只有一个生效，true表示仍未读入空格
        Boolean multilineComment = false;//用于控制多行注释,false表示未进入多行注释
        StringBuilder temp = new StringBuilder();//用于临时存储一行的代码
        Character c0;//前一个字符
        Character c1;//当前字符
        for (String aCode : code) {
            temp.delete(0,temp.length());//重置temp
            blankSign = false;//重置过滤空白的控制变量
           // multilineComment = false;//重置多行注释的控制变量


            if(aCode.isEmpty()){
                continue;
            }
            c1 = aCode.charAt(0);

            if (!multilineComment&&c1!=' '&&c1!='\t') {
                temp.append(c1); // TODO: 2018/4/7 其他字符直接写入构造器
            }



            for (int j = 1; j < aCode.length(); j++) {
                c0 = aCode.charAt(j-1); // TODO: 2018/4/7 前一个字符
                c1 = aCode.charAt(j);// TODO: 2018/4/7 当前处理字符

                //处于多行注释状态且前一个字符为'/',正处理字符为'/',则推出多行注释状态
                if (multilineComment&&c0 == '*' &&c1 == '/'){
                    // TODO: 2018/4/7 退出多行注释不会多写入字符，所以不用删除最后一个字符
                    multilineComment = false;//结束多行注释状态
                    continue;
                }



                //若处于多行注释状态则继续，若未处于多行注释状态，且能 进入多行注释状态，则进入多行注释状态。
                if(multilineComment||(c0=='/'&&c1=='*')){
                    if((c0=='/'&&c1=='*')){
                        // TODO: 2018/4/7 第一次进入多行注释将上次多写入的‘/’删除 / /
                        System.out.println("------------------>"+c0+c1+temp.length());
                        temp.deleteCharAt(temp.length()-1);
                    }
                    multilineComment = true;//进入多行注释状态
                    continue;
                }

                // TODO: 2018/4/7 处于空白过滤状态，表示已经读入一个空白（或tab）则什么都不干
                if(blankSign && (c1 == ' ' || c1 == '\t')){
                    continue;
                }

                //处于空白过滤状态，且读入空白则什么都不干
                if (!blankSign&&(c1 == ' ' || c1 == '\t')) {
                    blankSign = true;
                    temp.append(SplitToken);
                }else if(isSplitToken(c1)){
                    if (!blankSign){
                        temp.append(SplitToken);
                    }
                    temp.append(c1);
                    temp.append(SplitToken);
                    blankSign = true;
                }else if(c0 =='/' &&c1=='/'){
                    temp.deleteCharAt(temp.length()-1);// TODO: 2018/4/7 将上次多写入的‘/’删除 / /
                    break;// 本行处理结束
                }else {
                    temp.append(c1); // TODO: 2018/4/7 其他字符直接写入构造器
                    blankSign = false;//读入一个其他字符导致退出空白过滤状态。
                }
            }
            if(temp.length()!=0){
                list.add(temp.toString());//将此行数据写入list
            }


        }
        return list;
    }

    /**
     * 从文件按行读入代码
     * 
     * @param fileName
     * 文件名，包含路径
     * @return ArrayList
     * 按行存储的代码
     */
    /*
    public static List<String> readFile(String fileName) throws IOException {
        List<String> list = new ArrayList<String>();
        File file = new File(fileName);
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                list.add(s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }
*/


/*
    private Boolean IsSingleValueOperator(char c){
        char a[] = {'+','-','='};
        for (int i = 0; i < a.length; i++) {
            if(c==a[i]){
                return true;
            }
        }
        return false;
    }

    */


    /**
     * 如果是界符，将界符分割出来
     *
     * @param c
     * 一个字符
     * @return 是不是界符
     *
     */
    private static Boolean isSplitToken(Character c){
        Character delimiters[] = {'[',']','{','}','(',')',',',';'};
        for (Character anA : delimiters) {
            if (c == anA) {
                return true;
            }
        }
        return false;
    }

    private static Boolean isDoubleValueOperator(Character c0,Character c1){

        return (c0 == '|' && c1 == '|')
                || (c0 == '&' && c1 == '&')
                || (c0 == '>' && c1 == '=')
                || (c0 == '<' && c1 == '=')
                || (c0 == '=' && c1 == '=');

    }

    private static Boolean isSingleValueOperator(Character c){
        return (c == '|')
                || (c == '&')
                || (c == '>')
                || (c == '<')
                || (c == '=');
    }




}
