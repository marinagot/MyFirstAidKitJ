import java.util.UUID;

/**
 * Clase que establece los nombres a usar en la base de datos
 */

public class FirstAidKit{
    interface  ColumnasUser{
        String id = "id";
        String username = "username";
        String email = "email";
        String birthday = "birthday";
        String avatar = "avatar";
        String password = "password";
        String confirmPassword "confirmPassword";
        String idKit = "id_kit";
    }


    interface ColumnasTreatment{
        String id = "id";
        String idUser = "id_user";
        String frecuencia = "frecuencia";
        String fechaInicio = "fecha_inicio";
        String fechaFinal = "fecha_final";
    }

    interface Medicine{
        String id = "id";
        String medicine_name = "medicine_name";
        String idKit = "id_kit";
        String medicine_type = "medicine_type";
        String expiration_date = "expiration_date";
        String dose_number = "dose_number";
    }

    interface Kit{
        String id = "id";
        String idTratamiento = "id_tratamiento";
        String idMedicamento = "id_medicamento";
    }

    public static class FirstAidKit implements Kit{
        public static String generarIdKit(){
            return "kit_" + UUID.randomUUID().toString();
        }
    }

    public static class FirstAidKit implements User{
        public static String generarIdUser(){
            return "user_" + UUID.randomUUID().toString();
        }
    }

    public static class FirstAidKit implements Treatment{
        public static String generarIdTreatment(){
            return "treat_" + UUID.randomUUID().toString();
        }
    }

    public static class FirstAidKit implements Medicine{
        public static String generarIdMedicine(){
            return "med_" + UUID.randomUUID().toString();
        }
    }

    private FirstAidKit(){

    }
}