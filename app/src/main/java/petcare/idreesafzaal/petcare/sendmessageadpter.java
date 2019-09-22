package petcare.idreesafzaal.petcare;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.List;

public class sendmessageadpter extends RecyclerView.Adapter<sendmessageadpter.viewholder> {


    JSONArray jsonArray;
    List<String> list;
    List<String> ids;
    Context context;
    int totallength;
    int i;

    public sendmessageadpter(List<String> list, List<String> ids, Context context, int i) {
        this.i = i;
        this.list = list;
        this.ids = ids;
        this.context = context;

        if (list.size() > ids.size()) {
            totallength = list.size();
        }
        if (ids.size() > list.size()) {
            totallength = ids.size();
        }
        if (list.size() == ids.size()) {
            totallength = list.size();
        }


    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.messageinputoutputlayout, parent, false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String currenrid = sharedPreferences.getString("userId", null);
            // String sendmessage=list.get(position).toString();
            //String recievemessage=ids.get(position).toString();

            if (!list.get(position).toString().equals("")) {
                holder.send.setVisibility(View.VISIBLE);
                holder.send.setText(list.get(position).toString());
                holder.recive.setVisibility(View.INVISIBLE);

            } else {
                holder.send.setVisibility(View.INVISIBLE);
            }


            if (!ids.get(position).toString().equals("")) {
                holder.recive.setVisibility(View.VISIBLE);
                holder.recive.setText(ids.get(position).toString());

            } else {
                holder.recive.setVisibility(View.INVISIBLE);
            }

            //holder.send.setVisibility(View.INVISIBLE);


            // holder.recive.setText(ids.get(position).toString());


            // holder.recive.setVisibility(View.INVISIBLE);


            // holder.recive.setVisibility(View.INVISIBLE);
        } catch (Exception e) {

        }


    }

    @Override
    public int getItemCount() {
        return totallength;
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView send, recive;

        public viewholder(View itemView) {
            super(itemView);
            send = (TextView) itemView.findViewById(R.id.sendtext);
            recive = (TextView) itemView.findViewById(R.id.recievetext);

        }
    }
}
