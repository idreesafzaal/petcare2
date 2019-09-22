package petcare.idreesafzaal.petcare;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.preference.PreferenceManager;
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
import android.widget.RadioButton;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class petViews extends RecyclerView.Adapter<petViews.petViewHolder> {
    private Context context;
    private JSONArray data;
    localhost host = new localhost();
    Spinner spinner;
    String url1 = "http://" + host.api + "/andriodfiles/createFoodPlan.php";
    String bftime, dtime, ltime, breakfast, lunch, dinner, day, petids, userid;
    Calendar calendar;
    ArrayList<Integer> mselecteditem = new ArrayList<>();
    ArrayList<Integer> mlselecteditem = new ArrayList<>();
    ArrayList<Integer> mdselecteditem = new ArrayList<>();
    TimePickerDialog timePickerDialog;
    EditText chosetime;
    int currentHour;
    int currentMinute;
    String amPm;
    Animation animation;
    String petName, petInfo, petHBD, petCategory, petSubCategory, petGender, petid, pgender;
    byte[] imageBytes;

    String url = "http://" + host.api + "/andriodfiles/updatePetProfile.php";
    foodhelper fodhelper = new foodhelper();

    public petViews(Context context, JSONArray data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public petViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.user_pets, parent, false);
        return new petViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final petViewHolder holder, int position) {

        try {


            final JSONObject jsonObject = data.getJSONObject(position);
            holder.pet_name.setText(jsonObject.getString("petname"));
            holder.pet_gender.setText(jsonObject.getString("petgender"));
            holder.category.setText(jsonObject.getString("category"));
            holder.sub.setText(jsonObject.getString("subcategory"));
            holder.h.setText(jsonObject.getString("petbd"));
            holder.in.setText(jsonObject.getString("petbio"));
            holder.pi.setText(jsonObject.getString("petid"));
            String imgst = jsonObject.getString("image");
            try {


                imageBytes = Base64.decode(imgst, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                holder.imgview.setImageBitmap(decodedImage);
            } catch (Exception e) {
            }
            animation = AnimationUtils.loadAnimation(context, R.anim.rotateimage);
            holder.imgview.setAnimation(animation);


            //LayoutInflater inflater=LayoutInflater.from(context);
            // View view=inflater.inflate(R.layout.dialogue_update_food_plans,null);

            holder.editprofile.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.dailog_pet_profile_update, null);
                    final Button btn = (Button) view.findViewById(R.id.updatePetBtn);
                    // Intent intent=new Intent(context,petRegistration.class);
                    // context.startActivity(intent);
                    // v.startActivity(intent);

                    final EditText name = (EditText) view.findViewById(R.id.petname);
                    name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            btn.setEnabled(true);
                            btn.setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    });
                    final EditText cat = (EditText) view.findViewById(R.id.petCategory);
                    cat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            btn.setEnabled(true);
                            btn.setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    });
                    final EditText subcl = (EditText) view.findViewById(R.id.petSubCategory);
                    subcl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            btn.setEnabled(true);
                            btn.setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    });
                    final EditText hbd = (EditText) view.findViewById(R.id.petHBD);
                    hbd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            btn.setEnabled(true);
                            btn.setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    });
                    final EditText info = (EditText) view.findViewById(R.id.petInfo);
                    info.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            btn.setEnabled(true);
                            btn.setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    });
                    final RadioButton rd1 = (RadioButton) view.findViewById(R.id.rd1);
                    final RadioButton rd2 = (RadioButton) view.findViewById(R.id.rd2);
                    petid = holder.pi.getText().toString();
                    String gen = holder.pet_gender.getText().toString().trim();
                    if (gen.equals("Male")) {
                        rd1.setChecked(true);
                    } else if (gen.equals("Female")) {
                        rd1.setChecked(false);
                        rd2.setChecked(true);
                    }
                    name.setText(holder.pet_name.getText().toString());
                    cat.setText(holder.category.getText().toString());
                    subcl.setText(holder.sub.getText().toString());
                    hbd.setText(holder.h.getText().toString());
                    info.setText(holder.in.getText().toString());

                    btn.setEnabled(false);
                    btn.setBackgroundColor(Color.GRAY);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String pname = name.getText().toString();
                            final String pinfo = info.getText().toString();
                            final String pbd = hbd.getText().toString();
                            final String pcat = cat.getText().toString();
                            final String pscat = subcl.getText().toString();
                            String gmle = rd1.getText().toString();
                            String fmle = rd2.getText().toString();


                            if (pname.isEmpty()) {
                                name.setError("Enter pet name");
                                name.requestFocus();
                                return;
                            }
                            if (pinfo.isEmpty()) {
                                info.setError("Enter pet name");
                                info.requestFocus();
                                return;
                            }
                            if (pbd.isEmpty()) {
                                hbd.setError("Enter pet name");
                                hbd.requestFocus();
                                return;
                            }
                            if (pcat.isEmpty()) {
                                cat.setError("Enter pet name");
                                cat.requestFocus();
                                return;
                            }
                            if (pscat.isEmpty()) {
                                subcl.setError("Enter pet name");
                                subcl.requestFocus();
                                return;
                            }
                            if (!rd1.isChecked() && !rd2.isChecked()) {
                                rd1.setError("Please one of them");
                                rd1.requestFocus();
                            }
                            if (rd1.isChecked()) {
                                pgender = rd1.getText().toString().trim();

                            }
                            if (rd2.isChecked()) {
                                pgender = rd2.getText().toString().trim();
                            }
                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {

                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                        String message = jsonObject1.getString("message");
                                        if (message.equals("update")) {
                                            Toast.makeText(context, "Pet profile is updated now!!", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(context, view_pets.class);
                                            context.startActivity(intent);
                                        } else {
                                            Toast.makeText(context, "Some thing went wrong!!", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context, "Check your internet connection!!", Toast.LENGTH_LONG).show();
                                }
                            }) {
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("petname", pname);
                                    params.put("petid", petid);
                                    params.put("petinfo", pinfo);
                                    params.put("gender", pgender);
                                    params.put("date", pbd);
                                    params.put("category", pcat);
                                    params.put("subcategory", pscat);
                                    return params;
                                }
                            };
                            mysingleton.getmInstance(context).addtorequestque(request);
                        }

                    });
                    builder.setView(view);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            holder.editfoodplan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String petId = holder.pi.getText().toString().trim();
                    Intent intent = new Intent(context, petFoodPlanUpdate.class);
                    intent.putExtra("petid", petId);
                    context.startActivity(intent);
                }
            });


            //add food plan code...................................................................../
            holder.addfoodplan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fodhelper.getavaiablefood(context);
                    final Button brakfasttime, lunchtime, dinnertime, total_calories, total_lunch_cal, total_dinner_cal, addfoodplan;
                    final TextView tvbreakfast, tvlunch, tvdinner;

                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.addfoodplanagain, null);
                    total_calories = (Button) view.findViewById(R.id.breakfastcal);
                    total_lunch_cal = (Button) view.findViewById(R.id.lunchcal);
                    total_dinner_cal = (Button) view.findViewById(R.id.dinnercal);


//select breakfast here............................................................................/
                    tvbreakfast = (TextView) view.findViewById(R.id.breakfast);
                    tvbreakfast.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fodhelper.getbreakfast(context, tvbreakfast, mselecteditem, total_calories);
                        }
                    });
//end breakfast here............................................................................/

//select lunxh here........................................................................................
                    tvlunch = (TextView) view.findViewById(R.id.lunch);
                    tvlunch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fodhelper.getlunch(context, tvlunch, mlselecteditem, total_lunch_cal);

                        }
                    });


                    // select lunxh here........................................................................................


                    tvdinner = (TextView) view.findViewById(R.id.dinner);

                    tvdinner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fodhelper.getdinner(context, tvdinner, mdselecteditem, total_dinner_cal);

                        }
                    });


                    brakfasttime = (Button) view.findViewById(R.id.addbreakfastTimebtn);
                    brakfasttime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selecttime(brakfasttime);
                        }
                    });

                    lunchtime = (Button) view.findViewById(R.id.addlunchTimebtn);
                    lunchtime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selecttime(lunchtime);
                        }
                    });

                    dinnertime = (Button) view.findViewById(R.id.adddinnerTimebtn);
                    dinnertime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selecttime(dinnertime);
                        }
                    });


                    spinner = (Spinner) view.findViewById(R.id.addfoodday);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.days, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            day = spinner.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    addfoodplan = (Button) view.findViewById(R.id.addfoodplanBtn);
                    addfoodplan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String pid = holder.pi.getText().toString();
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                            userid = sharedPreferences.getString("userId", null);
                            double totalcal = Double.valueOf(total_calories.getText().toString()) + Double.valueOf(total_dinner_cal.getText()
                                    .toString()) + Double.valueOf(total_lunch_cal.getText().toString());

                            bftime = brakfasttime.getText().toString();
                            dtime = dinnertime.getText().toString();
                            ltime = lunchtime.getText().toString();
                            breakfast = tvbreakfast.getText().toString();
                            lunch = tvlunch.getText().toString();
                            dinner = tvdinner.getText().toString();
                            if (bftime.equals(dtime) || bftime.equals(ltime)) {
                                brakfasttime.setError("Time conflict");
                                brakfasttime.requestFocus();
                                return;
                            }
                            if (ltime.equals(bftime) || ltime.equals(dtime)) {
                                lunchtime.setError("Time conflict");
                                lunchtime.requestFocus();
                                return;
                            }
                            if (dtime.equals(bftime) || dtime.equals(ltime)) {
                                dinnertime.setError("Time conflict");
                                dinnertime.requestFocus();
                                return;
                            }
                            if (breakfast.isEmpty()) {
                                tvbreakfast.setError("Select breakfast");
                                tvbreakfast.requestFocus();
                                return;
                            }
                            if (lunch.isEmpty()) {
                                tvlunch.setError("Select breakfast");
                                tvlunch.requestFocus();
                                return;
                            }
                            if (dinner.isEmpty()) {
                                tvdinner.setError("Select breakfast");
                                tvdinner.requestFocus();
                                return;
                            }
                            makefoodplan(userid, day, pid, String.valueOf(totalcal), bftime, ltime, dtime, breakfast, lunch, dinner);
                        }
                    });


                    alert.setView(view);
                    alert.show();
                }
            });

            //end of add food plan code..................................................................................//

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return data.length();
    }

    class petViewHolder extends RecyclerView.ViewHolder {
        TextView pet_name, pet_gender, category, sub, in, h, pi;
        Button editprofile, editfoodplan, addfoodplan;
        ImageView imgview;

        public petViewHolder(View itemView) {
            super(itemView);
            pet_name = (TextView) itemView.findViewById(R.id.petname);
            pet_gender = (TextView) itemView.findViewById(R.id.petgender);
            category = (TextView) itemView.findViewById(R.id.petcategory);
            sub = (TextView) itemView.findViewById(R.id.subpetcategory);
            in = (TextView) itemView.findViewById(R.id.petinf);
            h = (TextView) itemView.findViewById(R.id.pethhbbdd);
            pi = (TextView) itemView.findViewById(R.id.petid);
            editfoodplan = (Button) itemView.findViewById(R.id.foodBtn);
            addfoodplan = (Button) itemView.findViewById(R.id.addfoodplan);
            imgview = (ImageView) itemView.findViewById(R.id.img1);
            editprofile = (Button) itemView.findViewById(R.id.profileBtn);

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


    public void makefoodplan(final String UserId, final String day, final String petid, final String totcaloreis, final String bt, final String lt, final String dt, final String breakfat, final String lunch, final String dinner) {
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String message = jsonObject.getString("message");
                    if (message.equals("alreadyHave")) {
                        Toast.makeText(context, "You have already food plan for" + " " + day, Toast.LENGTH_LONG).show();
                    } else if (message.equals("success")) {
                        Toast.makeText(context, "You have successfully create food plan for" + " " + day, Toast.LENGTH_LONG).show();
                    }
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
                params.put("userid", UserId);
                params.put("petid", petid);
                params.put("day", day);
                params.put("breakfast", breakfat);
                params.put("lunch", lunch);
                params.put("dinner", dinner);
                params.put("breakfasttime", bt);
                params.put("lunchtime", lt);
                params.put("dinnertime", dt);
                params.put("totalfoodcal", totcaloreis);
//
                return params;
            }
        };
        mysingleton.getmInstance(context).addtorequestque(stringRequest1);

    }
}
