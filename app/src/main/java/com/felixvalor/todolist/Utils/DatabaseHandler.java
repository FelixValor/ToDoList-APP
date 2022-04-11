package com.felixvalor.todolist.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
//import net.sqlcipher.database.SQLiteDatabase;
//import net.sqlcipher.database.SQLiteOpenHelper;


import com.felixvalor.todolist.Model.TaskModel;
import com.felixvalor.todolist.Model.UsersModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String TASK_TABLE = "CREATE TABLE task(id_task integer PRIMARY KEY, id_user integer, description varchar(255), status bit(1), title varchar(255))";
    private static final String USER_TABLE="CREATE TABLE users(id_user integer PRIMARY KEY,email varchar(255),password varchar(255), username varchar(255))";
    private static final String USER_INFO_TABLE="CREATE TABLE user_info(id_user integer)";
    private static final String DB_NAME = "toDoList.db";
    //private static final String PASSWORD_DB = "123456789";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        //SQLiteDatabase.loadLibs(context);
        db=this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE);
        db.execSQL(TASK_TABLE);
        db.execSQL(USER_INFO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public  List<UsersModel> getAllUsers(){
        List<UsersModel> userList = new ArrayList<>();
        Cursor cur = null;
        db=this.getReadableDatabase();
        db.beginTransaction();
        try{
            cur = db.rawQuery("SELECT * FROM USERS",null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        UsersModel user = new UsersModel();
                        user.setIdUser(cur.getInt(cur.getColumnIndex("id_user")));
                        user.setUsername(cur.getString(cur.getColumnIndex("username")));
                        user.setEmail(cur.getString(cur.getColumnIndex("email")));
                        user.setPassword(cur.getString(cur.getColumnIndex("password")));
                        userList.add(user);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }

        return userList;
    }

    public List<TaskModel> getAllTasks(){
        List<TaskModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.rawQuery("SELECT * FROM TASK WHERE id_user=(select * from user_info)",null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        TaskModel task = new TaskModel();
                        task.setIdTask(cur.getInt(cur.getColumnIndex("id_task")));
                        task.setId_user(cur.getInt(cur.getColumnIndex("id_user")));
                        task.setDescription(cur.getString(cur.getColumnIndex("description")));
                        task.setStatus(cur.getInt(cur.getColumnIndex("status")));
                        task.setTitle(cur.getString(cur.getColumnIndex("title")));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put("status", status);
        db.update("task", cv, "id_task" + "= ?", new String[] {String.valueOf(id)});
    }


}
