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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class LogupActivity extends AppCompatActivity {

    EditText editId,editEmail,editName, editPw, editPwConfirm;
    Button btnLogin;
    TextView txtResult;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logup);
    }


    public void logup(View view) {

        editEmail=(EditText)findViewById(R.id.editEmail);
        editName=(EditText)findViewById(R.id.editName);
        editId=(EditText)findViewById(R.id.editId);
        editPw=(EditText)findViewById(R.id.editPw);
        editPwConfirm=(EditText)findViewById(R.id.editPwConfirm);

        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        String id = editId.getText().toString();
        String pw = editPw.getText().toString();
        String pwConfirm = editPwConfirm.getText().toString();

        firebaseAuth = firebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();

        if(pw.equals(pwConfirm) && isValidPasswd() && isValidEmail()) {

            firebaseAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(LogupActivity.this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.d("Sign up", "DocumentSnapshot added on auth");

                        user.put("email",email);
                        user.put("id",id);
                        user.put("name",name);
                        user.put("pw",pw);
                        user.put("type",2);

                        db.collection("member").document(email)
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Sign_up_db", "DocumentSnapshot successfully written!");
                                        Toast.makeText(LogupActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("Sign_up_db", "Error writing document", e);
                                    }
                                });
                    }
                    else{
                        try{
                            task.getResult();
                        }catch (Exception e){
                            e.printStackTrace();
                            Log.w("Sign_up_Auth", "Error adding document", e);
                            Toast.makeText(LogupActivity.this, "오류 발생", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            });
        }
        else{
            Log.v("Sign_up_Valid","Validity does not match");
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

    private boolean isValidName() {

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

    private boolean isValidId(String id) {
        boolean flag;
        db = FirebaseFirestore.getInstance();


        if (editId.getText().toString().equals("")) {
            // 이메일 공백
            Log.v("test","false");
            return false;
        }
        else {  //id 중복처리
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