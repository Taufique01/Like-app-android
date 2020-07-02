package io.com.taufique.likeup.ui.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;

import io.com.taufique.likeup.ApiService;
import io.com.taufique.likeup.ApiServiceBuilder;
import io.com.taufique.likeup.Config;
import io.com.taufique.likeup.JsonModelObjects.ChecksumPaytm;
import io.com.taufique.likeup.JsonModelObjects.TransactionStatus;
import io.com.taufique.likeup.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PaymentActivity extends BaseDrawerActivity  implements PaytmPaymentTransactionCallback  {
    private Button buttonPlaceOrder;
    private SharedPreferences sharedPref;
    String custid = "", orderId = "";
    private static final String TAG = "lgx_PaymentActivity";
   // private MyBackendService myBackendService;
   // private String orderid_instamojo;

    private String contest_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Intent intent = getIntent();
        contest_id = intent.getStringExtra("contest_id");
        TextView payText= findViewById(R.id.tvPayText);
        payText.setText(Config.buildAmountText(intent.getStringExtra("amount")));
        sharedPref = getSharedPreferences("MY_KEY", Context.MODE_PRIVATE);






        buttonPlaceOrder = findViewById(R.id.button_place_order);
        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PaymentActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PaymentActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
                }
                //call this function on click event for BUY or CHECKOUT button
                generateCheckSum();
                Log.d(TAG, "generateCheckSum: generateCheckSum();generateCheckSum();generateCheckSum();generateCheckSum();");
            }
        });
    }

    private void generateCheckSum() {
        Log.d(TAG, "generateCheckSum: inside");
        String accessToken = sharedPref.getString("token", "");
        ApiService apiService = ApiServiceBuilder.getService();
        Call<ChecksumPaytm> call = apiService.getCustomerOrderAddPayment(accessToken,contest_id);
        call.enqueue(new Callback<ChecksumPaytm>() {

            @Override
            public void onResponse(Call<ChecksumPaytm> call, Response<ChecksumPaytm> response) {
                Log.d(TAG, "onResponse: response.body generateChecksum " + response.body());
                if (response.body().getChecksumHash() != null) {
                    ChecksumPaytm checksum = response.body();
                    // when app is ready to publish use production service
                    PaytmPGService service1paytm = PaytmPGService.getStagingService();
                    // when app is ready to publish use production service
                    // PaytmPGService  Service = PaytmPGService.getProductionService();

                    //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
                    HashMap<String, String> paramMap = new HashMap<String, String>();
                    //these are mandatory parameters
                    paramMap.put("MID", checksum.getMerchant_key()); //MID provided by paytm
                    paramMap.put("ORDER_ID", checksum.getOrderId());
                    paramMap.put("CUST_ID", checksum.getCustId());
                    paramMap.put("TXN_AMOUNT", checksum.getTnxAmount());
                    paramMap.put("CALLBACK_URL", checksum.getCall_back_url());
                    paramMap.put("CHECKSUMHASH", checksum.getChecksumHash());
                    paramMap.put("CHANNEL_ID", checksum.getChannel_id());//ne
                    paramMap.put("WEBSITE", checksum.getWebsite());//ne
                    paramMap.put("INDUSTRY_TYPE_ID", checksum.getIndustry_type_id());//ne
                    // paramMap.put( "EMAIL" , "abc@gmail.com");   // no need
                    //paramMap.put( "MOBILE_NO" , "999999999");  // no need
                    //paramMap.put("PAYMENT_TYPE_ID" ,"CC");    // no need
                    PaytmOrder Order = new PaytmOrder(paramMap);
                    service1paytm.initialize(Order, null);
                    // start payment service call here
                    service1paytm.startPaymentTransaction(PaymentActivity.this, true, true, PaymentActivity.this);
                } else {
                    existingOrderError();
                }
            }

            @Override
            public void onFailure(Call<ChecksumPaytm> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });
    }


    @Override
    public void onTransactionResponse(Bundle inResponse) {
        String orderId = inResponse.getString("ORDERID");
        //Toast.makeText(PaymentActivity.this, "orderid" + orderId, Toast.LENGTH_SHORT).show();
        ApiService apiService = ApiServiceBuilder.getService();
        String accessToken = sharedPref.getString("token", "");
        Call<TransactionStatus> call = apiService.checkTransactionStatus(orderId, accessToken, contest_id);
        call.enqueue(new Callback<TransactionStatus>() {
            @Override
            public void onResponse(Call<TransactionStatus> call, Response<TransactionStatus> response) {
                if (!response.isSuccessful()) {
                    handleUnknownError();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), ContestActivity.class);
                startActivity(intent);

                TransactionStatus status = response.body();
                Toast.makeText(PaymentActivity.this, "Your payment is " + status.getPaytStatus(), Toast.LENGTH_LONG).show();
                //showOrderStatus(false);
            }

            @Override
            public void onFailure(Call<TransactionStatus> call, Throwable t) {
                Toast.makeText(PaymentActivity.this, "FAILURE on VERIFICATION RESPONSE", Toast.LENGTH_SHORT).show();
            }
        });
        // Toast.makeText(PaymentActivity.this, "Success on payment", Toast.LENGTH_SHORT).show();
        //Toast.makeText(PaymentActivity.this, "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void networkNotAvailable() {
        Log.d(TAG, "networkNotAvailable: ");
    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage) {
        Log.d(TAG, "clientAuthenticationFailed: ");
    }

    @Override
    public void someUIErrorOccurred(String inErrorMessage) {
        Log.d(TAG, "someUIErrorOccurred: ");
    }

    @Override
    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
        Log.d(TAG, "onErrorLoadingWebPage: ");
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Log.d(TAG, "onBackPressedCancelTransaction: ");
    }

    @Override
    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
        Log.d(TAG, "onTransactionCancel: ");
    }

    public void handleUnknownError() {
        showErrorDialog(getString(R.string.msg_unknown));
    }

    public void existingOrderError() {
        //showErrorDialog(getString(R.string.existing_order_error));
    }

    public void showErrorDialog(String message) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.error))
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                })
                .show();
    }




    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }



}