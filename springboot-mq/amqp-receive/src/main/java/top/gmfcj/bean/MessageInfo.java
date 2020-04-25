package top.gmfcj.bean;


import java.io.Serializable;

public class MessageInfo implements Serializable {

    private String id;
    private Integer total;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "id='" + id + '\'' +
                ", total=" + total +
                ", content='" + content + '\'' +
                '}';
    }
}
