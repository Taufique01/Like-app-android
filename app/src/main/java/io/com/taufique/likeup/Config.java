package io.com.taufique.likeup;

public class Config {
    public static final String PAYTM_MERCHANT_ID = "Ulbgcl83114033677105"; //YOUR TEST MERCHANT ID
    public static final String url_paytm_callback = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=";//"http://rafi9z.pythonanywhere.com/api/paytm/response";// "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
    public static final String url_base = "http://192.168.43.137:8000";//paytmwingstest1.atwebpages.com/paytm/";
   public static String getFullUrl(String uri){
       return url_base+uri;

   }

    public static String buildAmountText(String amount){
        return "You Need to Pay The Entry Amount Rs "+amount+"/= to Participate into The Contest." ;

    }


}
