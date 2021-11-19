package com.example.howmanyseats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.howmanyseats.Geocoding.GeoPointer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity {
    ArrayList<userComment> commentDataList;
    ImageView store_layout;
    TextView storeName, address, phone, type, introduce, totalSeats, name;
    ImageButton back;
    Intent intent;
    FirebaseAuth auth;
    FirebaseFirestore db;
    String[] thing_list;
    int thing_cnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        auth = auth.getInstance();
        intent = getIntent();
        db = FirebaseFirestore.getInstance();
        //텍스트 설정
        storeName = findViewById(R.id.storeName);
        storeName.setText(intent.getStringExtra("storeName"));
        address = findViewById(R.id.address);
        address.setText(intent.getStringExtra("address"));
        phone = findViewById(R.id.phone);
        phone.setText(intent.getStringExtra("phone"));
        type = findViewById(R.id.type);
        type.setText(intent.getStringExtra("type"));
        introduce = findViewById(R.id.introduce);
        introduce.setText(intent.getStringExtra("introduce"));
        totalSeats = findViewById(R.id.totalSeat);
        //totalSeats.setText("남은 자리 " + intent.getStringExtra("totalSeat"));

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        name = findViewById(R.id.name);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //db에서 사용자 이름 찾기
        DocumentReference docRef = db.collection("member").document(auth.getCurrentUser().getEmail().toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) { //
                        name.setText(document.get("name").toString() +"님 후기를 남겨주세요");
                    } else {                  //없을경우

                    }
                } else {
                    Log.d("error", "get failed with ", task.getException());
                }
            }
        });

        // 배치도 그리는 부분
        thing_list = intent.getStringArrayExtra("positionIndex");
        thing_cnt = thing_list.length;
        String[] r = new String[4];
        int key = 0;
        int left_table = 0;
        ArrayList<Thing> thingArrayList = new ArrayList<>();
        for(int v = 0; v<thing_cnt;v++){
            r=customParser(thing_list[v]);
            thingArrayList.add(new Thing(r));
        }


//        for(int v=0; v<thingArrayList.size();v++){
//            Log.d("ssibal","cont"+thingArrayList.get(v).getX()+" "+thingArrayList.get(v).getY()+" "+thingArrayList.get(v).type);
//        }

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.uc_relativeCanvas);
        for(int v=0; v<thingArrayList.size();v++){
            ImageView v1 = new ImageView(getBaseContext());

            String filename = thingArrayList.get(v).getType();
            if(filename.equals("twotable24")){
                v1.setImageResource(R.drawable.twotable24);
                key = 0;
                left_table++;
            }else if(filename.equals("twotable_using24")){
                v1.setImageResource(R.drawable.twotable_using24);
                key = 0;

            }else if(filename.equals("fourtable_using24")){
                v1.setImageResource(R.drawable.fourtable_using24);
                key = 0;

            }else if(filename.equals("fourtable24")){
                v1.setImageResource(R.drawable.fourtable24);
                key = 0;
                left_table++;
            }else if(filename.equals("counter24")){
                v1.setImageResource(R.drawable.counter24);
                key = 1;
            }else if(filename.equals("door24")){
                v1.setImageResource(R.drawable.door24);
                key = 1;
            }else if(filename.equals("toilets24")){
                v1.setImageResource(R.drawable.toilets24);
                key = 1;
            }else if(filename.equals("kiosk24")){
                v1.setImageResource(R.drawable.kiosk24);
                key = 1;
            }

            relativeLayout.addView(v1);
            RelativeLayout.LayoutParams parms = (RelativeLayout.LayoutParams) v1.getLayoutParams();
            parms.leftMargin = thingArrayList.get(v).getX();
            parms.topMargin = thingArrayList.get(v).getY();
            if(key == 0){
                parms.width = 150;
                parms.height= 150;
            }else{
                parms.width = 100;
                parms.height= 100;
            }

            v1.setLayoutParams(parms);
            v1.setBackgroundColor(00000000);

        }
        totalSeats.setText("남은 자리 : " + Integer.toString(left_table));

        //
        // list view 화면 띄우기 addapter 이용
        this.initData();
        ListView listView = (ListView) findViewById(R.id.uc_lv_userComment);
        final MyAdapter adapter = new MyAdapter(this,commentDataList);
        listView.setAdapter(adapter);

        // list view 와 scroll view 화면 겹침 해결
        listView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                listView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    public void initData(){
        commentDataList = new ArrayList<userComment>();
        commentDataList.add(new userComment("최정우","1999.06.19","I hate this program mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm",3));
        commentDataList.add(new userComment("이기태","1999.02.23","I want wide room",1));
        commentDataList.add(new userComment("이상윤","1999.05.21","fuck DB",5));
        commentDataList.add(new userComment("고정훈","2000.02.23","why this is wrong",2));
        commentDataList.add(new userComment("최정우","1999.06.19","I hate this program",3));
        commentDataList.add(new userComment("이기태","1999.02.23","I want wide room",1));
        commentDataList.add(new userComment("이상윤","1999.05.21","fuck DB",5));
        commentDataList.add(new userComment("고정훈","2000.02.23","why this is wrong",2));
        commentDataList.add(new userComment("최정우","1999.06.19","I hate this program",3));
        commentDataList.add(new userComment("이기태","1999.02.23","I want wide room",1));
        commentDataList.add(new userComment("이상윤","1999.05.21","fuck DB",5));
        commentDataList.add(new userComment("고정훈","2000.02.23","why this is wrong",2));
    }

    // a 는 fourtable,x좌표,y좌표,사용여부
    public String[] customParser(@NonNull String a){
        String[] info = new String[4];
        info = a.split(",");
        return info;
    }

    public int dpToPx(int dp){
        int px=0;
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);
        float density = getResources().getDisplayMetrics().density;

        px = Math.round(dp*density);

        return px;
    }

    public int Pxtodp(int px){
        int dp = 0;

        return dp;
    }


}

class MyAdapter extends BaseAdapter{
    ArrayList<userComment> comments;
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;

    public MyAdapter(Context c,ArrayList<userComment> data){
        mContext = c;
        comments = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void addItem(userComment comment){
        comments.add(comment);
    }
    @Override
    public int getCount() {
        //return 3;
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.user_comment,null);

        ImageButton profile,more_vert;
        TextView username,date,comment;
        profile = (ImageButton)view.findViewById(R.id.uc_profile_btn);
        more_vert =(ImageButton)view.findViewById(R.id.uc_more_vert);
        username = (TextView) view.findViewById(R.id.uc_username);
        date = (TextView) view.findViewById(R.id.uc_date);
        comment = (TextView) view.findViewById(R.id.uc_comment);

        //profile.setImageDrawable(comments.get(position));
        //more_vert.setImageDrawable(comments.get(position));
        username.setText(comments.get(position).getUser_name());
        date.setText(comments.get(position).getDate());
        comment.setText(comments.get(position).getComment());

        return view;
    }
}