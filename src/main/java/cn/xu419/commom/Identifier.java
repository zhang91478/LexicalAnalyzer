package cn.xu419.commom;

/**
 * @author bsz
 * Created on 2018/4/11
 */
public class Identifier {
    private Integer id;//逻辑地址
    private String name;//名字
    private String type;
    private Integer size;//大小
    private Object value;

    public Identifier(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Identifier{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", size=" + size +
                ", value=" + value +
                '}';
    }
}
