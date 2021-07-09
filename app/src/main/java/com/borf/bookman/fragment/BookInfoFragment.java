package com.borf.bookman.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;

import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.borf.bookman.R;
import com.borf.bookman.anotation.Widget;
import com.borf.bookman.base.BaseFragment;
import com.borf.bookman.bean.AuthorInfo;
import com.borf.bookman.bean.BookInfo;
import com.borf.bookman.bean.BookInfoDao;
import com.borf.bookman.bean.BookTagInfo;
import com.borf.bookman.bean.BookTagInfoDao;
import com.borf.bookman.bean.Tag;
import com.borf.bookman.event.BookInfoDBEvent;
import com.borf.bookman.event.BookInfoEvent;
import com.borf.bookman.utils.DaoUtils;
import com.bumptech.glide.Glide;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zhy.view.flowlayout.TagView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
@Widget(name = "BookInfoFragment", iconRes = R.mipmap.icon_grid_button)
public class BookInfoFragment extends BaseFragment {

    private LayoutInflater mInflater;
    private TagFlowLayout mFlowLayout;
    private Long isbn;
    private ImageView mBookImg;
    private Button mBtnAddTag;
    private Set<Integer> selectTagSet = new HashSet<>();
    private List<Tag> tagList = new ArrayList<>();
    private TagAdapter<Tag> mTagAdapter;

    @Override
    protected View onCreateView() {
        mInflater = LayoutInflater.from(getContext());
        View view = mInflater.inflate(R.layout.fragment_book_info, null);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        isbn = bundle.getLong("isbn");
        LogUtils.d("BookInfoFragment:" + isbn);
        return view;
    }


    @Override
    protected void onViewCreated(@NonNull @NotNull View rootView) {
        super.onViewCreated(rootView);
        mFlowLayout = rootView.findViewById(R.id.id_flowlayout);
        mBookImg = rootView.findViewById(R.id.book_img);
        mBtnAddTag = rootView.findViewById(R.id.btn_add_tag);
        QMUITopBarLayout mTopBar = rootView.findViewById(R.id.topbar);
        QMUIAlphaImageButton button = mTopBar.addLeftBackImageButton();
        button.setOnClickListener(view -> popBackStack());
        Button rightBtn = mTopBar.addRightTextButton("保存", QMUIViewHelper.generateViewId());
        rightBtn.setOnClickListener(view -> {
            DaoUtils.bookTagInfoDao.queryBuilder().where(BookTagInfoDao.Properties.Isbn.eq(isbn)).buildDelete().executeDeleteWithoutDetachingEntities();
            if (!selectTagSet.isEmpty()) {
                for (int pos : selectTagSet) {
                    long id = tagList.get(pos).getId();
                    BookTagInfo bookTagInfo = new BookTagInfo();
                    bookTagInfo.setIsbn(isbn);
                    bookTagInfo.setTagCode(id);
                    DaoUtils.bookTagInfoDao.insert(bookTagInfo);
                }
            }
            ToastUtils.showShort("保存成功！！");
        });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                BookInfo bookInfo = DaoUtils.selectBookInfo(isbn);
//                List<BookTagInfo> bookTagInfoList = DaoUtils.bookTagInfoDao.queryBuilder().list();
//                List<AuthorInfo> authorInfoList = DaoUtils.authorInfoDao.queryBuilder().list();
//                List<Tag> tagList = DaoUtils.tagDao.queryBuilder().list();
//                EventBus.getDefault().post(new BookInfoDBEvent(bookInfo, bookTagInfoList, authorInfoList, tagList));
//
//            }
//        }).start();

        BookInfo bookInfo = DaoUtils.selectBookInfo(isbn);
        List<BookTagInfo> bookTagInfoList = DaoUtils.bookTagInfoDao.queryBuilder().list();
        List<AuthorInfo> authorInfoList = DaoUtils.authorInfoDao.queryBuilder().list();
        tagList = DaoUtils.tagDao.queryBuilder().list();

        mTopBar.setTitle(bookInfo.getName());


        mTagAdapter = new TagAdapter<Tag>(tagList)
        {
            @Override
            public View getView(FlowLayout parent, int position, Tag tag)
            {
                CheckedTextView tv = (CheckedTextView) mInflater.inflate(R.layout.item_tag, mFlowLayout, false);
                tv.setText(tag.getName());
                return tv;
            }
        };
        mFlowLayout.setAdapter(mTagAdapter);
//        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
//        {
//            @Override
//            public boolean onTagClick(View view, int position, FlowLayout parent)
//            {
//
//                ((TagView) view).toggle();
//                return true;
//            }
//        });
            mFlowLayout.setOnSelectListener(selectPosSet -> {
                selectTagSet = selectPosSet;
            });

        for (BookTagInfo bookTagInfo : bookTagInfoList) {
            for (int i = 0; i < tagList.size(); i++) {
                if (tagList.get(i).getId().equals(bookTagInfo.getTagCode())) {
                    selectTagSet.add(i);
                }
            }
        }
        mTagAdapter.setSelectedList(selectTagSet);
        if (!TextUtils.isEmpty(bookInfo.getImgUrl())) {
            Glide.with(getContext()).load(bookInfo.getImgUrl()).into(mBookImg);
        }

        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
        builder.setTitle("添加标签")
                .setPlaceholder("在此输入标签名称")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("添加", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            Tag tag = new Tag();
                            tag.setName("#" + text.toString().trim() + "#");
                            DaoUtils.tagDao.insert(tag);
                            tagList.add(tag);
                            mFlowLayout.getAdapter().notifyDataChanged();
                            dialog.dismiss();
                        } else {
                        }
                    }
                });
        mBtnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.show();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(BookInfoDBEvent event) {
        List<BookTagInfo> bookTagInfoList = event.getBookTagInfoList();
        BookInfo bookInfo = event.getBookInfo();
        List<Tag> tagList = event.getTagList();
        if (tagList.size() > 0) {
            mFlowLayout.setAdapter(new TagAdapter<Tag>(tagList)
            {
                @Override
                public View getView(FlowLayout parent, int position, Tag tag)
                {
                    TextView tv = (TextView) mInflater.inflate(R.layout.item_tag, mFlowLayout, false);
                    tv.setText(tag.getName());
                    return tv;
                }
            });
//        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
//        {
//            @Override
//            public boolean onTagClick(View view, int position, FlowLayout parent)
//            {
//                ToastUtils.showShort(mVals[position]);
//                return true;
//            }
//        });
            mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener()
            {
                @Override
                public void onSelected(Set<Integer> selectPosSet)
                {
                    ToastUtils.showShort("choose:" + selectPosSet.toString());
                }
            });
        }

        if (TextUtils.isEmpty(bookInfo.getImgUrl())) {
            Glide.with(getContext()).load(bookInfo.getImgUrl()).into(mBookImg);
        }
    }
}