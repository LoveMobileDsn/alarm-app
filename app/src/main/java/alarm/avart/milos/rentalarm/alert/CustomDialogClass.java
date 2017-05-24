package alarm.avart.milos.rentalarm.alert;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import alarm.avart.milos.rentalarm.R;

/**
 * Created by My Star on 8/7/2016.
 */
public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {

    public Activity c;
    public Button yes, no;

    public String alertContent;
    private TextView txt_dia;

    public CustomDialogClass(Context context) {
        super(context);
    }

    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    public CustomDialogClass(Activity a, String alertContent) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.alertContent = alertContent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        txt_dia = (TextView)findViewById(R.id.txt_dia);
        txt_dia.setText(alertContent);

        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_yes:
                c.finish();
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
