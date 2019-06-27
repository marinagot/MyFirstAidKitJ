import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;

import src.main.java.com.example.myfirstaidkit.data.FirstAidKit.User;
import src.main.java.com.example.myfirstaidkit.data.FirstAidKit.Medicine;
import src.main.java.com.example.myfirstaidkit.data.FirstAidKit.Treatment;
import src.main.java.com.example.myfirstaidkit.data.FirstAidKit.Kit;

public class BaseDatos extends SQLiteHelper {

    private static final String NOMBRE_BASE_DATOS = "Medicine.db";
    private static final int VERSION_ACTUAL = 1.1;
    private final Context contexto;

    interface Tablas {
        String USER = "user";
        String MEDICINE = "medicine";
        String TREATMENT = "treatment";
        String KIT = "kit";
    }

    interface Referencias {
        String ID_USER = String.format("REFERENCES %s(%s) ON DELETE CASCADE", Tablas.USER, User.ID_USER);
        String ID_MEDICINE = String.format("REFERENCES %s(%s)", Tablas.MEDICINE, Medicine.ID_MEDICINE);
        String ID_TREATMENT = String.format("REFERENCES %s(%s)", Tablas.TREATMENT, Treatment.ID_TREATMENT);
        String ID_KIT = String.format(" REFERENCES %s(%s)", Tablas.KIT, Kit.ID_KIT);

    }

    public BaseDatos
}