package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity4 extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        listView = findViewById(R.id.listView);

        DatabaseReference peopleReference = FirebaseDatabase.getInstance().getReference()
                .child("needhelp");

        // Now set the adapter with a given layout
        listView.setAdapter(new FirebaseListAdapter<UserInfo>(this, UserInfo.class,
                android.R.layout.simple_list_item_2, peopleReference) {

            // Populate view as needed
            @Override
            protected void populateView(View view, UserInfo person, int position) {
                ((TextView) view.findViewById(android.R.id.text1)).setText(person.getEmail());
                ((TextView) view.findViewById(android.R.id.text2)).setText(person.getLatitude() + " " + person.getLongitude());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserInfo person = (UserInfo) adapterView.getItemAtPosition(i);
                Toast.makeText(MainActivity4.this, person.getEmail(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity4.this, MainActivity5.class);
                intent.putExtra("lat", person.getLatitude());
                intent.putExtra("long", person.getLongitude());
                startActivity(intent);
            }
        });
    }
}