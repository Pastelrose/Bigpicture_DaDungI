package com.bigpicture.team.dadungi;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.bigpicture.team.dadungi.item.MemberInfoItem;
import com.bigpicture.team.dadungi.lib.EtcLib;
import com.bigpicture.team.dadungi.lib.MyLog;
import com.bigpicture.team.dadungi.lib.MyToast;
import com.bigpicture.team.dadungi.lib.StringLib;
import com.bigpicture.team.dadungi.remote.RemoteService;
import com.bigpicture.team.dadungi.remote.ServiceGenerator;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    Context context;
//    ImageView profileIconImage;
//    ImageView profileIconChangeImage;
    EditText idEdit;
    EditText pwEdit;
    EditText nicknameEdit;
    EditText ageEdit;
    EditText phone_numEdit;
    EditText car_modelEdit;
    EditText charge_typeEdit;

    MemberInfoItem currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        context = this;

        currentItem = ((MyApp)getApplication()).getMemberInfoItem();

        setToolbar();
        setView();
    }

    //화면이 보일때 호출되어 사용자 정보 및 프로필 아이콘을 설정한다


    @Override
    protected void onResume() {
        super.onResume();
    }

    //액티비티 툴바 설정
    private void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.profile_setting);
        }
    }

    //액티비티 화면 설정
    private void setView(){
//        profileIconImage = (ImageView)findViewById(R.id.profile_icon);
//        profileIconImage.setOnClickListener(this);

        idEdit = (EditText)findViewById(R.id.profile_id);
        idEdit.setText(currentItem.id);
        pwEdit = (EditText)findViewById(R.id.profile_pw);
        pwEdit.setText(currentItem.pw);
        nicknameEdit = (EditText)findViewById(R.id.profile_nickname);
        nicknameEdit.setText(currentItem.nickname);

        car_modelEdit = (EditText)findViewById(R.id.profile_car_model);
        car_modelEdit.setText(currentItem.car_model);
        charge_typeEdit = (EditText)findViewById(R.id.profile_charge_type);
        charge_typeEdit.setText(currentItem.charge_type);

        ageEdit = (EditText)findViewById(R.id.profile_age);
        ageEdit.setText(currentItem.age);


        String phoneNumber = EtcLib.getInstance().getPhoneNumber(context);

        phone_numEdit = (EditText)findViewById(R.id.profile_phone);
        phone_numEdit.setText(currentItem.phone_num);

        TextView phoneStateEdit = (TextView)findViewById(R.id.phone_state);
        if(phoneNumber.startsWith("0")){
            phoneStateEdit.setText("("+getResources().getString(R.string.device_number)+")");
        } else {
            phoneStateEdit.setText("("+getResources().getString(R.string.phone_number)+")");
        }
    }


//
//    //생일 선택 다이얼로그
//    private void setBirthdayDialog(){
//        GregorianCalendar calendar = new GregorianCalendar();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                String myMonth;
//                if(month + 1 <10){
//                    myMonth = "0"+(month+1);
//                }else {
//                    myMonth = ""+(month+1);
//                }
//
//                String myDay;
//                if(dayOfMonth <10){
//                    myDay = "0"+dayOfMonth;
//                } else {
//                    myDay = ""+dayOfMonth;
//                }
//                String date = year + " " +myMonth+" "+myDay;
//                birthEdit.setText(date);
//            }
//        },year,month,day).show();
//    }

    //오른쪽 상단 메뉴 구성. 닫기 메뉴만 설정되어있는 menu_close.xml 불러옴
    //menu: 메뉴객체, 메뉴를 보여준다면 return true, 보여주지 않으면 return false

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    //왼쪽의 화살표 메뉴와 오른쪽 상단 닫기 메뉴를 클릭했을때의 동작 지정.
    //여기서 모든 버튼이 액티비티를 종료
    //item: 메뉴 아이템 객체, 메뉴를 처리했다면 return true 그렇지 않다면 false

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home : close(); break;
            case R.id.action_submit : save(); break;
        }
        return true;
    }

    //사용자가 입력한 정보를 MemberInfoItem 객체에 저장해서 반환
    //return 사용자 정보 객체

    private MemberInfoItem getMemberInfoItem(){
        MemberInfoItem item = new MemberInfoItem();
        item.phone_num = EtcLib.getInstance().getPhoneNumber(context);
        item.id = idEdit.getText().toString();
        item.pw = pwEdit.getText().toString();
        item.nickname = nicknameEdit.getText().toString();
        item.age = ageEdit.getText().toString();
        item.car_model = car_modelEdit.getText().toString();
        item.charge_type = charge_typeEdit.getText().toString();
        return item;
    }

    //기존 사용자 정보와 새로 입력한 사용자 정보를 비교하면서 변경되었는지 파악
    //newItem : 사용자 정보 객체
    //변경되면 return true 아니라면 false
    private boolean isChanged(MemberInfoItem newItem){
        if(newItem.id.trim().equals(currentItem.id) && newItem.pw.trim().equals(currentItem.pw)
                && newItem.phone_num.trim().equals(currentItem.phone_num)){
            Log.d(TAG,"return"+false);
            return false;
        } else {
            return true;
        }
    }

    //사용자가 이름을 입력했는지 확인
    //newItem 새용자가 새로 입력한 정보 객체
    //입력했다면 return false 안했다면 true
    private boolean isNoID(MemberInfoItem newItem){
        if(StringLib.getInstance().isBlank(newItem.id)) {
            return true;
        } else {
            return false;
        }
    }
    private boolean isNoPW(MemberInfoItem newItem){
        if(StringLib.getInstance().isBlank(newItem.pw)) {
            return true;
        } else {
            return false;
        }
    }

    //나이가 숫자인지 체크
    private boolean checkAge(MemberInfoItem newItem){
        String str = "int";
        if(str.indexOf(newItem.age.getClass().getName()) < 0){
            return false;
        }
        return true;
    }

    //화면이 닫히기 전에 변경 유무를 확인해서 변경사항이 있다면 저장
    private void close(){
        MemberInfoItem newItem = getMemberInfoItem();

        if(!isChanged(newItem) && !isNoID(newItem) && !isNoPW(newItem)){
            finish();
        } else if(isNoID(newItem)) {
            MyToast.s(context, R.string.id_need);
            finish();
        } else if(isNoPW(newItem)){
            MyToast.s(context, R.string.pw_need);
            finish();
        } else if(checkAge(newItem)) {
            MyToast.s(context, "나이를 숫자로 입력하세요");
            finish();
        } else {
            new AlertDialog.Builder(this).setTitle(R.string.change_save).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    save();
                }
            }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).show();
        }
    }

    //사용자가 입력한 정보 저장
    private void save(){
        final MemberInfoItem newItem = getMemberInfoItem();

        if(!isChanged(newItem)){
            MyToast.s(this, R.string.no_change);
            finish();
            return;
        }

        MyLog.d(TAG,"insert Item "+newItem.toString());

        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.insertMemberInfo(newItem); //여기서 저장 요청
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    String id = response.body();
                    try{
                        if(currentItem.id == null){
                            MyToast.s(context,R.string.member_insert_fail_message);
                            return;
                        }
                    } catch (Exception e){
                        MyToast.s(context, R.string.member_insert_fail_message);
                        return;
                    }
                    currentItem.id = newItem.id;
                    currentItem.pw = newItem.pw;
                    currentItem.nickname = newItem.nickname;
                    currentItem.age = newItem.age;
                    currentItem.phone_num = newItem.phone_num;
                    currentItem.car_model = newItem.car_model;
                    currentItem.charge_type = newItem.charge_type;
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    //뒤로가기 버튼을 클릭했을때 close() 호출

    @Override
    public void onBackPressed() {
        close();
    }



}
