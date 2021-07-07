package com.borf.bookman.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class AuthorInfo {
    @Id(autoincrement = true)
    private Long id;

    private String isbn;

    private String type;

    private String name;

    @Generated(hash = 814625786)
    public AuthorInfo(Long id, String isbn, String type, String name) {
        this.id = id;
        this.isbn = isbn;
        this.type = type;
        this.name = name;
    }

    @Generated(hash = 115608329)
    public AuthorInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
