package com.example.myfirstaidkit.data;

import android.arch.persistence.room.Entity;

import java.util.Date;

@Entity(primaryKeys = {"id"})
public class Medicine {

    private String id;
    private String name;
    private String idUser;
    private String type;
    private Integer doseNumber;
    private Date expirationDate;

    public Medicine() {

    }

    public Medicine(String id, String name, String idUser, String type, Integer doseNumber, Date expirationDate) {
        this.id = id;
        this.name = name;
        this.idUser = idUser;
        this.type = type;
        this.doseNumber = doseNumber;
        this.expirationDate = expirationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDoseNumber() {
        return doseNumber;
    }

    public void setDoseNumber(Integer doseNumber) {
        this.doseNumber = doseNumber;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}




