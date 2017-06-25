package appsiam.tua.saran.mysiam;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Admin on 18/6/2560.
 */

public class MyAlert {

    private Context context;//

    public MyAlert(Context context) {
        this.context = context;
    }

    public void myDialog(String strTitle, String strMessage){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);//alertdialog.builder คือ class
        builder.setCancelable(false);//เป็นการ show message ต้องกด undo show message ถึงจะหายไป
        builder.setIcon(R.mipmap.ic_name);
        builder.setTitle(strTitle);
        builder.setMessage(strMessage);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.show();
    }


}// Main Class
