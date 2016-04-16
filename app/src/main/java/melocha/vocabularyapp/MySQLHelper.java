package melocha.vocabularyapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class MySQLHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public  static  final  String DATABASE_NAME = "VocabularyDB";
    //tables names
    private static final String NAME_TABLE = "TableOne";

    //tables columns names
    private  static final String ID = "id";
    private  static final String KEY_ENG = "engWord";

    private  static final String KEY_RUS = "rusWord";

    private static final String[]  COLUMNS = {ID, KEY_ENG, KEY_RUS};


    public MySQLHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + NAME_TABLE + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_ENG + " TEXT, " + KEY_RUS + " TEXT )";
        // create table
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
            // Enable foreign key constraints
            //db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + NAME_TABLE);
        // create fresh table
        this.onCreate(db);
    }

    public  void addEngRusPair(EngRusPair engRusPair){
        SQLiteDatabase db = this.getWritableDatabase();

        //если таблица уже задержит такое англ слова - спросить о перезаписи
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ENG, engRusPair.getEngWord());
        contentValues.put(KEY_RUS, engRusPair.getEngWord());
        //int id = Integer.parseInt(String.valueOf(db.insert(NAME_TABLE, null, contentValues)));
        db.insert(NAME_TABLE, null, contentValues);
        db.close();
        Log.d("addEngRusPair : ", engRusPair.toString());
    }

    public MySQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public ArrayList<EngRusPair> getAllPairs(){
       ArrayList<EngRusPair> pairs = new ArrayList<>();

       String query = "SELECT * FROM " + NAME_TABLE + " ORDER BY " + KEY_ENG;

        SQLiteDatabase db = getWritableDatabase();
        db.setForeignKeyConstraintsEnabled(true);
        Cursor cursor = db.rawQuery(query, null);
        EngRusPair engRusPair = null;

        if (cursor.moveToFirst()){
            engRusPair = new EngRusPair();
            engRusPair.setId(cursor.getInt(0));
            engRusPair.setEngWord(cursor.getString(1));
            engRusPair.setRusWord(cursor.getString(2));
            pairs.add(engRusPair);

            if (cursor.moveToNext()) {
                do {

                    engRusPair = new EngRusPair();
                    engRusPair.setId(cursor.getInt(0));
                    engRusPair.setEngWord(cursor.getString(1));
                    engRusPair.setRusWord(cursor.getString(2));
                    pairs.add(engRusPair);
                } while (cursor.moveToNext());
            }
        }
        return pairs;
    }


    public void deleteItem(EngRusPair engRusPair){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NAME_TABLE, ID + "= ?", new String[]{String.valueOf(engRusPair.getId())});
        db.close();
    }

    public EngRusPair getEngRusPair(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.setForeignKeyConstraintsEnabled(true);
        Cursor cursor =
                db.query(NAME_TABLE,
                COLUMNS,
                "id = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null
                );

        EngRusPair engRusPair = new EngRusPair();
        engRusPair.setId(Integer.parseInt(cursor.getString(0)));
        engRusPair.setEngWord(cursor.getString(1));
        engRusPair.setRusWord(cursor.getString(2));
        return  engRusPair;
    }


    public void updateRusEngPair(EngRusPair pair){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ENG, pair.getEngWord());
        values.put(KEY_RUS, pair.getRusWord());
        db.update(NAME_TABLE,
                values,
                ID + " = ?",
                new String[]{String.valueOf(pair.getId())}
        );

        db.close();
    }




}
