package com.example.howmanyseats.DB;

import com.example.howmanyseats.Store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public interface StoreDB {
    public Vector<Store> getAllStore();
    public ArrayList<Store> findStoreByName(String storeName);
    public ArrayList<Store> findStoreByAddress(String address);
}
