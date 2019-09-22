package petcare.idreesafzaal.petcare;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
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

public class doctorsuggestionuserholder extends RecyclerView.Adapter<doctorsuggestionuserholder.viewholder> {
    localhost host = new localhost();
    JSONArray jsonArray;
    Context context;
    public static int count = 0;
    String getlikeurl = "http://" + host.api + "/andriodfiles/likesuggestion.php";
    String updatecounturl = "http://" + host.api + "/andriodfiles/savelikecount.php";
    String dislikecounturl = "http://" + host.api + "/andriodfiles/removelike.php";

    public doctorsuggestionuserholder(JSONArray jsonArray, Context context) {
        this.context = context;
        this.jsonArray = jsonArray;

    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.showdoctorsuggestion, parent, false);

        return new viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, int position) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            holder.suggestid.setText(jsonObject.getString("suggest_Id"));
            holder.disease.setText(jsonObject.getString("diseases"));
            holder.discription.setText(jsonObject.getString("description"));
            holder.like.setText(jsonObject.getString("like"));
            //  holder.dislike.setText(jsonObject.getString("dislike"));

            holder.likebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    final String userId = sharedPreferences.getString("userId", null);
                    final String suggestid = holder.suggestid.getText().toString();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, getlikeurl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String message = jsonObject1.getString("code");
                                if (message.equals("already")) {
                                    String likes = jsonObject1.getString("likes");
                                    double totalcount = 0;
                                    double likecount = Double.parseDouble(likes) - 1;

                                    holder.like.setText(String.valueOf(likecount));
                                    dislikecount(suggestid, holder.like.getText().toString(), userId);
                                }
                                if (message.equals("success")) {

                                    String likes = jsonObject1.getString("likes");
                                    double totalcount = 0;
                                    double likecount = Double.parseDouble(likes) + 1;
                                    totalcount = totalcount + likecount;
                                    holder.like.setText(String.valueOf(totalcount));
                                    updatelikecount(suggestid, String.valueOf(totalcount), userId);

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
                            params.put("suggestionid", suggestid);
                            params.put("userid", userId);
                            return params;
                        }
                    };
                    mysingleton.getmInstance(context).addtorequestque(stringRequest);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView like, dislike, disease, discription, suggestid;
        ImageButton likebtn, dislikebtn;

        public viewholder(View itemView) {
            super(itemView);
            like = (TextView) itemView.findViewById(R.id.tvcount);
            // dislike=(TextView) itemView.findViewById(R.id.dislikecount);
            disease = (TextView) itemView.findViewById(R.id.disease);
            discription = (TextView) itemView.findViewById(R.id.diseasediscription);
            suggestid = (TextView) itemView.findViewById(R.id.suggestid);
            likebtn = (ImageButton) itemView.findViewById(R.id.like);
//            dislikebtn=(ImageButton) itemView.findViewById(R.id.dislike);
        }
    }


    public void updatelikecount(final String suggestid, final String tolticount, final String userid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, updatecounturl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("suggestionid", suggestid);
                params.put("totalcount", tolticount);
                params.put("userid", userid);
                return params;
            }
        };
        mysingleton.getmInstance(context).addtorequestque(stringRequest);
    }


    public void dislikecount(final String suggestid, final String tolticount, final String userid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, dislikecounturl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("suggestionid", suggestid);
                params.put("totalcount", tolticount);
                params.put("userid", userid);
                return params;
            }
        };
        mysingleton.getmInstance(context).addtorequestque(stringRequest);
    }
}
