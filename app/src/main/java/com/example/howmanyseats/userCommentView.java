package com.example.howmanyseats;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class userCommentView extends LinearLayout {
    ImageButton profile,more_vert;
    TextView username,date,comment;

    public userCommentView(Context context) {
        super(context);
        init(context);
    }

    public userCommentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_board,this,true);

        profile = findViewById(R.id.uc_profile_btn);
        more_vert = findViewById(R.id.uc_more_vert);
        username = findViewById(R.id.uc_username);
        date = findViewById(R.id.uc_date);
        comment = findViewById(R.id.uc_comment);
    }
}
