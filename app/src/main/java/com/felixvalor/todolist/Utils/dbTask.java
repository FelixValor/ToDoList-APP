package com.felixvalor.todolist.Utils;

import android.content.ContentValues;
import android.content.Context;


import androidx.annotation.Nullable;

import android.database.sqlite.SQLiteDatabase;
//import net.sqlcipher.database.SQLiteDatabase;

public class dbTask extends DatabaseHandler{
    Context context;

    public dbTask(@Nullable Context context) {
        super(context);
        this.context=context;
    }


    public long insertarTarea(String description, boolean status, String title, int id_user){
        long id=0;
        try {
            DatabaseHandler dbHandler = new DatabaseHandler(context);
            SQLiteDatabase db= dbHandler.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put("description", description );
            cv.put("status", status );
            cv.put("title", title );
            cv.put("id_user",id_user);

            id=db.insert("task", null, cv);
            db.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return id;
    }

    public void modificarTarea(int id,String titulo, String desc,int status) {
        DatabaseHandler dbHelper = new DatabaseHandler(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            String consulta="UPDATE task SET title='"+titulo+"',description='"+desc+"',status="+status+" WHERE id_task="+id;
            System.out.println(consulta);
            db.execSQL(consulta);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }
    }


    public boolean eliminarTarea(int id) {

        boolean correcto = false;

        DatabaseHandler dbHelper = new DatabaseHandler(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            String consulta="DELETE FROM task WHERE id_task="+id;
            System.out.println(consulta);
            db.execSQL(consulta);
            correcto = true;
        } catch (Exception ex) {
            ex.toString();
            correcto = false;
        } finally {
            db.close();
        }

        return correcto;
    }
}
