package com.example.guerra.repmanager_aaa;


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
import java.util.List;


public class marketList extends AppCompatActivity {

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
    public List<String> produtoNome = new ArrayList<String>();
    public List<String> produtoQuantidade = new ArrayList<String>();
    JSONArray listMarket;

    public void addProduct(View view){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(marketList.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_new_market, null);

        final EditText editProductName = (EditText) mView.findViewById(R.id.nomeDoProduto);


        final EditText editProductQuantity = (EditText) mView.findViewById(R.id.quantidadeDoProduto);
        Button addProduct = (Button) mView.findViewById(R.id.addProduct);

        String path = getFilesDir().getAbsolutePath() + File.separator + "RepManager";
        File jsonFile = new File(path + "marketList.json");

        if (!jsonFile.exists()){

            File projDir = new File(path);
            if (!projDir.exists())
                projDir.mkdirs();

            jsonFile = new File(path + "/marketList.json");
            String[] save = { "{\"marketList\":[{\"name\":\"Frango\",\"quantidade\":\"5\"}]}" };
            Save(jsonFile, save);
        }
        mBuilder.setView(mView);
        final AlertDialog dialogPop = mBuilder.create();


        addProduct.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!editProductName.getText().toString().isEmpty() && !editProductQuantity.getText().toString().isEmpty()){
                    JSONObject prodObj = new JSONObject();

                    String path = getFilesDir().getAbsolutePath( ) + File.separator + "RepManager";
                    File projDir = new File(path);
                    if(!projDir.exists())
                        projDir.mkdirs();

                    try{
                        JSONObject product = new JSONObject();
                        product.put("name", editProductName.getText().toString());
                        product.put("quantidade", editProductQuantity.getText().toString());
                        listMarket.put(product);
                        produtoNome.add(editProductName.getText().toString());
                        produtoQuantidade.add(editProductQuantity.getText().toString());
                        prodObj.put("marketList", listMarket);

                        File file = new File(path + "/marketList.json");
                        String[] save = {prodObj.toString()};
                        Save(file,save);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    ListView listView = (ListView)findViewById(R.id.marketList);
                    marketCustomAdapter newAdapter = new marketCustomAdapter();
                    listView.setAdapter(newAdapter);

                    dialogPop.dismiss();
                }
                else{
                    String path = getFilesDir().getAbsolutePath() + File.separator + "RepManager";
                    File file = new File(path+ "/marketList.json");
                    String [] women = Load(file);
                    Log.d("MeuApp", women[0]);
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
        setContentView(R.layout.activity_market_list);

        ListView marketListView = (ListView)findViewById(R.id.marketList);

        String path = getFilesDir().getAbsolutePath() + File.separator + "RepManager";
        File jsonFile = new File(path + "/marketList.json");

        if (!jsonFile.exists()){

            File projDir = new File(path);
            if (!projDir.exists())
                projDir.mkdirs();

            String[] save = { "{\"marketList\":[{\"name\":\"Frango\",\"quantidade\":\"5\"}]}" };
            Save(jsonFile, save);
        }

        try{
            JSONObject obj = new JSONObject(loadJSONFromAsset(path + "/marketList.json"));
            listMarket = obj.getJSONArray("marketList");
            for(int i = 0; i < listMarket.length(); i++){
                JSONObject jsonObj = listMarket.getJSONObject(i);
                produtoNome.add(jsonObj.getString("name"));
                produtoQuantidade.add(jsonObj.getString("quantidade"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        marketCustomAdapter  marketCustomAdapter  = new marketCustomAdapter();
        marketListView.setAdapter(marketCustomAdapter);
        fab = (FloatingActionButton)findViewById(R.id.fabo);


    }

    class marketCustomAdapter extends BaseAdapter{
        @Override
        public int getCount(){
            return listMarket.length();
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
            view = getLayoutInflater().inflate(R.layout.custom_market_screen_layout, null);

            ImageView imageView = (ImageView)view.findViewById(R.id.marketItem);
            TextView productName = (TextView)view.findViewById(R.id.nomeProduto);
            TextView productQuantity = (TextView)view.findViewById(R.id.quantidadeProduto);


            productName.setText(produtoNome.get(i));
            productQuantity.setText(produtoQuantidade.get(i));
            ImageButton delete = (ImageButton) view.findViewById(R.id.removeButtomMarket);

            delete.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                try {

                    produtoNome.remove(i);
                    produtoQuantidade.remove(i);
                    listMarket.remove(i);

                    String path = getFilesDir().getAbsolutePath() + File.separator + "RepManager";
                    JSONObject prodObj = new JSONObject();
                    prodObj.put("marketList", listMarket);
                    File file = new File(path + "/marketList.json");
                    String[] save = { prodObj.toString() };
                    Save(file, save);
                    ListView listView = (ListView)findViewById(R.id.marketList);
                    marketCustomAdapter newAdapter = new marketCustomAdapter();
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