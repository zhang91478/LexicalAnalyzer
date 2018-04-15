package cn.xu419.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bsz
 * Created on 2018/4/2
 */
public class StateMarchine {
    public Map<String,Integer> getSym(String text) {
        Map<String,Integer> map = new HashMap<String, Integer>();

        int point = 0;
        char ch;
        //处理空格
        do{
            point++;
            ch = text.charAt(point);

        }while (ch==' '||ch=='\n'||ch=='\r'||ch=='\t');

        StringBuilder temp = new StringBuilder();

        //TODO 识别出c开头
        while (point<=text.length()){

            ch = text.charAt(point);

            if(ch == 'c'){
                temp.append(ch);//temp = "c";
                point++;
                ch = text.charAt(point);
                if(ch=='o'){
                    temp.append(ch);//temp = "co";
                    point++;
                    ch = text.charAt(point);
                    if(ch=='n'){
                        temp.append(ch);//temp = "con";
                        point++;
                        ch = text.charAt(point);
                        if(ch=='s'){
                            temp.append(ch);//temp = "cons";
                            point++;
                            ch = text.charAt(point);
                            if(ch=='t'){
                                temp.append(ch);//temp = "const";
                                point++;
                                ch = temp.charAt(point);
                                if(ch==' '||ch=='\n'||ch=='\r'||ch=='\t'){
                                    map.put(temp.toString(),1);// TODO: 2018/4/2 将const加入map
                                    temp.reverse();//TODO 重置字符串构造器
                                }
                            }
                        }else if(text.charAt(point)=='t'){
                            temp.append(ch);//temp = "cont";
                            point++;
                            ch = text.charAt(point);
                            if(ch=='i'){
                                temp.append(ch);//temp = "conti";
                                point++;
                                ch = text.charAt(point);
                                if(ch=='n'){
                                    temp.append(ch);//temp = "contin";
                                    point++;
                                    ch = text.charAt(point);
                                    if(ch=='u'){
                                        temp.append(ch);//temp = "continu";
                                        point++;
                                        ch = text.charAt(point);
                                        if(ch=='e'){
                                            temp.append(ch);//temp = "continue";
                                            point++;
                                            ch = temp.charAt(point);
                                            if(ch==' '||ch=='\n'||ch=='\r'||ch=='\t'){
                                                map.put(temp.toString(),1);// TODO: 2018/4/2 将continue加入map
                                                temp.reverse();//TODO 重置字符串构造器
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else if(ch == 'h'){
                    temp.append(ch);//temp = "ch";
                    point++;
                }

            }

        }





        return map;
    }

}
