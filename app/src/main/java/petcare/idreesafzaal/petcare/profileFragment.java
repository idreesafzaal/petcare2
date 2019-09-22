package petcare.idreesafzaal.petcare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class profileFragment extends Fragment {

    EditText Name, Contact, Email;

    String name, contact, email, userid;

    String uname, ucantact, uemail;
    AlertDialog.Builder builder;
    ProgressBar error;
    localhost host = new localhost();
    String url = "http://" + host.api + "/andriodfiles/updateprofile.php";
    String schUrl = "http://" + host.api + "/andriodfiles/searchUser.php";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Name = (EditText) view.findViewById(R.id.name);
        Contact = (EditText) view.findViewById(R.id.number);

        Email = (EditText) view.findViewById(R.id.email);
        final Button updatePro = (Button) view.findViewById(R.id.updateproBtn);

        updatePro.setEnabled(false);

        updatePro.setBackgroundColor(Color.GRAY);

        Name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                updatePro.setEnabled(true);
                updatePro.setBackgroundResource(R.color.colorPrimaryDark);
            }
        });
        Contact.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                updatePro.setEnabled(true);
                updatePro.setBackgroundResource(R.color.colorPrimaryDark);
            }
        });
        Email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                updatePro.setEnabled(true);
                updatePro.setBackgroundResource(R.color.colorPrimaryDark);
            }
        });
//        if(Name.requestFocus()){
//            updatePro.setEnabled(true);
//            updatePro.setBackgroundColor(Color.BLUE);
//        }


        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        userid = sharedPref.getString("userId", null);
        // userid=getActivity().getIntent().getStringExtra("userid");
        builder = new AlertDialog.Builder(getContext());

        task data = new task();
        data.execute();


        updatePro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateuserprofile();
            }
        });
        return view;


    }


    public void updateuserprofile() {
        uname = Name.getText().toString();
        uemail = Email.getText().toString();
        ucantact = Contact.getText().toString();
        if (uname.equals("") || ucantact.equals("") || uemail.equals("")) {
            builder.setTitle("Something went wrong!");
            displayalert("fill all given fields");

        } else {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String messag = jsonObject.getString("message");
                        if (messag.equals("upadateprofile")) {
                            Toast.makeText(getContext(), "Your profile is updated now", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), home.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "Something is wrong", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    builder.setTitle("Something Went Wrong!!");
                    displayalert("Check Your Ineternet Connection!!");

                }
            }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userid", userid);
                    params.put("name", uname);
                    params.put("email", uemail);
                    params.put("number", ucantact);
                    ;


                    return params;
                }
            };
            mysingleton.getmInstance(getContext()).addtorequestque(stringRequest);
        }
    }

    public void displayalert(String message) {
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    ;

    class task extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, schUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String messag = jsonObject.getString("code");
                        name = jsonObject.getString("user_name");
                        email = jsonObject.getString("email");
                        contact = jsonObject.getString("phone");
                        if (messag.equals("userfound")) {
                            Name.setText(name);
                            Contact.setText(contact);
                            Email.setText(email);
                        } else {
                            Toast.makeText(getContext(), "Something is wrong", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        Toast.makeText(getContext(), "check your internet", Toast.LENGTH_LONG).show();
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
            mysingleton.getmInstance(getContext()).addtorequestque(stringRequest);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
