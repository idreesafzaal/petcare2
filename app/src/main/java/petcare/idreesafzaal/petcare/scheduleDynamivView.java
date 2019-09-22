package petcare.idreesafzaal.petcare;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class scheduleDynamivView extends RecyclerView.Adapter<scheduleDynamivView.scheduleViewHolder> {
    Context context;
    JSONArray jsonArray;
    String selectedday;
    String st, et;
    localhost host = new localhost();
    String url = "http://" + host.api + "/andriodfiles/updateSchedules.php";
    String ul = "http://" + host.api + "/andriodfiles/searchPetId.php";

    byte[] imageBytes;
    String petid;
     Bitmap bitmap;
    Animation animation;
    ImageView rotimg;

    Calendar calendar;
    TimePickerDialog timePickerDialog;
    EditText chosetime;
    int currentHour;
    int currentMinute;
    String amPm;

    public scheduleDynamivView(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;


    }

    @NonNull
    @Override
    public scheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.dynamiclayoutschedule, null);
        return new scheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final scheduleViewHolder holder, final int position) {
        //final String[]names=new String[0];

        try {

            JSONObject jsonObject = jsonArray.getJSONObject(position);
            holder.scheduletype.setText(jsonObject.getString("stype").toString());
            holder.starttimesh.setText(jsonObject.getString("st").toString());
            holder.endtimesh.setText(jsonObject.getString("et").toString());
            holder.scheduleid.setText(jsonObject.getString("sid").toString());
            holder.daysh.setText(jsonObject.getString("day"));
            holder.petnamesh.setText(jsonArray.getJSONObject(position).getString("ppp"));
            String imgdata = jsonObject.getString("image");
            if (imgdata!=null) {
                try {

//                imageBytes = Base64.decode(imgst, Base64.DEFAULT);
//                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    imageBytes = Base64.decode(imgdata, Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    holder.imgPet.setImageBitmap(bitmap);
                    animation = AnimationUtils.loadAnimation(context, R.anim.rotateimage);
                    holder.imgPet.setAnimation(animation);

                } catch (Exception e) {
                }
            }
            holder.updateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater ly = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View vi = ly.inflate(R.layout.scheduleupdatesdailogview, null);
                    final Spinner spinner = (Spinner) vi.findViewById(R.id.updatespinnerDialog);
                    final Button days = (Button) vi.findViewById(R.id.selectdays);
                    final String sid = holder.scheduleid.getText().toString();

                    rotimg = (ImageView) vi.findViewById(R.id.rotimg);

                    final Button starttime = (Button) vi.findViewById(R.id.updateStartTimeDialog);
                    starttime.setText(holder.starttimesh.getText().toString());

                    final Button endtime = (Button) vi.findViewById(R.id.updateEndTimeDialog);
                    endtime.setText(holder.endtimesh.getText().toString());

                    final Button updatescheduleBtn = (Button) vi.findViewById(R.id.updateScheduleBtnDailog);
                    rotatepic();
                    ArrayAdapter<CharSequence> day = ArrayAdapter.createFromResource(context, R.array.updateday, android.R.layout.simple_spinner_item);
                    day.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(day);


                    starttime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            calendar = Calendar.getInstance();
                            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                            currentMinute = calendar.get(Calendar.MINUTE);

                            timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                    if (hourOfDay >= 12) {
                                        amPm = "PM";
                                    } else {
                                        amPm = "AM";
                                    }
                                    starttime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);

                                }
                            }, currentHour, currentMinute, false);

                            timePickerDialog.show();
                        }
                    });


                    endtime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            calendar = Calendar.getInstance();
                            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                            currentMinute = calendar.get(Calendar.MINUTE);

                            timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                    if (hourOfDay >= 12) {
                                        amPm = "PM";
                                    } else {
                                        amPm = "AM";
                                    }
                                    endtime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);

                                }
                            }, currentHour, currentMinute, false);

                            timePickerDialog.show();
                        }
                    });


                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (parent.getItemAtPosition(position).equals("Select day")) {
                                selectedday = holder.daysh.getText().toString();
                                days.setText(selectedday);
                            } else {
                                selectedday = spinner.getSelectedItem().toString();
                                days.setText(selectedday);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    updatescheduleBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            st = starttime.getText().toString();
                            et = endtime.getText().toString();
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject object = jsonArray.getJSONObject(0);
                                        String message = object.getString("message");
                                        if (message.equals("update")) {
                                            Toast.makeText(context, "update now", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context, "check internet", Toast.LENGTH_LONG).show();
                                }
                            }) {
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("sid", sid);
                                    params.put("day", selectedday);
                                    params.put("st", st);
                                    params.put("et", et);
                                    return params;
                                }
                            };
                            mysingleton.getmInstance(context).addtorequestque(stringRequest);
                        }
                    });

                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setView(vi);
                    alert.show();
                }
            });
            //petid=jsonObject.getString("petid").toString();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void rotatepic() {
        animation = AnimationUtils.loadAnimation(context, R.anim.rotateimage);
        rotimg.setAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }


    public class scheduleViewHolder extends RecyclerView.ViewHolder {
        TextView scheduletype, petnamesh, starttimesh, endtimesh, daysh, scheduleid;
        Button updateBtn;
        ImageView imgPet;

        public scheduleViewHolder(View itemView) {
            super(itemView);
            scheduletype = (TextView) itemView.findViewById(R.id.shtype);
            daysh = (TextView) itemView.findViewById(R.id.scheduleDay);
            petnamesh = (TextView) itemView.findViewById(R.id.petnameshchedule);
            starttimesh = (TextView) itemView.findViewById(R.id.shstarttime);
            endtimesh = (TextView) itemView.findViewById(R.id.shendtime);
            scheduleid = (TextView) itemView.findViewById(R.id.scheduleid);
            imgPet = (ImageView) itemView.findViewById(R.id.petPic);
            updateBtn = (Button) itemView.findViewById(R.id.updatescheduleBtn);
        }
    }
}
