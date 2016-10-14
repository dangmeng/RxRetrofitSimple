package com.dhcc.simple.service;

import com.dhcc.simple.bean.DayInfo;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/8.
 */

public interface GitHubService {

    //https://api.github.com/users/dangmeng
    @GET("api/day/history")
    Observable<DayInfo> getDayInfo();

    @GET("users/dangmeng")
    Observable<String> getInfo();
}
