package com.duan2.camnangamthuc.camnangamthuc.SQLiteDatabase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

public class SQLiteHandler extends SQLiteOpenHelper {
    private static final String TAG = SQLiteHandler.class.getSimpleName();
    private static final String DATABASE_NAME = "camnangamthuc.db";
    //bảng tải về
    private static final String TABLE_NAME_DOW = "taive";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "Name";
    private static final String KEY_IMAGE = "Image";
    private static final String KEY_IMAGEVIEW = "ImageView";
    private static final String KEY_INFOMATION = "Infomation";
    private static final String KEY_INFOMATIONVIEW = "InfomationView";
    private static final String KEY_HAPPY = "Happy";
//bảng danh sách đi chợ
    private static final String TABLE_NAME_LISTSHOPING = "danhsach";
    private static final String LIST_ID = "id";
    private static final String LIST_TITLE = "Title";
    private static final String LIST_CONTENT = "Content";
//bảng lưu trữ
private static final String TABLE_NAME_STORAGE = "luutru";
    private static final String STORAGE_ID = "id";
    private static final String STORAGE_TITLE = "Title";
    private static final String STORAGE_CONTENT = "Content";
//bảng yêu thích
private static final String TABLE_NAME_FAVORITE = "yeuthich";
    private static final String FAVORITE_ID = "id";
    private static final String FAVORITE_KEYID = "keyid";
    private static final String FAVORITE_NAMEFOOD = "namefood";
    private static final String FAVORITE_IMAGEFOOD = "imagefood";
    private static final String FAVORITE_RESOURCESFOOD = "resourcesfood";
    private static final String FAVORITE_RECIPEFOOD = "recipefood";
    private static final String FAVORITE_NAMEUSEFOOD = "nameusefood";
    private static final String FAVORITE_EMAILUSEFOOD = "emailusefood";
    private static final String FAVORITE_IMAGEUSEFOOD = "imageusefood";
    private static final String FAVORITE_TIMEFOOD = "timefood";
    private static final int DATABASE_VERSION = 1;


    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //tạo bảng tải về
        String CREATE_TABLE_DOW = "CREATE TABLE " + TABLE_NAME_DOW + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_IMAGE + " TEXT ," + KEY_IMAGEVIEW + " TEXT,"
                + KEY_INFOMATION + " TEXT," + KEY_INFOMATIONVIEW + " TEXT,"+ KEY_HAPPY + " TEXT"  + ")";
//tạo bảng danh sách
        String CREATE_TABLE_LISTSHOPING = "CREATE TABLE " + TABLE_NAME_LISTSHOPING + "("
                + LIST_ID + " INTEGER PRIMARY KEY," + LIST_TITLE + " TEXT,"
                + LIST_CONTENT + " TEXT"  + ")";
//tạo bảng lưu trữ
        String CREATE_TABLE_STORAGE = "CREATE TABLE " + TABLE_NAME_STORAGE + "("
                + LIST_ID + " INTEGER PRIMARY KEY," + LIST_TITLE + " TEXT,"
                + LIST_CONTENT + " TEXT"  + ")";

        //tạo bảng yêu thích
        String CREATE_TABLE_FAVORITE = "CREATE TABLE " + TABLE_NAME_FAVORITE + "("
                + FAVORITE_ID + " INTEGER PRIMARY KEY," + FAVORITE_KEYID + " TEXT," + FAVORITE_NAMEFOOD + " TEXT,"
                + FAVORITE_IMAGEFOOD + " TEXT,"  +FAVORITE_RESOURCESFOOD + " TEXT,"  +FAVORITE_RECIPEFOOD + " TEXT,"  +
                FAVORITE_NAMEUSEFOOD + " TEXT," + FAVORITE_EMAILUSEFOOD + " TEXT,"+ FAVORITE_IMAGEUSEFOOD + " TEXT,"
                + FAVORITE_TIMEFOOD + " TEXT" +")";
        sqLiteDatabase.execSQL(CREATE_TABLE_DOW);
        sqLiteDatabase.execSQL(CREATE_TABLE_LISTSHOPING);
        sqLiteDatabase.execSQL(CREATE_TABLE_STORAGE);
        sqLiteDatabase.execSQL(CREATE_TABLE_FAVORITE);
        Log.d(TAG, "Đã tạo database ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DOW);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LISTSHOPING);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STORAGE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FAVORITE);
        // Create tables again
        onCreate(sqLiteDatabase);
    }
    // //thêm dữ liệu vào bảng taive
    public void addDow(String Name, String Image, String ImageView, String Infomation,
                       String InfomationView,String Happy) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, Name);
        values.put(KEY_IMAGE, Image);
        values.put(KEY_IMAGEVIEW, ImageView);
        values.put(KEY_INFOMATION, Infomation);
        values.put(KEY_INFOMATIONVIEW, InfomationView);
        values.put(KEY_HAPPY, Happy);
        // Inserting Row
        long id = db.insert(TABLE_NAME_DOW, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "Thêm mới dow thành công " + id);
    }
    //lấy dữ liệu bảng taive
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_DOW, null);
        return cursor;
    }
    //xóa dữ liệu bản tải về

    public void deleteDatadow(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("DELETE FROM " + TABLE_NAME_DOW +" WHERE id='%s' ",id);
        db.execSQL(query);
    }
    //thêm dữ liệu bản danh sách đi chợ
    public boolean insertDatalist(String Title, String Content){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LIST_TITLE, Title);
        values.put(LIST_CONTENT, Content);
        long result = db.insert(TABLE_NAME_LISTSHOPING, null, values);
        if(result == -1){
            return false;
        }else {
            return true;
        }
    }
    //lấy dữ liệu bảng danh sách
    public Cursor getAllDataDanhSach(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_LISTSHOPING, null);
        return cursor;
    }
    //cập nhật dữ liệu bản danh sách
    public boolean updateDataList(String id, String Title, String Content){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LIST_ID, id);
        values.put(LIST_TITLE, Title);
        values.put(LIST_CONTENT, Content);
        long result = db.update(TABLE_NAME_LISTSHOPING, values, LIST_ID + "=?", new String[]{id});
        if(result == -1){
            return false;
        }else {
            return true;
        }
    }
    //xóa dữ liệu bảng danh sách
    public void deleteDataList(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("DELETE FROM " + TABLE_NAME_LISTSHOPING +" WHERE id='%s' ",id);
        db.execSQL(query);
    }

    //thêm dữ liệu bảng lưu trữ
    public void insertDataStogare(String Title, String Content){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STORAGE_TITLE, Title);
        values.put(STORAGE_CONTENT, Content);
        long result = db.insert(TABLE_NAME_STORAGE, null, values);

        Log.d(TAG, "Thêm mới dow thành công " + result);
    }

    //lấy dữ liệu bảng lưu trữ
    public Cursor getAllStogare(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_STORAGE, null);
        return cursor;
    }
    //xóa dữ liệu bảng lưu trữ
    public void deleteDataStogare(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("DELETE FROM " + TABLE_NAME_STORAGE +" WHERE id='%s' ",id);
        db.execSQL(query);
    }
    //thêm mới vào bảng yêu thích
    public void addtofavorite(String keyid, String namefood, String imagefood, String resourcesfood,
                              String recipefood,String nameusefood,String emailusefood,String imageusefood,long timefood){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FAVORITE_KEYID, keyid);
        values.put(FAVORITE_NAMEFOOD, namefood);
        values.put(FAVORITE_IMAGEFOOD, imagefood);
        values.put(FAVORITE_RESOURCESFOOD, resourcesfood);
        values.put(FAVORITE_RECIPEFOOD, recipefood);
        values.put(FAVORITE_NAMEUSEFOOD, nameusefood);
        values.put(FAVORITE_EMAILUSEFOOD, emailusefood);
        values.put(FAVORITE_IMAGEUSEFOOD, imageusefood);
        values.put(FAVORITE_TIMEFOOD, timefood);
        long result = db.insert(TABLE_NAME_FAVORITE, null, values);
        Log.d(TAG, "Thêm mới dow thành công " + result);
    }
    //xóa dữ liệu bảng yêu thích
    public void deletefavorite(String keyid){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("DELETE FROM " + TABLE_NAME_FAVORITE +" WHERE keyid='%s' ",keyid);
        db.execSQL(query);
    }
    //lấy dữ liệu bảng yêu thích
    public Cursor getAllfavorite(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_FAVORITE, null);
        return cursor;
    }
    //tìm kiếm theo keyid của bảng yêu thích
    public boolean isfavorite(String keyid){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("SELECT *FROM " + TABLE_NAME_FAVORITE +" WHERE keyid='%s' ",keyid);
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.getCount() <=0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
