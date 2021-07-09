package com.borf.bookman.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;

@Entity
public class AuthorInfo {
    @Id(autoincrement = true)
    private Long id;

    private Long isbn;

    private String type;

    private String name;

    @ToOne(joinProperty = "isbn")
    private BookInfo bookInfo;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 379896367)
    private transient AuthorInfoDao myDao;

    @Generated(hash = 1374280833)
    private transient Long bookInfo__resolvedKey;


    @Generated(hash = 115608329)
    public AuthorInfo() {
    }

    @Generated(hash = 1353493858)
    public AuthorInfo(Long id, Long isbn, String type, String name) {
        this.id = id;
        this.isbn = isbn;
        this.type = type;
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getIsbn() {
        return this.isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 727085591)
    public BookInfo getBookInfo() {
        Long __key = this.isbn;
        if (bookInfo__resolvedKey == null || !bookInfo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BookInfoDao targetDao = daoSession.getBookInfoDao();
            BookInfo bookInfoNew = targetDao.load(__key);
            synchronized (this) {
                bookInfo = bookInfoNew;
                bookInfo__resolvedKey = __key;
            }
        }
        return bookInfo;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1186549486)
    public void setBookInfo(BookInfo bookInfo) {
        synchronized (this) {
            this.bookInfo = bookInfo;
            isbn = bookInfo == null ? null : bookInfo.getId();
            bookInfo__resolvedKey = isbn;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 394233262)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAuthorInfoDao() : null;
    }


}
