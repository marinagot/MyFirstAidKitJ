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

    private static final String NOMBRE_BASE_DATOS = "FirstAidKit.db";
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

    public BaseDatos {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        this.contexto = contexto;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }
    @Override

    public void onCreate(SQliteDatabase db){
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT UNIQUE NOT NULL,%s DATETIME NOT NULL,%s TEXT NOT NULL %s," +
                "%s TEXT NOT NULL %s)",
                Tablas.TREATMENT, BaseColumns._ID,


    }
}
