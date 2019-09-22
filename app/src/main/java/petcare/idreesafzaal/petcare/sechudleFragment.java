package petcare.idreesafzaal.petcare;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class sechudleFragment extends Fragment {
    Button playbtn, starttym, endtym, makeSchBtn, showerbtn, sstime, setime, makeshowerschedule, walkBtn, walkstime, walketime, makewalkschedule;
    String selectedday, selectname, selectedid;
    AlertDialog.Builder builder;
    String[] userids;
    String[] petids;
    String[] walkpetids;
    String selecteddaysh, selectedpetidsh, selectedstarttime, selectedendtimesh, selectedpetnamesh;
    String selecteddaywlk, selectedpetidwlk, selectedendtimewlk, selectedstarttimewlk, slecetdwalkname;
    String stype = "Shower schedule";
    String ptype = "Playing schedule";
    String wtype = "Walking schedule";
    localhost host = new localhost();
    String url = "http://" + host.api + "/andriodfiles/searchUserPet.php";

    Calendar calendar;
    TimePickerDialog timePickerDialog;
    EditText chosetime;
    int currentHour;
    int currentMinute;
    String amPm;
    String stime, etime;


    public static ArrayAdapter<String> adapter;
    ArrayList<String> arrayList = new ArrayList<String>();
    scheduleHelper schpler;
    ArrayAdapter<String> showeradpter;
    ArrayList<String> showerlist = new ArrayList<String>();
    String userid;

    Spinner sp, sp2, showersp1, showersp2, walksp1, walksp2;

    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.petsechudlefragment, null);
        playbtn = (Button) view.findViewById(R.id.playingBtn);
        showerbtn = (Button) view.findViewById(R.id.showerBtn);
        walkBtn = (Button) view.findViewById(R.id.walkingBtn);
        schpler = new scheduleHelper(getContext());

//service start here//////////////////////////////////////////////////////////////////////////////////////////


        adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, arrayList);
        showeradpter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, showerlist);
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        userid = sharedPref.getString("userId", null);
        builder = new AlertDialog.Builder(getContext());
        walkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View walkview = layoutInflater1.inflate(R.layout.walkingscheduledailog, null);
                walksp1 = (Spinner) walkview.findViewById(R.id.dayspinner2walk);
                walksp2 = (Spinner) walkview.findViewById(R.id.dyspinner1walk);
                walkstime = (Button) walkview.findViewById(R.id.startDayFargwalk);
                walketime = (Button) walkview.findViewById(R.id.endDayFargwalk);
                makewalkschedule = (Button) walkview.findViewById(R.id.walkScheduleBtnReg);

                ArrayAdapter<CharSequence> walkad = ArrayAdapter.createFromResource(getContext(), R.array.days, android.R.layout.simple_spinner_item);
                walkad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                walksp1.setAdapter(walkad);

                walkstime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendar = Calendar.getInstance();
                        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                        currentMinute = calendar.get(Calendar.MINUTE);

                        timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                if (hourOfDay >= 12) {
                                    amPm = "PM";
                                } else {
                                    amPm = "AM";
                                }
                                walkstime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);

                            }
                        }, currentHour, currentMinute, false);

                        timePickerDialog.show();
                    }
                });

                walketime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendar = Calendar.getInstance();
                        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                        currentMinute = calendar.get(Calendar.MINUTE);

                        timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                if (hourOfDay >= 12) {
                                    amPm = "PM";
                                } else {
                                    amPm = "AM";
                                }
                                walketime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);

                            }
                        }, currentHour, currentMinute, false);

                        timePickerDialog.show();
                    }
                });
                walksp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        slecetdwalkname = walksp1.getSelectedItem().toString();
                        selectedpetidwlk = walkpetids[position];
                        //Log.d("cvbb",Idss);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                walksp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selecteddaywlk = walksp1.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                makewalkschedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedstarttimewlk = walkstime.getText().toString();
                        selectedendtimewlk = walketime.getText().toString();
                        if (selectedstarttimewlk.isEmpty()) {
                            walkstime.setError("Select time");
                            walkstime.requestFocus();
                            return;
                        }
                        if (selectedendtimewlk.isEmpty()) {
                            walketime.setError("Select time");
                            walketime.requestFocus();
                            return;
                        }
                        schpler.makeschedule(slecetdwalkname, selectedpetidwlk, selecteddaywlk, selectedstarttimewlk, selectedendtimewlk, userid, wtype);
                    }
                });

                AlertDialog.Builder walk = new AlertDialog.Builder(getContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            adapter.clear();
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            walkpetids = new String[jsonArray.length()];
                            String message = jsonObject.getString("message");
                            if (message.equals("found")) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    arrayList.add(jsonArray.getJSONObject(i).getString("petname"));
                                    walkpetids[i] = (jsonArray.getJSONObject(i).getString("petid"));
                                }
                                walksp2.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "check internet connection", Toast.LENGTH_LONG).show();
                    }
                }) {
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("userid", userid);
                        return params;
                    }
                };
                mysingleton.getmInstance(getContext()).addtorequestque(stringRequest);
                walk.setView(walkview);
                walk.show();
            }
        });


        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view1 = inflater1.inflate(R.layout.playinglayoutdialog, null);
                starttym = (Button) view1.findViewById(R.id.startDayFarg);
                endtym = (Button) view1.findViewById(R.id.endDayFarg);
                makeSchBtn = (Button) view1.findViewById(R.id.playingScheduleBtnReg);
                sp = view1.findViewById(R.id.dyspinner1);
                sp2 = (Spinner) view1.findViewById(R.id.dayspinner2);


                ArrayAdapter<CharSequence> adapterday = ArrayAdapter.createFromResource(getContext(), R.array.days, android.R.layout.simple_spinner_item);
                adapterday.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp2.setAdapter(adapterday);

                sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectname = sp.getSelectedItem().toString();
                        selectedid = userids[position];
                        //Log.d("cvbb",Idss);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedday = sp2.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                starttym.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        calendar = Calendar.getInstance();
                        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                        currentMinute = calendar.get(Calendar.MINUTE);

                        timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                if (hourOfDay >= 12) {
                                    amPm = "PM";
                                } else {
                                    amPm = "AM";
                                }
                                starttym.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);

                            }
                        }, currentHour, currentMinute, false);

                        timePickerDialog.show();

                    }
                });


                endtym.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        calendar = Calendar.getInstance();
                        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                        currentMinute = calendar.get(Calendar.MINUTE);

                        timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                if (hourOfDay >= 12) {
                                    amPm = "PM";
                                } else {
                                    amPm = "AM";
                                }
                                endtym.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);

                            }
                        }, currentHour, currentMinute, false);

                        timePickerDialog.show();

                    }
                });

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            adapter.clear();
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            userids = new String[jsonArray.length()];
                            String message = jsonObject.getString("message");
                            if (message.equals("found")) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    arrayList.add(jsonArray.getJSONObject(i).getString("petname"));
                                    userids[i] = (jsonArray.getJSONObject(i).getString("petid"));
                                }
                                sp.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "check internet connection", Toast.LENGTH_LONG).show();
                    }
                }) {
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("userid", userid);
                        return params;
                    }
                };
                mysingleton.getmInstance(getContext()).addtorequestque(stringRequest);
                builder.setView(view1);
                builder.show();

                makeSchBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stime = starttym.getText().toString();
                        etime = endtym.getText().toString();
                        if (stime.isEmpty()) {
                            starttym.setError("Select Time");
                            starttym.requestFocus();
                            return;
                        }
                        if (etime.isEmpty()) {
                            endtym.setError("Select Time");
                            endtym.requestFocus();
                            return;
                        }
                        schpler.makeschedule(selectname, selectedid, selectedday, stime, etime, userid, ptype);
                    }
                });

            }
        });
        showerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View showerview = layoutInflater.inflate(R.layout.dialogshowerschedule, null);
                showersp1 = (Spinner) showerview.findViewById(R.id.dayspinner2Shower);
                showersp2 = (Spinner) showerview.findViewById(R.id.dyspinner1Shower);
                sstime = (Button) showerview.findViewById(R.id.startDayFargShower);
                setime = (Button) showerview.findViewById(R.id.endDayFargShower);
                makeshowerschedule = (Button) showerview.findViewById(R.id.showerScheduleBtnReg);

                ArrayAdapter<CharSequence> shwerad = ArrayAdapter.createFromResource(getContext(), R.array.days, android.R.layout.simple_spinner_item);
                shwerad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                showersp1.setAdapter(shwerad);
                showersp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedpetnamesh = showersp2.getSelectedItem().toString();
                        selectedpetidsh = petids[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                showersp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selecteddaysh = showersp1.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                sstime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selecttime(sstime);
                    }
                });
                setime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selecttime(setime);
                    }
                });


                makeshowerschedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedstarttime = sstime.getText().toString();
                        selectedendtimesh = setime.getText().toString();
                        if (selectedstarttime.isEmpty()) {
                            sstime.setError("Select time");
                            sstime.requestFocus();
                            return;
                        }
                        if (selectedendtimesh.isEmpty()) {
                            sstime.setError("Select time");
                            sstime.requestFocus();
                            return;
                        }
                        schpler.makeschedule(selectedpetnamesh, selectedpetidsh, selecteddaysh, selectedstarttime, selectedendtimesh, userid, stype);
                    }
                });

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            showeradpter.clear();
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            petids = new String[jsonArray.length()];
                            String message = jsonObject.getString("message");
                            if (message.equals("found")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    showerlist.add(jsonArray.getJSONObject(i).getString("petname"));
                                    petids[i] = (jsonArray.getJSONObject(i).getString("petid"));
                                }
                                showersp2.setAdapter(showeradpter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "check internet connection", Toast.LENGTH_LONG).show();
                    }
                }) {
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("userid", userid);
                        return params;
                    }
                };
                mysingleton.getmInstance(getContext()).addtorequestque(stringRequest);
                builder1.setView(showerview);
                builder1.show();

            }
        });

        return view;
    }
public void selecttime(final Button button){
    calendar = Calendar.getInstance();
    currentHour = calendar.get(Calendar.HOUR_OF_DAY);
    currentMinute = calendar.get(Calendar.MINUTE);

    timePickerDialog = new TimePickerDialog(getContext(),android.R.style.Theme_Holo_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
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
