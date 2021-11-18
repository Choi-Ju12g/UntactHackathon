package com.example.howmanyseats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.naver.maps.map.widget.LocationButtonView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.auth.AuthState;

public class MainActivity extends AppCompatActivity{

    //search 리스트, 어댑터
    private ArrayList<String> names;
    private ArrayList<Store> stores;
    private SearchAdapter sa;
    private ArrayList<String> list;

    //뷰
    private MapView mapView;
    private MapFragment map;
    private static final String TAG = "Main_Activity";
    private ImageView ivMenu;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private EditText searchText;

    //파이어베이스
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    //네이버 맵


    //네이버 맵
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;

    //맵에 표시할 주소들


    @RequiresApi(api = Build.VERSION_CODES.N)
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

        user = firebaseAuth.getCurrentUser();

        //db에서 store가져오기
        db = FirebaseFirestore.getInstance();
        /////////////
        stores = new ArrayList<>();
        names = new ArrayList<>();//식당이름들 db에서 받아와야함
        list = new ArrayList<>();//검색된 리스트 뷰에 추가할 리스트


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
                    MenuItem third = menu.findItem(R.id.action_test);
                    third.setIcon(R.drawable.ic_baseline_person_add_24);
                    third.setTitle("test");
                }
                //비로그인인 경우
                else{
                    MenuItem first = menu.findItem(R.id.action_login);
                    first.setTitle("로그인");
                    first.setIcon(R.drawable.ic_baseline_login_24);
                    MenuItem second = menu.findItem(R.id.action_logup);
                    second.setIcon(R.drawable.ic_baseline_person_add_24);
                    second.setTitle("회원가입");
                    MenuItem third = menu.findItem(R.id.action_test);
                    third.setIcon(R.drawable.ic_baseline_person_add_24);
                    third.setTitle("test");
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

                } else if (id == R.id.action_test){
                    Intent intent = new Intent(getBaseContext(), BoardActivity.class);
                    startActivity(intent);
                }
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });




        //맵 프래그먼트 화면에 출력
        map = new MapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainerView, map).commit();

        //async
        db.collection("store")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                stores.add(document.toObject(Store.class));
                                names.add(document.toObject(Store.class).getStoreName());
                            }

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
                        } else {
                            Store s = new Store();
                            s.addNull();
                            stores.add(s);
                        }
                    }
                });
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
