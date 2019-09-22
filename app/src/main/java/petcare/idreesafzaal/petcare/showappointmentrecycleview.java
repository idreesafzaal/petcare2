package petcare.idreesafzaal.petcare;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class showappointmentrecycleview extends RecyclerView.Adapter<showappointmentrecycleview.viewholder> {
    String time, date, em, apptid;
    localhost host = new localhost();
    String url = "http://" + host.api + "/andriodfiles/validateappointment.php";
    String urlrej = "http://" + host.api + "/andriodfiles/rejectappointment.php";
    String appurl="http://" + host.api + "/andriodfiles/acceptappointmentuser.php";
    Context context;
    byte[] imageBytes;
    String imagedata;
    JSONArray jsonArray;
    Calendar calendar;
    TimePickerDialog timePickerDialog;
    int currentHour;
    int currentMinute;
    String amPm, id;
    String stime, etime;
    Session session = null;
    ProgressDialog progressDialog = null;

    public showappointmentrecycleview(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }


    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.showappointmentlayout, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, final int position) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            holder.petownerid.setText(jsonObject.getString("userid").toString());
            imagedata=jsonObject.getString("image").toString();
            if (imagedata != null) {
                imageBytes = Base64.decode(imagedata, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
               holder.petownerpic.setImageBitmap(decodedImage);
            }
            holder.appid.setText(jsonObject.getString("appid").toString());
            holder.petownername.setText(jsonObject.getString("username").toString());
            holder.petowneremail.setText(jsonObject.getString("email").toString());
            holder.appointmentdate.setText(jsonObject.getString("date").toString());
            holder.rejectapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Cancel Appointment");
                    alert.setMessage("Do you want to reject this appointment?");

                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            apptid = holder.appid.getText().toString();
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlrej,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONArray jsonArray = new JSONArray(response);
                                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                                String mess = jsonObject.getString("code");
                                                if (mess.equals("succesfully")) {
                                                    jsonArray.remove(position);
                                                    notifyItemRemoved(position);
                                                    em = holder.petowneremail.getText().toString();
                                                    String message = "Your appointment is cancel due to some resaon";
                                                    Properties props = new Properties();
                                                    props.put("mail.smtp.host", "smtp.gmail.com");
                                                    props.put("mail.smtp.socketFactory.port", "465");
                                                    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                                                    props.put("mail.smtp.auth", "true");
                                                    props.put("mail.smtp.port", "465");
                                                    session = Session.getDefaultInstance(props, new Authenticator() {
                                                        @Override
                                                        protected PasswordAuthentication getPasswordAuthentication() {
                                                            return new PasswordAuthentication("petcare652@gmail.com", "petcare143");
                                                        }
                                                    });
                                                    progressDialog = new ProgressDialog(context);
                                                    progressDialog.setMessage("Please Wait..");
                                                    progressDialog.setIndeterminate(false);
                                                    progressDialog.setCancelable(true);
                                                    progressDialog.show();
                                                    RetrieveTask task = new RetrieveTask(message, em);
                                                    task.execute();
                                                } else {
                                                    Toast.makeText(context, "you have already fixed appointment with other user please select other time", Toast.LENGTH_LONG).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context, "something went wrong check your internet connection", Toast.LENGTH_LONG).show();
                                }
                            }) {
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("appid", apptid);
                                    return params;
                                }
                            };
                            mysingleton.getmInstance(context).addtorequestque(stringRequest);
                        }
                    });
                    alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();
                }
            });


            holder.acceptapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
                    final String docid=sharedPreferences.getString("doctorId",null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    final Button button;
                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = layoutInflater.inflate(R.layout.timelayoutforapp, null);
                    button = (Button) view.findViewById(R.id.timepickerBtn);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            calendar = Calendar.getInstance();
                            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                            currentMinute = calendar.get(Calendar.MINUTE);

                            timePickerDialog = new TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                    if (hourOfDay >= 12) {
                                        amPm = "PM";
                                    } else {
                                        amPm = "AM";
                                    }
                                    button.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);

                                }
                            }, currentHour, currentMinute, false);

                            timePickerDialog.show();
                        }
                    });
                    builder.setView(view);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            time = button.getText().toString();
                            date = holder.appointmentdate.getText().toString();
                            id = holder.appid.getText().toString();
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONArray jsonArray = new JSONArray(response);
                                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                String mess = jsonObject.getString("code");
                                                if (mess.equals("succesfully")) {
                                                    accptedappointment(id,docid,time);
                                                    jsonArray.remove(position);
                                                    notifyItemRemoved(position);
                                                    em = holder.petowneremail.getText().toString();
                                                    String message = "Your appointment is fixed at " + time + "\n" + "kindly contact with doctor";
                                                    Properties props = new Properties();
                                                    props.put("mail.smtp.host", "smtp.gmail.com");
                                                    props.put("mail.smtp.socketFactory.port", "465");
                                                    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                                                    props.put("mail.smtp.auth", "true");
                                                    props.put("mail.smtp.port", "465");
                                                    session = Session.getDefaultInstance(props, new Authenticator() {
                                                        @Override
                                                        protected PasswordAuthentication getPasswordAuthentication() {
                                                            return new PasswordAuthentication("petcare652@gmail.com", "petcare143");
                                                        }
                                                    });
                                                    progressDialog = new ProgressDialog(context);
                                                    progressDialog.setMessage("Please Wait..");
                                                    progressDialog.setIndeterminate(false);
                                                    progressDialog.setCancelable(true);
                                                    progressDialog.show();
                                                    RetrieveTask task = new RetrieveTask(message, em);
                                                    task.execute();
                                                } else {
                                                    Toast.makeText(context, "you have already fixed appointment with other user please select other time", Toast.LENGTH_LONG).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context, "something went wrong check your internet connection", Toast.LENGTH_LONG).show();
                                }
                            }) {
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("time", time);
                                    params.put("appid", id);
                                    return params;
                                }
                            };
                            mysingleton.getmInstance(context).addtorequestque(stringRequest);


                        }
                    });
                    builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
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

    class RetrieveTask extends AsyncTask<String, Void, String> {

        String mess;
        String mail;

        public RetrieveTask(String mess, String mail) {

            this.mess = mess;
            this.mail = mail;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                javax.mail.Message message = new MimeMessage(session);
                // MimeMessage message=new MimeMessage(session);
                message.setFrom(new InternetAddress("petcare652@gmail.com"));
                message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(mail));
                message.setSubject("Send mail from doctor");
                message.setContent(mess, "text/html; charset=utf-8");
                //Transport.send(message);
                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            Toast.makeText(context, "Email is send to Pet owner", Toast.LENGTH_LONG).show();
        }
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView petownerid, petownername, petowneremail, appointmentdate, appid;
        ImageView petownerpic;
        Button acceptapp, rejectapp;

        public viewholder(View itemView) {
            super(itemView);
            petownerpic = (ImageView) itemView.findViewById(R.id.petownerpic);
            appid = (TextView) itemView.findViewById(R.id.appid);
            petownerid = (TextView) itemView.findViewById(R.id.petownerid);
            petownername = (TextView) itemView.findViewById(R.id.petownername);
            petowneremail = (TextView) itemView.findViewById(R.id.petowneremail);
            appointmentdate = (TextView) itemView.findViewById(R.id.appointmentdate);
            acceptapp = (Button) itemView.findViewById(R.id.acceptBtn);
            rejectapp = (Button) itemView.findViewById(R.id.rejectBtn);
        }
    }

    public void  accptedappointment(final String appid, final String docid, final String time){
       StringRequest  stringRequest1=new StringRequest(Request.Method.POST, appurl, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               try {
                   JSONArray jsonArray=new JSONArray(response);
                   JSONObject jsonObject=jsonArray.getJSONObject(0);
                   String message=jsonObject.getString("message");
                   if (message.equals("success")){
                       Toast.makeText(context,"appointment is accept",Toast.LENGTH_LONG).show();
                   }

               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       }){
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String,String> params=new HashMap<String, String>();
               params.put("appid",appid);
               params.put("docid",docid);
               params.put("time",time);
               params.put("status","accept");
               return params;
           }
       };
       mysingleton.getmInstance(context).addtorequestque(stringRequest1);
    }
}
