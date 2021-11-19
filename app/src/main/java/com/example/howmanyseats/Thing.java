package com.example.howmanyseats;

import android.util.DisplayMetrics;
import android.view.Display;
import java.util.ArrayList;

public class Thing {

    int x,y;
    String type;


    Thing(int x, int y, String type){
        this.x = x;
        this.y = y;
        this.type = type;
    }

    Thing(String[] a){
        if(a[0].equals("twoTable")){
            if(a[3].equals("0")){
                this.type = "twotable24";
            }else{
                this.type = "twotable_using24";
            }
        }else if(a[0].equals("fourTable")){
            if(a[3].equals("0")){
                this.type ="fourtable24";
            }else{
                this.type ="fourtable_using24";
            }
        }else if(a[0].equals("counter") && a[3].equals("0")){
            this.type ="counter24";
        }else if(a[0].equals("door")&&a[3].equals("0")){
            this.type ="door24";
        }else if(a[0].equals("wc")&&a[3].equals("0")){
            this.type ="toilets24";
        }else if(a[0].equals("now")&&a[3].equals("0")){
            this.type ="kiosk24";
        }else{
            this.type = null;
        }
        this.x = Integer.parseInt(a[1])-220;
        this.y = Integer.parseInt(a[2])-100;
    }


    public String  getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void connect_view(){

    }

    public void set_ratio_xy(float ratio){
        this.x *= ratio;
        this.y *= ratio;
    }

}
