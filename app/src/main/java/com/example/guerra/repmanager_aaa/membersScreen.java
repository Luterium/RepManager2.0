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
import android.widget.Spinner;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class membersScreen extends AppCompatActivity {

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

    public FloatingActionButton fabMember;
    public JSONArray membersList;
    public List<String> names = new ArrayList<String>();
    public List<String> dates = new ArrayList<String>();
    public TextView memberDate;
    DatePickerDialog.OnDateSetListener mDataSetListener;

    public void setDate(View view){


        Log.d("MyApp","suck my dick222");
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                membersScreen.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDataSetListener,
                year, month, day
        );



        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public void addMember(View view) {
        Log.d("MyApp","entrei");


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(membersScreen.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_choose_member, null);

        final EditText editMemberName = (EditText) mView.findViewById(R.id.editMemberName);
        Button addMember = (Button) mView.findViewById(R.id.addMember);

        memberDate = (TextView) mView.findViewById(R.id.dataIngresso);

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

        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                if(!memberDate.getText().toString().isEmpty() && !editMemberName.getText().toString().isEmpty()){

                    JSONObject memberObj = new JSONObject();

                    String path = getFilesDir().getAbsolutePath() + File.separator + "RepManager";
                    File projDir = new File(path);
                    if (!projDir.exists())
                        projDir.mkdirs();

                    try {
                        JSONObject member = new JSONObject();

                        member.put("name", editMemberName.getText().toString());
                        member.put("ingressDate", memberDate.getText().toString());

                        membersList.put(member);
                        names.add(editMemberName.getText().toString());
                        dates.add(memberDate.getText().toString());
                        memberObj.put("members", membersList);

                        File file = new File(path + "/members.json");
                        String[] save = { memberObj.toString() };
                        Save(file, save);

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    ListView listView = (ListView)findViewById(R.id.membersList);
                    CustomAdapter newAdapter = new CustomAdapter();
                    listView.setAdapter(newAdapter);

                    dialogPop.dismiss();
                    Log.d("MyApp","suck my dick");

                }
                else{
                    String path = getFilesDir().getAbsolutePath() + File.separator + "RepManager";

                    File file = new File(path + "/members.json");
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
        setContentView(R.layout.activity_members_screen);
        ListView listView = (ListView)findViewById(R.id.membersList);


        String path = getFilesDir().getAbsolutePath() + File.separator + "RepManager";
        File jsonFile = new File(path + "/members.json");

        if (!jsonFile.exists()){
            File projDir = new File(path);
            if (!projDir.exists())
                projDir.mkdirs();

            String[] save = { "{\"members\":[{\"name\":\"Jesus Cristo\",\"ingressDate\":\"25/12/0000\"}]}" };
            Save(jsonFile, save);
        }
        try{
            JSONObject obj = new JSONObject(loadJSONFromAsset(path + "/members.json"));
            membersList = obj.getJSONArray("members");
            for(int i = 0; i < membersList.length(); i++){
                JSONObject jsonObj = membersList.getJSONObject(i);
                names.add(jsonObj.getString("name"));
                dates.add(jsonObj.getString("ingressDate"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
        fabMember = (FloatingActionButton)findViewById(R.id.fabMember);
        


    }

    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount(){
            return membersList.length();
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
            view = getLayoutInflater().inflate(R.layout.custom_members_screen_layout, null);

            TextView memberName = (TextView)view.findViewById(R.id.memberName);
            TextView joinDate = (TextView)view.findViewById(R.id.joinDate);

            memberName.setText(names.get(i));
            joinDate.setText(dates.get(i));

            ImageButton delete = (ImageButton) view.findViewById(R.id.removeButtom);

            delete.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    Log.d("MyApp","suck my dick" + i);

                    try {

                        names.remove(i);
                        dates.remove(i);
                        membersList.remove(i);

                        String path = getFilesDir().getAbsolutePath() + File.separator + "RepManager";
                        JSONObject memberObj = new JSONObject();
                        memberObj.put("members", membersList);
                        File file = new File(path + "/members.json");
                        String[] save = { memberObj.toString() };
                        Save(file, save);
                        ListView listView = (ListView)findViewById(R.id.membersList);
                        membersScreen.CustomAdapter newAdapter = new membersScreen.CustomAdapter();
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
