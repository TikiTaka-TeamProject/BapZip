package com.sparta.bapzip.kakaomap.infrastructure;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sparta.bapzip.kakaomap.application.KakaoMapCallable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class KakaoMapApiClient implements KakaoMapCallable {

    private final String API_URL = "https://dapi.kakao.com/v2/local/search/address.json?query=";

    @Value("${KAKAO_REST_API_KEY}")
    private String apiKey;

    public JsonObject getDocuments(String query){

        RestTemplate restTemplate = new RestTemplate();
        Gson gson = new Gson();
        String url = API_URL + query;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "KakaoAK " + apiKey);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        String response =  restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        JsonObject documents = null;
        if (!Objects.requireNonNull(jsonObject).getAsJsonArray("documents").isEmpty()){
            documents = jsonObject.getAsJsonArray("documents").get(0).getAsJsonObject();
        }
        return documents;
    }

}
