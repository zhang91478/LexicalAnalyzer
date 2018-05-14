package cn.xu419.commom.Par;

import java.util.HashSet;
import java.util.Set;

/**
 * @author bsz
 * Created on 2018/5/10
 */
public class NonTerminator extends Symbol {
    private Set<Terminator> FirstSet;
    private Set<Terminator> FollowSet;
    private int tag;

    public NonTerminator(String value) {
        super(false,value);
        tag = 0;
        FirstSet = new HashSet<Terminator>();
        FollowSet = new HashSet<Terminator>();
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public Set<Terminator> getFirstSet() {
        return FirstSet;
    }

    public void setFirstSet(Set<Terminator> firstSet) {
        FirstSet = firstSet;
    }

    public Set<Terminator> getFollowSet() {
        return FollowSet;
    }

    public void setFollowSet(Set<Terminator> followSet) {
        FollowSet = followSet;
    }
}
