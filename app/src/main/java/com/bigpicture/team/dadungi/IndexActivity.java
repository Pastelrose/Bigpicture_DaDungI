package com.bigpicture.team.dadungi;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bigpicture.team.dadungi.item.MemberInfoItem;
import com.bigpicture.team.dadungi.lib.EtcLib;
import com.bigpicture.team.dadungi.lib.GeoLib;
import com.bigpicture.team.dadungi.lib.MyLog;
import com.bigpicture.team.dadungi.lib.RemoteLib;
import com.bigpicture.team.dadungi.lib.StringLib;
import com.bigpicture.team.dadungi.remote.RemoteService;
import com.bigpicture.team.dadungi.remote.ServiceGenerator;

import java.lang.reflect.Member;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//시작 액티비티, 사용자 정보를 통하여 메인으로 갈지 프로필로 갈지 결정한다
public class IndexActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    Context context;

    //인터넷 연결확인
    //인터넷이 연결되어있지 않다면 showNoService()호출
    //saveInstanceState: 액티비티가 새로 생성되었을 경우에 이전 상태값을 갖는 객체

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        //context = this;

        if (!RemoteLib.getInstance().isConnected(this)) {
            showNoService();
            return;
        }
    }

    //0.5초 이후에 startTask() 호출하여 서버에서 사용자 정보 조회
    @Override
    protected void onStart() {
        super.onStart();
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startMain();
            }
        },500);
    }

    //인터넷 접속 불가 메세지와 함께 종료 버튼
    private void showNoService(){
        TextView messageText = (TextView)findViewById(R.id.message);
        messageText.setVisibility(View.VISIBLE);

        Button closeButton = (Button)findViewById(R.id.close);
        closeButton.setVisibility(View.VISIBLE);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //메인액티비티를 실행하고 현재 액티비티 종료
    public void startMain(){
        Intent intent = new Intent(IndexActivity.this, MainActivity.class);
        startActivity(intent);

        finish();
    }

}
