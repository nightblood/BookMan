package com.borf.bookman.utils;

import android.database.Cursor;

import com.blankj.utilcode.util.LogUtils;
import com.borf.bookman.MyApplication;
import com.borf.bookman.bean.AuthorInfoDao;
import com.borf.bookman.bean.BookInfo;
import com.borf.bookman.bean.BookInfoDao;
import com.borf.bookman.bean.BookTagInfo;
import com.borf.bookman.bean.BookTagInfoDao;
import com.borf.bookman.bean.DaoSession;
import com.borf.bookman.bean.Tag;
import com.borf.bookman.bean.TagDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class DaoUtils {
    public static DaoSession daoSession = MyApplication.getDaoSession();
    public static BookInfoDao bookInfoDao = daoSession.getBookInfoDao();
    public static TagDao tagDao = daoSession.getTagDao();
    public static BookTagInfoDao bookTagInfoDao = daoSession.getBookTagInfoDao();
    public static AuthorInfoDao authorInfoDao = daoSession.getAuthorInfoDao();
    private static final String SQL_DISTINCT_ISBN = "SELECT DISTINCT "+ BookInfoDao.Properties.Isbn.columnName
            + " FROM "+BookInfoDao.TABLENAME + " WHERE ISBN";

    public static BookInfo selectBookInfo(Long isbn) {
        QueryBuilder qb = bookInfoDao.queryBuilder(); //获取QueryBuilder
        List<BookInfo> res = qb.where(BookInfoDao.Properties.Isbn.eq(isbn)).list();
        if (res.size() > 0) {
            LogUtils.d("在库里找到图书。");
            return res.get(0);
        }
        return null;
    }

    public static List<String> listEName() {
        ArrayList<String> result = new ArrayList<String>();
        Cursor c = daoSession.getDatabase().rawQuery(SQL_DISTINCT_ISBN, null);
        try{
            if (c.moveToFirst()) {
                do {
                    result.add(c.getString(0));
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        return result;
    }

}
