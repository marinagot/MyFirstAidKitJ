package com.example.myfirstaidkit.data;

import java.util.Date;

public class Medicine {

    Integer id_med;
    String medicine_name;
    Integer idKit;
    String medicine_type;
    Integer dose_number;
    Date expiration_date;

    public Medicine(Integer id_med, String medicine_name, Integer idKit, String medicine_type, Integer dose_number, Date expiration_date) {
        this.id_med = id_med;
        this.medicine_name = medicine_name;
        this.idKit = idKit;
        this.medicine_type = medicine_type;
        this.dose_number = dose_number;
        this.expiration_date = expiration_date;
    }

    public Integer getId_med() {
        return id_med;
    }

    public void setId_med(Integer id_med) {
        this.id_med = id_med;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public Integer getIdKit() {
        return idKit;
    }

    public void setIdKit(Integer idKit) {
        this.idKit = idKit;
    }

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




