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
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.borf.bookman.MyApplication;
import com.borf.bookman.R;
import com.borf.bookman.base.BaseFragment;
import com.borf.bookman.base.BaseRecyclerAdapter;
import com.borf.bookman.base.RecyclerViewHolder;
import com.borf.bookman.bean.BookInfo;
import com.borf.bookman.bean.BookInfoDao;
import com.borf.bookman.bean.Tag;
import com.borf.bookman.decorator.GridDividerItemDecoration;
import com.borf.bookman.fragment.BookInfoFragment;
import com.borf.bookman.utils.DaoUtils;
import com.bumptech.glide.Glide;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author cginechen
 * @date 2016-10-20
 */

public abstract class HomeController extends LinearLayout {

    protected QMUITopBarLayout mTopBar;
    protected RecyclerView mRecyclerView;

    private HomeControlListener mHomeControlListener;
    private ItemAdapter mItemAdapter;
    private int mDiffRecyclerViewSaveStateId = QMUIViewHelper.generateViewId();

    public HomeController(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        mTopBar = new QMUITopBarLayout(context);
        mTopBar.setId(View.generateViewId());
        mTopBar.setFitsSystemWindows(true);
        mRecyclerView = new RecyclerView(context);
        mRecyclerView.setId(View.generateViewId());
        addView(mTopBar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mRecyclerView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, 1f));
        initTopBar();
        initRecyclerView();
    }

    protected void startFragment(BaseFragment fragment) {
        if (mHomeControlListener != null) {
            mHomeControlListener.startFragment(fragment);
        }
    }

    public void setHomeControlListener(HomeControlListener homeControlListener) {
        mHomeControlListener = homeControlListener;
    }

    protected abstract String getTitle();

    private void initTopBar() {
        mTopBar.setTitle(getTitle());

        mTopBar.addRightImageButton(R.mipmap.search, R.id.topbar_right_about_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getContext());
                builder.setTitle("搜索图书")
                        .setPlaceholder("在此输入书名")
                        .setInputType(InputType.TYPE_CLASS_TEXT)
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                List<BookInfo> bookInfoList = DaoUtils.bookInfoDao.queryBuilder().list();
                                mItemAdapter.setData(bookInfoList);
                                mItemAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        })
                        .addAction("搜索", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                CharSequence text = builder.getEditText().getText();
                                List<BookInfo> bookInfoList;
                                if (text != null && text.length() > 0) {
                                    String key = "%" + text.toString() + "%";
                                    bookInfoList = DaoUtils.bookInfoDao.queryBuilder()
                                            .where(BookInfoDao.Properties.Name.like(key))
                                            .list();
//                                    DaoUtils.bookTagInfoDao.queryBuilder().where()
                                } else {
                                    bookInfoList = DaoUtils.bookInfoDao.queryBuilder().list();
                                }
                                if (bookInfoList.size() != 0) {
                                    mItemAdapter.setData(bookInfoList);
                                    mItemAdapter.notifyDataSetChanged();
                                } else {
                                    ToastUtils.showLong("查无此书！！");
                                }
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        });
    }

    private void initRecyclerView() {
        mItemAdapter = getItemAdapter();
        mItemAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                BookInfo item = mItemAdapter.getItem(pos);
                ToastUtils.showLong(item.getName());
                Context context = getContext();
//                if (context instanceof Activity) {
//                    Intent intent = new Intent(context, BookInfoActivity.class);
//                    intent.putExtra("isbn", item.getIsbn());
//                    context.startActivity(intent);
//                }
                Bundle bundle = new Bundle();
                bundle.putLong("isbn", item.getIsbn());
                BookInfoFragment bookInfoFragment = new BookInfoFragment();
                bookInfoFragment.setArguments(bundle);
                startFragment(bookInfoFragment);

//                QDItemDescription item = mItemAdapter.getItem(pos);
//                try {
//                    BaseFragment fragment = item.getDemoClass().newInstance();
//                    if (fragment instanceof QDNotchHelperFragment) {
//                        Context context = getContext();
//                        Intent intent = QDMainActivity.of(context, QDNotchHelperFragment.class);
//                        context.startActivity(intent);
//                        if (context instanceof Activity) {
//                            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                        }
//                    } else {
//                        startFragment(fragment);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
        mRecyclerView.setAdapter(mItemAdapter);
        int spanCount = 3;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        mRecyclerView.addItemDecoration(new GridDividerItemDecoration(getContext(), spanCount));

//        mItemAdapter.he
    }

    protected abstract ItemAdapter getItemAdapter();

    public interface HomeControlListener {
        void startFragment(BaseFragment fragment);
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        int id = mRecyclerView.getId();
        mRecyclerView.setId(mDiffRecyclerViewSaveStateId);
        super.dispatchSaveInstanceState(container);
        mRecyclerView.setId(id);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        int id = mRecyclerView.getId();
        mRecyclerView.setId(mDiffRecyclerViewSaveStateId);
        super.dispatchRestoreInstanceState(container);
        mRecyclerView.setId(id);
    }

    static class ItemAdapter extends BaseRecyclerAdapter<BookInfo> {

        public ItemAdapter(Context ctx, List<BookInfo> data) {
            super(ctx, data);
        }

        @Override
        public int getItemLayoutId(int viewType) {
            return R.layout.home_item_layout;
        }

        @Override
        public void bindData(RecyclerViewHolder holder, int position, BookInfo item) {
            holder.getTextView(R.id.item_name).setText(item.getName());
            Glide.with(MyApplication.getContext())
                    .load(item.getImgUrl())
                    .into(holder.getImageView(R.id.item_icon));

//            if (item.getIconRes() != 0) {
//                holder.getImageView(R.id.item_icon1).setImageResource(item.getIconRes());
//            }
        }
    }
}
