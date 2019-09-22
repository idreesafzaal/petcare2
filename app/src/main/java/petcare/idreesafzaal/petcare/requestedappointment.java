package petcare.idreesafzaal.petcare;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class requestedappointment extends Fragment {

    TextView message;
    localhost host = new localhost();
    String url = "http://" + host.api + "/andriodfiles/showappointment.php";
    String doctorid;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    TextView textView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclelayoutrequestedapp, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.reyView);
        textView = (TextView) view.findViewById(R.id.errorapp);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        message = (TextView) view.findViewById(R.id.errormessage);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        doctorid = sharedPreferences.getString("doctorId", null);
        progressDialog = ProgressDialog.show(getContext(), "Loading", "wait a while please..", true);
        getdata data = new getdata();
        data.execute();
        return view;


    }

    class getdata extends AsyncTask<String, Void, System> {

        @Override
        protected System doInBackground(String... strings) {
            try {


                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            String message = jsonArray.getJSONObject(0).getString("code");
                            if (!message.equals("login_failed")) {
                                recyclerView.setAdapter(new showappointmentrecycleview(getContext(), jsonArray));
                                textView.setVisibility(View.INVISIBLE);
                            } else {
                                textView.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("doctorid", doctorid);
                        return params;
                    }
                };
                mysingleton.getmInstance(getContext()).addtorequestque(stringRequest);
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(System system) {
            progressDialog.dismiss();
        }
    }
}
