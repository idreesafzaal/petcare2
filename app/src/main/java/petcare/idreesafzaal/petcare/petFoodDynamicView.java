package petcare.idreesafzaal.petcare;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class petFoodDynamicView extends RecyclerView.Adapter<petFoodDynamicView.viewHolder> {
    Context context;
    JSONArray jsonArray;
    Calendar calendar;
    TimePickerDialog timePickerDialog;
    EditText chosetime;
    int currentHour;
    int currentMinute;
    String amPm, petid;
    localhost host = new localhost();
    String url = "http://" + host.api + "/andriodfiles/updatePetFoodPlan.php";

    public petFoodDynamicView(Context context, JSONArray jsonArray, String petid) {
        this.petid = petid;
        this.context = context;
        this.jsonArray = jsonArray;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.petfoodrecyclerview, null);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, int position) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            holder.dyDay.setText(jsonObject.getString("day"));
            holder.dyBreakfast.setText(jsonObject.getString("breakfast"));
            holder.dyLunch.setText(jsonObject.getString("lunch"));
            holder.dyDinner.setText(jsonObject.getString("dinner"));
            holder.dyCal.setText(jsonObject.getString("calories"));
            holder.fooodId.setText(jsonObject.getString("planid"));
            holder.dbt.setText(jsonObject.getString("bt"));
            holder.dlt.setText(jsonObject.getString("lt"));
            holder.ddt.setText(jsonObject.getString("dt"));
            holder.updateDyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.dialogue_update_food_plans, null);
                    final Button button = (Button) view.findViewById(R.id.dyupdateFoodBtn);
                    button.setEnabled(false);
                    button.setBackgroundColor(Color.GRAY);
                    final EditText breakfast = (EditText) view.findViewById(R.id.dyupdatebreakfast);
                    breakfast.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            button.setEnabled(true);
                            button.setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    });
                    final EditText lunch = (EditText) view.findViewById(R.id.dyupdatelunch);
                    lunch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            button.setEnabled(true);
                            button.setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    });
                    final EditText dinner = (EditText) view.findViewById(R.id.dyupdatedinner);
                    dinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            button.setEnabled(true);
                            button.setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    });
                    final EditText calories = (EditText) view.findViewById(R.id.dyupdateTotalCalories);
                    calories.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            button.setEnabled(true);
                            button.setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    });
                    final Button bt = (Button) view.findViewById(R.id.dybreakfastTimebtn);
                    bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            button.setEnabled(true);
                            button.setBackgroundResource(R.color.colorPrimaryDark);
                            selecttime(bt);

                        }


                    });
                    final Button lt = (Button) view.findViewById(R.id.dylunchTimebtn);
                    lt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            button.setEnabled(true);
                            button.setBackgroundResource(R.color.colorPrimaryDark);
                            selecttime(lt);


                        }
                    });
                    final Button dt = (Button) view.findViewById(R.id.dydinnerTimebtn);
                    dt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            button.setEnabled(true);
                            button.setBackgroundResource(R.color.colorPrimaryDark);
                            selecttime(dt);


                        }
                    });
                    breakfast.setText(holder.dyBreakfast.getText().toString());
                    dinner.setText(holder.dyDinner.getText().toString());
                    lunch.setText(holder.dyLunch.getText().toString());
                    calories.setText(holder.dyCal.getText().toString());
                    bt.setText(holder.dbt.getText().toString());
                    lt.setText(holder.dlt.getText().toString());
                    dt.setText(holder.ddt.getText().toString());
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String b = breakfast.getText().toString();
                            final String l = lunch.getText().toString();
                            final String d = dinner.getText().toString();
                            final String c = calories.getText().toString();
                            final String fdplnId = holder.fooodId.getText().toString().trim();
                            final String day = holder.dyDay.getText().toString().trim();
                            final String bb = bt.getText().toString();
                            final String ll = lt.getText().toString();
                            final String dd = dt.getText().toString();
                            if (b.isEmpty()) {
                                breakfast.setError("Enter breakfast");
                                breakfast.requestFocus();
                                return;
                            }
                            if (l.isEmpty()) {
                                lunch.setError("Enter lunch");
                                lunch.requestFocus();
                                return;
                            }
                            if (d.isEmpty()) {
                                dinner.setError("Enter dinner");
                                dinner.requestFocus();
                                return;
                            }
                            if (c.isEmpty()) {
                                calories.setError("Enter Total calories");
                                calories.requestFocus();
                                return;
                            }
                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {

                                        JSONArray jsonArray1 = new JSONArray(response);
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                                        String message = jsonObject1.getString("message");
                                        if (message.equals("update")) {
                                            Toast.makeText(context, "fopd plan is updated now", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(context, petFoodPlanUpdate.class);
                                            intent.putExtra("petid", petid);
                                            context.startActivity(intent);
                                        } else {
                                            Toast.makeText(context, "fail", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                                }
                            }) {
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("day", day);
                                    params.put("planid", fdplnId);
                                    params.put("breakfast", b);
                                    params.put("dinner", d);
                                    params.put("lunch", l);
                                    params.put("calories", c);
                                    params.put("breakfasttime", bb);
                                    params.put("lunchtime", ll);
                                    params.put("dinnertime", dd);
                                    return params;
                                }

                            };
                            mysingleton.getmInstance(context).addtorequestque(stringRequest1);

                        }
                    });
                    builder.setView(view);
                    // final AlertDialog alertDialog=builder.create();
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

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView dyDay, dyBreakfast, dyLunch, dyDinner, dyCal, fooodId, dbt, dlt, ddt;
        Button updateDyBtn;

        public viewHolder(View itemView) {
            super(itemView);
            dyDay = (TextView) itemView.findViewById(R.id.planDay);
            dyBreakfast = (TextView) itemView.findViewById(R.id.petDyBreakfast);
            dyLunch = (TextView) itemView.findViewById(R.id.petDyluch);
            dyDinner = (TextView) itemView.findViewById(R.id.petDyDinner);
            dyCal = (TextView) itemView.findViewById(R.id.petDyCal);
            fooodId = (TextView) itemView.findViewById(R.id.foodPlanId);
            dbt = (TextView) itemView.findViewById(R.id.bt);
            dlt = (TextView) itemView.findViewById(R.id.lt);
            ddt = (TextView) itemView.findViewById(R.id.dt);
            updateDyBtn = (Button) itemView.findViewById(R.id.updateDyBtn);
        }
    }

    public void selecttime(final Button button) {
        calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(context, android.R.style.Theme_Holo_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
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
}
