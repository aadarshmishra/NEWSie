package com.example.aadarsh.newsie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Categories extends AppCompatActivity {

    String[] categories = {"Trending","Business","Entertainment","General","Health","Science"
                            ,"Sports","Technology"};

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        listView = findViewById(R.id.categories);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,categories);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String product = ((TextView) view).getText().toString();

                if (product.equals("Trending")) {
                    Intent intent = new Intent(Categories.this,HomeActivity.class);
                    finish();
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(Categories.this, CategorisedActivity.class);
                    intent.putExtra("category", product);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }
}
