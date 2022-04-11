package com.felixvalor.todolist;


import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.felixvalor.todolist.Model.TaskModel;
import com.felixvalor.todolist.Utils.ComunicacionCMS;
import com.felixvalor.todolist.Utils.DatabaseHandler;
import com.felixvalor.todolist.Utils.dbTask;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

//import net.sqlcipher.database.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase;

public class Task_PopActivity extends Activity {

    private EditText newTaskTitle, newTaskDesc;
    FloatingActionButton btn_submitTask;
    private int id_task, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity_pop);
        newTaskTitle = findViewById(R.id.newtask_title);
        newTaskDesc = findViewById(R.id.newtask_description);
        btn_submitTask = findViewById(R.id.btn_submitTask);
        dbTask dbTask = new dbTask(Task_PopActivity.this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .3));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.NO_GRAVITY;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);
        DatabaseHandler db = new DatabaseHandler(this);
        SQLiteDatabase SQLdb = db.getWritableDatabase();
        Cursor cur = null;
        int id_user = 0;
        try {
            cur = SQLdb.rawQuery("SELECT * FROM user_info", null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        id_user = cur.getInt(cur.getColumnIndex("id_user"));
                    } while (cur.moveToNext());
                }
            }
        } catch (SQLException exception) {

        }

        boolean isUpdate = (boolean) getIntent().getSerializableExtra("isUpdate");

        if (isUpdate) {
            id_task = (int) getIntent().getSerializableExtra("id_task");
            String titulo = (String) getIntent().getSerializableExtra("title");
            String descripcion = (String) getIntent().getSerializableExtra("description");
            status = (int) getIntent().getSerializableExtra("status");
            newTaskTitle.setText(titulo);
            newTaskDesc.setText(descripcion);
        }

        int finalId_user = id_user;
        btn_submitTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isUpdate) {
                    String titulo = newTaskTitle.getText().toString();
                    String desc = newTaskDesc.getText().toString();
                    if (titulo.isEmpty()) {
                        Toast.makeText(Task_PopActivity.this, "No puedes dejar el titulo vacio", Toast.LENGTH_LONG).show();
                    } else {
                        dbTask.modificarTarea(id_task, titulo, desc, status);
                        ComunicacionCMS.modificarTareasCMS(new TaskModel(id_task, finalId_user, desc, status, titulo), finalId_user, Task_PopActivity.this);
                        finish();
                    }

                } else {
                    String title = newTaskTitle.getText().toString();
                    String desc = newTaskDesc.getText().toString();
                    if (title.isEmpty()) {
                        Toast.makeText(Task_PopActivity.this, "No puedes dejar el titulo vacio", Toast.LENGTH_LONG).show();
                    } else {
                        TaskModel task = new TaskModel(0, finalId_user, desc, 0, title);
                        long id = dbTask.insertarTarea(desc, false, title, finalId_user);
                        ComunicacionCMS.annadirTareasCMS(task, finalId_user, Task_PopActivity.this);
                        if (id > 0) {
                            Toast.makeText(Task_PopActivity.this, "Registro guardado", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Task_PopActivity.this, "Error al insertar tarea", Toast.LENGTH_LONG).show();
                        }
                        db.getAllTasks();
                        finish();
                    }
                }
            }
        });

        /*activadorMapa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mapView.setVisibility(View.VISIBLE);
                } else {
                    mapView.setVisibility(View.INVISIBLE);
                }
            }
        });
        mapView.onCreate(savedInstanceState);
        LatLng ny = new LatLng(40.7143528, -74.0059731);
        if (mapView != null) {

            mapView.getMapAsync(googleMap -> {
                LatLng latLng = new LatLng(ny.latitude, ny.longitude);
                googleMap.addMarker(new MarkerOptions().position(latLng)
                        .title("Ubicacion"));
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            });

        }*/


    }

    /*private void getLocalizacion() {
        int permiso = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permiso == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }*/


    /*public void onMapReady(@NonNull GoogleMap googleMap) {
        map=googleMap;
        //map.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("eoooooo"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);

    }*/
}