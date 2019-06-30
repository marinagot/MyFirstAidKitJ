package com.example.myfirstaidkit.data;

import android.arch.persistence.room.Entity;

import java.util.Date;

@Entity(primaryKeys = {"id"})
public class Medicine {

    public Integer id;
    public String name;
    public Integer idUser;
    public String type;
    public Integer dose_number;
    public Date expiration_date;

    public Medicine() {

    }

    public Medicine(Integer id,String name, Integer idUser, String type, Integer dose_number, Date expiration_date) {

        this.id = id;
        this.name = name;
        this.idUser = idUser;
        this.type = type;
        this.dose_number = dose_number;
        this.expiration_date = expiration_date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMedicine_name() {
        return name;
    }

    public void setMedicine_name(String name) {
        this.name = name;
    }

    public Integer getIdUser() {return idUser;}

    public void setIdUser(Integer idUser) {this.idUser = idUser;}

    public String getMedicine_type() {
        return type;
    }

    public void setMedicine_type(String medicine_type) {
        this.type = type;
    }

    public Integer getDose_number() {
        return dose_number;
    }

    public void setDose_number(Integer dose_number) {
        this.dose_number = dose_number;
    }

    public Date getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(Date expiration_date) {
        this.expiration_date = expiration_date;
    }
}




