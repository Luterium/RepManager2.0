package com.example.guerra.repmanager_aaa;


import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.security.AccessController.getContext;

public class Tasks extends AppCompatActivity {

    public String loadJSONFromAsset(String path) {
        String json = null;
        try {
            File initialFile = new File(path);
            InputStream is =  new FileInputStream(initialFile);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }


    public FloatingActionButton fab;
    public List<String> responsibles = new ArrayList<String>();
    public List<String> tasks = new ArrayList<String>();
    public List<String> dates = new ArrayList<String>();
    JSONArray taskList;
    public TextView memberDate;
    DatePickerDialog.OnDateSetListener mDataSetListener;

    public void setDate(View view){


        Log.d("MyApp","suck my dick222");
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                Tasks.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDataSetListener,
                year, month, day
        );



        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public void addTask(View view) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Tasks.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_new_task, null);

        final EditText editTaskDescription = (EditText) mView.findViewById(R.id.editTaskDescription);
        Button addTask = (Button) mView.findViewById(R.id.addTask);

        final Spinner memberSpinner = (Spinner) mView.findViewById(R.id.memberSpinner);
        List<String> membersList = new ArrayList<String>();




        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Tasks.this, android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
            }

        };


        String path = getFilesDir().getAbsolutePath() + File.separator + "RepManager";
        File jsonFile = new File(path + "/members.json");

        if (!jsonFile.exists()){

            File projDir = new File(path);
            if (!projDir.exists())
                projDir.mkdirs();

            jsonFile = new File(path + "/members.json");
            String[] save = { "{\"members\":[{\"name\":\"Jesus Cristo\",\"ingressDate\":\"25/12/0000\"}]}" };
            Save(jsonFile, save);
        }

        try {

            JSONObject obj = new JSONObject(loadJSONFromAsset(path + "/members.json"));
            JSONArray memberList = obj.getJSONArray("members");
            for (int i = 0; i < memberList.length(); i++){
                JSONObject jsonObj = memberList.getJSONObject(i);
                adapter.add(jsonObj.getString("name"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        adapter.add("Membro");
        memberSpinner.setAdapter(adapter);
        memberSpinner.setSelection(adapter.getCount());



        memberDate = (TextView) mView.findViewById(R.id.limiteData);


        mDataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                memberDate.setText(date);
            }
        };
        mBuilder.setView(mView);
        final AlertDialog dialogPop = mBuilder.create();

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(!memberSpinner.getSelectedItem().toString().equals("Membro") && !memberDate.getText().toString().isEmpty() && !editTaskDescription.getText().toString().isEmpty()){


                    JSONObject taskObj = new JSONObject();

                    String path = getFilesDir().getAbsolutePath() + File.separator + "RepManager";
                    File projDir = new File(path);
                    if (!projDir.exists())
                        projDir.mkdirs();



                    try {
                        JSONObject member = new JSONObject();
                        member.put("responsible", memberSpinner.getSelectedItem().toString());
                        member.put("task", editTaskDescription.getText().toString());
                        member.put("date", memberDate.getText().toString());
                        taskList.put(member);
                        responsibles.add(memberSpinner.getSelectedItem().toString());
                        tasks.add(editTaskDescription.getText().toString());
                        dates.add(memberDate.getText().toString());
                        taskObj.put("tasks", taskList);

                        File file = new File(path + "/tasks.json");
                        String[] save = { taskObj.toString() };
                        Save(file, save);

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    ListView listView = (ListView)findViewById(R.id.listView);
                    CustomAdapter newAdapter = new CustomAdapter();
                    listView.setAdapter(newAdapter);


                    dialogPop.dismiss();
                    Log.d("MyApp","suck my dick");

                }
                else{
                    String path = getFilesDir().getAbsolutePath() + File.separator + "RepManager";

                    File file = new File(path + "/tasks.json");
                    String [] man = Load(file);

                    Log.d("MyApp",man[0]);
                }
            }
        });

        dialogPop.show();

        mBuilder.setView(mView);
        Log.d("MyApp","I am hereaaaaa");
    }

    public static String[] Load(File file)
    {
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        String test;
        int anzahl=0;
        try
        {
            while ((test=br.readLine()) != null)
            {
                anzahl++;
            }
        }
        catch (IOException e) {e.printStackTrace();}

        try
        {
            fis.getChannel().position(0);
        }
        catch (IOException e) {e.printStackTrace();}

        String[] array = new String[anzahl];

        String line;
        int i = 0;
        try
        {
            while((line=br.readLine())!=null)
            {
                array[i] = line;
                i++;
            }
        }
        catch (IOException e) {e.printStackTrace();}
        return array;
    }

    public static void Save(File file, String[] data)
    {
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        try
        {
            try
            {
                for (int i = 0; i<data.length; i++)
                {
                    fos.write(data[i].getBytes());
                    if (i < data.length-1)
                    {
                        fos.write("\n".getBytes());
                    }
                }
            }
            catch (IOException e) {e.printStackTrace();}
        }
        finally
        {
            try
            {
                fos.close();
            }
            catch (IOException e) {e.printStackTrace();}
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        ListView listView = (ListView)findViewById(R.id.listView);



        String path = getFilesDir().getAbsolutePath() + File.separator + "RepManager";
        File jsonFile = new File(path + "/tasks.json");

        if (!jsonFile.exists()){

            File projDir = new File(path);
            if (!projDir.exists())
                projDir.mkdirs();

            String[] save = { "{\"tasks\" : [{\"responsible\" : \"Jesus Cristo\",\"task\" : \"Primeira Task\",\"date\" : \"25/12/0000\"}]}" };
            Save(jsonFile, save);
        }

        try {

            JSONObject obj = new JSONObject(loadJSONFromAsset(path + "/tasks.json"));
            taskList = obj.getJSONArray("tasks");
            for (int i = 0; i < taskList.length(); i++){

                JSONObject jsonObj = taskList.getJSONObject(i);
                responsibles.add(jsonObj.getString("responsible"));
                tasks.add(jsonObj.getString("task"));
                dates.add(jsonObj.getString("date"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
        fab = (FloatingActionButton)findViewById(R.id.fab);

    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount(){
            return taskList.length();
        }

        @Override
        public Object getItem(int i){
            return null;
        }

        @Override
        public long getItemId(int i){
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup){

            view = getLayoutInflater().inflate(R.layout.custom_task_layout, null);
//          ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
            TextView taskView = (TextView)view.findViewById(R.id.taskView);
            TextView responsibleViewContent = (TextView)view.findViewById(R.id.responsibleViewContent);
            TextView dataViewContent = (TextView)view.findViewById(R.id.dataViewContent);

            responsibleViewContent.setText(responsibles.get(i));
            taskView.setText(tasks.get(i));
            dataViewContent.setText(dates.get(i));
            ImageButton delete = (ImageButton) view.findViewById(R.id.removeButtom);

            delete.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    Log.d("MyApp","suck my dick" + i);

                    try {

                        responsibles.remove(i);
                        tasks.remove(i);
                        dates.remove(i);
                        taskList.remove(i);

                        String path = getFilesDir().getAbsolutePath() + File.separator + "RepManager";
                        JSONObject taskObj = new JSONObject();
                        taskObj.put("tasks", taskList);
                        File file = new File(path + "/tasks.json");
                        String[] save = { taskObj.toString() };
                        Save(file, save);
                        ListView listView = (ListView)findViewById(R.id.listView);
                        CustomAdapter newAdapter = new CustomAdapter();
                        listView.setAdapter(newAdapter);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });


            return view;
        }

    }
}

