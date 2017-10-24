package com.bigpicture.team.dadungi;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.bigpicture.team.dadungi.item.EnterpriseInfoItem;
import com.github.kimkevin.cachepot.CachePot;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.bigpicture.team.dadungi.adapter.MapListAdapter;
import com.bigpicture.team.dadungi.item.GeoItem;
import com.bigpicture.team.dadungi.lib.GoLib;
import com.bigpicture.team.dadungi.lib.GeoLib;
import com.bigpicture.team.dadungi.lib.MyLog;
import com.bigpicture.team.dadungi.lib.MyToast;
import com.bigpicture.team.dadungi.remote.RemoteService;
import com.bigpicture.team.dadungi.remote.ServiceGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 지도에서 맛집 위치를 보여주는 프래그먼트
 */
public class MapFragment extends Fragment
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener {
    private final String TAG = this.getClass().getSimpleName();

    Context context;

    MapInfoFragment mapInfoFragment;
    FragmentTransaction ft;
    FragmentManager fm2;
    boolean isMarkerClicked = false;

    String select_type = "*";
    GoogleMap map;

    LatLng currentLatLng;
    int distanceMeter = 640;
    int currentZoomLevel = Constant.MAP_ZOOM_LEVEL_DETAIL;
    boolean isOnList = false;

    Toast zoomGuideToast;

    private HashMap<Marker, EnterpriseInfoItem> markerMap = new HashMap<>();

    RecyclerView list;
    MapListAdapter adapter;
    ArrayList<EnterpriseInfoItem> infoList = new ArrayList<>();

    Button listOpen;

    /**
     * MapFragment 인스턴스를 생성해서 반환한다.
     * @return MapFragment 인스턴스
     */
    public static MapFragment newInstance() {
        MapFragment f = new MapFragment();
        return f;
    }



    /**
     * fragment_mapl 기반으로 뷰를 생성한다.
     * @param inflater XML를 객체로 변환하는 LayoutInflater 객체
     * @param container null이 아니라면 부모 뷰
     * @param savedInstanceState null이 아니라면 이전에 저장된 상태를 가진 객체
     * @return 생성한 뷰 객체
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getActivity();

//        memberId = ((MyApp)this.getActivity().getApplication()).getMemberId();

        View v = inflater.inflate(R.layout.fragment_map, container, false);

        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ( keyCode == KeyEvent.KEYCODE_BACK && isMarkerClicked){
                    ft = fm2.beginTransaction();
                    ft.remove(mapInfoFragment);
                    ft.commit();
                    isMarkerClicked = false;
                    return true;
                } else {
                    return false;
                }
            }
        });
        return v;
    }

    /**
     * onCreateView() 메소드 뒤에 호출되며 구글맵을 화면에 보여준다.
     * 그리고 화면 구성을 위한 작업을 한다.
     * @param view onCreateView() 메소드에 의해 반환된 뷰
     * @param savedInstanceState null이 아니라면 이전에 저장된 상태를 가진 객체
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.menu2);

        fm2=getChildFragmentManager();
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }
        fragment.getMapAsync(this);


        List<String> categories = new ArrayList<String>();
        String[] types = getResources().getStringArray(R.array.types);
        categories = spinnerSetting(types,categories);

        Spinner spinner = (Spinner)view.findViewById(R.id.spinner);
        ArrayAdapter<String> adspin = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,categories);

        adspin.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spinner.setAdapter(adspin);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_type = parent.getItemAtPosition(position).toString();
                if(select_type.contains("전체"))select_type="*";
                MyLog.d(TAG,select_type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                select_type = "*";
                MyLog.d(TAG,select_type);
            }
        });



    }

    /**
     * spinner element setting
     */
    public List spinnerSetting(String[] arr,List spinlist){
        for(int i =0; i<arr.length;i++){
            spinlist.add(arr[i]);
        }
        return spinlist;
    }

    /**
     * 목록을 보여주는 버튼을 현재 상태 on 인자에 기반하여 설정한다.
     * @param on 현재 버튼 상태가 목록보기라면 true, 목록닫기라면 false
     */
    private void setInfoList(boolean on) {
        if (!on) {
            isOnList = false;
            list.setVisibility(View.GONE);
            listOpen.setText(R.string.list_open);
        } else {
            isOnList = true;
            list.setVisibility(View.VISIBLE);
            listOpen.setText(R.string.list_close);
        }
    }

    /**
     * 구글맵이 준비되었을 때 호출되며 구글맵을 설정하고 기본 마커를 추가하는 작업을 한다.
     * @param map 구글맵 객체
     */
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        map.setInfoWindowAdapter(null);

        map.setOnMarkerClickListener(this);
        map.setOnMapClickListener(this);

        String fineLocationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;

        if (ActivityCompat.checkSelfPermission(context, fineLocationPermission)
                != PackageManager.PERMISSION_GRANTED) return;

        map.setMyLocationEnabled(true);

        map.setOnCameraMoveListener(this);
        map.setOnCameraIdleListener(this);

        UiSettings setting = map.getUiSettings();
        setting.setMyLocationButtonEnabled(true);
        setting.setCompassEnabled(true);
        setting.setZoomControlsEnabled(true);
        setting.setMapToolbarEnabled(false);

        MyLog.d(TAG, GeoItem.getKnownLocation().toString());

        if (GeoItem.getKnownLocation() != null) {
            movePosition(GeoItem.getKnownLocation(), Constant.MAP_ZOOM_LEVEL_DETAIL);
        }
        showList();
    }
    /**
     * 구글맵에서 마커가 클릭되었을 때 호출된다.
     * @param marker 클릭한 마커에 대한 정보를 가진 객체
     * @return 마커 이벤트를 처리했다면 true, 그렇지 않다면 false
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        EnterpriseInfoItem ei = markerMap.get(marker);

        CachePot.getInstance().push(ei);

        ft = fm2.beginTransaction();
        mapInfoFragment = new MapInfoFragment();
        ft.replace(R.id.map_info, mapInfoFragment).commit();
        isMarkerClicked = true;

        Log.d("choi","item info: name: "+ei.getName() + "type: "+ei.getType()+ei.getAddr()+ei.getBef());
        return true;
    }

    /**
     * 구글맵의 카메라를 위도와 경도 그리고 줌레벨을 기반으로 이동한다.
     * @param latlng 위도, 경도 객체
     * @param zoomLevel 줌레벨
     */
    private void movePosition(LatLng latlng, float zoomLevel) {
        CameraPosition cp = new CameraPosition.Builder().target((latlng)).zoom(zoomLevel).build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
    }

    /**
     * 사용자가 맵을 클릭했을 때 하단 버튼이 목록보기 상태라면 목록닫기 상태로 변경한다.
     * @param latLng 위도, 경도 객체
     */
    @Override
    public void onMapClick(LatLng latLng) {
//        setInfoList(false);
        if(isMarkerClicked) {
            ft = fm2.beginTransaction();
            ft.remove(mapInfoFragment);
            ft.commit();
            isMarkerClicked = false;
        }
    }

    /**
     * 주어진 정보를 기반으로 충전소 정보를 조회하고 지도에 표시한다.
     * @param latLng 위도, 경도 객체
     * @param distance 거리
     * @param userLatLng 사용자 현재 위도, 경도 객체
     */
    private void listMap( LatLng latLng, int distance, LatLng userLatLng, String select_type) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<EnterpriseInfoItem>> call = remoteService.listMap(latLng.latitude,
                latLng.longitude, distance, userLatLng.latitude, userLatLng.longitude, select_type);
        call.enqueue(new Callback<ArrayList<EnterpriseInfoItem>>() {
            @Override
            public void onResponse(Call<ArrayList<EnterpriseInfoItem>> call,
                                   Response<ArrayList<EnterpriseInfoItem>> response) {
                ArrayList<EnterpriseInfoItem> list = response.body();

                if (list == null) {
                    list = new ArrayList<>();
                }

                if (response.isSuccessful()) {
                    setMap(list);
                    infoList = list;
                } else {
                    MyLog.d(TAG, "not success");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<EnterpriseInfoItem>> call, Throwable t) {
            }
        });
    }

    /**
     * 맛집 리스트를 지도에 표시하는 메소드를 호출하고 지도에서 원을 그린다.
     * @param list 맛집 리스트
     */
    private void setMap(ArrayList<EnterpriseInfoItem> list) {
        if (map != null && list != null) {
            map.clear();
            addMarker(list);
        }
        //adapter.setItemList(list);
        drawCircle(currentLatLng);
    }

    /**
     * 맛집 리스트를 지도에 추가한다.
     * @param list 맛집 리스트
     */
    private void addMarker(ArrayList<EnterpriseInfoItem> list) {
        MyLog.d(TAG, "addMarker list.size() " + list.size());

        if (list == null || list.size() == 0) return;

        for (EnterpriseInfoItem item : list) {
            MyLog.d(TAG, "addMarker " + item);
            if (item.lat != 0 && item.lon != 0) {
                Marker marker = map.addMarker(getMarker(item));

                markerMap.put(marker, item);
            }
        }
    }

    /**
     * ESCInfoItem으로 지도에 표시할 마커를 생성한다.
     * @param item 충전소 정보 아이템 객체
     * @return 지도에 표시할 마커 객체
     */
    private MarkerOptions getMarker(EnterpriseInfoItem item) {
        final MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng(item.lat, item.lon));
        marker.title(item.name);
        marker.draggable(false);

        return marker;
    }


    /**
     * 충전소 마커를 표시할 수 있는 원을 지도에 그린다.
     * @param position 위도, 경도 객체
     */
    private void drawCircle(LatLng position) {
        double radiusInMeters = distanceMeter;
        int strokeColor = 0x440000ff;
        int shadeColor = 0x110000ff;

        CircleOptions circleOptions
                = new CircleOptions().center(position).radius(radiusInMeters)
                .fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(4);
        map.addCircle(circleOptions);
    }

    /**
     * 지도를 움직일 경우 충전소 정보를 조회해서 화면에 표시할 수 있도록 한다.
     */
    @Override
    public void onCameraMove() {
//        showList();
    }

    @Override
    public void onCameraIdle() {
        GeoLib.getInstance().setLastKnownLocation(getActivity());
        showList();
    }

    /**
     * 지도를 일정 레벨 이상 확대했을 경우, 해당 위치에 있는 충전소 리스트를 서버에 요청한다.
     */
    private void showList() {
        currentZoomLevel = (int) map.getCameraPosition().zoom;
        currentLatLng = map.getCameraPosition().target;

        if (currentZoomLevel < Constant.MAP_MAX_ZOOM_LEVEL) {

            map.clear();

            if (zoomGuideToast != null) {
                zoomGuideToast.cancel();
            }
            zoomGuideToast = Toast.makeText(context
                    , getResources().getString(R.string.message_zoom_level_max_over)
                    , Toast.LENGTH_SHORT);
            zoomGuideToast.show();

            return;
        }

        distanceMeter = GeoLib.getInstance().getDistanceMeterFromScreenCenter(map);

        listMap(currentLatLng, distanceMeter, GeoItem.getKnownLocation(), select_type);
    }
}