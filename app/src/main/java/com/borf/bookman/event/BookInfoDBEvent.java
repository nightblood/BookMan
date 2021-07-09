package com.borf.bookman.event;

import com.borf.bookman.bean.AuthorInfo;
import com.borf.bookman.bean.BookInfo;
import com.borf.bookman.bean.BookTagInfo;
import com.borf.bookman.bean.Tag;

import java.util.List;

public class BookInfoDBEvent {
    private List<Tag> tagList;
    private BookInfo bookInfo;

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public List<BookTagInfo> getBookTagInfoList() {
        return bookTagInfoList;
    }

    public List<AuthorInfo> getAuthorInfoList() {
        return authorInfoList;
    }

    private List<BookTagInfo> bookTagInfoList;
    private List<AuthorInfo> authorInfoList;
    public BookInfoDBEvent(BookInfo bookInfo, List<BookTagInfo> bookTagInfoList, List<AuthorInfo> authorInfoList, List<Tag> tagList) {
        this.bookInfo = bookInfo;
        this.bookTagInfoList = bookTagInfoList;
        this.authorInfoList = authorInfoList;
        this.tagList =tagList;
    }

    public List<Tag> getTagList() {
        return tagList;
    }
}
