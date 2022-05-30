package com.example.charge.entity;

import androidx.annotation.NonNull;

public class ImageInfo {

    private int imageWidth = 0;

    private int imageHeight = 0;

    private String imageUrl = "";

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
