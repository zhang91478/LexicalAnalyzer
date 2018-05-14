package cn.xu419.commom.Par;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;



/**
 * @author bsz
 * Created on 2018/5/9
 */
public class Parser {

    private static ArrayList<Program> programs = new ArrayList<Program>();//语法
    private static ArrayList<NonTerminator> VN = new ArrayList<NonTerminator>();//终结符
    private static ArrayList<Terminator> VT = new ArrayList<Terminator>();




    public static boolean process() {
        ArrayList<Program> list = new ArrayList<Program>();

        // TODO: 2018/5/14 将programs复制到list
        for (int i=0;i<programs.size();i++) {
            ArrayList<Symbol> right = new ArrayList<Symbol>();
            NonTerminator left = new NonTerminator(programs.get(i).getLeft().getValue());
            for (Symbol s:programs.get(i).getRight()){
                if(s.getKind()){
                    right.add(new Terminator(s.getValue()));
                }else {
                    right.add(new NonTerminator(s.getValue()));
                }
            }
            Program p = new Program();
            p.setLeft(left);
            p.setRight(right);
            list.add(p);
        }

        // TODO: 2018/5/14 打印结束，开始求First
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
                VN.get(isInVN(list.get(i).getLeft().getValue())).setTag(1);
                i = removeFormProgramList(list, list.get(i).getLeft().getValue(), i);
            } else {
                list.remove(i);
                i--;
            }
        }

        for (NonTerminator nt : VN) {
            if (!isInProgramsList(list, nt.getValue()) && nt.getTag() != 1) {
                nt.setTag(-1);//表示不能推出空
            }
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
        for (NonTerminator nt:VN) {
            System.out.println(nt.getValue()+":"+nt.getTag());
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
        programs.get(0).getLeft().getFollowSet().add(new Terminator("#"));

        for (NonTerminator nt:VN) {
            for (Program program:programs) {
                ArrayList<Symbol> right = program.getRight();
                for (int i = 0;i<right.size()-1;i++){
                    if(!right.get(i).getKind()){
                        for (int j = 1;j<right.size()-i;j++){
                            if(right.get(i+j).getKind()){
                                VN.get(isInVN( right.get(i).getValue())).getFollowSet().add(VT.get(isInVT(right.get(i+j).getValue())));
                                break;
                            } else if((!right.get(i+j).getKind())&&VN.get(isInVN( right.get(i+j).getValue())).getTag()==-1){

                                VN.get(isInVN( right.get(i).getValue())).getFollowSet().addAll(VN.get(isInVN( right.get(i+j).getValue())).getFirstSet());
                                break;
                            } else {

                                VN.get(isInVN( right.get(i).getValue())).getFollowSet().remove(VT.get(isInVT("~")));
                                if((i+j)==(right.size()-1)){
                                    VN.get(isInVN( right.get(i).getValue())).getFollowSet().addAll(program.getLeft().getFollowSet());
                                }
                            }
                        }

                    }
                    if(!right.get(right.size()-1).getKind()){
                        VN.get(isInVN(right.get(right.size()-1).getValue())).getFollowSet().addAll(program.getLeft().getFollowSet());
                    }
                }
            }


        }
        System.out.println("---------------------打印一下FOLLOW");
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
                                if(s.matches("(_|[A-Z])+")){
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


    private static int isInVN(String value){
        // TODO: 2018/5/13 检查终结符是否在表格里，返回序号
        for (int i = 0;i<VN.size();i++){
            if(VN.get(i).getValue().equals(value)){
                return i;
            }
        }
        return -1;
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
