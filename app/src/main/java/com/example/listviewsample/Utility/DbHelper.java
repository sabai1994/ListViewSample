package com.example.listviewsample.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.listviewsample.Model.LanguageInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    //Db structure
    private  static final String DB_Name = "Language_Db";
    private static final Integer DB_Ver = 1;

    //TB structure
    private String tbl_language = "tblLanguage";
    private String colId="ID";
    private  String  colName ="Name";
    private  String colDesc="Description";
    private String colReleaseDate="ReleaseDate";
    SQLiteDatabase db;
    public DbHelper (Context context){
        //Code to create database
        super(context, DB_Name, null, DB_Ver);
    }
    public DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    //Code to create Database
        String query = "CREATE TABLE "+tbl_language+ "( "+colId+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +colName+" TEXT,"
                +colDesc+" TEXT,"
                +colReleaseDate+ " DATE)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS "+tbl_language;
        db.execSQL(query);

        //Call onCreate Again
        onCreate(db);
    }
    public  int Insert(LanguageInfo info){
        db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(colName,info.getName());
        values.put(colDesc,info.getDescription());
        values.put(colReleaseDate,info.getReleaseDate().toString());
        db.insert(tbl_language,null,values);

        db = getReadableDatabase();
        Cursor cur = db.rawQuery("select max("+colId+") from "+tbl_language,null);
        cur.moveToNext();
        int result = Integer.parseInt(cur.getString(0));
        return  result;
    }
    public int Update(LanguageInfo info){
    db = getWritableDatabase();
    ContentValues contentValues = new ContentValues();
        contentValues.put(colName,info.getName());
        contentValues.put(colDesc,info.getDescription());
        contentValues.put(colReleaseDate,info.getReleaseDate().toString());
    int result = db.update(tbl_language,contentValues,colId+" = ?",new String[]{String.valueOf(info.getId())});
   return result;
    }
    //Delete record
    public void delete(LanguageInfo info){
    db = getWritableDatabase();
    db.execSQL("Delete from "+ tbl_language+ " where "+ colId+" = ?",new String[]{String.valueOf(info.getId())});
    }
    //Get ALl Data From Language
    public ArrayList<LanguageInfo> GetAll() throws ParseException {
        ArrayList<LanguageInfo> infoList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM "+tbl_language,null);

        while(cur.moveToNext()){
            LanguageInfo info = new LanguageInfo();
            info.setId(Integer.parseInt(cur.getString(cur.getColumnIndex(colId))) );
            info.setName(cur.getString(cur.getColumnIndex(colName)));
            info.setReleaseDate(new SimpleDateFormat("dd-MM-yyyy").parse(cur.getString(cur.getColumnIndex(colReleaseDate))));
            info.setDescription(cur.getString(cur.getColumnIndex(colDesc)));
            infoList.add(info);
        }
        return  infoList;
    }
    //Delete for Language
    public void Delete(LanguageInfo info){
        db = getWritableDatabase();
        db.execSQL("delete from "+tbl_language+" where "+colId+" ="+info.getId());
    }
}
