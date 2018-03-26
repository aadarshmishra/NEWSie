package com.example.aadarsh.newsie;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchResult extends AppCompatActivity {

    private List<News> newsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    boolean firstClick = true;
    JSONArray article = null;
    private TextToSpeech speech;

    FloatingActionButton fab;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_apps_black_24dp);

        getSupportActionBar().setTitle(query.substring(0, 1).toUpperCase() + query.substring(1));

        fab = findViewById(R.id.fab2);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstClick) {
                    playback();
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_stop_black_24dp));
                    firstClick = false;
                }
                else {
                    speech.stop();
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
                    firstClick = true;
                }
            }
        });

        speech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    speech.setLanguage(new Locale("en", "IN"));
                }
            }
        });

        Intent intent = getIntent();
        query = intent.getStringExtra("query");

        recyclerView = findViewById(R.id.my_recycler_view1);

        adapter = new NewsAdapter(newsList, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        getNewsData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return true;
    }

    public void getNewsData() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.d("news", "get data");
        String url = "https://newsapi.org/v2/top-headlines?q=" + query + "&apiKey=9213b6cc10d542ac9c0b8ebd9b5d80df";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                prepareNewsData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void prepareNewsData(JSONObject response) {

        try {
            article = response.getJSONArray("articles");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0 ; i < article.length() ; i++) {
            try {
                JSONObject object = article.getJSONObject(i);
                String publisher = object.getJSONObject("source").getString("name");
                String title = object.getString("title");
                String time = object.getString("publishedAt");
                String urlImage = object.getString("urlToImage");
                String url = object.getString("url");

                News news = new News(title,publisher,time,urlImage,url);
                newsList.add(news);
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void playback() {
        try {
            for (int i = 0 ; i < article.length() ; i++) {
                JSONObject object = article.getJSONObject(i);
                String title = object.getString("title");
                speech.speak(title, TextToSpeech.QUEUE_ADD, null);
                speech.playSilentUtterance(1500, TextToSpeech.QUEUE_ADD, null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
