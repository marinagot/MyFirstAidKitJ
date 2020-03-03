package com.example.myfirstaidkit.data;


import java.util.UUID;

/**
 * Clase que establece los nombres a usar en la base de datos
 */

public class FirstAidKit{

    interface ColumnUser {
        String ID = "id";
        String USERNAME = "username";
        String EMAIL = "email";
        String PASSWORD = "password";
        String SYNC_ID = "sync_id";
    }


    interface ColumnTreatment {
        String ID = "id";
        String NAME = "name";
        String ID_USER = "id_user";
    }

    interface ColumnMedicine {
        String ID = "id";
        String NAME = "name";
        String ID_USER = "id_user";
        String TYPE = "type";
        String EXPIRATION_DATE = "expiration_date";
        String DOSE_NUMBER = "dose_number";
    }

    interface ColumnMedTretRel {
        String ID = "id";
        String ID_TRAT = "id_tratamiento";
        String ID_MED = "id_medicamento";
        String FREQUENCY = "frequency";
        String INITIAL_DATE = "initialDate";
        String FINAL_DATE = "finalDate";
    }

    interface ColumnHour {
        String ID = "id";
        String ID_REL = "id_relacion";
        String HOUR = "hour";
    }

    public static class MedTretRelDb implements ColumnMedTretRel {
        public static String createIdRel(){
            return "REL_" + UUID.randomUUID().toString();
        }
    }

    public static class UsersDb implements ColumnUser {
        public static String createIdUser(){
            return "USER_" + UUID.randomUUID().toString();
        }
    }

    public static class HoursDb implements ColumnHour {
        public static String createIdHour(){
            return "HOURS_" + UUID.randomUUID().toString();
        }
    }

    public static class TreatmentsDb implements ColumnTreatment {
        public static String createIdTreatment(){
            return "TREAT_" + UUID.randomUUID().toString();
        }
    }

    public static class MedicinesDb implements ColumnMedicine {
        public static String createIdMedicine(){
            return "MED_" + UUID.randomUUID().toString();
        }
    }

    private FirstAidKit(){

    }
}