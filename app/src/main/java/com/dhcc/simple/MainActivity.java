package com.dhcc.simple;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dhcc.simple.bean.DayInfo;
import com.dhcc.simple.manager.RetrofitManager;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ProgressBar mProgressBar;
    private Subscription mSubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mListView = (ListView) findViewById(R.id.listview);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        getUserInfoFromNet();
    }

    private void getUserInfoFromNet() {

        mSubscribe = RetrofitManager.getService().getDayInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DayInfo>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        mProgressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCompleted() {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(DayInfo dayInfo) {
                        List<String> datas = dayInfo.getResults();
                        if (mAdapter == null) {
                            mAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, datas);
                        }
                        mListView.setAdapter(mAdapter);
                    }
                });

        /*RetrofitManager.getService().getInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onNext(String result) {
                        Log.i("MainActivity--->", result);
                    }
                });*/
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        if (mSubscribe.isUnsubscribed()){
            mSubscribe.unsubscribe();
        }
        super.onDestroy();

    }
}
