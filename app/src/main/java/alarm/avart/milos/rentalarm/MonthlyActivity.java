package alarm.avart.milos.rentalarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Milos-PC on 8/1/2016.
 */
public class MonthlyActivity extends Activity {

    EditText editText;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);
        editText = (EditText)findViewById(R.id.editText);

        Button monthly = (Button)findViewById(R.id.monthly_btn);

        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String edit_text_str = editText.getText().toString();

                if (edit_text_str.equals(""))
                {
                    alertBox();
                }
                else {
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("payment_monthly", edit_text_str);
                    editor.commit();

                    Intent i = new Intent(MonthlyActivity.this, NicknameActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    public void alertBox()
    {

        AlertDialog alertDialog = new AlertDialog.Builder(MonthlyActivity.this).create();
        alertDialog.setTitle("Empty !");
        alertDialog.setMessage("Insert monthly payment!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();

    }


}
