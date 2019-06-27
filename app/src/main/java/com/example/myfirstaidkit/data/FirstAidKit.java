import java.util.UUID;

/**
 * Clase que establece los nombres a usar en la base de datos
 */

public class FirstAidKit{
    interface  User{
        String idUser = "id_user";
        String username = "username";
        String email = "email";
        String birthday = "birthday";
        String avatar = "avatar";
        String password = "password";
        String confirmPassword "confirmPassword";
        String idKit = "id_kit";
    }


    interface Treatment{
        String idTreatment = "id_treatment";
        String idUser = "id_user";
        String frecuencia = "frecuencia";
        String fechaInicio = "fecha_inicio";
        String fechaFinal = "fecha_final";
    }

    interface Medicine{
        String idMed = "id_med";
        String medicine_name = "medicine_name";
        String idKit = "id_kit";
        String medicine_type = "medicine_type";
        String expiration_date = "expiration_date";
        String dose_number = "dose_number";
    }

    interface Kit{
        String idRelacion = "id_rel";
        String idTratamiento = "id_tratamiento";
        String idMedicamento = "id_medicamento";
    }
}