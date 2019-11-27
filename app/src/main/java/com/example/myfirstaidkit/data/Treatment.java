package com.example.myfirstaidkit.data;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = {"id"})
public class Treatment{

    @SerializedName("_id")
    private String id;
    @SerializedName("user_id")
    private String idUser;
    private String name;


    public Treatment() {

    }

    public Treatment(String id, String idUser, String name) {
        this.id = id;
        this.idUser = idUser;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}