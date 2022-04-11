package com.felixvalor.todolist;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.felixvalor.todolist.Utils.ComunicacionCMS;
import com.felixvalor.todolist.Utils.DatabaseHandler;

//import net.sqlcipher.database.SQLiteDatabase;

import android.database.sqlite.SQLiteDatabase;

public class SplashActivity extends AppCompatActivity {
    ProgressBar splashProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setTitle("");
        ComunicacionCMS.cargaUsuarios(this);
        ComunicacionCMS.cargaTareas(this);
        splashProgress=findViewById(R.id.splashProgress);
        splashProgress.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        ObjectAnimator.ofInt(splashProgress,"progress",100).setDuration(5000).start();
        DatabaseHandler db = new DatabaseHandler(this);
        SQLiteDatabase SQLdb= db.getWritableDatabase();
        Cursor cur=null;
        String id_user="";
        try{
            cur=SQLdb.rawQuery("SELECT * FROM user_info",null);
            if (cur!=null){
                if (cur.moveToFirst()){
                    do {
                        id_user= cur.getString(cur.getColumnIndex("id_user"));
                    }while (cur.moveToNext());
                }
            }
        }catch (SQLException exception){

        }
        System.out.println(id_user);

        String finalId_user = id_user;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(finalId_user.isEmpty()){
                    Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },5000);
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMetered = cm.isActiveNetworkMetered();
        if (isMetered){
            Toast.makeText(SplashActivity.this, "ESTAS UTILIZANDO LOS DATOS MOVILES", Toast.LENGTH_LONG).show();
        }

    }






}
