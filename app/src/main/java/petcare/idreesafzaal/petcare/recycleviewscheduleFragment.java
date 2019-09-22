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
import android.widget.Button;
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

public class recycleviewscheduleFragment extends Fragment {
    localhost host = new localhost();
    String url = "http://" + host.api + "/andriodfiles/serachUserPetSchedule.php";
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    String userid;
    String[] names;
    Button errorbtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dynamicviewschedulefragment, null);
        errorbtn=(Button)view.findViewById(R.id.errorbtn);
        errorbtn.setVisibility(View.INVISIBLE);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleFragment);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true); // THIS ALSO SETS setStackFromBottom to true
        recyclerView.setLayoutManager(mLayoutManager);
        // recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        userid = sharedPreferences.getString("userId", null);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("please wait a while");
        progressDialog.setMessage("data is loading");
        progressDialog.show();
        task data = new task();
        data.execute();
        return view;

    }

    class task extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {


            try {
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                            String message=jsonObject.getString("code");
                            if(message.equals("login_failed")){
                               errorbtn.setVisibility(View.VISIBLE);

                            }
                            if(message.equals("found")) {
                                recyclerView.setAdapter(new scheduleDynamivView(getContext(), jsonArray));
                                recyclerView.scrollToPosition(jsonArray.length() - 1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Toast.makeText(getContext(), "internet problem", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                        }
                    }
                }) {
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("userid", userid);
                        return params;
                    }
                };
                mysingleton.getmInstance(getContext()).addtorequestque(request);
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.cancel();
        }
    }
}
