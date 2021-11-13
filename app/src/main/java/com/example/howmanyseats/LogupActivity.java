package com.example.howmanyseats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogupActivity extends AppCompatActivity {

    EditText editId, editPw;
    Button btnLogin;
    TextView txtResult;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logup);
    }

    public void logup(View view) {

        editId=(EditText)findViewById(R.id.editId);
        editPw=(EditText)findViewById(R.id.editPw);
        String id = editId.getText().toString();
        String pw = editPw.getText().toString();
        String pwConfirm = editPw.getText().toString();

        if(pw.equals(pwConfirm)) {
            firebaseAuth = firebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(id, pw).addOnCompleteListener(LogupActivity.this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Toast.makeText(LogupActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogupActivity.this, MapsActivity.class);
                    startActivity(intent);
                }
            });
        }

        else{
            Toast.makeText(getBaseContext(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT);
        }

    }
}