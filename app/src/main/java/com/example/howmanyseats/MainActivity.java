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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.auth.AuthState;

public class MainActivity extends AppCompatActivity{
    //for test
    private ArrayList<String> names;
    private SearchAdapter sa;
    ArrayList<String> list;


    //뷰
    private MapView mapView;
    private static final String TAG = "Main_Activity";
    private ImageView ivMenu;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private EditText searchText;
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
        setContentView(R.layout.activity_main);

        ivMenu = findViewById(R.id.iv_menu);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation);
        searchText =findViewById(R.id.search);
        firebaseAuth=firebaseAuth.getInstance();
        Menu menu = navigationView.getMenu();

        FirebaseUser user = firebaseAuth.getCurrentUser();


        /////////////
        names = new ArrayList<>();
        list = new ArrayList<>();
        names.add("이상윤");
        names.add("이기태");
        names.add("최정우");
        names.add("고정훈");

        ListView listView = findViewById(R.id.listView);
        sa = new SearchAdapter(this, list);

        /////////
        //액션바 변경하기(들어갈 수 있는 타입 : Toolbar type
        setSupportActionBar((androidx.appcompat.widget.Toolbar) toolbar);

        //메뉴바 클릭시 메뉴 표시
        ivMenu.setOnClickListener(new View.OnClickListener() {                       //메뉴 터치시 메뉴 드로어 열림

            @Override
            public void onClick(View v) {
                //유저가 로그인 되어있을 경우
                if(firebaseAuth.getCurrentUser() != null){
                    MenuItem first = menu.findItem(R.id.action_login);
                    first.setTitle("프로필");
                    first.setIcon(R.drawable.profile);
                    MenuItem second = menu.findItem(R.id.action_logup);
                    second.setTitle("로그아웃");
                    second.setIcon(R.drawable.sign_out);
                }
                //비로그인인 경우
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

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = searchText.getText().toString();
                search(text);
                listView.setAdapter(sa);
            }
        });


        //맵 프래그먼트 화면에 출력
        MapFragment map = new MapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainerView, map).commit();
    }

    public void search(String charText) {
        list.clear();
        // 문자 입력이 없을때는 없음
        if (charText.length() == 0) {

        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < names.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (names.get(i).toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    Log.v("search",names.get(i));
                    list.add(names.get(i));
                }
            }
        }

        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        sa.notifyDataSetChanged();
    }

}
