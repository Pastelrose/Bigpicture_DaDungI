package com.bigpicture.team.dadungi;

import android.app.Application;
import android.os.StrictMode;

import com.bigpicture.team.dadungi.item.EnterpriseInfoItem;
import com.bigpicture.team.dadungi.item.MemberInfoItem;
import com.tsengvn.typekit.Typekit;

/**
 * Created by NuuN on 2017-08-22.
 */

public class MyApp extends Application{
    private MemberInfoItem memberInfoItem;
    private EnterpriseInfoItem enterpriseInfoItem;

    @Override
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance().addNormal(Typekit.createFromAsset(this,"fonts/NanumBarunGothic.ttf"));
        Typekit.getInstance().addBold(Typekit.createFromAsset(this,"fonts/NanumBarunGothicBold.ttf"));

        //FileUriExposedException은 문제 해결을 위한 코드
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public MemberInfoItem getMemberInfoItem(){
        if (memberInfoItem == null) memberInfoItem = new MemberInfoItem();
        return memberInfoItem;
    }

    public void setMemberInfoItem(MemberInfoItem item){
        this.memberInfoItem = item;
    }

    public String getMemberId(){
        return memberInfoItem.id;
    }

    public void setEnterpriseInfoItem(EnterpriseInfoItem enterpriseInfoItem){
        this.enterpriseInfoItem = enterpriseInfoItem;
    }

    public EnterpriseInfoItem getEnterpriseInfoItem(){
        return enterpriseInfoItem;
    }
}
