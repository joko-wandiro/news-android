package example.com.news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.EditText;
import android.widget.TextView;
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

public class PostActivity extends AppCompatActivity {
    public String URL_POST = "http://192.168.1.6/news/public/api/posts/view/";
    TextView label_post_title;
    TextView label_post_date;
    TextView label_post_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        loadPost();
    }

    private void loadPost() {
        String id = this.getIntent().getExtras().getString("id");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_POST + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject record = new JSONObject(response);
                            label_post_title = (TextView) findViewById(R.id.label_post_title);
                            label_post_date = (TextView) findViewById(R.id.label_post_date);
                            label_post_content = (TextView) findViewById(R.id.label_post_content);
                            label_post_title.setText(record.getString("title"));
                            label_post_date.setText(record.getString("published_at"));
                            label_post_content.setText(Html.fromHtml(record.getString("content")));
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
}
