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

package com.borf.bookman.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.borf.bookman.MainActivity;
import com.borf.bookman.event.BaseEvent;
import com.qmuiteam.qmui.arch.QMUIActivity;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.borf.bookman.MyApplication.getContext;


@SuppressLint("Registered")
public class BaseActivity extends QMUIActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int backViewInitOffset() {
        return QMUIDisplayHelper.dp2px(getContext(), 100);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        QDUpgradeManager.getInstance(getContext()).runUpgradeTipTaskIfExist(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public Intent onLastActivityFinish() {
        return new Intent(this, MainActivity.class);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(BaseEvent baseEvent) {

    }
}
