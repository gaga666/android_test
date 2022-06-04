package com.example.charge.entity;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageInfo {

    @JsonProperty("image_width")
    private int imageWidth;

    @JsonProperty("image_height")
    private int imageHeight;

    @JsonProperty("image_url")
    private String imageUrl;

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return "ImageInfo{" +
                "imageWidth: " + imageWidth +
                ", imageHeight: " + imageHeight +
                ", imageUrl: " + imageUrl +
                '}';
    }
}
