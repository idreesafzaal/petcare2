package petcare.idreesafzaal.petcare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class foodhelper {

    // double total_cal,brekfast_cal,lunch_cal,dinner_cal;


    String[] listitem;
    String[] calores;


    String[] lunchitem;
    String[] lunchcalories;
    boolean[] lunchchecked;

    String[] dinneritem;
    String[] dinnercalories;
    boolean[] dinnerchecked;
    localhost host = new localhost();

    boolean[] checkeditem;
    String avaibleFoodURL = "http://" + host.api + "/andriodfiles/avaibleFOOD.php";

    public foodhelper() {


    }

    public void getavaiablefood(final Context context) {
        StringRequest request = new StringRequest(Request.Method.POST, avaibleFoodURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    int len = jsonArray.length();
                    lunchitem = new String[len];
                    lunchcalories = new String[len];
                    dinneritem = new String[len];
                    dinnercalories = new String[len];
                    listitem = new String[len];
                    calores = new String[len];
                    for (int i = 0; i < len; i++) {
                        //JSONObject jsonObject=jsonArray.getJSONObject(i);
                        lunchitem[i] = (jsonArray.getJSONObject(i).getString("lunch"));
                        lunchcalories[i] = (jsonArray.getJSONObject(i).getString("lunch_cal"));

                        listitem[i] = (jsonArray.getJSONObject(i).getString("item"));
                        calores[i] = (jsonArray.getJSONObject(i).getString("calores"));

                        dinneritem[i] = (jsonArray.getJSONObject(i).getString("dinner"));
                        dinnercalories[i] = (jsonArray.getJSONObject(i).getString("dinner_cal"));
                        // listitem=g
                        // listitem=jsonArray.getJSONObject(i).getString("item");
                    }

                    checkeditem = new boolean[listitem.length];
                    lunchchecked = new boolean[lunchitem.length];
                    dinnerchecked = new boolean[dinneritem.length];
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        mysingleton.getmInstance(context).addtorequestque(request);

    }

    public void food(Context context, final TextView breakfast, final TextView lunch, final ArrayList<Integer> mselecteditem) {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(context);
        mbuilder.setTitle("Avaible food for pets");
        mbuilder.setMultiChoiceItems(listitem, checkeditem, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {

                if (isChecked) {
                    if (!mselecteditem.contains(position)) {
                        mselecteditem.add(position);
                    }

                } else if (mselecteditem.contains(position)) {
                    mselecteditem.remove((Integer) position);

                }
            }
        });
        mbuilder.setCancelable(false);
        mbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = "";
                int cal = 0;
                for (int i = 0; i < mselecteditem.size(); i++) {
                    item = item + listitem[mselecteditem.get(i)];
                    cal = cal + Integer.valueOf(calores[mselecteditem.get(i)]);

                    if (i != mselecteditem.size() - 1) {
                        item = item + ", ";
                    }
                }

                breakfast.setText(item);
                String cale = String.valueOf(cal);
                lunch.setText(cale);

            }
        });

        mbuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mbuilder.setNeutralButton(R.string.clear_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < checkeditem.length; i++) {
                    checkeditem[i] = false;
                    mselecteditem.clear();
                    breakfast.setText("");
                }
            }
        });
        // mbuilder.setView(breakTimeBtn);

        AlertDialog md = mbuilder.create();
        md.show();


    }


    public void getbreakfast(final Context context, final TextView breakfast, final ArrayList<Integer> mselecteditem, final Button bt) {

        AlertDialog.Builder b_builder = new AlertDialog.Builder(context);
        b_builder.setTitle("Avaible food for pets");
        b_builder.setMultiChoiceItems(listitem, checkeditem, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {

                if (isChecked) {

                    if (!mselecteditem.contains(position)) {
                        mselecteditem.add(position);
                    }

                } else if (mselecteditem.contains(position)) {
                    mselecteditem.remove((Integer) position);

                }
            }
        });
        b_builder.setCancelable(false);
        b_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = "";
                double brekfast_cal = 0;

                for (int i = 0; i < mselecteditem.size(); i++) {

                    item = item + listitem[mselecteditem.get(i)];
                    brekfast_cal = brekfast_cal + Double.valueOf(calores[mselecteditem.get(i)]);

                    if (i != mselecteditem.size() - 1) {
                        item = item + ", ";
                    }

                }

                breakfast.setText(item);
                // double colrs= gettotal_cal();

                String cale = String.valueOf(brekfast_cal);
                bt.setVisibility(View.VISIBLE);
                bt.setText(cale);


            }
        });

        b_builder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b_builder.setNeutralButton(R.string.clear_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < checkeditem.length; i++) {
                    checkeditem[i] = false;
                    mselecteditem.clear();
                    breakfast.setText("");
                    bt.setText("");
                    bt.setVisibility(View.INVISIBLE);
                }
            }
        });
        // mbuilder.setView(breakTimeBtn);

        AlertDialog bf = b_builder.create();



    }


    public void getdinner(Context context, final TextView dinner, final ArrayList<Integer> mselecteditem, final Button bt) {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(context);
        mbuilder.setTitle("Avaible food for pets");
        mbuilder.setMultiChoiceItems(dinneritem, dinnerchecked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {

                if (isChecked) {
                    if (!mselecteditem.contains(position)) {
                        mselecteditem.add(position);
                    }

                } else if (mselecteditem.contains(position)) {
                    mselecteditem.remove((Integer) position);

                }
            }
        });
        mbuilder.setCancelable(false);
        mbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = "";
                double dinner_cal = 0;
                // int cal = 0;
                for (int i = 0; i < mselecteditem.size(); i++) {
                    item = item + dinneritem[mselecteditem.get(i)];
                    dinner_cal = dinner_cal + Integer.valueOf(dinnercalories[mselecteditem.get(i)]);

                    if (i != mselecteditem.size() - 1) {
                        item = item + ", ";
                    }
                }

                dinner.setText(item);
                // gettotal_cal();
                String cale = String.valueOf(dinner_cal);
                bt.setText(cale);

            }
        });

        mbuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mbuilder.setNeutralButton(R.string.clear_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < dinnerchecked.length; i++) {
                    dinnerchecked[i] = false;
                    mselecteditem.clear();
                    dinner.setText("");
                    bt.setText("");
                }
            }
        });
        // mbuilder.setView(breakTimeBtn);

        AlertDialog md = mbuilder.create();
        md.show();


    }


    public void getlunch(Context context, final TextView lunch, final ArrayList<Integer> mselecteditem, final Button bt) {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(context);
        mbuilder.setTitle("Avaible food for pets");
        mbuilder.setMultiChoiceItems(lunchitem, lunchchecked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {

                if (isChecked) {
                    if (!mselecteditem.contains(position)) {
                        mselecteditem.add(position);
                    }

                } else if (mselecteditem.contains(position)) {
                    mselecteditem.remove((Integer) position);

                }
            }
        });
        mbuilder.setCancelable(false);
        mbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = "";
                double lunch_cal = 0;
                for (int i = 0; i < mselecteditem.size(); i++) {
                    item = item + lunchitem[mselecteditem.get(i)];
                    try {
                        lunch_cal = lunch_cal + Double.valueOf(lunchcalories[mselecteditem.get(i)]);
                    } catch (Exception e) {
                    }

                    if (i != mselecteditem.size() - 1) {
                        item = item + ", ";
                    }
                }

                lunch.setText(item);
                //double a=Double.valueOf(cl);
                // double colrs=lunch_cal+a;

                //bt.setVisibility(View.VISIBLE);
                String cale = String.valueOf(lunch_cal);
                bt.setText(cale);


            }
        });

        mbuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mbuilder.setNeutralButton(R.string.clear_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < lunchchecked.length; i++) {
                    lunchchecked[i] = false;
                    mselecteditem.clear();
                    lunch.setText("");
                    bt.setText("");
                }
            }
        });
        // mbuilder.setView(breakTimeBtn);

        AlertDialog md = mbuilder.create();
        md.show();


    }

//public double gettotal_cal(){
//        total_cal=brekfast_cal+lunch_cal+dinner_cal;
//        return  total_cal;
//}

}
