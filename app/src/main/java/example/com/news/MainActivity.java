package example.com.news;

import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String URL_CATEGORIES = "http://192.168.1.6/news/public/api/categories";
    private static final String URL_POSTS = "http://192.168.1.6/news/public/api/posts";
    private static final String URL_POSTS_CATEGORY = "http://192.168.1.6/news/public/api/posts/category/";

    ListView posts_listview;
    private List<Post> postList;
    long id = 0;
    Post post;
    int page = 1;
    int total_page;
    Button btn_prev;
    Button btn_next;
    TabLayout tablayout;
    Map<String, String> categories = new HashMap<String, String>();
    int id_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tablayout = findViewById(R.id.tablayout);
        tablayout.addTab(tablayout.newTab().setText("Terbaru"));
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    JSONObject record = (JSONObject) tab.getTag();
                    page = 1;
                    if (record == null) {
                        loadPosts(page);
                    } else {
                        id_category = record.getInt("categories.id");
                        loadPostsCategory(id_category, page);
                    }
                    Log.i("id_category", Integer.toString(id_category));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        btn_prev = findViewById(R.id.btn_prev);
        btn_next = findViewById(R.id.btn_next);
        posts_listview = findViewById(R.id.posts_listview);
        posts_listview.setClickable(true);
        posts_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id_selection) {
                id = id_selection;
                post = (Post) posts_listview.getAdapter().getItem(position);
                Log.i("record", post.getId());
                display_post(post);
            }
        });
        loadCategories();
        loadPosts(page);
    }

    private void loadCategories() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CATEGORIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject output = new JSONObject(response);
                            JSONArray records = output.getJSONArray("records");
                            for (int i = 0; i < records.length(); i++) {
                                JSONObject record = records.getJSONObject(i);
                                categories.put(record.getString("categories.name"), record.getString("categories.id"));
                                tablayout.addTab(tablayout.newTab().setText(record.getString("categories.name")).setTag(record));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        setButtonEnabled();
    }

    private void loadPostsCategory(int id_category, int page) {
        String URL = URL_POSTS_CATEGORY + id_category + "?page=" + page;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject output = new JSONObject(response);
                            total_page = output.getInt("total_page");
                            JSONArray records = output.getJSONArray("records");
                            postList = new ArrayList<>();
                            for (int i = 0; i < records.length(); i++) {
                                JSONObject record = records.getJSONObject(i);
                                Post post = new Post(record.getString("posts.id"),
                                        record.getString("posts.title"),
                                        record.getString("posts.published_at"),
                                        record.getString("image"));

                                postList.add(post);
                            }
                            ListViewAdapter adapter = new ListViewAdapter(postList, getApplicationContext());
                            posts_listview.setAdapter(adapter);
                            setButtonEnabled();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loadPosts(int page) {
        String URL = URL_POSTS + "?page=" + page;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject output = new JSONObject(response);
                            total_page = output.getInt("total_page");
                            JSONArray records = output.getJSONArray("records");
                            postList = new ArrayList<>();
                            for (int i = 0; i < records.length(); i++) {
                                JSONObject record = records.getJSONObject(i);
                                Post post = new Post(record.getString("posts.id"),
                                        record.getString("posts.title"),
                                        record.getString("posts.published_at"),
                                        record.getString("image"));

                                postList.add(post);
                            }
                            ListViewAdapter adapter = new ListViewAdapter(postList, getApplicationContext());
                            posts_listview.setAdapter(adapter);
                            setButtonEnabled();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void display_post(Post post) {
        String id = post.getId();
        Intent intent = new Intent(MainActivity.this, PostActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
        Log.i("action", "Post");
    }

    public void setButtonEnabled() {
        if (page == 1) {
            btn_prev.setEnabled(false);
        } else {
            btn_prev.setEnabled(true);
        }
        if (page == total_page) {
            btn_next.setEnabled(false);
        } else {
            btn_next.setEnabled(true);
        }
    }

    public void prev(View view) {
        if (page > 1) {
            page--;
            loadPosts(page);
        }
    }

    public void next(View view) {
        if (page < total_page) {
            page++;
            loadPosts(page);
        }
    }
}

