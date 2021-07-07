package com.borf.bookman.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {
        @Index(value = "isbn DESC", unique = true)
})
public class BookInfo {
    @Id(autoincrement = true)
    private Long id;
    private String context;

    private String type;

    private String workExample;

    private String name;

    private String url;

    private String isbn;

    private String sameAs;

    private String imgUrl;

    private String status;

    @Generated(hash = 1936229831)
    public BookInfo(Long id, String context, String type, String workExample,
            String name, String url, String isbn, String sameAs, String imgUrl,
            String status) {
        this.id = id;
        this.context = context;
        this.type = type;
        this.workExample = workExample;
        this.name = name;
        this.url = url;
        this.isbn = isbn;
        this.sameAs = sameAs;
        this.imgUrl = imgUrl;
        this.status = status;
    }

    @Generated(hash = 1952025412)
    public BookInfo() {
    }

    public String getContext() {
        return this.context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWorkExample() {
        return this.workExample;
    }

    public void setWorkExample(String workExample) {
        this.workExample = workExample;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getSameAs() {
        return this.sameAs;
    }

    public void setSameAs(String sameAs) {
        this.sameAs = sameAs;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
