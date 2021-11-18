package com.example.howmanyseats;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.howmanyseats.DB.FirestoreStoreDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.Vector;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {


    private MapView mapView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private FirebaseFirestore db;
    private Vector<Store> list;

    public MapFragment() {
    }

    public static MapFragment newInstance()
    {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE); //현재위치 반환 구현체
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_map,
                container, false);

        mapView = (MapView) rootView.findViewById(R.id.navermap);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap)
    {
        this.naverMap = naverMap;
        UiSettings settings = naverMap.getUiSettings();

        //현재위치 설정
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        settings.setLocationButtonEnabled(true);
        //배경 지도 선택
        naverMap.setMapType(NaverMap.MapType.Basic);

        //건물 표시
        naverMap.setLayerGroupEnabled(naverMap.LAYER_GROUP_BUILDING, true);

        //db의 상점들 출력

        Vector<Store> list = new Vector<>();

        class BackgroundTask extends AsyncTask<Vector<Store> , Store, Vector<Store>> {

            private int cnt = 0;

            public BackgroundTask() {
                super();
            }

            @Override
            protected void onCancelled(Vector<Store> stores) {
                super.onCancelled(stores);
            }

            @Override
            protected Vector<Store> doInBackground(Vector<Store>... vectors) {

                db.collection("store")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        vectors[0].add(document.toObject(Store.class));
                                        publishProgress(vectors[0].get(vectors[0].size()-1));
                                    }
                                } else {
                                    Store s = new Store();
                                    s.addNull();
                                    vectors[0].add(s);
                                }
                            }
                        });

                return vectors[0];
            }

            protected void onPreExecute() {

            }

            @Override
            protected void onProgressUpdate(Store ... values) {
                cnt++;
                Log.v("marker",String.valueOf(cnt) + ": " + values[0].getAddress().toString());

            }

            @Override
            protected void onPostExecute(Vector<Store> stores) {
                super.onPostExecute(stores);
                Log.v("finish", String.valueOf(stores.size()));
                for(int i = 0; i < stores.size(); i++){
                    Log.v("finish", stores.get(i).getAddress().toString());
                }
            }

            protected void onCancelled() {
            }
        }

        BackgroundTask bt = new BackgroundTask();
        bt.execute(list);

    }

    @Override
    public void onStart()
    {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void createMarker(LatLng loc){
        Marker marker = new Marker();
        marker.setPosition(loc);
        marker.setMap(this.naverMap);
    }
}