package petcare.idreesafzaal.petcare;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class showacceptappointment extends Fragment {
    localhost host=new localhost();
    RecyclerView recyclerView;
    Button button;
    String url =  "http://" + host.api + "/andriodfiles/showacceptedappiontment.php";

    public showacceptappointment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_showacceptappointment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.acceptapprecyclerview);
        button = (Button) view.findViewById(R.id.errormessagebtnfrag);
        button.setVisibility(View.INVISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String doctorid = sharedPreferences.getString("doctorId", null);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String message=jsonObject.getString("code");
                    if(message.equals("success")){
                        recyclerView.setAdapter(new showacceptedappointmentholder(jsonArray,getContext()));
                        recyclerView.scrollToPosition(jsonArray.length()-1);
                    }
                    else if(message.equals("fail")){
                         button.setVisibility(View.VISIBLE);
                    }
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
                params.put("doctorid", doctorid);
                return params;
            }
        };
        mysingleton.getmInstance(getContext()).addtorequestque(stringRequest);
        return view;
    }

}
