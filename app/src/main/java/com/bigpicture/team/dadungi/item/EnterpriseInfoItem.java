package com.bigpicture.team.dadungi.item;

import com.google.gson.annotations.SerializedName;

/**
 * 업체 정보를 저장하는 객체
 */
@org.parceler.Parcel
public class EnterpriseInfoItem {
    public int seq;
    public String type;
    public String name;
    public String addr;
    public String tel;
    public String bef;
    public double lat;
    public double lon;
    @SerializedName("reg_date") public String regDate;
    @SerializedName("mod_date") public String modDate;
    @SerializedName("user_distance_meter") public double userDistanceMeter;
    @SerializedName("image_filename") public String imageFilename;

    @Override
    public String toString() {
        return "EnterpriseInfoItem{" +
                "seq=" + seq +
                ", type=" + type +
                ", name=" + name +
                ", addr='" + addr + '\'' +
                ", tel='" + tel + '\'' +
                ", bef='" + bef + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", regDate='" + regDate + '\'' +
                ", modDate='" + modDate + '\'' +
                ", userDistanceMeter=" + userDistanceMeter +
                ", imageFilename='" + imageFilename + '\'' +
                '}';
    }
}
