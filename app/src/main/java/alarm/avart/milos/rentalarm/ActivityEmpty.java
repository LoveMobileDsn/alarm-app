package alarm.avart.milos.rentalarm;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Milos-PC on 8/4/2016.
 */
public class ActivityEmpty extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_empty);

        ImageView add = (ImageView)findViewById(R.id.addImage);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(ActivityEmpty.this, AddAlarmActivity.class);
                startActivity(i);
            }
        });

    }

}
