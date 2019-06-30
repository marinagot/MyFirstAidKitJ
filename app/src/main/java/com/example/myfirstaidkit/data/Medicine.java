package com.example.myfirstaidkit.data;

import android.arch.persistence.room.Entity;

import java.util.Date;
@Entity(primaryKeys = {"id"})
public class Medicine {

    public Integer id;
    public String medicine_name;
    public Integer idUser;
    public String medicine_type;
    public Integer dose_number;
    public Date expiration_date;

    public Medicine() {}

    public Medicine(Integer id, String medicine_name, Integer idUser, String medicine_type, Integer dose_number, Date expiration_date) {

        this.id = id;
        this.medicine_name = medicine_name;
        this.idUser = idUser;
        this.medicine_type = medicine_type;
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
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public Integer getIdUser() {return idUser;}

    public void setIdUser(Integer idUser) {this.idUser = idUser;}

    public String getMedicine_type() {
        return medicine_type;
    }

    public void setMedicine_type(String medicine_type) {
        this.medicine_type = medicine_type;
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




