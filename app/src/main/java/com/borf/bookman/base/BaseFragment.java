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

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.borf.bookman.event.BaseEvent;
import com.borf.bookman.fragment.HomeFragment;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.arch.SwipeBackLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by cgspine on 2018/1/7.
 */

public abstract class BaseFragment extends QMUIFragment {

    private static final String TAG = "BaseFragment";

    private int mBindId = -1;

    public BaseFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(BaseEvent baseEvent) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int backViewInitOffset(Context context, int dragDirection, int moveEdge) {
        if (moveEdge == SwipeBackLayout.EDGE_TOP || moveEdge == SwipeBackLayout.EDGE_BOTTOM) {
            return 0;
        }
        return QMUIDisplayHelper.dp2px(context, 100);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, getClass().getSimpleName() + " onResume");

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, getClass().getSimpleName() + " onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, getClass().getSimpleName() + " onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, getClass().getSimpleName() + " onStop");
    }

    @Override
    public Object onLastFragmentFinish() {
        return new HomeFragment();
    }

    protected void goToWebExplorer(@NonNull String url, @Nullable String title) {
//        Intent intent = MainActivity.createWebExplorerIntent(getContext(), url, title);
//        startActivity(intent);
    }

//    protected void injectDocToTopBar(QMUITopBar topBar) {
//        final QDItemDescription description = QDDataManager.getInstance().getDescription(this.getClass());
//        if (description != null) {
//            topBar.addRightTextButton("DOC", QMUIViewHelper.generateViewId())
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            goToWebExplorer(description.getDocUrl(), description.getName());
//                        }
//                    });
//        }
//    }
//
//    protected void injectDocToTopBar(QMUITopBarLayout topBar) {
//        final QDItemDescription description = QDDataManager.getInstance().getDescription(this.getClass());
//        if (description != null) {
//            topBar.addRightTextButton("DOC", QMUIViewHelper.generateViewId())
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            goToWebExplorer(description.getDocUrl(), description.getName());
//                        }
//                    });
//        }
//    }
}
