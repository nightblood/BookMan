package com.borf.bookman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.borf.bookman.activity.CustomCaptureActivity;
import com.borf.bookman.base.BaseFragmentActivity;
import com.borf.bookman.bean.AuthorInfo;
import com.borf.bookman.bean.AuthorInfoDao;
import com.borf.bookman.bean.BookInfo;
import com.borf.bookman.bean.BookInfoDao;
import com.borf.bookman.bean.DaoSession;
import com.borf.bookman.event.BookInfoEvent;
import com.borf.bookman.fragment.HomeFragment;
import com.borf.bookman.utils.DaoUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qmuiteam.qmui.arch.annotation.DefaultFirstFragment;
import com.qmuiteam.qmui.arch.annotation.FirstFragments;
import com.qmuiteam.qmui.skin.QMUISkinHelper;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.skin.QMUISkinValueBuilder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.util.QMUIViewOffsetHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView2;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.fragment.app.FragmentContainerView;

@FirstFragments(
        value = {
                HomeFragment.class,

        }
)
@DefaultFirstFragment(HomeFragment.class)
public class MainActivity extends BaseFragmentActivity {

    private TextView mTextView;
    private Button mBtnScan;
    private Activity mContext;
    private QMUISkinManager.OnSkinChangeListener mOnSkinChangeListener = new QMUISkinManager.OnSkinChangeListener() {
        @Override
        public void onSkinChange(QMUISkinManager skinManager, int oldSkin, int newSkin) {
            QMUIStatusBarHelper.setStatusBarLightMode(MainActivity.this);
//            if (newSkin == QDSkinManager.SKIN_WHITE) {
//                QMUIStatusBarHelper.setStatusBarLightMode(MainActivity.this);
//            } else {
//                QMUIStatusBarHelper.setStatusBarDarkMode(MainActivity.this);
//            }
        }
    };
    private QMUIPopup mGlobalAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUISkinManager skinManager = QMUISkinManager.defaultInstance(this);
        setSkinManager(skinManager);
        mOnSkinChangeListener.onSkinChange(skinManager, -1, skinManager.getCurrentSkin());
        mContext = this;

    }

    private BookInfo searchInDouban(Long isbn) throws IOException, JSONException {
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

    private BookInfo getBook(Long isbn) {
        try {
            LogUtils.d("开始搜索图书：" + isbn);
            BookInfo result = DaoUtils.selectBookInfo(isbn);
            if (result != null) {
                return result;
            }
            return searchInDouban(isbn);
        }catch (Exception exception) {
            LogUtils.e(exception);
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() != null) {
                new Thread(() -> {
                    String isbnStr = result.getContents();
                    try {
                        Long isbn = Long.parseLong(isbnStr);
                        LogUtils.d("开始搜索图书：" + isbn);
                        BookInfo book = DaoUtils.selectBookInfo(isbn);
                        if (book != null) {
                            EventBus.getDefault().post(new BookInfoEvent(book, book.getName() + "，此书已在书架中。", "exist"));
                            return;
                        }
                        book =  searchInDouban(isbn);
                        EventBus.getDefault().post(new BookInfoEvent(book, "新书 " +book.getName() + " 已添加到书架中。", "new"));
                    }catch (Exception exception) {
                        LogUtils.e(exception);
                        EventBus.getDefault().post(new BookInfoEvent(null, "系统异常，请联系最帅的人！！！", "error"));
                    }
                }).start();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(BookInfoEvent bookInfoEvent) {
        new QMUIDialog.MessageDialogBuilder(MainActivity.this)
                .setTitle("提示")
                .setMessage(bookInfoEvent.getMsg())
                .addAction("确定", (dialog, index) -> dialog.dismiss())
                .show();
    }

    @Override
    protected RootView onCreateRootView(int fragmentContainerId) {
        return new CustomRootView(this, fragmentContainerId);
    }

    private void showGlobalActionPopup(View v) {
        String[] listItems = new String[]{
                "扫一扫",
                "搜索",
                "测试"
        };
        List<String> data = new ArrayList<>();

        Collections.addAll(data, listItems);

        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, data);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    new IntentIntegrator(MainActivity.this)
                            .setCaptureActivity(CustomCaptureActivity.class)
                            .setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)// 扫码的类型,可选：一维码，二维码，一/二维码
                            .setPrompt("请对准二维码")// 设置提示语
                            .setCameraId(0)// 选择摄像头,可使用前置或者后置
                            .setBeepEnabled(false)// 是否开启声音,扫完码之后会"哔"的一声
                            .setBarcodeImageEnabled(true)// 扫完码之后生成二维码的图片
                            .initiateScan();// 初始化扫码
                } else if (i == 1) {

                }
                else if (i == 2) {
                    new Thread(() -> {
                        BookInfo book = null;

                            book = getBook(9787521729245l);
                            EventBus.getDefault().post(new BookInfoEvent(book, "小测测", "test"));

                    }).start();
                }
                if (mGlobalAction != null) {
                    mGlobalAction.dismiss();
                }
            }
        };
        mGlobalAction = QMUIPopups.listPopup(this,
                QMUIDisplayHelper.dp2px(this, 250),
                QMUIDisplayHelper.dp2px(this, 300),
                adapter,
                onItemClickListener)
                .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                .preferredDirection(QMUIPopup.DIRECTION_TOP)
                .shadow(true)
                .edgeProtection(QMUIDisplayHelper.dp2px(this, 10))
                .offsetYIfTop(QMUIDisplayHelper.dp2px(this, 5))
                .skinManager(QMUISkinManager.defaultInstance(this))
                .show(v);
    }
    class CustomRootView extends RootView {

        private FragmentContainerView fragmentContainer;
        private QMUIRadiusImageView2 globalBtn;
        private QMUIViewOffsetHelper globalBtnOffsetHelper;
        private int btnSize;
        private final int touchSlop;
        private float touchDownX = 0;
        private float touchDownY = 0;
        private float lastTouchX = 0;
        private float lastTouchY = 0;
        private boolean isDragging;
        private boolean isTouchDownInGlobalBtn = false;

        public CustomRootView(Context context, int fragmentContainerId) {
            super(context, fragmentContainerId);

            btnSize = QMUIDisplayHelper.dp2px(context, 56);

            fragmentContainer = new FragmentContainerView(context);
            fragmentContainer.setId(fragmentContainerId);
            addView(fragmentContainer, new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


            globalBtn = new QMUIRadiusImageView2(context);
            globalBtn.setImageResource(R.mipmap.icon_theme);
            globalBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            globalBtn.setRadiusAndShadow(btnSize / 2,
                    QMUIDisplayHelper.dp2px(getContext(), 16), 0.4f);
            globalBtn.setBorderWidth(1);
            globalBtn.setBorderColor(QMUIResHelper.getAttrColor(context, R.attr.qmui_skin_support_color_separator));
            globalBtn.setBackgroundColor(QMUIResHelper.getAttrColor(context, R.attr.app_skin_common_background));
            globalBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showGlobalActionPopup(v);
                }
            });
            FrameLayout.LayoutParams globalBtnLp = new FrameLayout.LayoutParams(btnSize, btnSize);
            globalBtnLp.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            globalBtnLp.bottomMargin = QMUIDisplayHelper.dp2px(context, 60);
            globalBtnLp.rightMargin = QMUIDisplayHelper.dp2px(context, 24);
            QMUISkinValueBuilder builder = QMUISkinValueBuilder.acquire();
            builder.background(R.attr.app_skin_common_background);
            builder.border(R.attr.qmui_skin_support_color_separator);
            builder.tintColor(R.attr.app_skin_common_img_tint_color);
            QMUISkinHelper.setSkinValue(globalBtn, builder);
            builder.release();
            addView(globalBtn, globalBtnLp);
            globalBtnOffsetHelper = new QMUIViewOffsetHelper(globalBtn);
            touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            globalBtnOffsetHelper.onViewLayout();
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent event) {
            float x = event.getX(), y = event.getY();
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                isTouchDownInGlobalBtn = isDownInGlobalBtn(x, y);
                touchDownX = lastTouchX = x;
                touchDownY = lastTouchY = y;
            } else if (action == MotionEvent.ACTION_MOVE) {
                if (!isDragging && isTouchDownInGlobalBtn) {
                    int dx = (int) (x - touchDownX);
                    int dy = (int) (y - touchDownY);
                    if (Math.sqrt(dx * dx + dy * dy) > touchSlop) {
                        isDragging = true;
                    }
                }

                if (isDragging) {
                    int dx = (int) (x - lastTouchX);
                    int dy = (int) (y - lastTouchY);
                    int gx = globalBtn.getLeft();
                    int gy = globalBtn.getTop();
                    int gw = globalBtn.getWidth(), w = getWidth();
                    int gh = globalBtn.getHeight(), h = getHeight();
                    if (gx + dx < 0) {
                        dx = -gx;
                    } else if (gx + dx + gw > w) {
                        dx = w - gw - gx;
                    }

                    if (gy + dy < 0) {
                        dy = -gy;
                    } else if (gy + dy + gh > h) {
                        dy = h - gh - gy;
                    }
                    globalBtnOffsetHelper.setLeftAndRightOffset(
                            globalBtnOffsetHelper.getLeftAndRightOffset() + dx);
                    globalBtnOffsetHelper.setTopAndBottomOffset(
                            globalBtnOffsetHelper.getTopAndBottomOffset() + dy);
                }
                lastTouchX = x;
                lastTouchY = y;
            } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
                isDragging = false;
                isTouchDownInGlobalBtn = false;
            }
            return isDragging;
        }

        private boolean isDownInGlobalBtn(float x, float y) {
            return globalBtn.getLeft() < x && globalBtn.getRight() > x &&
                    globalBtn.getTop() < y && globalBtn.getBottom() > y;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX(), y = event.getY();
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                isTouchDownInGlobalBtn = isDownInGlobalBtn(x, y);
                touchDownX = lastTouchX = x;
                touchDownY = lastTouchY = y;
            } else if (action == MotionEvent.ACTION_MOVE) {
                if (!isDragging && isTouchDownInGlobalBtn) {
                    int dx = (int) (x - touchDownX);
                    int dy = (int) (y - touchDownY);
                    if (Math.sqrt(dx * dx + dy * dy) > touchSlop) {
                        isDragging = true;
                    }
                }

                if (isDragging) {
                    int dx = (int) (x - lastTouchX);
                    int dy = (int) (y - lastTouchY);
                    int gx = globalBtn.getLeft();
                    int gy = globalBtn.getTop();
                    int gw = globalBtn.getWidth(), w = getWidth();
                    int gh = globalBtn.getHeight(), h = getHeight();
                    if (gx + dx < 0) {
                        dx = -gx;
                    } else if (gx + dx + gw > w) {
                        dx = w - gw - gx;
                    }

                    if (gy + dy < 0) {
                        dy = -gy;
                    } else if (gy + dy + gh > h) {
                        dy = h - gh - gy;
                    }
                    globalBtnOffsetHelper.setLeftAndRightOffset(
                            globalBtnOffsetHelper.getLeftAndRightOffset() + dx);
                    globalBtnOffsetHelper.setTopAndBottomOffset(
                            globalBtnOffsetHelper.getTopAndBottomOffset() + dy);
                }
                lastTouchX = x;
                lastTouchY = y;
            } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
                isDragging = false;
                isTouchDownInGlobalBtn = false;
            }
            return isDragging || super.onTouchEvent(event);
        }

        @Override
        public FragmentContainerView getFragmentContainerView() {
            return fragmentContainer;
        }
    }
}