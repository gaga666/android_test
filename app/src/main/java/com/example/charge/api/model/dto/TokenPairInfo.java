package com.example.charge.api.model.dto;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;


public class TokenPairInfo {

    /**
     * 刷新令牌: 用于更新 Refresh-Access Token Pair
     */
    @JsonProperty("refresh_token")
    private String refreshToken;

    /**
     * 访问令牌: 用于需鉴权的 API 请求
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * 令牌类型
     */
    @JsonProperty("token_type")
    private String tokenType;

    public TokenPairInfo() {
    }

    public TokenPairInfo(String refreshToken, String accessToken, String tokenType) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public TokenPairInfo setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public TokenPairInfo setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getTokenType() {
        return tokenType;
    }

    public TokenPairInfo setTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "TokenPairInfo{" +
                "refreshToken: '" + refreshToken + '\'' +
                ", accessToken: '" + accessToken + '\'' +
                ", tokenType: '" + tokenType + '\'' +
                '}';
    }
}
