package cybertech.childguide;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * stagent24@gmail.com
 * Created by CyberTech on 6/4/2013.
 */
public class MyDialog {

    public static void dialogOK(Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Information Dialog");
        builder.setNegativeButton("OK", null);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.show();
    }

    public static void dialogOK(Context context, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setNegativeButton("OK", null);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.show();
    }

    public static void dialog(Context context, String msg, DialogInterface.OnCancelListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Information Dialog");
        builder.setOnCancelListener(listener);
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.show();
    }

    public static void dialogOK(Context context, String msg, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Information Dialog");
        builder.setNegativeButton("OK", listener);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.show();
    }

}