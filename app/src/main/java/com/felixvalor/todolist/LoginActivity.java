package com.felixvalor.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.felixvalor.todolist.Model.UsersModel;
import com.felixvalor.todolist.Utils.DatabaseHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;



import android.database.sqlite.SQLiteDatabase;
//import net.sqlcipher.database.SQLiteDatabase;

import org.mindrot.jbcrypt.BCrypt;

import java.util.List;



public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("Iniciar Sesion");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnRegister = findViewById(R.id.btn_register);
        TextView txtUsername= findViewById(R.id.txt_username);
        TextView txtPassword= findViewById(R.id.txt_password);
        DatabaseHandler db = new DatabaseHandler(this);
        SQLiteDatabase SQLdb= db.getWritableDatabase();


        List<UsersModel> listaUsuarios=db.getAllUsers();
        System.out.println(listaUsuarios.size());


        if (!isNetworkAvailable()){
            btnRegister.setEnabled(false);
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txtUsername.getText().toString().equals("")||txtPassword.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                }else{
                    boolean sesionValida=false;
                    int id_user = 0;
                    if (listaUsuarios.size()==0){
                        Toast.makeText(LoginActivity.this, "No existen usuarios localmente, reinicie la aplicaion y asegurese de tener conexion a internet", Toast.LENGTH_LONG).show();
                    }else{
                        for (int i=0;i<listaUsuarios.size();i++){
                            System.out.println(listaUsuarios.get(i).getUsername());
                            if(listaUsuarios.get(i).getUsername().equals(txtUsername.getText().toString())){

                                if (BCrypt.checkpw(txtPassword.getText().toString(),listaUsuarios.get(i).getPassword())){
                                    sesionValida=true;
                                    id_user=listaUsuarios.get(i).getIdUser();
                                    break;
                                }else{
                                    Toast.makeText(LoginActivity.this, "NO COINCIDE LA CONTRASEÑA", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                    if(sesionValida){
                        ContentValues cv = new ContentValues();
                        cv.put("id_user", id_user );
                        SQLdb.execSQL("delete from user_info");
                        SQLdb.insert("user_info",null,cv);
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
