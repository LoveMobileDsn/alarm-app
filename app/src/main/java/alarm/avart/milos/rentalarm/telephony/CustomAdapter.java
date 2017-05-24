package alarm.avart.milos.rentalarm.telephony;

/**
 * Created by Milos-PC on 8/3/2016.
 */
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;


import alarm.avart.milos.rentalarm.R;

/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class CustomAdapter extends BaseAdapter implements OnClickListener, AdapterView.OnItemLongClickListener {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater = null;
    public Resources res;
    ListModel tempValues=null;
    int i=0;

    /*************  CustomAdapter Constructor *****************/
    public CustomAdapter(Activity a, ArrayList d,Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        data=d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        return false;
    }

    /********* Create a holder to contain inflated xml file elements ***********/
    public static class ViewHolder{

        public TextView pay_name_text;
        public TextView pay_date_text;
        public TextView pay_amount_text;
        public TextView pay_method_text;
        public TextView pay_end_text;
        public TextView pay_count_text;

    }

    /*********** Depends upon data size called for each row , Create each ListView row ***********/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi=convertView;
        ViewHolder holder;

        if(convertView==null){

            /********** Inflate tabitem.xml file for each row ( Defined below ) ************/
            vi = inflater.inflate(R.layout.alarm_row, null);

            /******** View Holder Object to contain tabitem.xml file elements ************/
            holder= new ViewHolder();
            holder.pay_name_text = (TextView)vi.findViewById(R.id.nick_name);
            holder.pay_date_text =(TextView)vi.findViewById(R.id.payment_date);
            holder.pay_amount_text =(TextView)vi.findViewById(R.id.payment_amount);
            holder.pay_method_text =(TextView)vi.findViewById(R.id.payment_method);
            holder.pay_end_text = (TextView)vi.findViewById(R.id.contract_end);
            holder.pay_count_text = (TextView)vi.findViewById(R.id.payment_count);



            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
//            holder.text.setText("No Data");
        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = (ListModel) data.get(position);

            /************  Set Model values in Holder elements ***********/

            holder.pay_name_text.setText(tempValues.getPay_name());
            holder.pay_date_text.setText(tempValues.getPay_date());
            holder.pay_amount_text.setText(tempValues.getPay_amount());
            holder.pay_method_text.setText(tempValues.getPay_method());
            holder.pay_end_text.setText(tempValues.getPay_end());
            holder.pay_count_text.setText(tempValues.getPay_count());

            /******** Set Item Click Listner for LayoutInflater for each row ***********/
            vi.setOnClickListener(new OnItemClickListener(position));
            vi.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });

        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked");
    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements OnClickListener{
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {

//            CustomListViewAndroidExample sct = (CustomListViewAndroidExample)activity;
//            sct.onItemClick(mPosition);
        }
    }
}