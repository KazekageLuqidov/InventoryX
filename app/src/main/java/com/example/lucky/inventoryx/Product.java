package com.example.lucky.inventoryx;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Lucky on 09/29/2017.
 */

public class Product
{
    public String barcode;
    public String date;
    public String discription;
    public String pname;
    public String time;


    public Product()
    {

    }

    public Product(String barcode,String date,String discription,String pname,String time)
    {
        this.barcode=barcode;
        this.date=date;
        this.discription=discription;
        this.pname=pname;
        this.time=time;
    }

    /*
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public void setPname(String pname){
        this.pname = pname;
    }
    public void setTime(String time){
        this.time = time;
    }



    public String getBarcode() {
        return barcode;
    }

    public String getDate() {
    return date;
}

    public String getDiscription() {
        return discription;
    }

    public String getPname() {
        return pname;
    }


    public String getTime() {
        return time;
    }

*/
}
