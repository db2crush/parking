package com.yalo.erunn.parking;

/**
 * Created by erunn on 2017-06-04.
 */

public class Parking {
    public String name;
    String roadAddress;
    String address;
    int quantity;
    String free;



    public Parking(String name, String roadAddress, String address, int quantity, String free) {
        this.name = name;
        this.roadAddress= roadAddress;
        this.address = address;
        this.quantity = quantity;
        this.free = free;

    };

    public void setName(String name){this.name=name;};
    public void setRoadAddress(String address){this.roadAddress=address;};
    public void setQuantity(int quantity){this.quantity=quantity;};
    public void setFree(String free){this.free=free;};

    public String getName(){return this.name;};
    public String getRoadAddress(){return this.roadAddress;};
    public String getAddress(){return this.address;};
    public int getQuantity(){return this.quantity;};
    public String getFree(){return this.free;};
}
