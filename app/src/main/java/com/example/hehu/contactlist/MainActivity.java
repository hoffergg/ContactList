package com.example.hehu.contactlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openContactList(View view){
        Intent intent = new Intent(this,ContactListActivity.class);
        startActivity(intent);
    }

    public void openPickContact(View view){
        Intent intent = new Intent(this,PickContactActivity.class);
        startActivity(intent);

    }
}
