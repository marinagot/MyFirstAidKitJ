package com.example.myfirstaidkit.data;

import android.arch.persistence.room.Entity;

import java.util.Date;

@Entity(primaryKeys = {"id"})
public class Treatment{

    public Integer id;
    public Integer idUser;
    public String name;


    public Treatment() {

    }

    public Treatment(Integer id, Integer idUser, String name) {
        this.id = id;
        this.idUser = idUser;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}