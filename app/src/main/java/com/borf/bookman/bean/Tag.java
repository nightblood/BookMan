package com.borf.bookman.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(indexes = {
        @Index(value = "id DESC", unique = true)
})
public class Tag {
    @Id(autoincrement = true)
    private Long id;

    private String desc;

    private String name;

    @ToMany(referencedJoinProperty = "tagCode")
    private List<BookTagInfo> bookTagInfoList;

/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;

/** Used for active entity operations. */
@Generated(hash = 2076396065)
private transient TagDao myDao;




@Generated(hash = 2075471904)
public Tag(Long id, String desc, String name) {
    this.id = id;
    this.desc = desc;
    this.name = name;
}

@Generated(hash = 1605720318)
public Tag() {
}




public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}


public String getName() {
    return this.name;
}

public void setName(String name) {
    this.name = name;
}

public String getDesc() {
    return this.desc;
}

public void setDesc(String desc) {
    this.desc = desc;
}

/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 82287725)
public List<BookTagInfo> getBookTagInfoList() {
    if (bookTagInfoList == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        BookTagInfoDao targetDao = daoSession.getBookTagInfoDao();
        List<BookTagInfo> bookTagInfoListNew = targetDao
                ._queryTag_BookTagInfoList(id);
        synchronized (this) {
            if (bookTagInfoList == null) {
                bookTagInfoList = bookTagInfoListNew;
            }
        }
    }
    return bookTagInfoList;
}

/** Resets a to-many relationship, making the next get call to query for a fresh result. */
@Generated(hash = 204692288)
public synchronized void resetBookTagInfoList() {
    bookTagInfoList = null;
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
@Generated(hash = 441429822)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getTagDao() : null;
}

}
