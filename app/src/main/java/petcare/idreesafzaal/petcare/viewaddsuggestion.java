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
public class viewaddsuggestion extends Fragment {

    //TextView like,dislike,disease,discription,suu;
    public viewaddsuggestion() {
        // Required empty public constructor
    }

    localhost host = new localhost();
    RecyclerView recyclerView;
    String url = "http://" + host.api + "/andriodfiles/showdoctorsuggestion.php";
    String doctorid;
    Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewaddsuggestion, container, false);
        button=(Button) view.findViewById(R.id.errormessagebtn);
        button.setVisibility(View.INVISIBLE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        doctorid = sharedPreferences.getString("doctorId", null);
        recyclerView = (RecyclerView) view.findViewById(R.id.suggestionrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String message=jsonObject.getString("message");
                    if(message.equals("found")) {
                        recyclerView.setAdapter(new doctorsuggestionholder(jsonArray, getContext(), doctorid));
                    }
                    if(message.equals("notfound")){
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
