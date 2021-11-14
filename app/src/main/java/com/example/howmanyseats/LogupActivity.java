package com.example.howmanyseats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogupActivity extends AppCompatActivity {

    EditText editId, editPw, editPwConfirm;
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
        editPwConfirm=(EditText)findViewById(R.id.editPwConfirm);
        String id = editId.getText().toString();
        String pw = editPw.getText().toString();
        String pwConfirm = editPwConfirm.getText().toString();

        if(pw.equals(pwConfirm) && isValidPasswd() && isValidEmail()) {

            firebaseAuth = firebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(id, pw).addOnCompleteListener(LogupActivity.this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LogupActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        try{
                            task.getResult();
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(LogupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }


                }
            });
        }
        else{
            Log.v("error","회원가입 실패");
            Toast.makeText(getBaseContext(),"회원가입에 실패하였습니다. (비밀번호 일치여부, 이메일 공백 여부 확인)",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidEmail() {
        if (editId.getText().toString().equals("")) {
            // 이메일 공백
            Log.v("test","false");
            return false;
        }
        else {
            Log.v("test","true");
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (editPw.getText().toString().equals("")) {
            // 비밀번호 공백
            Log.v("test","false");
            return false;
        }
        else {
            Log.v("test","true");
            return true;
        }
    }
}