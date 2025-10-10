package com.sparta.bapzip.kakaolocal.application;

import com.google.gson.JsonObject;

public interface KakaoLocalCallable {

    JsonObject getDocuments(String address);
}
