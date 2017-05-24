package alarm.avart.milos.rentalarm;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

/**
 * Created by Milos-PC on 8/1/2016.
 */

public class SplashActivity extends Activity {
    private static int SPLASH_TIME_OUT = 3000;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView title_text = (TextView)findViewById(R.id.app_titile);
        TextView description_text = (TextView)findViewById(R.id.app_description);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/light_font.otf");
        title_text.setTypeface(myTypeface);
        description_text.setTypeface(myTypeface);





        new Handler().postDelayed(new Runnable() {
         /*
          * Showing splash screen with a timer. This will be useful when you
          * want to show case your app logo / company
          */

            @Override
            public void run() {
                SharedPreferences editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String pay_amount = editor.getString("payment_monthly", "");

                // This method will be executed once the timer is over
                // Start your app main activity
                if (pay_amount.equals(""))
                {
                    Intent i = new Intent(SplashActivity.this, ActivityEmpty.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }
}
