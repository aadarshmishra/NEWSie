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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Aadarsh on 3/24/2018.
 */

public class CategorisedActivity extends AppCompatActivity {

    JSONArray article = null;
    private List<News> newsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextToSpeech speech;
    private NewsAdapter adapter;
    private String category;
    boolean firstClick = true;
    private FloatingActionButton fab;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorised);android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_apps_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategorisedActivity.this, Categories.class);
                finish();
                startActivity(intent);
            }
        });

        category = getIntent().getStringExtra("category");

        getSupportActionBar().setTitle(category);

        fab = findViewById(R.id.fab1);

        speech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    speech.setLanguage(new Locale("en", "IN"));
                }
            }
        });

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

        shimmerFrameLayout = findViewById(R.id.shimmerframe);

        recyclerView = findViewById(R.id.my_recycler_view2);

        adapter = new NewsAdapter(newsList, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Intent intent1 = new Intent(CategorisedActivity.this,SearchResult.class);
            intent1.putExtra("query",query);
            finish();
            startActivity(intent1);
        }
        getNewsData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                Log.d("nws","refresh");
                newsList.clear();
                this.recreate();
                break;

            default:
                break;
        }
        return true;
    }

    public void getNewsData() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://newsapi.org/v2/top-headlines?country=in&category=" + category + "&apiKey=9213b6cc10d542ac9c0b8ebd9b5d80df";
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
                shimmerFrameLayout.startShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
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

    @Override
    protected void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmerAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmerAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speech.stop();
    }
}