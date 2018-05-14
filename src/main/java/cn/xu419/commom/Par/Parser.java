package cn.xu419.commom.Par;


import cn.xu419.commom.Lex.Token;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;


/**
 * @author bsz
 * Created on 2018/5/9
 */
public class Parser {

    private static ArrayList<Program> programs = new ArrayList<Program>();//语法
    private static ArrayList<NonTerminator> VN = new ArrayList<NonTerminator>();//终结符
    private static ArrayList<Terminator> VT = new ArrayList<Terminator>();
    private static int[][] TABLE;




    public static boolean process(ArrayList<Terminator> input) {
        ArrayList<Program> list = new ArrayList<Program>();

        // TODO: 2018/5/14 将programs复制到list
        for (int i=0;i<programs.size();i++) {
            ArrayList<Symbol> right = new ArrayList<Symbol>();
            ArrayList<NonTerminator> nts = new ArrayList<NonTerminator>();
            NonTerminator left = new NonTerminator(programs.get(i).getLeft().getValue());
            for (Symbol s:programs.get(i).getRight()){
                if(s.getKind()){
                    right.add(new Terminator(s.getValue()));
                }else {
                    int count = -1;
                    for (int j = 0; j < nts.size(); j++) {
                        if (nts.get(j).getValue().equals(s.getValue())){
                            count = j;break;
                        }
                    }
                    if(count>-1){
                        right.add(nts.get(count));
                    }else {
                        NonTerminator nonTerminator = new NonTerminator(s.getValue());
                        right.add(nonTerminator);
                        nts.add(nonTerminator);
                    }
                }
            }
            Program p = new Program();
            p.setLeft(left);
            p.setRight(right);
            list.add(p);
        }

        // TODO: 2018/5/14
        System.out.println("打印一下list看复制是否成功");
        for (Program program:list) {
           System.out.println(program.toString());

        }


        // TODO: 2018/5/14 第一次扫描文法找出确定能够推空的和确定不能推空的
        for (int i = 0; i < list.size(); i++) {
            ArrayList<Symbol> right = list.get(i).getRight();
            if (!right.get(0).getKind()) {
                for (int j = 1; j < right.size(); j++) {
                    if (right.get(j).getKind()) {
                        list.remove(i);
                        i--;
                        break;
                    }
                }
            } else if (right.get(0).getValue().equals("~")) {

                list.get(i).getLeft().setTag(1);//表示能推出空
                VN.get(isInVN(list.get(i).getLeft().getValue())).setTag(1);//同步更新VN
                i = removeFormProgramList(list, list.get(i).getLeft().getValue(), i);
            } else {
                list.remove(i);
                i--;
            }
        }

        // TODO: 2018/5/14 确定能否推空完成，打印一下表格
        System.out.println("可推空的非终结符表：");
        for (NonTerminator nt111:VN) {
            System.out.println(nt111.getValue()+":"+nt111.getTag());
        }


        for (NonTerminator nt : VN) {
            if (!isInProgramsList(list, nt.getValue()) && nt.getTag() != 1) {
                nt.setTag(-1);//表示不能推出空
                for (Program aList : list) {
                    if (aList.getLeft().getValue().equals(nt.getValue())) {
                        aList.getLeft().setTag(-1);
                        break;
                    }
                }
            }
        }

        // TODO: 2018/5/14 确定能否推空完成，打印一下表格
        System.out.println("可推空的非终结符表：");
        for (NonTerminator nt111:VN) {
            System.out.println(nt111.getValue()+":"+nt111.getTag());
        }

        // TODO: 2018/5/14 重复扫描文法确定剩余文法是否能推空
        boolean isTheThirdStepOk;
        do {
            isTheThirdStepOk = false;
            for (int j = 0; j < list.size(); j++) {
                ArrayList<Symbol> right = list.get(j).getRight();
                for (int k = 0; k < right.size(); k++) {
                    //如果是非终结符 看能不能推空
                    if (!right.get(k).getKind()) {
                        NonTerminator nt = VN.get(isInVN(right.get(k).getValue()));

                        if (nt.getTag() == 1) {
                            right.remove(k);
                            k--;
                        } else if (nt.getTag() == -1) {
                            list.remove(j);
                            j--;
                        }
                    }
                }
                for (NonTerminator nt : VN) {
                    if (isInProgramsList(list, nt.getValue()) && nt.getTag() != -1) {
                        nt.setTag(-1);//表示不能推出空
                    }
                }
                if (right.size() == 0) {
                    if (list.get(j).getLeft().getTag() != 1) {
                        list.get(j).getLeft().setTag(1);
                        VN.get(isInVN(list.get(j).getLeft().getValue())).setTag(1);
                        isTheThirdStepOk = true;
                    }
                    j = removeFormProgramList(list, list.get(j).getLeft().getValue(), j);

                }
            }
        } while (isTheThirdStepOk);


        // TODO: 2018/5/14 确定能否推空完成，打印一下表格
        System.out.println("可推空的非终结符表：");
        for (NonTerminator nt111:VN) {
            System.out.println(nt111.getValue()+":"+nt111.getTag());
        }

        // TODO: 2018/5/14 打印结束，开始求First

        for (NonTerminator nt : VN) {
            for (Program program:programs) {
                ArrayList<Symbol> right = program.getRight();
                for (Symbol symbol:right){
                    if(symbol.getKind()){
                        program.getLeft().getFirstSet().add(VT.get(isInVT(symbol.getValue())));
                        break;
                    }else if(VN.get(isInVN(symbol.getValue())).getTag()==-1){
                        program.getLeft().getFirstSet().addAll(VN.get(isInVN(symbol.getValue())).getFirstSet());
                        break;
                    }else {
                        program.getLeft().getFirstSet().addAll(VN.get(isInVN(symbol.getValue())).getFirstSet());
                        if(symbol!=right.get(right.size()-1)){
                            program.getLeft().getFirstSet().remove(VT.get(isInVT("~")));
                        }
                    }
                }
            }

        }

        // TODO: 2018/5/14 确定能否推空完成，打印First
        System.out.println("---------------------打印一下FIRST");
        for (NonTerminator nt:VN) {
            System.out.println(nt.getValue()+":"+nt.getFirstSet());
        }

        // TODO: 2018/5/14 求完First  求Follow
        VT.add(new Terminator("#"));
        programs.get(0).getLeft().getFollowSet().add(VT.get(isInVT("#")));


        for (NonTerminator nt:VN) {
            for (Program program:programs) {

                System.out.println("打印一下FOLLOW");
                for (NonTerminator nt111:VN) {
                    System.out.println(nt111.getValue()+":"+nt111.getFollowSet());
                }
                ArrayList<Symbol> right = program.getRight();
                for (int i = 0;i<right.size()-1;i++){
                    //如果当前是非终结符
                    if(!right.get(i).getKind()){
                        for (int j = 1;j<right.size()-i;j++){
                            NonTerminator nonTerminator = VN.get(isInVN(right.get(i).getValue()));//当前处理的非终结符
                            Symbol symbol = right.get(i+j);//下一个要判断的字符。
                            if(symbol.getKind()){
                                //如果下一个字符为非总结符。则结束求follow
                                nonTerminator.getFollowSet().add(VT.get(isInVT(symbol.getValue())));
                                break;
                            } else if((!symbol.getKind())&&VN.get(isInVN(symbol.getValue())).getTag()==-1){
                                //下一个字符是非终结符且不能推空,则把他的first加进来
                                nonTerminator.getFollowSet().addAll(VN.get(isInVN(symbol.getValue())).getFirstSet());
                                break;
                            } else {
                                //如果下一个字符是非终结符且能推空，则把他的first加进来，移除空然后看下一个
                                nonTerminator.getFollowSet().addAll(VN.get(isInVN(symbol.getValue())).getFirstSet());
                                nonTerminator.getFollowSet().remove(VT.get(isInVT("~")));
                                //如果下一个字符是最后一个字符的话，将让的左边的的Follow加进来
                                if((i+j)==(right.size()-1)){
                                    nonTerminator.getFollowSet().addAll(program.getLeft().getFollowSet());
                                }
                            }
                        }
                    }
                }
                //如果字符是最后一个将他的左边的follow加进来
                if(!right.get(right.size()-1).getKind()){
                    VN.get(isInVN(right.get(right.size()-1).getValue())).getFollowSet().addAll(program.getLeft().getFollowSet());
                }
            }


        }
        System.out.println("打印一下FOLLOW");
        for (NonTerminator nt:VN) {
            System.out.println(nt.getValue()+":"+nt.getFollowSet());
        }

        // TODO: 2018/5/14 求select


        for (Program p : programs) {
            ArrayList<Symbol> right = p.getRight();
            boolean isNeedAddLeft = true;//需不需要把左边加入
            for (Symbol symbol:right){
                if(symbol.getKind()){
                    if(symbol.getValue().equals("~")){
                        p.getSELECT().addAll(p.getLeft().getFollowSet());
                    }else {
                        p.getSELECT().add(VT.get(isInVT(symbol.getValue())));
                    }
                    isNeedAddLeft = false;
                    break;
                }else if(VN.get(isInVN(symbol.getValue())).getTag()==-1){
                    p.getSELECT().addAll(VN.get(isInVN(symbol.getValue())).getFirstSet());
                    isNeedAddLeft = false;
                    break;
                }else {
                    p.getSELECT().addAll(VN.get(isInVN(symbol.getValue())).getFirstSet());
                    p.getSELECT().remove(VT.get(isInVT("~")));
                }
            }
            if(isNeedAddLeft){
                p.getSELECT().addAll(p.getLeft().getFollowSet());
            }
        }

        // TODO: 2018/5/14 打印一下SELECT
        System.out.println("打印一下SELECT");
        for (Program p : programs) {
            System.out.println(p.toString()+"="+p.getSELECT().toString());
        }


        // TODO: 2018/5/14 暂时不判定是否是了LL(1)文法，直接求表

        // TODO: 2018/5/14 初始化表
        TABLE = new int[VN.size()][];
        for (int i = 0; i < VN.size(); i++) {
            TABLE[i] = new int[VT.size()];
            for (int j = 0; j < VT.size(); j++) {
                TABLE[i][j] = -1;
            }
        }

        for (int i = 0; i < programs.size(); i++) {
            HashSet<Symbol> set = programs.get(i).getSELECT();
            for (Symbol s : set) {
                TABLE[isInVN(programs.get(i).getLeft().getValue())][isInVT(s.getValue())]= i;
            }
        }
        System.out.println("打印一下表格");
        System.out.print("\t");
        for (Terminator aVT : VT) {
            System.out.print(aVT.getValue() + "\t");
        }
        System.out.println();
        for (int i = 0; i < VN.size(); i++) {
            System.out.print(VN.get(i).getValue()+"\t");
            for (int j = 0; j < VT.size(); j++) {
                System.out.print(TABLE[i][j]+"\t");
            }
            System.out.println();
        }

        Stack<Symbol> analysisStack = new Stack<Symbol>();

        analysisStack.push(programs.get(0).getLeft());
        int count = 0;//记录输入串位置
        int step= 1;//记录步骤
        while (!analysisStack.empty()){
            System.out.println("第"+step+"步");
            System.out.println("分析栈"+analysisStack+"\t");
            System.out.print("剩余输入串");
            for (int i = count; i < input.size(); i++) {
                System.out.print(input.get(i).getValue());
            }
            System.out.println();
            if(!analysisStack.peek().getKind()){
                //如果栈顶是非终结符则选择产生式替换，打印产生式
//                System.out.println(analysisStack.peek().getValue());
//                System.out.println(input.get(count).getValue());


                int x = isInVN(analysisStack.peek().getValue());
                int y = isInVT(input.get(count).getValue());
//                System.out.print("x="+x+",y="+y);
                int orderOfProgram = TABLE[x][y];
                if(orderOfProgram>-1){
                    System.out.println("操作："+programs.get(orderOfProgram));
                    ArrayList<Symbol> right = programs.get(orderOfProgram).getRight();
                    analysisStack.pop();//先删除栈顶字符
                    if(!right.get(0).getValue().equals("~")){
                        for (int i = right.size()-1; i >= 0 ; i--) {
                            analysisStack.push(right.get(i));
                        }
                    }
                }else {
                    // TODO: 2018/5/14 报错。查表查不到
                    System.out.println("查不到产生式");
                    break;
                }
            } else if(analysisStack.peek().getValue().equals(input.get(count).getValue())){
                //如果匹配了字符
                count++;//去掉一个输入串
                analysisStack.pop();//删除分析栈的栈顶字符
                System.out.println("操作：“"+input.get(count).getValue()+"”匹配");
            } else {
                // TODO: 2018/5/14 报错
                System.out.print("无法匹配终结符");
                break;
            }
            step++;
            System.out.println();

        }









        return false;
    }



    private static int removeFormProgramList(ArrayList<Program> list, String s,int count){

        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getLeft().getValue().equals(s)){
                list.remove(i);
                i--;
                if(count>=i){
                    count--;
                }
            }

        }
        count++;

        return count;
    }

    public static void readProgramFile(String fileName) {

        File file = new File(fileName);
        int state = 0;
        Program program = new Program();
        ArrayList<Symbol> right = new ArrayList<Symbol>();

        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String str;
            String left="";

            while((str = br.readLine())!=null){//使用readLine方法，一次读一行
                switch (state){
                    case 0:{
                        if (!str.isEmpty()){
                            // TODO: 2018/5/13 添加一个非终结符
                            left = str;

                            int i = isInVN(str);

                            if(i>-1){
                                program.setLeft(VN.get(i));
                            }else {
                                NonTerminator nt = new NonTerminator(str);
                                VN.add(nt);
                                program.setLeft(nt);
                            }
                            state = 1;
                        }
                    }break;
                    case 1:{
                        if(!str.isEmpty()){
                            for (String s : str.split(" ")) {
                                System.out.println("分解后的右面:"+s);
                                if(s.matches("('|[A-Z])+")){
                                    // TODO: 2018/5/13 加入一个右边的非终结符
                                    int i = isInVN(s);
                                    if(i>-1){
                                        right.add(VN.get(i));
                                    }else {
                                        NonTerminator nt = new NonTerminator(s);
                                        right.add(nt);
                                        VN.add(nt);
                                    }
                                } else {
                                    // TODO: 2018/5/14 加入一个终结符
                                    int i = isInVT(s);
                                    if(i>-1){
                                        right.add(VT.get(i));
                                    }else {
                                        Terminator t = new Terminator(s);
                                        right.add(t);
                                        VT.add(t);
                                    }
                                }
                            }
                            program.setRight(right);
                            programs.add(program);
                            program = new Program();

                            program.setLeft(VN.get(isInVN(left)));
                            right = new ArrayList<Symbol>();
                        }
                        else {
                            state = 0;
                        }
                    }break;
                }
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static boolean isInProgramsList(ArrayList<Program> list,String value){
        for (Program p : list){
            if(p.getLeft().getValue().equals(value)){
                return true;
            }
        }
        return false;
    }

    public static int isInVN(String value){
        // TODO: 2018/5/13 检查终结符是否在表格里，返回序号
        int result = -1;
        for (int i = 0;i<VN.size();i++){
            if(value.equals(VN.get(i).getValue())){
                result =  i;

            }
        }
        return result;
    }

    private static int isInVT(String value){
        // TODO: 2018/5/13 检查终结符是否在表格里，返回序号
        for (int i = 0;i<VT.size();i++){
            if(VT.get(i).getValue().equals(value)){
                return i;
            }
        }
        return -1;
    }

    public static ArrayList<Program> getPrograms() {
        return programs;
    }

    public static ArrayList<NonTerminator> getVN() {
        return VN;
    }

    public static ArrayList<Terminator> getVT() {
        return VT;
    }
}
