package alarm.avart.milos.rentalarm.telephony;

/**
 * Created by Milos-PC on 8/3/2016.
 */
public class ListModel {
    private  String pay_name = "";
    private  String pay_date = "";
    private  String pay_amount = "";
    private  String pay_method = "";
    private  String pay_end = "";
    private  String pay_count = "";

    /*********** Set Methods ******************/
    public void setPay_name(String pay_name)
    {
        this.pay_name = pay_name;
    }

    public void setPay_amount(String pay_amount)
    {
        this.pay_amount = pay_amount;
    }

    public void setPay_date(String pay_date)
    {
        this.pay_date = pay_date;
    }



    public void setPay_end(String pay_end)
    {
        this.pay_end = pay_end;
    }


    public void setPay_count(String pay_count)
    {
        this.pay_count = pay_count;
    }

    public void setPay_method(String pay_method)
    {
        this.pay_method = pay_method;
    }



    /*********** Get Methods ****************/
    public String getPay_name()
    {
        return this.pay_name;
    }

    public String getPay_date()
    {
        return this.pay_date;
    }

    public String getPay_amount()
    {
        return this.pay_amount;
    }

    public String getPay_method()
    {
        return this.pay_method;
    }


    public String getPay_end()
    {
        return this.pay_end;
    }
    public String getPay_count()
    {
        return this.pay_count;
    }






}
