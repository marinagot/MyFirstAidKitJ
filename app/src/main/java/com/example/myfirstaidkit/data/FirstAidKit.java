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
    }


    interface Columnas_Treatment{

        String ID = "id";
        String NAME = "name";
        String ID_USER = "id_user";
        String FRECUENCIA = "frecuencia";
        String FECHAINICIO = "fecha_inicio";
        String FECHAFINAL = "fecha_final";
    }

    interface Columnas_Medicine{

        String ID = "id";
        String MEDICINE_NAME = "medicine_name";
        String ID_USER = "id_user";
        String MEDICINE_TYPE = "medicine_type";
        String EXPIRATION_DATE = "expiration_date";
        String DOSE_NUMBER = "dose_number";
    }

    interface ColumnasRelacion{
        String ID = "id";
        String ID_TRAT = "id_tratamiento";
        String ID_MED = "id_medicamento";
    }

    public static class Relacion_db implements ColumnasRelacion{
        public static String generarIdKit(){
            return "KIT_" + UUID.randomUUID().toString();
        }
    }

    public static class Users_db implements Columnas_User{
        public static String generarIdUser(){
            return "USER_" + UUID.randomUUID().toString();
        }
    }

    public static class Treatments_db implements Columnas_Treatment{
        public static String generarIdTreatment(){return "TREAT_" + UUID.randomUUID().toString();}
    }

    public static class Medicines_db implements Columnas_Medicine{
        public static String generarIdMedicine(){return "MED_" + UUID.randomUUID().toString();}
    }

    private FirstAidKit(){

    }
}