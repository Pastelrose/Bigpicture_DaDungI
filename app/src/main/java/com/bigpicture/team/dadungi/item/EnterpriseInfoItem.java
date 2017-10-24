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
    @SerializedName("user_distance_meter") public double userDistanceMeter;


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
                ", userDistanceMeter=" + userDistanceMeter +
                '}';
    }

    public int getSeq() {
        return seq;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getAddr() {
        return addr;
    }

    public String getTel() {
        return tel;
    }

    public String getBef() {
        return bef;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getUserDistanceMeter() {
        return userDistanceMeter;
    }

    public EnterpriseInfoItem(String type, String name, String addr) {
        this.type = type;
        this.name = name;
        this.addr = addr;
    }
    public EnterpriseInfoItem() {
        this.seq = -1;
        this.name = "이름";
        this.type = "타입";
        this.addr = "주소";
        this.tel = "연락처";
        this.bef = "혜택";
    }
}
