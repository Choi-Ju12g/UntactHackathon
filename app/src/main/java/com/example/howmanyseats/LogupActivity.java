package com.example.howmanyseats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LogupActivity extends AppCompatActivity {

    EditText editId,editEmail,editName, editPw, editPwConfirm;
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

        if(pw.equals(pwConfirm) && isValidPasswd() && isValidEmail() && isValidName()) {

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
                                        Toast.makeText(LogupActivity.this, "???????????? ??????", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(LogupActivity.this, "?????? ??????", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            });
        }
        else{
            Log.v("Sign_up_Valid","Validity does not match");
            Toast.makeText(getBaseContext(),"??????????????? ?????????????????????. (???????????? ????????????, ????????? ?????? ?????? ??????)",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidEmail() {
        if (editId.getText().toString().equals("")) {
            // ????????? ??????
            return false;
        }
        else {
            return true;
        }
    }

    private boolean isValidName() {

        if (editId.getText().toString().equals("")) {
            // ????????? ??????
            return false;
        }
        else {
            return true;
        }
    }

    // ???????????? ????????? ??????
    private boolean isValidPasswd() {
        if (editPw.getText().toString().equals("")) {
            // ???????????? ??????
            return false;
        }
        else {
            return true;
        }
    }
}