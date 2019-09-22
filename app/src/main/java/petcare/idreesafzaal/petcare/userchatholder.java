package petcare.idreesafzaal.petcare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class userchatholder extends RecyclerView.Adapter<userchatholder.viewholder> {
    byte[] imageBytes;
    JSONArray jsonArray;
    Context context;

    public userchatholder(JSONArray jsonArray, Context context) {
        this.jsonArray = jsonArray;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.doctorchatlayoutforrecycleview, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, int position) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            holder.userchatname.setText(jsonObject.getString("name"));
            holder.userchatid.setText(jsonObject.getString("userid"));
            String imagedata = jsonObject.getString("image");
            imageBytes = Base64.decode(imagedata, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.circleImageView.setImageBitmap(decodedImage);
            holder.chatbutton.setText(jsonObject.getString("email").toString());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = holder.userchatid.getText().toString().trim();
                    Intent intent = new Intent(context, messages.class);
                    intent.putExtra("rid", id);
                    context.startActivity(intent);
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
        CircleImageView circleImageView;
        TextView userchatname, userchatid;
        TextView chatbutton;

        public viewholder(View itemView) {
            super(itemView);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.doctorchatimage);
            userchatname = (TextView) itemView.findViewById(R.id.doctorchatname);
            userchatid = (TextView) itemView.findViewById(R.id.chatid);
            chatbutton = (TextView) itemView.findViewById(R.id.chatemail);
        }
    }
}
