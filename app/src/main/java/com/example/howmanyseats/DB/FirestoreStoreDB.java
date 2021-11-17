package com.example.howmanyseats.DB;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.howmanyseats.Store;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class FirestoreStoreDB implements StoreDB{

    private FirebaseFirestore db;


    public FirestoreStoreDB(){
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public Vector<Store> getAllStore() {
        Vector<Store> list = new Vector<>();

        db.collection("store")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list.add(document.toObject(Store.class));
                            }
                        } else {
                            Store s = new Store();
                            s.addNull();
                            list.add(s);
                        }
                    }
                });
        return list;
    }

    @Override
    public ArrayList<Store> findStoreByName(String storeName) {
        ArrayList<Store> st = new ArrayList<>();


        return st;
    }

    @Override
    public ArrayList<Store> findStoreByAddress(String address) {
        ArrayList<Store> list = new ArrayList<>();

        db.collection("store")
                .whereEqualTo("address", address)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list.add(document.toObject(Store.class));

                            }
                        } else {
                            Store s = new Store();
                            s.addNull();
                            list.add(s);
                        }
                    }
                });

        return list;
    }
}
