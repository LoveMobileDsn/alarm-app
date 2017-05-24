package alarm.avart.milos.rentalarm;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Milos-PC on 8/1/2016.
 */
public class PayMethodActivity extends Activity {

    public Spinner spinner;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymethod);

        spinner = (Spinner)findViewById(R.id.spinner);

        spinner.setPrompt("Please select the payment method");


        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

//        // Spinner item selection Listener
//        addListenerOnSpinnerItemSelection();
//
//        // Button click Listener
//        addListenerOnButton();

        Button payment = (Button)findViewById(R.id.payment_btn);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cur_payment = String.valueOf(spinner.getSelectedItem());


                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("payment_method", cur_payment);
                editor.commit();

                Intent i = new Intent(PayMethodActivity.this, MonthlyActivity.class);
                startActivity(i);
            }
        });


    }


}
