package com.bigpicture.team.dadungi.item;

import com.google.gson.annotations.SerializedName;

/**
 * 충전소 이미지 정보를 저장하는 객체
 */
public class ImageItem {
    @SerializedName("cpId") public int cpId;
    @SerializedName("file_name") public String fileName;
    @SerializedName("image_memo") public String imageMemo;
    @SerializedName("reg_date") public String regDate;

    @Override
    public String toString() {
        return "ImageItem{" +
                "seq=" + cpId +
                ", fileName='" + fileName + '\'' +
                ", imageMemo='" + imageMemo + '\'' +
                ", regDate='" + regDate + '\'' +
                '}';
    }
}
