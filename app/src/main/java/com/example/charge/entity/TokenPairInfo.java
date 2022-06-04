package com.example.charge.entity;

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

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public String toString() {
        return "TokenPairInfo{" +
                "refreshToken: '" + refreshToken + '\'' +
                ", accessToken: '" + accessToken + '\'' +
                ", tokenType: '" + tokenType + '\'' +
                '}';
    }
}
