package com.example.howmanyseats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cz.msebera.android.httpclient.impl.execchain.MainClientExec;


public class LoginActivity extends AppCompatActivity {
    EditText editId, editPw;
    Button btnLogin;
    TextView txtResult;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    public void login(View view) {

        editId=(EditText)findViewById(R.id.editId);
        editPw=(EditText)findViewById(R.id.editPw);

        String id = editId.getText().toString();
        String pw = editPw.getText().toString();

        firebaseAuth = firebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(id, pw)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {//성공했을때
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {//실패했을때
                            try{
                                task.getResult();
                            }catch(Exception e){
                                e.printStackTrace();
                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

}