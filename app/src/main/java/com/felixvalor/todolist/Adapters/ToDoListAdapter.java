package com.felixvalor.todolist.Adapters;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.felixvalor.todolist.Model.TaskModel;
import com.felixvalor.todolist.R;
import com.felixvalor.todolist.Task_PopActivity;
import com.felixvalor.todolist.Utils.DatabaseHandler;
import com.felixvalor.todolist.Utils.ComunicacionCMS;
import com.felixvalor.todolist.Utils.dbTask;

import java.util.ArrayList;
import java.util.List;


public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.TarjetaViewHolder>{

    ArrayList<TaskModel> datos = new ArrayList<>();
    ArrayList<TaskModel> todoListFull=new ArrayList<>();



    public interface OnItemClickListener{
        void onItemClick(TaskModel item);
    }

    public ToDoListAdapter(ArrayList<TaskModel> datos) {
        this.datos = datos;
    }

    @Override
    public TarjetaViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.new_task_popup, viewGroup, false);
        TarjetaViewHolder tvh = new TarjetaViewHolder(itemView);
        return tvh;
    }

    @Override
    public void onBindViewHolder(TarjetaViewHolder viewHolder, int pos) {
        TaskModel item = datos.get(pos);
        viewHolder.bindTarjetas(item);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public static class TarjetaViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CheckBox task;
        public TarjetaViewHolder(View itemView) {
            super(itemView);

            task=itemView.findViewById(R.id.cardItemCheckBox);
            name=itemView.findViewById(R.id.cardItemTitle);
        }

        public void bindTarjetas(TaskModel tdlm) {

            name.setText(tdlm.getTitle());
            task.setChecked(toBoolean(tdlm.getStatus()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i=new Intent(itemView.getContext(), Task_PopActivity.class);
                    i.putExtra("isUpdate",true);
                    i.putExtra("id_task",tdlm.getIdTask());
                    i.putExtra("title",tdlm.getTitle());
                    i.putExtra("description",tdlm.getDescription());
                    i.putExtra("status",tdlm.getStatus());
                    itemView.getContext().startActivity(i);
                    Toast.makeText(itemView.getContext(), "ID:"+tdlm.getIdTask(), Toast.LENGTH_LONG).show();
                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    TarjetaViewHolder holder = new TarjetaViewHolder(view);
                    dbTask db = new dbTask(view.getContext());
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("¿Desea eliminar la tarea \""+tdlm.getTitle()+"\"?")
                            .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    System.out.println(i);
                                    System.out.println(holder.getAdapterPosition());
                                    System.out.println("El id es"+tdlm.getIdTask());
                                    ComunicacionCMS.borrarTareaCMS(tdlm.getIdTask(), itemView.getContext());
                                    db.eliminarTarea(tdlm.getIdTask());

                                    db.close();
                                    Toast.makeText(itemView.getContext(), "Registro eliminado", Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {


                                }
                            }).show();
                    return true;
                }
            });


            task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    DatabaseHandler db = new DatabaseHandler(itemView.getContext());
                    if (isChecked){
                        db.updateStatus(tdlm.getIdTask(),1);
                        //EnviarCMS.annadirTareasCMS(tdlm,tdlm.getId_user(),itemView.getContext());3
                        ComunicacionCMS.modificarTareasCMS(tdlm,tdlm.getId_user(),itemView.getContext());
                    }else{
                        db.updateStatus(tdlm.getIdTask(),0);
                        ComunicacionCMS.modificarTareasCMS(tdlm,tdlm.getId_user(),itemView.getContext());
                    }

                }
            });
        }

    }
    private static boolean toBoolean(int n) {
        return n != 0;
    }

    public Filter getFilter() {
        return exampleFilter;
    }


    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {


            List<TaskModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(datos);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (TaskModel item : datos) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            datos.clear();

            datos.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
}