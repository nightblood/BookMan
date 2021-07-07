package com.borf.bookman.event;

import com.borf.bookman.bean.BookInfo;

public class BookInfoEvent {
    private BookInfo bookInfo;
    private String msg;
    private String operation;

    public BookInfoEvent(BookInfo bookInfo, String msg, String operation) {
        this.bookInfo = bookInfo;
        this.msg = msg;
        this.operation = operation;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public String getMsg() {
        return msg;
    }

    public String getOperation() {
        return operation;
    }
}
