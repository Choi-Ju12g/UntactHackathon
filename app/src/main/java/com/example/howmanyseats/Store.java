package com.example.howmanyseats;


import java.util.List;

public class Store {
    private String address;
    private String detailAddress;
    private String businessName;
    private String phone;
    private String storeName;
    private StoreStruct storeStruct;
    private List<String> positionIndex;
    private Long totalSeat;
    private String type;

    public List<String> getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(List<String> positionIndex) {
        this.positionIndex = positionIndex;
    }

    public Long getTotalSeat() {
        return totalSeat;
    }

    public void setTotalSeat(Long totalSeat) {
        this.totalSeat = totalSeat;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress() {
        return address;
    }

    public void setStoreAddress(String address) {
        this.address = address;
    }

    public StoreStruct getStoreStruct() {
        return storeStruct;
    }

    public void setStoreStruct(StoreStruct storeStruct) {
        this.storeStruct = storeStruct;
    }

    public void addNull(){
        String address = "not real address";
        String detailAddress;
        String businessName;
        String phone;
        String storeName;
        StoreStruct storeStruct;
        List<String> positionIndex;
        Long totalSeat;
        String type;
    }
}
