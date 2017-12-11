package com.example.guerra.repmanager_aaa;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Finances extends AppCompatActivity {


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
    public List<String> contaNome = new ArrayList<String>();
    public List<String> contaValor = new ArrayList<String>();
    public List<String> contaVencimento = new ArrayList<String>();
    JSONArray financeList;
    public TextView memberDate;
    DatePickerDialog.OnDateSetListener mDataSetListener;

    public void setDate(View view){


        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                Finances.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDataSetListener,
                year, month, day
        );



        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void addDespesa(View view) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Finances.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_new_gasto, null);

        final EditText editDespesaNome = (EditText) mView.findViewById(R.id.nomeDespesa);
        final EditText editDespesaValor = (EditText) mView.findViewById(R.id.valorDespesa);
        Button addDespesa = (Button) mView.findViewById(R.id.addDespesa);



        String path = getFilesDir().getAbsolutePath() + File.separator + "RepManager";
        File jsonFile = new File(path + "/finances.json");

        if (!jsonFile.exists()){

            File projDir = new File(path);
            if (!projDir.exists())
                projDir.mkdirs();

            jsonFile = new File(path + "/finances.json");
            String[] save = { "{\"finance\":[{\"name\":\"Eletricidade\",\"valor\":\"570,00\",\"vencimento\":\"10/12/2017\"}]}" };
            Save(jsonFile, save);
        }


        memberDate = (TextView) mView.findViewById(R.id.vencimentoDespesa);


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

        addDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(!memberDate.getText().toString().isEmpty() && !editDespesaValor.getText().toString().isEmpty()&& !editDespesaNome.getText().toString().isEmpty()){


                    JSONObject despesaObj = new JSONObject();

                    String path = getFilesDir().getAbsolutePath() + File.separator + "RepManager";
                    File projDir = new File(path);
                    if (!projDir.exists())
                        projDir.mkdirs();



                    try {
                        JSONObject despesa = new JSONObject();
                        despesa.put("name", editDespesaNome.getText().toString());
                        despesa.put("valor", editDespesaValor.getText().toString());
                        despesa.put("vencimento", memberDate.getText().toString());
                        financeList.put(despesa);

                        contaNome.add(editDespesaNome.getText().toString());
                        contaValor.add(editDespesaValor.getText().toString());
                        contaVencimento.add(memberDate.getText().toString());
                        despesaObj.put("finance", financeList);

                        File file = new File(path + "/finances.json");
                        String[] save = { despesaObj.toString() };
                        Save(file, save);

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    ListView listView = (ListView)findViewById(R.id.financesView);
                    financesCustomAdapter newAdapter = new financesCustomAdapter();
                    listView.setAdapter(newAdapter);


                    dialogPop.dismiss();

                }
                else{
                    String path = getFilesDir().getAbsolutePath() + File.separator + "RepManager";

                    File file = new File(path + "/finances.json");
                    String [] moman = Load(file);

                    Log.d("MyApp",moman[0]);
                }
            }
        });

        dialogPop.show();

        mBuilder.setView(mView);
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
        setContentView(R.layout.activity_finances);

        ListView financesListView = (ListView)findViewById(R.id.financesView);


        String path = getFilesDir().getAbsolutePath() + File.separator + "RepManager";
        File jsonFile = new File(path + "/finances.json");

        if (!jsonFile.exists()){

            File projDir = new File(path);
            if (!projDir.exists())
                projDir.mkdirs();

            String[] save = { "{\"finance\":[{\"name\":\"Eletricidade\",\"valor\":\"570,00\",\"vencimento\":\"10/12/2017\"}]}" };
            Save(jsonFile, save);
        }

        try {

            JSONObject obj = new JSONObject(loadJSONFromAsset(path + "/finances.json"));
            financeList = obj.getJSONArray("finance");
            for (int i = 0; i < financeList.length(); i++){

                JSONObject jsonObj = financeList.getJSONObject(i);
                contaNome.add(jsonObj.getString("name"));
                contaValor.add(jsonObj.getString("valor"));
                contaVencimento.add(jsonObj.getString("vencimento"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        financesCustomAdapter financesCustomAdapter = new financesCustomAdapter();
        financesListView.setAdapter(financesCustomAdapter);
        fab = (FloatingActionButton)findViewById(R.id.fabf);

    }
    class financesCustomAdapter extends BaseAdapter {

        @Override
        public int getCount(){
            return financeList.length();
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

            view = getLayoutInflater().inflate(R.layout.custom_finance_layout, null);
//          ImageView imageView = (ImageView)view.findViewById(R.id.financeIcon);
            TextView financeName = (TextView)view.findViewById(R.id.nomeConta);
            TextView financeValue = (TextView)view.findViewById(R.id.valorConta);
            TextView financeDate = (TextView)view.findViewById(R.id.vencimentoConta);

            financeName.setText(contaNome.get(i));
            financeValue.setText(contaValor.get(i));
            financeDate.setText(contaVencimento.get(i));

            ImageButton delete = (ImageButton) view.findViewById(R.id.removeButtomFinance);

            delete.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {

                    try {
                        contaNome.remove(i);
                        contaValor.remove(i);
                        contaVencimento.remove(i);
                        financeList.remove(i);

                        String path = getFilesDir().getAbsolutePath() + File.separator + "RepManager";
                        JSONObject despesaObj = new JSONObject();
                        despesaObj.put("finance", financeList);
                        File file = new File(path + "/finances.json");
                        String[] save = { despesaObj.toString() };
                        Save(file, save);
                        ListView listView = (ListView)findViewById(R.id.financesView);
                        financesCustomAdapter newAdapter = new financesCustomAdapter();
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
