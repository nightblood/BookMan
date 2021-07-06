package com.borf.bookman;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.borf.bookman.bean.BookInfo;
import com.borf.bookman.bean.BookInfoDao;
import com.borf.bookman.bean.DaoSession;
import com.borf.bookman.event.MessageEvent;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import androidx.annotation.WorkerThread;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class MainActivity extends Activity {

    private TextView mTextView;
    private Button mBtnScan;
    private Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);
        mBtnScan = (Button) findViewById(R.id.btn_scan);
        mContext = this;
//        mBtnScan.setOnClickListener(v -> new IntentIntegrator(mContext).initiateScan());
        mBtnScan.setOnClickListener(v -> EventBus.getDefault().post(new MessageEvent("欢迎大家浏览我写的博客")));
        EventBus.getDefault().register(this);

    }

    private void getBook(String isbn) throws IOException, JSONException {
        System.out.println(isbn);
        String urlIsbn = "http://douban.com/isbn/" + isbn + "/";
        Document document = Jsoup.connect(urlIsbn)
                .header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36 QIHU 360SE")
                .get();
        String title = document.getElementsByTag("title").get(0).text();
        if (title.contains("豆瓣错误")) {
            return;
        }
        Element ele = document.getElementsByClass("nbg").get(0);
        String bookImg = ele.attr("href");
        String strBookInfo = document.getElementsByAttributeValue("type", "application/ld+json").get(0).data();
        JSONObject jsonObject = new JSONObject(strBookInfo);
        String name = jsonObject.getString("name");
//        String isbn = jsonObject.getString("isbn");
        String context = jsonObject.getString("@context");
        String type = jsonObject.getString("@type");
        String workExample = jsonObject.getString("workExample");
        String url = jsonObject.getString("url");
        String sameAs = jsonObject.getString("sameAs");
        JSONArray jsonArray = jsonObject.getJSONArray("author");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = (JSONObject) jsonArray.get(i);
            String author_type = object.getString("@type");
            String author_name = object.getString("name");
        }
        DaoSession daoSession = MyApplication.getDaoSession();
        BookInfoDao bookInfoDao = daoSession.getBookInfoDao();
        BookInfo bookInfo = new BookInfo();
        bookInfo.setContext(context);
        bookInfo.setImgUrl(bookImg);
        bookInfo.setIsbn(isbn);
        bookInfo.setName(name);
        bookInfo.setUrl(url);
        bookInfo.setWorkExample(workExample);
        bookInfo.setType(type);
        bookInfo.setSameAs(sameAs);

        bookInfoDao.insert(bookInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                mTextView.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void Event(MessageEvent messageEvent) throws IOException, JSONException {
        System.out.println(messageEvent.getMessage());

        getBook("9787020008728");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}