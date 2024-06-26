package com.mos.global.auth.handler;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class OAuthRequestData {

  public String code;

  /* KAKAO
  *     kakao.client.id
        kakao.client.secret
        kakao.redirect.url
        kakao.auth.uri
        kakao.api.url
  * */
  public static String KAKAO_API_URI;
  public static String KAKAO_CLIENT_ID;
  public static String KAKAO_CLIENT_SECRET;
  public static String KAKAO_REDIRECT_URL;
  public static String KAKAO_AUTH_URI;

  public OAuthRequestData setCode(String code) {
    this.code = code;
    return this;
  }

  @Value("${kakao.api.url}")
  public void setKakaoApiUri(String kakaoApiUri) {
    KAKAO_API_URI = kakaoApiUri;
  }

  @Value("${kakao.client.id}")
  public void setKakaoClientId(String kakaoClientId) {
    KAKAO_CLIENT_ID = kakaoClientId;
  }

  @Value("${kakao.client.secret}")
  public void setKakaoClientSecret(String kakaoClientSecret) {
    KAKAO_CLIENT_SECRET = kakaoClientSecret;
  }

  @Value("${kakao.redirect.url}")
  public void setKakaoRedirectUrl(String kakaoRedirectUrl) {
    KAKAO_REDIRECT_URL = kakaoRedirectUrl;
  }

  @Value("${kakao.auth.uri}")
  public void setKakaoAuthUri(String kakaoAuthUri) {
    KAKAO_AUTH_URI = kakaoAuthUri;
  }


  /* GITHUB
  *     github.clientId
        github.clientSecret
  * */
  public static String GITHUB_CLIENT_ID;
  public static String GITHUB_CLIENT_SECRET;

  @Value("${github.clientId}")
  public void setGithubClientId(String githubClientId) {
    GITHUB_CLIENT_ID = githubClientId;
  }

  @Value("${github.clientSecret}")
  public void setGithubClientSecret(String githubClientSecret) {
    GITHUB_CLIENT_SECRET = githubClientSecret;
  }

  /*
  * GOOGLE
  *     google.clientId
  *     google.clientSecret
  *     google.redirect-uri
  * */
  public static String GOOGLE_CLIENT_ID;
  public static String GOOGLE_CLIENT_SECRET;
  public static String GOOGLE_REDIRECT_URI;

  @Value("${google.clientId}")
  public void setGoogleClientId(String googleClientId) {
    GOOGLE_CLIENT_ID = googleClientId;
  }

  @Value("${google.clientSecret}")
  public void setGoogleClientSecret(String googleClientSecret) {
    GOOGLE_CLIENT_SECRET = googleClientSecret;
  }

  @Value("${google.redirect-uri}")
  public void setGoogleRedirectUri(String googleRedirectUri) {
    GOOGLE_REDIRECT_URI = googleRedirectUri;
  }


  /*
   * NAVER
   *     naver.clientId
   *     naver.clientSecret
   *     naver.redirect-uri
   * */
  public static String NAVER_CLIENT_ID;
  public static String NAVER_CLIENT_SECRET;
  public static String NAVER_REDIRECT_URI;
  public static String NAVER_AUTH_URI;
  public static String NAVER_API_URL;

  @Value("${naver.clientId}")
  public void setNaverClientId(String naverClientId) {
    NAVER_CLIENT_ID = naverClientId;
  }

  @Value("${naver.clientSecret}")
  public void setNaverClientSecret(String naverClientSecret) {
    NAVER_CLIENT_SECRET = naverClientSecret;
  }

  @Value("${naver.redirect-uri}")
  public void setNaverRedirectUri(String naverRedirectUri) {
    NAVER_REDIRECT_URI = naverRedirectUri;
  }

  @Value("${naver.auth.uri}")
  public void setNaverAuthUri(String naverAuthUri) {
    NAVER_AUTH_URI = naverAuthUri;
  }

  @Value("${naver.api.url}")
  public void setNaverApiUrl(String naverApiUrl) {
    NAVER_API_URL = naverApiUrl;
  }

  @Override
  public String toString() {
    return new GsonBuilder().create().toJson(this);
  }


}
