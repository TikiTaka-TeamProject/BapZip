package com.sparta.bapzip.kakaomap.application;

import com.google.gson.JsonObject;

public interface KakaoMapCallable {

    JsonObject getDocuments(String address);
}
