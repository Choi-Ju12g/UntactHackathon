package com.example.howmanyseats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.howmanyseats.Geocoding.GeoPointer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    //search 리스트, 어댑터
    private ArrayList<Store> stores;
    private SearchAdapter sa;
    private ArrayList<Store> list;

    //뷰
    private MapFragment map;
    private ImageView ivMenu;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private EditText searchText;
    private TextView blank;
    //파이어베이스
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        else{

        }
    }
    @Override
    public void onPause() {
        super.onPause();
        map.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //다시 메인화면으로 왔을때 맵 초기화 db에서 값 다시가져오게 하기
        map = new MapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainerView, map).commit();

    }

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
        blank = findViewById(R.id.blank);
        blank.bringToFront();
        navigationView.bringToFront();
        searchText.bringToFront();


        firebaseAuth=firebaseAuth.getInstance();
        Menu menu = navigationView.getMenu();


        //db에서 store가져오기
        db = FirebaseFirestore.getInstance();
        /////////////
        stores = new ArrayList<>();
        list = new ArrayList<>();//검색된 리스트 뷰에 추가할 리스트


        ListView listView = findViewById(R.id.listView);
        listView.bringToFront();
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

                                    if(list.size() != 0){
                                        blank.setVisibility(View.VISIBLE);
                                        GeoPointer.OnGeoPointListener listener = new GeoPointer.OnGeoPointListener() {
                                            @Override
                                            public void onPoint(GeoPointer.Point[] p) {
                                                int sCnt = 0, fCnt = 0;
                                                for (GeoPointer.Point point : p) {
                                                    if (point.havePoint) sCnt++;
                                                    else fCnt++;
                                                    CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(point.getY(),point.getX()));
                                                    map.cameraUpdate(cameraUpdate);
                                                }
                                                Log.d("TEST_CODE", String.format("성공 : %s, 실패 : %s", sCnt, fCnt));
                                            }

                                            @Override
                                            public void onProgress(int progress, int max) {
                                                Log.d("TEST_CODE", String.format("좌표를 얻어오는중 %s / %s", progress, max));
                                            }
                                        };
                                        GeoPointer geoPointer = new GeoPointer(getBaseContext(), listener);
                                        geoPointer.execute(list.get(0).getAddress());
                                    }
                                    else{
                                        blank.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    if(firebaseAuth.getCurrentUser() != null) {

                                        Intent intent = new Intent(getBaseContext(), BoardActivity.class);
                                        Store s = list.get(i);

                                        intent.putExtra("address", s.getAddress());
                                        intent.putExtra("buisnessName", s.getBusinessName());
                                        intent.putExtra("detailAddress", s.getDetailAddress());
                                        intent.putExtra("id", s.getId());
                                        intent.putExtra("introduce", s.getIntroduce());
                                        intent.putExtra("phone", s.getPhone());
                                        intent.putExtra("positionIndex", s.getPositionIndex());
                                        intent.putExtra("storeName", s.getStoreName());
                                        intent.putExtra("totalSeat", s.getTotalSeat().toString());
                                        intent.putExtra("type", s.getType());
                                        Log.v("total Seat : ", s.getTotalSeat().toString());

                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(getBaseContext(), "로그인이 필요한 서비스입니다.", Toast.LENGTH_SHORT).show();
                                    }

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
            for(int i = 0;i < stores.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (stores.get(i).getStoreName().toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    Log.v("search",stores.get(i).getStoreName());
                    list.add(stores.get(i));
                }
            }
        }

        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        sa.notifyDataSetChanged();
    }

}
