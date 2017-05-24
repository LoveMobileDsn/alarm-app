package alarm.avart.milos.rentalarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Milos-PC on 8/1/2016.
 */

public class AddAlarmActivity extends Activity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    DatePickerDialog startDatePickerDialog;
    DatePickerDialog endDatePickerDialog;
    SimpleDateFormat dateFormatter;
    TextView start_day;
    TextView end_day;

    int start_year;
    int end_year;
    int start_month;
    int end_month;
    int start_dayOfMonth;
    int end_dayOfMonth;

    int pay_count;
    int pay_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        start_day = (TextView)findViewById(R.id.startday_text);
        end_day = (TextView)findViewById(R.id.endday_text);
        start_day.setInputType(InputType.TYPE_NULL);
        start_day.requestFocus();

        end_day.setInputType(InputType.TYPE_NULL);
        end_day.requestFocus();

        setDateTimeField();

        // set the font of button
        Button toPayBtn = (Button)findViewById(R.id.on_payment_button);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/light_font.otf");
        toPayBtn.setTypeface(myTypeface);
        toPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateDate();

                pay_date = end_dayOfMonth;
                String pay_count_str = String.valueOf(pay_count);
                String pay_date_str = String.valueOf(pay_date);

                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("pay_start_day", start_day.getText().toString());
                editor.putString("pay_end_day", end_day.getText().toString());
                editor.putString("pay_count", pay_count_str);
                editor.putString("pay_date", pay_date_str);
                editor.commit();

                Intent i = new Intent(AddAlarmActivity.this, PayMethodActivity.class);
                startActivity(i);

            }

        });

    }

    public void setDateTimeField(){

        start_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDatePickerDialog.show();

            }
        });

        end_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDatePickerDialog.show();

            }
        });
        Calendar newCalendar = Calendar.getInstance();
        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                start_day.setText(dateFormatter.format(newDate.getTime()));

                start_year = year;
                start_month = monthOfYear;
                start_dayOfMonth = dayOfMonth;

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                end_day.setText(dateFormatter.format(newDate.getTime()));

                end_year = year;
                end_month = monthOfYear;
                end_dayOfMonth = dayOfMonth;

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    public void calculateDate(){

        if (start_month > end_month)
        {
            pay_count = (end_year - start_year - 1) * 12 + (12 - start_month + end_month);
        }
        else
        {
            pay_count = (end_year - start_year) * 12 + (end_month - start_month);
        }

        if (pay_count < 0)
        {
            alertBox();
        }
    }

    public void alertBox()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(AddAlarmActivity.this).create();
        alertDialog.setTitle("Warning");
        alertDialog.setMessage("Incorrect the date, Please reselect the date!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

}
