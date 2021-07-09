package com.borf.bookman.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;

@Entity(indexes = {
        @Index(value = "isbn,tagCode DESC", unique = true)
})
public class BookTagInfo {
    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "isbn")
    private Long isbn;

    private Long tagCode;

    @ToOne(joinProperty = "id")
    private Tag tag;

    @ToOne(joinProperty = "id")
    private BookInfo bookInfo;

/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;

/** Used for active entity operations. */
@Generated(hash = 2136232970)
private transient BookTagInfoDao myDao;


@Generated(hash = 391161666)
public BookTagInfo() {
}

@Generated(hash = 1601337515)
public BookTagInfo(Long id, Long isbn, Long tagCode) {
    this.id = id;
    this.isbn = isbn;
    this.tagCode = tagCode;
}

@Generated(hash = 1006483784)
private transient Long tag__resolvedKey;

@Generated(hash = 1374280833)
private transient Long bookInfo__resolvedKey;


public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public Long getTagCode() {
    return this.tagCode;
}

public void setTagCode(Long tagCode) {
    this.tagCode = tagCode;
}

/** To-one relationship, resolved on first access. */
@Generated(hash = 516322085)
public Tag getTag() {
    Long __key = this.id;
    if (tag__resolvedKey == null || !tag__resolvedKey.equals(__key)) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        TagDao targetDao = daoSession.getTagDao();
        Tag tagNew = targetDao.load(__key);
        synchronized (this) {
            tag = tagNew;
            tag__resolvedKey = __key;
        }
    }
    return tag;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 196303131)
public void setTag(Tag tag) {
    synchronized (this) {
        this.tag = tag;
        id = tag == null ? null : tag.getId();
        tag__resolvedKey = id;
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


public Long getIsbn() {
    return this.isbn;
}

public void setIsbn(Long isbn) {
    this.isbn = isbn;
}

/** To-one relationship, resolved on first access. */
@Generated(hash = 1234583458)
public BookInfo getBookInfo() {
    Long __key = this.id;
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
@Generated(hash = 905413723)
public void setBookInfo(BookInfo bookInfo) {
    synchronized (this) {
        this.bookInfo = bookInfo;
        id = bookInfo == null ? null : bookInfo.getId();
        bookInfo__resolvedKey = id;
    }
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 2016298495)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getBookTagInfoDao() : null;
}


}
