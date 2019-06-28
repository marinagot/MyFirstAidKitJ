package com.example.myfirstaidkit.data;


import java.util.UUID;

/**
 * Clase que establece los nombres a usar en la base de datos
 */

public class FirstAidKit{

    interface  Columnas_User{

        String ID = "id";
        String USERNAME = "username";
        String EMAIL = "email";
        String BIRTHDAY = "birthday";
        String AVATAR = "avatar";
        String PASSWORD = "password";
        String CONFIRMPASSWORD = "confirmPassword";
        String IDKIT = "id_kit";
    }


    interface Columnas_Treatment{

        String ID = "id";
        String IDUSER = "id_user";
        String FRECUENCIA = "frecuencia";
        String FECHAINICIO = "fecha_inicio";
        String FECHAFINAL = "fecha_final";
    }

    interface Columnas_Medicine{

        String ID = "id";
        String MEDICINE_NAME = "medicine_name";
        String IDKIT = "id_kit";
        String MEDICINE_TYPE = "medicine_type";
        String EXPIRATION_DATE = "expiration_date";
        String DOSE_NUMBER = "dose_number";
    }

    interface ColumnasKit{
        String ID = "id";
        String ID_TRAT = "id_tratamiento";
        String ID_MED = "id_medicamento";
    }

    public static class Kit implements ColumnasKit{
        public static String generarIdKit(){
            return "KIT_" + UUID.randomUUID().toString();
        }
    }

    public static class User implements Columnas_User{
        public static String generarIdUser(){
            return "USER_" + UUID.randomUUID().toString();
        }
    }

    public static class Treatment implements Columnas_Treatment{
        public static String generarIdTreatment(){return "TREAT_" + UUID.randomUUID().toString();}
    }

    public static class Medicine implements Columnas_Medicine{
        public static String generarIdMedicine(){return "MED_" + UUID.randomUUID().toString();}
    }

    private FirstAidKit(){

    }
}