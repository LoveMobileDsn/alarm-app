package alarm.avart.milos.rentalarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;

import alarm.avart.milos.rentalarm.alarm.Alarm;
import alarm.avart.milos.rentalarm.alarm.AlarmActivity;
import alarm.avart.milos.rentalarm.alert.StaticWakeLock;

/**
 * Created by Milos-PC on 8/1/2016.
 */
public class NicknameActivity extends Activity {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    EditText nickname_text;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);

        nickname_text = (EditText) findViewById(R.id.nickname_input);

        Button done = (Button) findViewById(R.id.done_btn);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nickname_str = nickname_text.getText().toString();
                if (nickname_str.equals("")) {
                    alertBox();
                } else {

                    SharedPreferences editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    String pay_name = nickname_str;
                    String pay_amount = editor.getString("payment_monthly", "");
                    String pay_method = editor.getString("payment_method", "");
                    String pay_start = editor.getString("pay_start_day", "");
                    String pay_end = editor.getString("pay_end_day", "");
                    String pay_count = editor.getString("pay_count", "");

                    String re_pay_date = setPaymentDate(pay_method, pay_end);

                    ContentValues values = new ContentValues();
                    values.put(MyProvider.PAYNAME, pay_name);
                    values.put(MyProvider.PAYDATE, re_pay_date);
                    values.put(MyProvider.PAYAMOUNT, pay_amount);
                    values.put(MyProvider.PAYMETHOD, pay_method);
                    values.put(MyProvider.PAYCONTRACTSTARTDAY, pay_start);
                    values.put(MyProvider.PAYCONTRACTENDDAY, pay_end);
                    values.put(MyProvider.PAYCOUNT, pay_count);

                    //set the alarm area
                    setAlarm(pay_method, pay_start, pay_end);

                    // save the schedule at the database
                    Uri uri = getContentResolver().insert(MyProvider.CONTENT_URI, values);

                    // sending intent
                    Intent i = new Intent(NicknameActivity.this, AlarmActivity.class);
                    startActivity(i);
                }
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void setAlarm(String pay_method, String pay_start, String pay_end) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 14);
    }

    public String setPaymentDate(String pay_method, String end_month) {

        Log.d("this is test string", end_month);
        String[] separate = end_month.split("-");

        int date = Integer.parseInt(separate[0]);
        int month = Integer.parseInt(separate[1]);

        if (pay_method.equals("Pre-Payment")) {
            if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                if (date == 31) return String.valueOf(1);
                else return String.valueOf(date + 1);

            }
            if (month == 4 || month == 6 || month == 9 || month == 11) {
                if (date == 30) return String.valueOf(1);
                else return String.valueOf(date + 1);
            } else {
                // When Feb...
                if (date == 28 || date == 29) return String.valueOf(1);
                else return String.valueOf(date + 1);
            }

        } else return String.valueOf(date);
    }


    public void alertBox() {

        AlertDialog alertDialog = new AlertDialog.Builder(NicknameActivity.this).create();
        alertDialog.setTitle("Empty !");
        alertDialog.setMessage("Insert Nice!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Nickname Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://alarm.avart.milos.rentalarm/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Nickname Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://alarm.avart.milos.rentalarm/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
