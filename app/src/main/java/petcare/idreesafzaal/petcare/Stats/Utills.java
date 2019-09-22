package petcare.idreesafzaal.petcare.Stats;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.WindowManager;

import com.gmail.samehadar.iosdialog.IOSDialog;

import petcare.idreesafzaal.petcare.R;

public class Utills
{
    private static IOSDialog createISODialog(Context context)
    {
      IOSDialog iosDialog=new IOSDialog.Builder(context).setOnCancelListener(new DialogInterface.OnCancelListener() {
          @Override
          public void onCancel(DialogInterface dialog) {

          }
      }).setDimAmount(3)
              .setSpinnerColorRes(R.color.white)
              .setMessageColorRes(R.color.white)
              .setMessageContent("Please Wait...")
              .setCancelable(true)
              .setMessageContentGravity(Gravity.END)
              .build();
        try {
            iosDialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        iosDialog.setCancelable(false);

        // dialog_start_date.setMessage(Message);
        return  iosDialog;
    }

    public static boolean isNetworkAvailable(Context context) {
        // Using ConnectivityManager to check for Network Connection
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
