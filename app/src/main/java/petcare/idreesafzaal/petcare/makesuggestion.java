package petcare.idreesafzaal.petcare;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
public class makesuggestion extends Fragment {


    public makesuggestion() {
        // Required empty public constructor
    }

    localhost host = new localhost();
    EditText diseases, discription;
    Button suggestionbtn;
    String docid, strdiseases, strdiscription;
    String url = "http://" + host.api + "/andriodfiles/makesuggestion.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_makesuggestion, container, false);
        diseases = (EditText) view.findViewById(R.id.disease);
        discription = (EditText) view.findViewById(R.id.diseasediscription);
        suggestionbtn = (Button) view.findViewById(R.id.suggestBtn);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        docid = sharedPreferences.getString("doctorId", null);

        suggestionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    strdiseases = diseases.getText().toString();
                    strdiscription = discription.getText().toString();
                    if (strdiseases.isEmpty()) {
                        diseases.setError("Enter diseases");
                        diseases.requestFocus();
                        return;
                    }
                    if (strdiscription.isEmpty()) {
                        discription.setError("Enter discription");
                        discription.requestFocus();
                        return;
                    }
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String message = jsonObject.getString("code");
                                if (message.equals("success")) {
                                    Toast.makeText(getContext(), "Thanks for making suggestion", Toast.LENGTH_LONG).show();
                                    diseases.setText("");
                                    discription.setText("");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "Something went wrong check internet connection", Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("doctorid", docid);
                            params.put("diseases", strdiseases);
                            params.put("discription", strdiscription);
                            return params;
                        }
                    };
                    mysingleton.getmInstance(getContext()).addtorequestque(stringRequest);
                } catch (Exception e) {

                }
            }
        });
        return view;

    }

}
