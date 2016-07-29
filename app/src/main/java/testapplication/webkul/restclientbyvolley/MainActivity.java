package testapplication.webkul.restclientbyvolley;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;
        String url = "http://prinsikapandey.com/d0/multivendor_mobikul/api/homepage/?width=540";
//    String url = "http://cs-cartdemo.webkul.com/mobikul/cscart/api/homepage/?width=540";

    public static String TOKEN;
    private byte[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            data = ("prinsika.pandey637@webkul.com:9i739VKI20FCEhnL96CLgg7R4K6cr2a9").getBytes("UTF-8");
//            data = ("cs-carttest@webkul.com:610oroYdr0kwd7Tq2h1f8trr6C515N65").getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Spinner spinner = (Spinner) findViewById(R.id.spinner);
                    String constant = spinner.getSelectedItem().toString();
                    if (constant.equals("NO_WRAP")) {
//        Encoder flag bit to omit all line terminators
                        TOKEN = Base64.encodeToString(data, Base64.NO_WRAP);
                    } else {
//        Default values for encoder/decoder flags.
                        TOKEN = Base64.encodeToString(data, Base64.DEFAULT);
                    }

//                    final String tokenChange = TOKEN.replaceFirst("Q==", "Q");
                    String tokenChange = TOKEN.replaceFirst("", "");
//                    tokenChange = tokenChange.replaceFirst("UxN", "\nUxN");
                    ((TextView) findViewById(R.id.token)).setText(tokenChange);
//                    Log.d("NO_WRAP", Base64.NO_WRAP + "----" + TOKEN);
//                    Log.d("DEFAULT", Base64.DEFAULT + "----" + Base64.encodeToString(data, Base64.DEFAULT));

                    queue = MySingleton.getInstance(MainActivity.this).getRequestQueue();
                    final String finalTokenChange = tokenChange;
                    StringRequest strreq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String Response) {
                            try {
                                String data = (new JSONObject(Response)).toString(4);
                                ((TextView) findViewById(R.id.response)).setText(data);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(MainActivity.this, "Get Response", Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError e) {
                            e.printStackTrace();
                            ((TextView) findViewById(R.id.response)).setText(url + "----------\n \n-----------" + e + "");
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws com.android.volley.AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/json");
                            params.put("Authorization: Basic", finalTokenChange);
                            return params;
                        }
                    };

                    RetryPolicy policy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    strreq.setRetryPolicy(policy);
                    MySingleton.getInstance(MainActivity.this).addToRequestQueue(strreq);


                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
