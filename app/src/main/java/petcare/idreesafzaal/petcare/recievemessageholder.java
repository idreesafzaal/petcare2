package petcare.idreesafzaal.petcare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class recievemessageholder extends RecyclerView.Adapter<recievemessageholder.viewholder> {

    List<String> list;
    Context context;
    public recievemessageholder(List<String> list,Context context){

        this.list=list;
    }
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.messageinputoutputlayout,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.send.setVisibility(View.INVISIBLE);
        String m=list.get(position).toString();
        holder.recivemessage.setText(list.get(position).toString());

    }

    @Override
    public int getItemCount() {

            return list.size();


    }



    public class viewholder extends RecyclerView.ViewHolder{
           TextView recivemessage,send;

        public viewholder(View itemView) {
            super(itemView);
            send=(TextView) itemView.findViewById(R.id.sendtext);
            recivemessage=(TextView) itemView.findViewById(R.id.recievetext);
        }
    }
}
