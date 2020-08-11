package com.example.arcadi.treballfigrau.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.CalendarContract;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.arcadi.treballfigrau.R;
import com.example.arcadi.treballfigrau.activity.LoginActivity;
import com.example.arcadi.treballfigrau.activity.ShowTask;
import com.example.arcadi.treballfigrau.databaseHelpers.RequestHandler;
import com.example.arcadi.treballfigrau.general.Constants;
import com.example.arcadi.treballfigrau.general.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Task> list;
    List<Task> tmp_list;
    Context context;
    private final int SHOW_MENU = 1;
    private final int HIDE_MENU = 2;

    public RecyclerAdapter(Context context, List<Task> articlesList) {
        this.list = articlesList;
        this.context = context;
    }



    @Override
    public int getItemViewType(int position) {
        if(list.get(position).isShowMenu()){
            return SHOW_MENU;
        }else{
            return HIDE_MENU;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(viewType==SHOW_MENU){
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_menu, parent, false);
            return new MenuViewHolder(v);
        }else{
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list, parent, false);
            return new MyViewHolder(v);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final Task entity = list.get(position);
        if(holder instanceof MyViewHolder){
            ((MyViewHolder)holder).title.setText(entity.getName());
            ((MyViewHolder)holder).description.setText(entity.getDescription());
            ((MyViewHolder)holder).bindView(position);
            ((MyViewHolder)holder).container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showMenu(position);
                    return true;
                }
            });
            ((MyViewHolder)holder).container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ShowTask.class);
                 //   i.putExtra("sampleObject", entity);
                  //  startActivity(i);
                    int id = entity.getId();
                     context.startActivity(new Intent(context, ShowTask.class).putExtra("Task",entity.getId()));
                }
            });
        }
        if(holder instanceof MenuViewHolder){

            ((MenuViewHolder)holder).complete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    entity.setCompleted(1);
                                    Task_Completed(entity.getId(), entity.getPoints());
                                    updateTaskList (list);
                                    closeMenu();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }
            });

            ((MenuViewHolder)holder).undo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    if (entity.getCompleted() == 1 ) {
                                        entity.setCompleted(0);
                                        //((MyViewHolder) holder).container.setBackgroundColor(Color.parseColor("#567845"));
                                        Undo_Complete(entity.getId(), entity.getPoints());
                                        updateTaskList(list);
                                    }
                                    closeMenu();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }
            });
            ((MenuViewHolder)holder).delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    delete_task(entity.getId());
                                    list.remove(entity);
                                    updateTaskList (list);
                                    closeMenu();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }
            });

        }

    }


    public void updateTaskList(List<Task> newlist) {
        List<Task> tmp_list = new ArrayList<Task>(newlist);
        list.clear();
        list.addAll(tmp_list);
        Collections.copy(list,tmp_list);
        this.notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void showMenu(int position) {
        for(int i=0; i<list.size(); i++){
            list.get(i).setShowMenu(false);
        }
        list.get(position).setShowMenu(true);
        notifyDataSetChanged();
    }


    public boolean isMenuShown() {
        for(int i=0; i<list.size(); i++){
            if(list.get(i).isShowMenu()){
                return true;
            }
        }
        return false;
    }

    public void closeMenu() {
        for(int i=0; i<list.size(); i++){
            list.get(i).setShowMenu(false);
        }
         notifyDataSetChanged();

    }

    public  class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,description;
        ConstraintLayout container;
         LinearLayout background;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            container = itemView.findViewById(R.id.container);
            description = itemView.findViewById(R.id.descritpion);
           background = (LinearLayout) itemView.findViewById((R.id.linearLayout3));

        }

        public  void bindView(int pos) {
            int value = list.get(pos).getCompleted();

            if(value == 1 ) {
              container.setBackgroundColor (ContextCompat.getColor(context, R.color.light_green));
            } else
                container.setBackgroundColor(ContextCompat.getColor(context, R.color.default1));
        }
    }
    public class MenuViewHolder extends RecyclerView.ViewHolder{
         ImageView   complete, delete, undo;
        ConstraintLayout container;
        public MenuViewHolder(View view){
            super(view);
            complete = view.findViewById(R.id.Complete);
            undo = view.findViewById(R.id.Undo);
            container = itemView.findViewById(R.id.container);
            delete = view.findViewById(R.id.Delete);
        }
    }

    private void delete_task(final Integer id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_DELETE_TASK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                         //   Toast.makeText(getApplicationContext(), "Task registered successfully" ,  Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                     //   Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Id", String.valueOf(id));
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void Undo_Complete(final Integer id, final Integer points) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_UPDATE_TASK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            SharedPrefManager.getInstance(context).subPoints(points);
                            //   Toast.makeText(getApplicationContext(), "Task registered successfully" ,  Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //   Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Id", String.valueOf(id));
                params.put("Points", String.valueOf(points));
                params.put("Id_User",  String.valueOf(SharedPrefManager.getInstance(context).getID()));
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void Task_Completed(final Integer id, final Integer Points) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.COMPLETE_TASK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            SharedPrefManager.getInstance(context).addPoints(Points);
                            //   Toast.makeText(getApplicationContext(), "Task registered successfully" ,  Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //   Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("PID", String.valueOf(id));
                params.put("Points", String.valueOf(Points));
                params.put("Id_User",  String.valueOf(SharedPrefManager.getInstance(context).getID()));
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
        SharedPrefManager.getInstance(context).addPoints(Points);
    }


}

