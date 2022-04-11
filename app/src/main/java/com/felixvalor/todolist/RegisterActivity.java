package com.felixvalor.todolist;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.felixvalor.todolist.Model.UsersModel;
import com.felixvalor.todolist.Utils.ComunicacionCMS;
import com.felixvalor.todolist.Utils.DatabaseHandler;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

//import net.sqlcipher.database.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase;


import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    CheckBox captcha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Registrarse");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button btnRegister = findViewById(R.id.btn_register);
        TextView txtUsername= findViewById(R.id.txt_username);
        TextView txtPassword= findViewById(R.id.txt_password);
        TextView txtPasswordConfirm= findViewById(R.id.txt_passwordConfirm);
        TextView txtEmail= findViewById(R.id.txt_email);
        captcha=findViewById(R.id.capcha);

        DatabaseHandler db = new DatabaseHandler(this);
        SQLiteDatabase SQLdb= db.getWritableDatabase();

        List<UsersModel> listaUsuarios=db.getAllUsers();
        System.out.println(listaUsuarios.size());

        captcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recaptcha();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username,email,password,passwordConfirm;
                username=txtUsername.getText().toString().trim();
                password=txtPassword.getText().toString().trim();
                passwordConfirm=txtPasswordConfirm.getText().toString().trim();
                email=txtEmail.getText().toString().trim();
                boolean usernameExist=false;
                for (int i=0;i<listaUsuarios.size();i++){
                    if (!username.equals(listaUsuarios.get(i).getUsername())){
                        usernameExist=false;
                    }else{
                        usernameExist=true;
                        break;
                    }

                }

                if (username.equals("")||password.equals("")||email.equals("")||passwordConfirm.equals("")){
                    Toast.makeText(RegisterActivity.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                }else if (!password.equals(passwordConfirm)){
                    Toast.makeText(RegisterActivity.this, "La contraseña debe coincidir en los dos campos", Toast.LENGTH_SHORT).show();
                }else if(!VALID_EMAIL_ADDRESS_REGEX.matcher(email).find()){
                    Toast.makeText(RegisterActivity.this, "Correo incorecto", Toast.LENGTH_SHORT).show();
                }else if (usernameExist){
                    Toast.makeText(RegisterActivity.this, "Este usuario ya existe", Toast.LENGTH_SHORT).show();
                }else if(!captcha.isChecked()){
                    Toast.makeText(RegisterActivity.this, "Completa el captcha", Toast.LENGTH_SHORT).show();
                }else if(!isNetworkAvailable()){
                    Toast.makeText(RegisterActivity.this, "No puedes registrarte sin conexion a internet", Toast.LENGTH_SHORT).show();
                }else{
                    password=hashPassword(password);
                    UsersModel newUser=new UsersModel(0,email,password,username);
                    ComunicacionCMS.annadirUsuarioCMS(newUser,RegisterActivity.this);
                    SQLdb.execSQL("INSERT INTO USERS (email,username,password) values('"+email+"','"+username+"','"+password+"')");
                    ComunicacionCMS.cargaUsuarios(RegisterActivity.this);
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }
    private String hashPassword(String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    private void recaptcha(){
        SafetyNet.getClient(RegisterActivity.this).verifyWithRecaptcha("6LdPEmAdAAAAAMFly7SttPOqQezVHU9vdJXin68y")
                .addOnSuccessListener(new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(@NonNull SafetyNetApi.RecaptchaTokenResponse recaptchaTokenResponse) {
                        String captchaToken=recaptchaTokenResponse.getTokenResult();
                        captcha.setChecked(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        captcha.setChecked(false);
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
