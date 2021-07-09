package com.borf.bookman.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Unique;

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

    @Index(name = "index_isbn", unique = true)
    @Property(nameInDb = "isbn")
    private Long isbn;

    private String sameAs;

    private String imgUrl;

    private String status;

//    @ToMany(referencedJoinProperty = "isbn")
//    private List<AuthorInfo> authorInfoList;

    @Generated(hash = 1952025412)
    public BookInfo() {
    }

    @Generated(hash = 667671600)
    public BookInfo(Long id, String context, String type, String workExample,
            String name, String url, Long isbn, String sameAs, String imgUrl,
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

    public Long getIsbn() {
        return this.isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

}
