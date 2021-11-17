package com.example.howmanyseats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity {
    ArrayList<userComment> commentDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        this.initData();

        ListView listView = (ListView) findViewById(R.id.uc_lv_userComment);
        final MyAdapter adapter = new MyAdapter(this,commentDataList);
        listView.setAdapter(adapter);

    }


    public void initData(){
        commentDataList = new ArrayList<userComment>();
        commentDataList.add(new userComment("최정우","1999.06.19","I hate this program mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm",3));
        commentDataList.add(new userComment("이기태","1999.02.23","I want wide room",1));
        commentDataList.add(new userComment("이상윤","1999.05.21","fuck DB",5));
        commentDataList.add(new userComment("고정훈","2000.02.23","why this is wrong",2));
        commentDataList.add(new userComment("최정우","1999.06.19","I hate this program",3));
        commentDataList.add(new userComment("이기태","1999.02.23","I want wide room",1));
//        commentDataList.add(new userComment("이상윤","1999.05.21","fuck DB",5));
//        commentDataList.add(new userComment("고정훈","2000.02.23","why this is wrong",2));
//        commentDataList.add(new userComment("최정우","1999.06.19","I hate this program",3));
//        commentDataList.add(new userComment("이기태","1999.02.23","I want wide room",1));
//        commentDataList.add(new userComment("이상윤","1999.05.21","fuck DB",5));
//        commentDataList.add(new userComment("고정훈","2000.02.23","why this is wrong",2));
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