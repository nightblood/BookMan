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

package com.borf.bookman.fragment.controller;

import android.content.Context;

import com.borf.bookman.bean.BookInfo;
import com.borf.bookman.manager.QDDataManager;

import java.util.ArrayList;
import java.util.List;


/**
 * @author cginechen
 * @date 2016-10-20
 */

public class HomeComponentsController extends HomeController {

    private ItemAdapter bookAdapter;

    public HomeComponentsController(Context context) {
        super(context);
    }

    @Override
    protected String getTitle() {
        return "书架";
    }

    @Override
    protected ItemAdapter getItemAdapter() {
        List<BookInfo> allBooks = QDDataManager.getInstance().getAllBooks();
        if (allBooks == null)
            allBooks = new ArrayList<>();
        bookAdapter = new ItemAdapter(getContext(), allBooks);
        return bookAdapter;
    }

    public void addBook(BookInfo bookInfo) {
        bookAdapter.add(0, bookInfo);
    }
}
