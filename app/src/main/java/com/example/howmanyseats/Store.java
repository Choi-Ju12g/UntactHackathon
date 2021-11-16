package com.example.howmanyseats;


public class Store {
    private String storeName;
    private String storeAddress;
    private String storeNumber;
    private StoreStruct storeStruct;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(String storeNumber) {
        this.storeNumber = storeNumber;
    }

    public StoreStruct getStoreStruct() {
        return storeStruct;
    }

    public void setStoreStruct(StoreStruct storeStruct) {
        this.storeStruct = storeStruct;
    }
}
