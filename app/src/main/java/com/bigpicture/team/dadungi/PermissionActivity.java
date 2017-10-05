package com.bigpicture.team.dadungi;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NuuN on 2017-08-21.
 */

// 앱 실행시에 필요한 권한을 처리하기 위한 액티비티

public class PermissionActivity extends AppCompatActivity {
    private static final int PERMISSION_MULTI_CODE = 100;

    //화면을 구성하고 SDK버전과 권한을 처리.

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        if(Build.VERSION.SDK_INT < 23){
            goIndexActivity();
        } else {
            if(checkAndRequestPermissions()){
                goIndexActivity();
            }
        }
    }

    //권한을 확인하고 권한이 부여되어있지 않다면 요청한다. 모든 권한이 부여되면 true
    private boolean checkAndRequestPermissions() {
        String [] permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String permission:permissions){
            //뿅
            if(ContextCompat.checkSelfPermission(this,permission) !=
                    PackageManager.PERMISSION_GRANTED){
                listPermissionsNeeded.add(permission);
            }
        }
        if(!listPermissionsNeeded.isEmpty()){
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_MULTI_CODE);
            return false;
        }
        return true;
    }

    //권한 요청 결과를 받는 메소드
    //requestCode:요청코드, permissions:권한종류, grantResults:권한결과
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length == 0) return;

        switch (requestCode) {
            case PERMISSION_MULTI_CODE:
                checkpermissionResult(permissions, grantResults);
                break;
        }
    }

    private void checkpermissionResult(String[] permissions, int[] grantResults) {
        boolean isAllgranted = true;
        for(int i = 0; i<permissions.length; i++){
            if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                isAllgranted = false;
            }
        }

        //권한이 모두 부여되었다면
        if(isAllgranted){
            goIndexActivity();
        }
        //권한이 모두 부여되지 않았다면
        else {
            showPermissionDialog();
        }
    }

    //권한 설정 화면으로 이동할지 선택하는 다이얼로그를 보임
    private void showPermissionDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.permission_setting_title);
        dialog.setMessage(R.string.permission_setting_message);
        dialog.setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Toast.makeText(PermissionActivity.this, R.string.permission_setting_restart,Toast.LENGTH_LONG).show();
                finish();
                goAppSettingActivity();
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        dialog.show();
    }


    //현재 엑티비티를 종료하고 인덱스로 넘어감
    private void goIndexActivity() {
        Intent intent = new Intent(getApplicationContext(),IndexActivity.class);
        startActivity(intent);
        finish();
    }

    //권한을 설정할 수 있는 설정 액티비티를 실행
    private void goAppSettingActivity(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",getPackageName(),null);
        intent.setData(uri);
        startActivity(intent);
    }


}
