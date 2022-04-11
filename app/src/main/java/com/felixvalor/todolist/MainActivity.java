package com.felixvalor.todolist;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.SearchView;
import android.widget.TextView;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.felixvalor.todolist.Adapters.ToDoListAdapter;
import com.felixvalor.todolist.Model.TaskModel;
import com.felixvalor.todolist.Utils.DatabaseHandler;
import com.felixvalor.todolist.Utils.dbTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.onesignal.OneSignal;


//import net.sqlcipher.database.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String ONESIGNAL_APP_ID = "df64bb12-0f68-401c-b6aa-ff2c2de0f1f4";
    private dbTask db;



    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView tasksRecyclerView;
    private ToDoListAdapter taskAdapter;



    private FloatingActionButton btnNewAdd, btnAddTask;
    private TextView txtAddTask;
    private Animation mFabOpenAnim, mFabCloseAnim;
    private boolean isOpen;

    int id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compruebaRoot("su");
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        db = new dbTask(this);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        tasksRecyclerView=findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager((this)));

        taskAdapter=new ToDoListAdapter((ArrayList<TaskModel>) db.getAllTasks());
        tasksRecyclerView.setAdapter(taskAdapter);

        btnNewAdd =findViewById(R.id.btn_new_add);
        btnAddTask =findViewById(R.id.btn_add_task);

        txtAddTask =findViewById(R.id.txt_add_task);

        mFabOpenAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_open);
        mFabCloseAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_close);

        isOpen = false;
        btnNewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isOpen){
                    isOpen = false;
                    btnAddTask.setAnimation(mFabCloseAnim);
                    txtAddTask.setVisibility(View.INVISIBLE);
                }
                else {
                    isOpen = true;
                    btnAddTask.setAnimation(mFabOpenAnim);
                    txtAddTask.setVisibility(View.VISIBLE);

                }
            }
        });

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Task_PopActivity.class);
                i.putExtra("isUpdate",false);
                startActivity(i);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
                swipeRefreshLayout.setRefreshing(false);
            }
        });



    }

    private void compruebaRoot(String command) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            Toast.makeText(MainActivity.this, "MOVIL ROOTEADO", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "MOVIL NO ROOTEADO", Toast.LENGTH_LONG).show();
        } finally {
            if (process != null) {
                try {
                    process.destroy();
                } catch (Exception e) { }
            }
        }
    }

    private void refresh(){
        cargaTareas();
    }

    public void cargaTareas(){
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        //net.sqlcipher.database.SQLiteDatabase db= dbHandler.getWritableDatabase("123456789");
        SQLiteDatabase db= dbHandler.getWritableDatabase();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //JsonArrayRequest jsonArrayRequestTask = new JsonArrayRequest("http://192.168.90.86:8080/api/task/listar", new Response.Listener<JSONArray>() {
        //JsonArrayRequest jsonArrayRequestTask = new JsonArrayRequest("http://192.168.68.109:8080/api/task/listar", new Response.Listener<JSONArray>() {
            JsonArrayRequest jsonArrayRequestTask = new JsonArrayRequest("https://to-do-list-felix.herokuapp.com/api/task/listar", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i= 0; i< response.length();i++){
                    try {
                        JSONObject jsonObject= response.getJSONObject(i);

                        int id_task= jsonObject.getInt("id_task");
                        int id_user= jsonObject.getJSONObject("fk").getInt("id_user");
                        String description= jsonObject.getString("description");
                        boolean status= jsonObject.getBoolean("status");
                        String title= jsonObject.getString("title");

                        ContentValues cv = new ContentValues();
                        cv.put("id_task", id_task );
                        cv.put("id_user", id_user );
                        cv.put("description", description );
                        cv.put("status", status );
                        cv.put("title", title );

                        db.insert("task", null, cv);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequestTask);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity,menu);
        MenuItem menuItem= menu.findItem(R.id.item_buscar);
        SearchView searchView= (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint("Pulse para buscar");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                taskAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(R.id.chat_bluetooth==itemId){
            Intent i = new Intent(getApplicationContext(), ChatBluetoothActivity.class);
            startActivity(i);
        }

        if(R.id.item_map==itemId){
            Intent i = new Intent(getApplicationContext(), MapActivity.class);
            startActivity(i);
        }

        if(R.id.item_contactame_tlf==itemId){
            Intent intent=new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:012345678"));
            startActivity(intent);
        }

        if(R.id.item_contactame_email==itemId){
            Intent intent=new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:valorfelix@gmail.com"));
            startActivity(intent);
        }

        if(R.id.item_cerrar_sesion==itemId){
            DatabaseHandler db = new DatabaseHandler(this);
            SQLiteDatabase SQLdb= db.getWritableDatabase();
            SQLdb.execSQL("delete from user_info");
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        }
        return true;
    }
}