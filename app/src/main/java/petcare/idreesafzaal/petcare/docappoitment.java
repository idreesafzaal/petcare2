package petcare.idreesafzaal.petcare;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class docappoitment extends RecyclerView.Adapter<docappoitment.viewholder> {

    JSONArray jsonArray;
    String id, i, petownerid;
    localhost host = new localhost();
    Context context;
    String appurl = "http://" + host.api + "/andriodfiles/ownerappointmentrequest.php";
    public static final int REQUEST_CALL = 1;
    byte[] imageBytes;
    private DatePickerDialog.OnDateSetListener listener;
    int cuurentmonth, day, currentyear;

    public docappoitment(JSONArray jsonArray, Context context) {
        this.context = context;
        this.jsonArray = jsonArray;

    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layoutforrecycleviewappointment, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, int position) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            holder.docname.setText(jsonObject.getString("name"));
            holder.docinfo.setText(jsonObject.getString("info"));
            holder.docaddress.setText(jsonObject.getString("address"));
            String imagedata = jsonObject.getString("image");
            final String phonenumber = jsonObject.getString("contact");
            holder.ids.setText(jsonObject.getString("userid"));
            try {

                imageBytes = Base64.decode(imagedata, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                holder.imag.setImageBitmap(decodedImage);

            } catch (Exception e) {
            }
            holder.appbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Button dateBtn, continueBtn;
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.pickappointmentdate, null);
                    dateBtn = (Button) view.findViewById(R.id.datepickerBtn);
                    continueBtn = (Button) view.findViewById(R.id.okaybtn);
                    dateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Calendar calendar = Calendar.getInstance();
                            day = calendar.get(Calendar.DAY_OF_MONTH);
                            cuurentmonth = calendar.get(Calendar.MONTH);
                            currentyear = calendar.get(Calendar.YEAR);
                            DatePickerDialog dialog = new DatePickerDialog(context,
                                    android.R.style.Theme_Holo_Dialog_MinWidth, listener,
                                    currentyear, cuurentmonth, day);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.show();
                        }
                    });
                    listener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            month = month + 1;
                            if (dayOfMonth >= day && month >= cuurentmonth && year == currentyear) {

                                String date = dayOfMonth + "/" + month + "/" + year;
                                dateBtn.setText(date);
                            } else {
                                Toast.makeText(context, "Select valid date"
                                        , Toast.LENGTH_LONG).show();
                                dateBtn.setText("");
                            }
                        }
                    };
                    continueBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            i = holder.ids.getText().toString().trim();
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                            petownerid = sharedPreferences.getString("userId", null);
                            final String appdate = dateBtn.getText().toString();
                            if (appdate.isEmpty()) {
                                dateBtn.setError("Pick date here");
                                dateBtn.requestFocus();
                                return;
                            }
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, appurl, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                        String message = jsonObject1.getString("message");
                                        if (message.equals("success")) {
                                            Toast.makeText(context, "Your request is send to doctor " +
                                                    "you will get email after proceeding request", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(context, "You have already send this request "
                                                    , Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context, "Something went wrong check your internet connection "
                                            , Toast.LENGTH_LONG).show();
                                }
                            }) {
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("doctorid", i);
                                    params.put("ownerid", petownerid);
                                    params.put("appdate", appdate);
                                    return params;
                                }
                            };
                            mysingleton.getmInstance(context).addtorequestque(stringRequest);
                        }
                    });
                    builder.setView(view);
                    builder.show();
                }
            });
            holder.callbtn.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("MissingPermission")
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Call to docotor");
                    builder.setMessage("If you want to call press ok" + phonenumber);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + phonenumber.trim()));
                            context.startActivity(intent);
                        }
                    });
                    builder.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();


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
        TextView docname, docinfo, docaddress, ids;
        Button callbtn, appbtn;
        ImageView imag;

        public viewholder(View itemView) {
            super(itemView);
            docname = (TextView) itemView.findViewById(R.id.docname);
            docinfo = (TextView) itemView.findViewById(R.id.docinfo);
            docaddress = (TextView) itemView.findViewById(R.id.docaddress);
            ids = (TextView) itemView.findViewById(R.id.doctorids);
            callbtn = (Button) itemView.findViewById(R.id.callBtn);
            appbtn = (Button) itemView.findViewById(R.id.appBtn);
            imag = (ImageView) itemView.findViewById(R.id.img1doc);
        }
    }


}
