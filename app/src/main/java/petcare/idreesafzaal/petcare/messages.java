package petcare.idreesafzaal.petcare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class messages extends AppCompatActivity {
    Toolbar toolbar;
    localhost host = new localhost();
    String url = "http://" + host.api + "/andriodfiles/sendmessage.php";
    String url1 = "http://" + host.api + "/andriodfiles/getmessageoncreate.php";
    String url2 = "http://" + host.api + "/andriodfiles/recievemessage.php";

    RecyclerView recyclerView, res;
    Button sendbtn;
    EditText text;
    int length;
    String senderid, recieverid;
    List<String> sendmessage;
    List<String> senderids = new ArrayList<String>();
    List<String> list = new ArrayList<String>();
    List<String> list1 = new ArrayList<String>();
    List<String> recievedata = new ArrayList<String>();
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Intent intent = new Intent(messages.this, messagerecieveservice.class);
        startService(intent);
        recyclerView = (RecyclerView) findViewById(R.id.messagerecycleview);

        res = (RecyclerView) findViewById(R.id.recievemessage);
        res.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(messages.this));
        sendmessage = new ArrayList<String>();
        sendbtn = (Button) findViewById(R.id.sendbtn);
        text = (EditText) findViewById(R.id.sendchatview);
        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(messages.this);
        senderid = sharedPreference.getString("userId", null);
        recieverid = getIntent().getStringExtra("rid");
        Runnable message = new Runnable() {
            @Override
            public void run() {
                try {


                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                recievedata.clear();
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    recievedata.add(jsonObject.getString("message"));
                                }
                                // recievemessageholder(recievedata,messages.this)
                                recyclerView.setAdapter(new sendmessageadpter(list, recievedata, messages.this, 2));
                                if (list.size() > recievedata.size()) {
                                    length = list.size();
                                }
                                if (recievedata.size() > list.size()) {
                                    length = recievedata.size();
                                }
                                if (recievedata.size() == list.size()) {
                                    length = recievedata.size();
                                }

                                recyclerView.scrollToPosition(length - 1);
                                // recyclerView.notify();
                                //Toast.makeText(messages.this,recievedata.get(0).toString(),Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("currentid", senderid);
                            params.put("senderid", recieverid);
                            return params;
                        }
                    };
                    mysingleton.getmInstance(messages.this).addtorequestque(stringRequest);
                    //Toast.makeText(messages.this,"reapt code",Toast.LENGTH_SHORT).show();
                    handler.postDelayed(this, 3000);
                } catch (Exception e) {

                }
            }

        };
        message.run();


        buttonclicl();

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String sendermessage = text.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            list.clear();
                            senderids.clear();
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                list.add(jsonObject.getString("message"));

                            }
                            if (list.size() > recievedata.size()) {
                                length = list.size();
                            }
                            if (recievedata.size() > list.size()) {
                                length = recievedata.size();
                            }
                            if (recievedata.size() == list.size()) {
                                length = recievedata.size();
                            }
                            recyclerView.setAdapter(new sendmessageadpter(list, senderids, messages.this, 1));
                            recyclerView.scrollToPosition(length - 1);
                            text.setText("");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("senderid", senderid);
                        params.put("recieverid", recieverid);
                        params.put("messsage", sendermessage);
                        return params;
                    }
                };
                mysingleton.getmInstance(messages.this).addtorequestque(stringRequest);

            }
        });


        toolbar = (Toolbar) findViewById(R.id.meassge_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(messages.this, chatroom.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    public void buttonclicl() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    list.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        list.add(jsonObject.getString("message"));
                    }
                    recyclerView.setAdapter(new sendmessageadpter(list, senderids, messages.this, 1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("senderid", senderid);
                params.put("recieverid", recieverid);
                return params;
            }
        };
        mysingleton.getmInstance(messages.this).addtorequestque(stringRequest);
    }


}
