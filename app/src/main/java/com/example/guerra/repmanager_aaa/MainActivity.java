package com.example.guerra.repmanager_aaa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void openTasks(View view)
    {
        Intent intent = new Intent(MainActivity.this, Tasks.class);
        startActivity(intent);
    }

    public void openRepublicInfo(View view)
    {
        Intent intent = new Intent (MainActivity.this, membersScreen.class);
        startActivity(intent);
    }

    public void openMarket(View view)
    {
        Intent intent = new Intent (MainActivity.this, marketList.class);
        startActivity(intent);
    }

    public void openFinances(View view)
    {
        Intent intent = new Intent (MainActivity.this, Finances.class);
        startActivity(intent);

    }

    public void openCadastro(View view)
    {
        Intent intent = new Intent (MainActivity.this, cadastroInicial.class);
        startActivity(intent);
    }
    public void openSobre(View view)
    {
        Intent intent = new Intent (MainActivity.this, Sobre.class);
        startActivity(intent);
    }
    public void openFAQ(View view)
    {
        Intent intent = new Intent (MainActivity.this, Main2Activity.class);
        startActivity(intent);
    }
    public void openWeb(View view)
    {
        Intent intent = new Intent (MainActivity.this, webView.class);
        startActivity(intent);
    }
}
