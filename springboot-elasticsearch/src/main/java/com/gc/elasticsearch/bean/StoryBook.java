package com.gc.elasticsearch.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "test", type = "book", shards = 1, replicas = 0, refreshInterval = "-1")
public class StoryBook {

    /**
     * Id注解加上后，在Elasticsearch里相应于该列就是主键了，在查询时就可以直接用主键查询
     *
     */
    @Id
    private Integer id;


    private String bookName;
    private String author;

    public StoryBook() {
    }

    public StoryBook(String bookName, String author) {
        this.bookName = bookName;
        this.author = author;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
