package com.example.myfirstaidkit.data;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;


@Entity(primaryKeys = {"id"})
public class Medicine {

    @SerializedName("_id")

    private String id;
    private String name;
    @SerializedName("user_id")
    private String idUser;
    private String type;
    @SerializedName("dose_number")
    private Integer doseNumber;
    @SerializedName("expiry_date")
    private Long expirationDate;

    public Medicine() {

    }

    public Medicine(String id, String name, String idUser, String type, Integer doseNumber, Long expirationDate) {
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

    public Long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}




