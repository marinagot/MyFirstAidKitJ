package com.example.myfirstaidkit.data;


import java.util.UUID;

/**
 * Clase que establece los nombres a usar en la base de datos
 */

public class FirstAidKit{

    interface  Column_User{

        String ID = "id";
        String USERNAME = "username";
        String EMAIL = "email";
        String BIRTHDAY = "birthday";
        String AVATAR = "avatar";
        String PASSWORD = "password";
    }


    interface Column_Treatment{

        String ID = "id";
        String NAME = "name";
        String ID_USER = "id_user";
        String FREQUENCY = "frequency";
        String INITIALDATE = "initialDate";
        String FINALDATE = "finalDate";
    }

    interface Column_Medicine{

        String ID = "id";
        String NAME = "name";
        String ID_USER = "id_user";
        String TYPE = "type";
        String EXPIRATION_DATE = "expiration_date";
        String DOSE_NUMBER = "dose_number";
    }

    interface Column_Med_Tret_Rel{
        String ID = "id";
        String ID_TRAT = "id_tratamiento";
        String ID_MED = "id_medicamento";
    }

    public static class Med_Tret_Rel_db implements Column_Med_Tret_Rel{
        public static String createIdRel(){
            return "REL_" + UUID.randomUUID().toString();
        }
    }

    public static class Users_db implements Column_User{
        public static String createIdUser(){
            return "USER_" + UUID.randomUUID().toString();
        }
    }

    public static class Treatments_db implements Column_Treatment{
        public static String createIdTreatment(){return "TREAT_" + UUID.randomUUID().toString();}
    }

    public static class Medicines_db implements Column_Medicine{
        public static String createIdMedicine(){return "MED_" + UUID.randomUUID().toString();}
    }

    private FirstAidKit(){

    }
}