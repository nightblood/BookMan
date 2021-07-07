/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.borf.bookman.manager;


import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.borf.bookman.MyApplication;
import com.borf.bookman.base.BaseFragment;
import com.borf.bookman.bean.AuthorInfo;
import com.borf.bookman.bean.AuthorInfoDao;
import com.borf.bookman.bean.BookInfo;
import com.borf.bookman.bean.BookInfoDao;
import com.borf.bookman.bean.DaoSession;
import com.borf.bookman.fragment.QDButtonFragment;
import com.borf.bookman.model.QDItemDescription;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cginechen
 * @date 2016-10-21
 */

public class QDDataManager {
    private static QDDataManager _sInstance;
    private QDWidgetContainer mWidgetContainer;

    private List<Class<? extends BaseFragment>> mComponentsNames;

    public QDDataManager() {
        mWidgetContainer = QDWidgetContainer.getInstance();
        initComponentsDesc();
    }

    public static QDDataManager getInstance() {
        if (_sInstance == null) {
            _sInstance = new QDDataManager();
        }
        return _sInstance;
    }


    /**
     * Components
     */
    private void initComponentsDesc() {
        mComponentsNames = new ArrayList<>();
        mComponentsNames.add(QDButtonFragment.class);

    }


    public QDItemDescription getDescription(Class<? extends BaseFragment> cls) {
        return mWidgetContainer.get(cls);
    }

    public String getName(Class<? extends BaseFragment> cls) {
        QDItemDescription itemDescription = getDescription(cls);
        if (itemDescription == null) {
            return null;
        }
        return itemDescription.getName();
    }

    public String getDocUrl(Class<? extends BaseFragment> cls) {
        QDItemDescription itemDescription = getDescription(cls);
        if (itemDescription == null) {
            return null;
        }
        return itemDescription.getDocUrl();
    }

    public List<QDItemDescription> getComponentsDescriptions() {
        List<QDItemDescription> list = new ArrayList<>();
        for (int i = 0; i < mComponentsNames.size(); i++) {
            list.add(mWidgetContainer.get(mComponentsNames.get(i)));
        }
        return list;
    }
    public List<BookInfo> getAllBooks() {
        List<BookInfo> bookInfos = selectBookInDb("");
        return bookInfos;
    }

    private List<BookInfo> selectBookInDb(String isbn) {
        DaoSession daoSession = MyApplication.getDaoSession();

        BookInfoDao bookInfoDao = daoSession.getBookInfoDao();

        QueryBuilder qb = bookInfoDao.queryBuilder(); //获取QueryBuilder
        if (!TextUtils.isEmpty(isbn)) {
            qb = qb.where(BookInfoDao.Properties.Isbn.eq(isbn));
        }
        List<BookInfo> res = qb.list();
        if (res.size() > 0) {
            LogUtils.d("在库里找到图书。");
            return res;
        }
        return null;
    }

    private BookInfo searchInDouban(String isbn) throws IOException, JSONException {
        LogUtils.d("未在库里找到图书。前往豆瓣搜索图书。");

        String urlIsbn = "http://douban.com/isbn/" + isbn + "/";
        Document document = Jsoup.connect(urlIsbn)
                .header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36 QIHU 360SE")
                .get();
        String title = document.getElementsByTag("title").get(0).text();
        if (title.contains("豆瓣错误")) {
            LogUtils.i("豆瓣图书中查无此书，请检查isbn是否正确。" + isbn);
            return null;
        }
        LogUtils.i("豆瓣图书中查到，正在解析数据。");

        Element ele = document.getElementsByClass("nbg").get(0);
        String bookImg = ele.attr("href");
        String strBookInfo = document.getElementsByAttributeValue("type", "application/ld+json").get(0).data();
        JSONObject jsonObject = new JSONObject(strBookInfo);
        String name = jsonObject.getString("name");
        String context = jsonObject.getString("@context");
        String type = jsonObject.getString("@type");
        String workExample = jsonObject.getString("workExample");
        String url = jsonObject.getString("url");
        String sameAs = jsonObject.getString("sameAs");
        JSONArray jsonArray = jsonObject.getJSONArray("author");

        BookInfo bookInfo = new BookInfo();
        bookInfo.setContext(context);
        bookInfo.setImgUrl(bookImg);
        bookInfo.setIsbn(isbn);
        bookInfo.setName(name);
        bookInfo.setUrl(url);
        bookInfo.setWorkExample(workExample);
        bookInfo.setType(type);
        bookInfo.setSameAs(sameAs);

        DaoSession daoSession = MyApplication.getDaoSession();
        BookInfoDao bookInfoDao = daoSession.getBookInfoDao();
        AuthorInfoDao authorInfoDao = daoSession.getAuthorInfoDao();

        bookInfoDao.insert(bookInfo);

        List<AuthorInfo> authorInfos = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            AuthorInfo authorInfo = new AuthorInfo();
            JSONObject object = (JSONObject) jsonArray.get(i);
            String author_type = object.getString("@type");
            String author_name = object.getString("name");
            authorInfo.setIsbn(isbn);
            authorInfo.setName(author_name);
            authorInfo.setType(author_type);
            authorInfos.add(authorInfo);
            authorInfoDao.insert(authorInfo);
        }
        return bookInfo;
    }

    private BookInfo getBook(String isbn) {
        try {
            System.out.println(isbn);
            LogUtils.d("开始搜索图书：" + isbn);
            List<BookInfo> result = selectBookInDb(isbn);
            if (result != null) {
                return result.get(0);
            }
            return searchInDouban(isbn);
        }catch (Exception exception) {
            LogUtils.e(exception);
            return null;
        }
    }



}
