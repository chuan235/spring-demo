package top.gmfcj.bean;

import javax.persistence.*;

@Entity
@Table(name = "count_result")
public class CountResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "word")
    private String word;

    @Column(name = "total")
    private Integer total;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "CountResult{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", total=" + total +
                '}';
    }
}
