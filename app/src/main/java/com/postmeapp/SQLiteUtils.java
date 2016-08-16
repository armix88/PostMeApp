package com.postmeapp;
import java.util.*;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class SQLiteUtils {

    private SQLiteDatabase db = null ;
    private String db_name;
    private String db_path;
    private String db_fullpath;
    private HashMap <String,Cursor> cursors;
    private Activity mActivity;

    public SQLiteUtils(Activity activity, String dbpath, String dbname) {
        super();
        db = null;
        db_name = dbname;
        db_path = dbpath;

        if(!dbpath.endsWith("/"))
            dbpath+="/";

        db_fullpath = db_path+db_name;
        db_fullpath.replace("//","/");
        cursors = new HashMap<String,Cursor>();
        mActivity = activity;
    }

    public void Connect(){
        try {
            db= SQLiteDatabase.openDatabase(db_fullpath,(CursorFactory) null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        } catch (Exception e) {
            Log.e(Utils.getTag(),"Connect() exception: "+e);
            Disconnect();
        }
    }

    public void Disconnect(){
        try{
            for(Map.Entry<String,Cursor> cur: cursors.entrySet())
                cur.getValue().close();

            db.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Cursor Load(String strQuery,String[] strParamArgs){

        Cursor cur=null;

        try{
            cur = db.rawQuery(strQuery, strParamArgs);
            cursors.put(strQuery,cur);
        }catch (Exception e) {
            Log.e(Utils.getTag(),"Load():"+e);
            cur = null;
        }

        return cur;
    }

    public Cursor getCursor(String strQuery){
        return cursors.get(strQuery);
    }

    public String getDb_fullpath() {
        return db_fullpath;
    }

}
