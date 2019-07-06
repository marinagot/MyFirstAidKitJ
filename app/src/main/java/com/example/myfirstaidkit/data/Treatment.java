package com.example.myfirstaidkit.data;

import android.arch.persistence.room.Entity;

import java.util.Date;

@Entity(primaryKeys = {"id"})
public class Treatment{

    private long id;
    private long idUser;
    private String name;


    public Treatment() {

    }

    public Treatment(long id, long idUser, String name) {
        this.id = id;
        this.idUser = idUser;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}