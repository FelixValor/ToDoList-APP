package com.felixvalor.todolist.Utils;

import android.content.ContentValues;
import android.content.Context;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.felixvalor.todolist.Model.TaskModel;
import com.felixvalor.todolist.Model.UsersModel;

//import net.sqlcipher.database.SQLiteDatabase;

import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ComunicacionCMS {
    public static void annadirTareasCMS(TaskModel task, int id_user, Context context){
        //RECUERDA QUE EN EL CMS HAS DADO PERMISOS GLOBALES PORQUE SINO REDIRIJE AL LOGIN
        String postUrl = "https://to-do-list-felix.herokuapp.com/api/task/recibir";
        //String postUrl = "http://192.168.90.86:8080/api/task/recibir";
        //String postUrl = "http://192.168.68.109:8080/api/task/recibir";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject postData = new JSONObject();
        JSONObject fk=new JSONObject();
        try {

            fk.put("id_user", id_user);
            fk.put("username", "");
            fk.put("email", "");
            fk.put("password", "");

            postData.put("id_task", task.getIdTask());
            postData.put("fk", fk);
            postData.put("description", task.getDescription());
            postData.put("status", task.getStatus());
            postData.put("title", task.getTitle());


        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }


    public static void annadirUsuarioCMS(UsersModel user, Context context){
        //RECUERDA QUE EN EL CMS HAS DADO PERMISOS GLOBALES PORQUE SINO REDIRIJE AL LOGIN
        String postUrl = "https://to-do-list-felix.herokuapp.com/api/user/recibir";
        //String postUrl = "http://192.168.90.86:8080/api/user/recibir";
        //String postUrl = "http://192.168.68.109:8080/api/user/recibir";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject postData = new JSONObject();
        try {

            postData.put("id_user", user.getIdUser());
            postData.put("username", user.getUsername());
            postData.put("email", user.getEmail());
            postData.put("password", user.getPassword());

        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }



    public static void modificarTareasCMS(TaskModel task, int id_user, Context context){
        String postUrl = "https://to-do-list-felix.herokuapp.com/api/task/recibir";
        //String postUrl = "http://192.168.90.86:8080/api/task/recibir";
        //String postUrl = "http://192.168.68.109:8080/api/task/recibir";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject postData = new JSONObject();
        JSONObject fk=new JSONObject();
        try {

            fk.put("id_user", id_user);
            fk.put("username", "");
            fk.put("email", "");
            fk.put("password", "");

            postData.put("id_task", task.getIdTask());
            postData.put("fk", fk);
            postData.put("description", task.getDescription());
            postData.put("status", task.getStatus());
            postData.put("title", task.getTitle());


        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }


    public static void borrarTareaCMS(int id_task, Context context){
        //RECUERDA QUE EN EL CMS HAS DADO PERMISOS GLOBALES PORQUE SINO REDIRIJE AL LOGIN
        //String postUrl = "http://192.168.90.86:8080/api/task/recibir/"+id_task;
        //String postUrl = "http://192.168.68.109:8080/api/task/recibir/"+id_task;
        String postUrl = "https://to-do-list-felix.herokuapp.com/api/task/recibir/"+id_task;
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest= new StringRequest(Request.Method.DELETE, postUrl,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);

    }

    public static void cargaTareas(Context context){
        DatabaseHandler dbHandler = new DatabaseHandler(context);
        SQLiteDatabase db= dbHandler.getWritableDatabase();

        RequestQueue requestTask = Volley.newRequestQueue(context);
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
        requestTask.add(jsonArrayRequestTask);
    }

    public static void cargaUsuarios(Context context) {
        DatabaseHandler dbHandler = new DatabaseHandler(context);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        RequestQueue requestUser = Volley.newRequestQueue(context);
        //JsonArrayRequest jsonArrayRequestUser = new JsonArrayRequest("http://192.168.90.86:8080/api/user/listar", new Response.Listener<JSONArray>() {
        //JsonArrayRequest jsonArrayRequestUser = new JsonArrayRequest("http://192.168.68.109:8080/api/user/listar", new Response.Listener<JSONArray>() {
            JsonArrayRequest jsonArrayRequestUser = new JsonArrayRequest("https://to-do-list-felix.herokuapp.com/api/user/listar", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        int id_user = jsonObject.getInt("id_user");
                        String email = jsonObject.getString("email");
                        String password = jsonObject.getString("password");
                        String username = jsonObject.getString("username");
                        ContentValues cv = new ContentValues();
                        cv.put("id_user", id_user);
                        cv.put("email", email);
                        cv.put("password", password);
                        cv.put("username", username);
                        db.insert("users", null, cv);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestUser.add(jsonArrayRequestUser);
    }
}
