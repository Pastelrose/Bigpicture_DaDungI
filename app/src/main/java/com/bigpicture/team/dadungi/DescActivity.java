package com.bigpicture.team.dadungi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigpicture.team.dadungi.item.EnterpriseInfoItem;
import com.bigpicture.team.dadungi.remote.RemoteService;
import com.bigpicture.team.dadungi.remote.ServiceGenerator;
import com.github.kimkevin.cachepot.CachePot;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tsengvn.typekit.TypekitContextWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bigpicture.team.dadungi.R.id.map;

public class DescActivity extends AppCompatActivity implements OnMapReadyCallback {

    //툴바 적용이 안됨
    EnterpriseInfoItem item;
    Toolbar toolbar;
    TextView tel;
    Context context;
    ScrollView scrollView;
    GoogleMap map2;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc);

        context = this;

        EnterpriseInfoItem ei = CachePot.getInstance().pop(EnterpriseInfoItem.class);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("업체 정보");
        }

        searchInfo(ei);

        tel = (TextView) findViewById(R.id.tel);
        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DescActivity.this);

                alert.setMessage("전화를 연결하시겠습니까?");

                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + item.getTel().replace(")","-")));
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }

                });
                if(item.getTel().length()>1)
                    alert.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void searchInfo(EnterpriseInfoItem ei){
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<EnterpriseInfoItem> call = remoteService.selectEnterpriseInfo(ei.getSeq());
        call.enqueue(new Callback<EnterpriseInfoItem>() {
            @Override
            public void onResponse(Call<EnterpriseInfoItem> call,
                                   Response<EnterpriseInfoItem> response) {
                EnterpriseInfoItem enterItem = response.body();

                if (response.isSuccessful()) {
                    item = enterItem;
                    setView();
                } else {}
            }

            @Override
            public void onFailure(Call<EnterpriseInfoItem> call, Throwable t) {
            }
        });
    }

    public void setView(){
        ((TextView) findViewById(R.id.coopName)).setText(item.getName());
        ((TextView) findViewById(R.id.coopType)).setText(item.getType());
        if(item.getTel().length()>1) {
            ((TextView) findViewById(R.id.tel)).setText(item.getTel());
            ((TextView) findViewById(R.id.tel)).setTextColor(Color.BLUE);
            ((TextView) findViewById(R.id.tel)).setTypeface(null, Typeface.BOLD);
        }
        ((TextView) findViewById(R.id.benDesc)).setText(item.getBef());
        ((TextView) findViewById(R.id.coopAddr)).setText(item.getAddr()) ;

        scrollView = (ScrollView) findViewById(R.id.scroll_view);

        FragmentManager fm = getSupportFragmentManager();
        WorkaroundMapFragment fragment = (WorkaroundMapFragment) fm.findFragmentById(map);
        if (fragment == null) {
            fragment = (WorkaroundMapFragment) SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.content_main, fragment).commit();
        }
        fragment.getMapAsync(this);

        fragment.setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                scrollView.requestDisallowInterceptTouchEvent(true);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map2 = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) return;

        map2.setMyLocationEnabled(true);

        UiSettings setting = map2.getUiSettings();
        setting.setMyLocationButtonEnabled(true);
        setting.setCompassEnabled(true);
        setting.setZoomControlsEnabled(true);
        setting.setMapToolbarEnabled(true);

        MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng(item.getLat(), item.getLon()));
        marker.draggable(false);
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.test_marker2));
        map2.addMarker(marker);

        movePosition(new LatLng(item.getLat(), item.getLon()), Constant.MAP_ZOOM_LEVEL_DETAIL);
    }

    /**
     * 지정된 위치 정보와 줌 레벨을 기반으로 지도를 표시한다.
     * @param latlng 위도, 경도 객체
     * @param zoomLevel 지도 줌 레벨
     */
    private void movePosition(LatLng latlng, float zoomLevel) {
        CameraPosition cp = new CameraPosition.Builder().target((latlng)).zoom(zoomLevel).build();
        map2.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
    }

}
