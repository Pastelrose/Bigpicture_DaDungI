package com.bigpicture.team.dadungi.item;

import com.google.gson.annotations.SerializedName;

/**
 * 사용자 정보를 저장하는 객체
 */
public class MemberInfoItem {
    public String id;
    public String pw;
    public String age;
    public String phone_num;
    public String nickname;
    public String car_model;
    public String charge_type;
    public String link_login;
    @SerializedName("reg_date") public String regDate;

    @Override
    public String toString() {
        return "MemberInfoItem{" +
                "member_id=" + id +
                ", member_pw='" + pw + '\'' +
                ", nickname='" + nickname + '\'' +
                ", age='" + age + '\'' +
                ", phone_num='" + phone_num + '\'' +
                ", car_model='" + car_model + '\'' +
                ", charge_type='" + charge_type + '\'' +
                '}';
    }
}
