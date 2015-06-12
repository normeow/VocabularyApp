package melocha.vocabularyapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Admin on 11.03.2015.
 */
public class MySQLHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public  static  final  String DATABASE_NAME = "VocabularyDB";
    //tables names
    private static final String NAME_TABLE_ENGWORDS = "english_words";
    private static final String NAME_TABLE_RUSWORDS = "rus_words";

    //tables columns names
    private  static final String KEY_ENG_ID = "id";
    private  static final String KEY_ENG_WORD = "engWord";

    private  static final String KEY_RUS_ID = "id";
    private  static final String KEY_RUS_WORD = "rusWord";
    private  static final String KEY_RUS_FOREGIN = "fKeyId";

    private static final String[]  ENG_COLUMNS = {KEY_ENG_ID, KEY_ENG_WORD};
    private static final String[]  RUS_COLUMNS = {KEY_RUS_ID, KEY_RUS_WORD, KEY_RUS_FOREGIN};


    public MySQLHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String str = "CREATE TABLE " + NAME_TABLE_ENGWORDS + " (" +
                KEY_ENG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_ENG_WORD + " TEXT )";
        String CREATE_ENGWORDS_TABLE = str;

       String CREATE_RUSWORDS_TABLE = "CREATE TABLE " + NAME_TABLE_RUSWORDS + " ( " +
                KEY_RUS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_RUS_WORD + " TEXT, " +
                KEY_RUS_FOREGIN + " INTEGER, " +
                "FOREIGN KEY(" + KEY_RUS_FOREGIN + ") REFERENCES " + NAME_TABLE_ENGWORDS + " (" + KEY_ENG_ID + ") )";

        // create tables
        db.execSQL(CREATE_ENGWORDS_TABLE);
        db.execSQL(CREATE_RUSWORDS_TABLE);

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + NAME_TABLE_ENGWORDS);
        db.execSQL("DROP TABLE IF EXISTS" + NAME_TABLE_RUSWORDS);
        // create fresh table
        this.onCreate(db);
    }

    public  void addEngRusPair(EngRusPair engRusPair){
        SQLiteDatabase db = this.getWritableDatabase();

        //если таблица уже задержит такое англ слова - спросить о перезаписи
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ENG_WORD, engRusPair.getEngWord());
        int id = Integer.parseInt(String.valueOf(db.insert(NAME_TABLE_ENGWORDS, null, contentValues)));

        //fill rus_words table
        //настоящий айдишник, который в таблице у англ слова не является айдишником обхкта-аргумента! потому что там его вообще нет!

        ArrayList<String> rusWords = engRusPair.getRusWords();
        for (int i = 0; i < rusWords.size(); i++) {

            contentValues = new ContentValues();
            contentValues.put(KEY_RUS_FOREGIN, id);
            contentValues.put(KEY_RUS_WORD, rusWords.get(i));
            db.insert(NAME_TABLE_RUSWORDS, null, contentValues);
        }
        db.close();
        Log.d("addEngRusPair : ", engRusPair.toString());
    }

    public MySQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public ArrayList<EngRusPair> getAllPairs(){
       ArrayList<EngRusPair> pairs = new ArrayList<>();

       String query = "SELECT " +
                "tEng." +  KEY_ENG_ID  +
                ", tEng." + KEY_ENG_WORD +
                ", tRus." + KEY_RUS_WORD +
                " FROM " + NAME_TABLE_ENGWORDS + " tEng INNER JOIN "+ NAME_TABLE_RUSWORDS + " tRus ON tEng." + KEY_ENG_ID + " = tRus." + KEY_RUS_FOREGIN;

        SQLiteDatabase db = getWritableDatabase();
        db.setForeignKeyConstraintsEnabled(true);
        Cursor cursor = db.rawQuery(query, null);
        EngRusPair engRusPair = null;
        ArrayList<String> rusWords = new ArrayList<String>();

        if (cursor.moveToFirst()){
            engRusPair = new EngRusPair();
            int curId = cursor.getInt(0);

            engRusPair.setId(curId);
            engRusPair.setEngWord(cursor.getString(1));
            rusWords.add(cursor.getString(2));

            if (cursor.moveToNext()) {
                do {
                    int nextId = cursor.getInt(0);
                    if (curId != nextId) {
                        engRusPair.setRusWords(rusWords);
                        pairs.add(engRusPair);
                        engRusPair = new EngRusPair();
                        rusWords = new ArrayList<String>();
                        curId = nextId;
                        engRusPair.setId(curId);
                        engRusPair.setEngWord(cursor.getString(1));
                        rusWords.add(cursor.getString(2));
                    } else {
                        rusWords.add(cursor.getString(2));
                    }
                } while (cursor.moveToNext());
                engRusPair.setRusWords(rusWords);
                pairs.add(engRusPair);
            }
            else {
                engRusPair.setRusWords(rusWords);
                pairs.add(engRusPair);
            }
        }
        return pairs;
    }
    //todo метод возвращающий пары по условию
    public ArrayList<EngRusPair> getAllPairs(String query){
        ArrayList<EngRusPair> pairs = new ArrayList<>();

       /* String query = "SELECT " +
                "tEng." +  KEY_ENG_ID  +
                ", tEng." + KEY_ENG_WORD +
                ", tRus." + KEY_RUS_WORD +
                " FROM " + NAME_TABLE_ENGWORDS + " tEng INNER JOIN "+ NAME_TABLE_RUSWORDS + " tRus ON tEng." + KEY_ENG_ID + " = tRus." + KEY_RUS_FOREGIN;*/

        SQLiteDatabase db = getWritableDatabase();
        db.setForeignKeyConstraintsEnabled(true);
        Cursor cursor = db.rawQuery(query, null);
        EngRusPair engRusPair = null;
        ArrayList<String> rusWords = new ArrayList<String>();

        if (cursor.moveToFirst()){
            engRusPair = new EngRusPair();
            int curId = cursor.getInt(0);

            engRusPair.setId(curId);
            engRusPair.setEngWord(cursor.getString(1));
            rusWords.add(cursor.getString(2));

            if (cursor.moveToNext()) {
                do {
                    int nextId = cursor.getInt(0);
                    if (curId != nextId) {
                        engRusPair.setRusWords(rusWords);
                        pairs.add(engRusPair);
                        engRusPair = new EngRusPair();
                        rusWords = new ArrayList<String>();
                        curId = nextId;
                        engRusPair.setId(curId);
                        engRusPair.setEngWord(cursor.getString(1));
                        rusWords.add(cursor.getString(2));
                    } else {
                        rusWords.add(cursor.getString(2));
                    }
                } while (cursor.moveToNext());
                engRusPair.setRusWords(rusWords);
                pairs.add(engRusPair);
            }
            else {
                engRusPair.setRusWords(rusWords);
                pairs.add(engRusPair);
            }
        }
        return pairs;
    }

    public void deleteItem(EngRusPair engRusPair){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NAME_TABLE_RUSWORDS, KEY_RUS_FOREGIN + "= ?", new String[]{String.valueOf(engRusPair.getId())});
        db.delete(NAME_TABLE_ENGWORDS, KEY_ENG_ID+ "= ?", new String[]{String.valueOf(engRusPair.getId())});
        db.close();
    }

    public EngRusPair getEngRusPair(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.setForeignKeyConstraintsEnabled(true);
        Cursor cursor =
                db.query(NAME_TABLE_ENGWORDS,
                ENG_COLUMNS,
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

        //получить массив слов по айдишнику из второй таблицы
        String query = "SELECT " + KEY_RUS_WORD +  " FROM " + NAME_TABLE_RUSWORDS + " WHERE " + KEY_RUS_FOREGIN + " = " + String.valueOf(id);
        cursor = db.rawQuery(query, null);
        String t = cursor.getString(0);
        Log.d("Cursor: ", t);

        cursor.getString(1);
        return  engRusPair;
    }


    public void updateRusEngPair(EngRusPair pair){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ENG_WORD, pair.getEngWord());
        db.update(NAME_TABLE_ENGWORDS,
                values,
                KEY_ENG_ID + " = ?",
                new String[]{String.valueOf(pair.getId())}
        );
        db.delete(NAME_TABLE_RUSWORDS, KEY_RUS_FOREGIN + "= ?", new String[]{String.valueOf(pair.getId())});
        ArrayList<String> rusWords = pair.getRusWords();
        for (int i = 0; i < rusWords.size(); i++) {

            values  = new ContentValues();
            values.put(KEY_RUS_FOREGIN, pair.getId());
            values.put(KEY_RUS_WORD, rusWords.get(i));
            db.insert(NAME_TABLE_RUSWORDS, null, values );
        }
        db.close();
    }




}
