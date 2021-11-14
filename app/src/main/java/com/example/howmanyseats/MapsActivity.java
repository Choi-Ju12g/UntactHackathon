package com.example.howmanyseats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import cz.msebera.android.httpclient.auth.AuthState;

public class MapsActivity<Toolbar> extends AppCompatActivity implements OnMapReadyCallback {
    //뷰
    private MapView mapView;
    private static final String TAG = "Main_Activity";
    private ImageView ivMenu;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    //파이어베이스
    private FirebaseAuth firebaseAuth;
    //네이버 맵
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        mapView = findViewById(R.id.map_view);
        ivMenu = findViewById(R.id.iv_menu);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation);
        firebaseAuth=firebaseAuth.getInstance();
        Menu menu = navigationView.getMenu();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        //액션바 변경하기(들어갈 수 있는 타입 : Toolbar type
        setSupportActionBar((androidx.appcompat.widget.Toolbar) toolbar);

        ivMenu.setOnClickListener(new View.OnClickListener() {                       //메뉴 터치시 메뉴 드로어 열림

            @Override
            public void onClick(View v) {
                if(firebaseAuth.getCurrentUser() != null){
                    MenuItem first = menu.findItem(R.id.action_login);
                    first.setTitle("프로필");
                    first.setIcon(R.drawable.profile);
                    MenuItem second = menu.findItem(R.id.action_logup);
                    second.setTitle("로그아웃");
                    second.setIcon(R.drawable.sign_out);
                }
                else{
                    MenuItem first = menu.findItem(R.id.action_login);
                    first.setTitle("로그인");
                    first.setIcon(R.drawable.ic_baseline_login_24);
                    MenuItem second = menu.findItem(R.id.action_logup);
                    second.setIcon(R.drawable.ic_baseline_person_add_24);
                    second.setTitle("회원가입");
                }
                    drawerLayout.openDrawer(GravityCompat.START);
            }
        });



        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);             //메뉴바 안의 항목들 터치리스너
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_login) {
                    if(item.getTitle().equals("로그인")){
                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                    else if(item.getTitle().equals("프로필")){
                        Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
                        startActivity(intent);
                    }

                } else if (id == R.id.action_logup) {
                    if(item.getTitle().equals("회원가입")){
                        Intent intent = new Intent(getBaseContext(), LogupActivity.class);
                        startActivity(intent);
                    }
                    else if(item.getTitle().equals("로그아웃")){
                        firebaseAuth.signOut();
                        Toast.makeText(getApplicationContext(), "로그아웃", Toast.LENGTH_SHORT).show();
                    }

                }
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,  @NonNull int[] grantResults) {
//        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
//            if (!locationSource.isActivated()) { // 권한 거부됨
//                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
//            }
//            else{
//                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
//            }
//            return;
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

}
