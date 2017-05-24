package alarm.avart.milos.rentalarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import alarm.avart.milos.rentalarm.alarm.Alarm;
import alarm.avart.milos.rentalarm.alarm.AlarmActivity;
import alarm.avart.milos.rentalarm.alarm.AlarmListAdapter;
import alarm.avart.milos.rentalarm.alert.CustomDialogClass;
import alarm.avart.milos.rentalarm.alert.StaticWakeLock;
import alarm.avart.milos.rentalarm.database.Database;
import alarm.avart.milos.rentalarm.service.AlarmServiceBroadcastReciever;
import alarm.avart.milos.rentalarm.telephony.CustomAdapter;
import alarm.avart.milos.rentalarm.telephony.ListModel;

public class MainActivity extends Activity implements android.view.View.OnClickListener{

    ListView alarmList;
    CustomAdapter adapter;
    public  MainActivity CustomListView = null;
    public ArrayList<ListModel> CustomListViewValuesArr = new ArrayList<ListModel>();
    Cursor cursor;
    int total_money;
    long CurrentSelectID;

    // TODO my
    private MediaPlayer mediaPlayer;
    private Alarm alarm;
    private boolean alarmActive;

    AlarmListAdapter alarmListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomListView = this;
        /******** Take some data in Arraylist ( CustomListViewValuesArr ) ***********/
        setListData();
        Resources res = getResources();
        alarmList = (ListView)findViewById(R.id.alarm_list);

        /**************** set the edit text *********/
        TextView total_count = (TextView)findViewById(R.id.rent_count);

        TextView total_money_text = (TextView)findViewById(R.id.total_money);

        /**************** Create Custom Adapter *********/
        adapter = new CustomAdapter(CustomListView, CustomListViewValuesArr, res);
        alarmList.setAdapter(adapter);
        alarmList.setLongClickable(true);

        total_count.setText(String.valueOf(cursor.getCount()));
        total_money_text.setText(String.format("$%s", String.valueOf(total_money)));


        // send the activity to Add alarm page.

        Button addMore = (Button)findViewById(R.id.addmore_btn);
        addMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddAlarmActivity.class);
                startActivity(i);
            }
        });
//        longClickMethod();
        alarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "haha", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // TODO my
        Bundle bundle = this.getIntent().getExtras();
        try {
            alarm = (Alarm) bundle.getSerializable("alarm");
        } catch (NullPointerException nullPointerEx) {

        }

        alarmListAdapter = new AlarmListAdapter(this, 1);

        Button close = (Button) findViewById(R.id.close);
        if(bundle == null) {
            close.setVisibility(View.INVISIBLE);
        } else {
            close.setVisibility(View.VISIBLE);
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!alarmActive)
//                    return;
//                alarmActive = false;

                try {
                    mediaPlayer.stop();
                } catch (IllegalStateException ise) {
                } catch (NullPointerException nullPo) {
                }

                try {
                    mediaPlayer.release();
                } catch (Exception e) {
                }

                Calendar newAlarmTime = Calendar.getInstance();
                newAlarmTime.set(Calendar.HOUR_OF_DAY, 16);
                newAlarmTime.set(Calendar.MINUTE, 59);
                newAlarmTime.set(Calendar.SECOND, 0);
                newAlarmTime.set(Calendar.YEAR, 2016);
                newAlarmTime.set(Calendar.MONTH, 7);
                newAlarmTime.set(Calendar.DATE, 7);
                try {
                    alarm.setAlarmTime(newAlarmTime);
                } catch (NullPointerException nullPointer) {
                }

                // TODO my alarm reset
                try {
                    Database.init(getApplicationContext());
                    if (getMathAlarm().getId() < 1) {
                        Database.create(getMathAlarm());
                    } else {
                        Database.update(getMathAlarm());
                    }

                    Intent mathAlarmServiceIntent = new Intent(MainActivity.this, AlarmServiceBroadcastReciever.class);
                    sendBroadcast(mathAlarmServiceIntent, null);

                    Toast.makeText(MainActivity.this, getMathAlarm().getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException nullPo) {

                }

                CustomDialogClass cdc=new CustomDialogClass(MainActivity.this, "Do you realy want to exit?");
                cdc.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdc.show();

            }
        });

        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {

                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d(getClass().getSimpleName(), "Incoming call: " + incomingNumber);
                        try {
                            mediaPlayer.pause();
                        } catch (IllegalStateException e) {

                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.d(getClass().getSimpleName(), "Call State Idle");
                        try {
                            mediaPlayer.start();
                        } catch (IllegalStateException e) {

                        } catch (NullPointerException nullPointer) {

                        }
                        break;
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        startAlarm();
    }

    // TODO my alarm reset
    public Alarm getMathAlarm() {
        return alarm;
    }
    // TODO my alarm reset end

    private void startAlarm() {

        try {
            if (alarm.getAlarmTonePath() != "") {
                mediaPlayer = new MediaPlayer();

                try {
                    mediaPlayer.setVolume(1.0f, 1.0f);
                    mediaPlayer.setDataSource(this,
                            Uri.parse(alarm.getAlarmTonePath()));
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                } catch (Exception e) {
                    mediaPlayer.release();
                    alarmActive = false;
                }
            }
        } catch (NullPointerException nullPointer) {

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        alarmActive = true;
    }

    @Override
    public void onBackPressed() {
        if (!alarmActive)
            super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        StaticWakeLock.lockOff(this);
    }

    @Override
    protected void onDestroy() {
        try {
            mediaPlayer.stop();
        } catch (Exception e) {

        }
        try {
            mediaPlayer.release();
        } catch (Exception e) {

        }
        super.onDestroy();
    }
    // TODO my end
    @Override
    protected void onStart() {
        super.onStart();
        longClickMethod();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        longClickMethod();
    }

    /****** Function to set data in ArrayList *************/

    public void setListData(){

        total_money = 0;
        cursor = getAllAlarm();
        cursor.moveToFirst();
        do {
            final ListModel sched = new ListModel();
            sched.setPay_count(String.format("1/%s", cursor.getString(0)));
            sched.setPay_name(cursor.getString(1));
            if (Integer.parseInt(cursor.getString(2)) < 10) sched.setPay_date(String.format("0%s",cursor.getString(2)));
            else sched.setPay_date(cursor.getString(2));
            sched.setPay_amount(String.format("$%s", cursor.getString(3)));
            sched.setPay_method(cursor.getString(4));
            sched.setPay_end(String.format("%s ~ %s", cursor.getString(5), cursor.getString(6)));

            total_money = total_money + Integer.parseInt(cursor.getString(3));
            CustomListViewValuesArr.add(sched);
        } while (cursor.moveToNext()); /// while (cursor.moveToNext())
    }

    public Cursor getAllAlarm() {

        Cursor mCursor = null;
        Uri allAlarm = MyProvider.CONTENT_URI;
        if (android.os.Build.VERSION.SDK_INT < 11) {
            // ---before Honeycomb---
            mCursor = managedQuery(allAlarm, null, null, null, null);
        } else {
            // ---Honeycomb and later---
            CursorLoader cursorLoader = new CursorLoader(this, allAlarm,
                    null, null, null, null);
            mCursor = cursorLoader.loadInBackground();
        }

//        mCursor.
        return mCursor;

    }

    public void longClickMethod(){
//        alarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), "hahaSubFunction", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
        alarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "haha", Toast.LENGTH_SHORT).show();

                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                CurrentSelectID = id;
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Delete");
                dialog.setMessage(String.format("Delete this contract ?"));
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int count = 0;
                        Uri uriToDel = ContentUris.withAppendedId(MyProvider.CONTENT_URI, CurrentSelectID);
                        count = getContentResolver().delete(uriToDel, null, null);
                        if (count > 0) {
                            Toast.makeText(getBaseContext(), "Delete success", Toast.LENGTH_SHORT).show();
                            setListData();
                        } else {
                            Toast.makeText(getBaseContext(), "Delete fail", Toast.LENGTH_SHORT).show();
                        }
//
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}



