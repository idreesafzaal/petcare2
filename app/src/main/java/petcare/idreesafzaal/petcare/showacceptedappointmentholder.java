package petcare.idreesafzaal.petcare;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class showacceptedappointmentholder extends RecyclerView.Adapter<showacceptedappointmentholder.viewholder> {

    JSONArray jsonArray;
    Context context;
    byte[] imageBytes;
    String imagedata;
    public showacceptedappointmentholder(JSONArray jsonArray,Context context){
        this.jsonArray=jsonArray;
        this.context=context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.showappointmentlayout, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
          holder.acceptapp.setVisibility(View.INVISIBLE);
          holder.rejectapp.setVisibility(View.INVISIBLE);
        JSONObject jsonObject = null;
        try {
            jsonObject = jsonArray.getJSONObject(position);
            holder.petownerid.setText(jsonObject.getString("userid").toString());
            imagedata=jsonObject.getString("image").toString();
            if (imagedata != null) {
                imageBytes = Base64.decode(imagedata, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                holder.petownerpic.setImageBitmap(decodedImage);
            }
            holder.appid.setText(jsonObject.getString("appid").toString());
            holder.petownername.setText(jsonObject.getString("username").toString());
            holder.petowneremail.setText(jsonObject.getString("date").toString());
            holder.appointmentdate.setText(jsonObject.getString("time").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class viewholder extends RecyclerView.ViewHolder{
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
}
