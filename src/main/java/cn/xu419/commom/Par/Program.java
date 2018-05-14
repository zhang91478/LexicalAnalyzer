package cn.xu419.commom.Par;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * @author bsz
 * Created on 2018/5/10
 */
public class Program {
    private NonTerminator left ;
    private ArrayList<Symbol> right;
    private HashSet<Symbol> SELECT;

    public Program() {
        SELECT = new HashSet<Symbol>();
    }

    public HashSet<Symbol> getSELECT() {
        return SELECT;
    }

    public void setSELECT(HashSet<Symbol> SELECT) {
        this.SELECT = SELECT;
    }

    public NonTerminator getLeft() {
        return left;
    }

    public void setLeft(NonTerminator left) {
        this.left = left;
    }

    public ArrayList<Symbol> getRight() {
        return right;
    }

    public void setRight(ArrayList<Symbol> right) {
        this.right = right;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        for (Symbol symbol:right){
            s.append(symbol.toString());
        }

        return left.toString()+"-->"+s;
    }
}
