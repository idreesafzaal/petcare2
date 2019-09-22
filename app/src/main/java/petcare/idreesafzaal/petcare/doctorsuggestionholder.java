package petcare.idreesafzaal.petcare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
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
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class doctorsuggestionholder extends RecyclerView.Adapter<doctorsuggestionholder.viewholder> {
    localhost host = new localhost();
    JSONArray jsonArray;
    Context context;
    String doctorid;
    String url = "http://" + host.api + "/andriodfiles/deletesuggestion.php";

    public doctorsuggestionholder(JSONArray jsonArray, Context context, String doctorid) {
        this.jsonArray = jsonArray;
        this.context = context;
        this.doctorid = doctorid;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.showdoctorsuggestion, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, final int position) {
        try {
            final JSONObject jsonObject = jsonArray.getJSONObject(position);
            holder.suggestid.setText(jsonObject.getString("suggest_Id"));
            holder.disease.setText(jsonObject.getString("diseases"));
            holder.discription.setText(jsonObject.getString("description"));
            holder.like.setText(jsonObject.getString("like"));

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Toast.makeText(context,holder.suggestid.getText().toString(),Toast.LENGTH_LONG).show();
                            jsonArray.remove(position);
                            notifyItemRemoved(position);
                            deletesuggestion(doctorid, holder.suggestid.getText().toString());
                        }
                    });
                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setTitle("delete suggestion");
                    builder.setMessage("Do you want to delete it");
                    builder.show();
                    return false;
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
        TextView like, disease, discription, suggestid;

        public viewholder(View itemView) {
            super(itemView);
            like = (TextView) itemView.findViewById(R.id.tvcount);
            disease = (TextView) itemView.findViewById(R.id.disease);
            discription = (TextView) itemView.findViewById(R.id.diseasediscription);
            suggestid = (TextView) itemView.findViewById(R.id.suggestid);
        }
    }

    public void deletesuggestion(final String doctorid, final String suggestionid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String meassage = jsonObject.getString("message");
                    if (meassage.equals("success")) {
                        Toast.makeText(context, "your suggestion is delete now", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Something went wrong check your internet connection", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("doctorid", doctorid);
                params.put("suggestionid", suggestionid);
                return params;
            }
        };
        mysingleton.getmInstance(context).addtorequestque(stringRequest);
    }
}
