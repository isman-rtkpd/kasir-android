package com.monopecez.kaskup2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/* loaded from: classes2.dex */
public class UpdateHargaActivity extends MainActivity {
    static Button jsonButtonCancel;
    static Button jsonButtonOk;
    static Button jsonButtonRequest;
    static EditText jsonInput;
    static TextView jsonTVResponse;

    static TextView jsonRequestCode;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.kupat.test.MainActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(Bundle mSavedInstanceState) {
        super.onCreate(mSavedInstanceState);
        setContentView(R.layout.update_harga);
        jsonRequestCode = (TextView) findViewById(R.id.hargaBaruRequestCode);
        jsonInput = (EditText) findViewById(R.id.hargaBaruET);
        jsonTVResponse = (TextView) findViewById(R.id.hargaBaruResponse);
        jsonButtonOk = (Button) findViewById(R.id.hargaBaruButton);
        jsonButtonCancel = (Button) findViewById(R.id.hargaBaruButtonCancel);
        jsonButtonRequest = (Button) findViewById(R.id.hargaBaruButtonRequest);
        jsonButtonOk.setOnClickListener(new View.OnClickListener() { // from class: com.kupat.test.UpdateHargaActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                System.out.println("YANG DIINPUT : " + jsonInput.getText().toString());
                String jsonString = jsonInput.getText().toString();
                if (jsonString.length() != 0) {
                    try {
                        new JSONObject(jsonString);
                        SharedPreferences sharedPref = UpdateHargaActivity.this.getSharedPreferences("pricesPreferences", 0);
                        SharedPreferences.Editor editor = sharedPref.edit();

                        editor.putString("created", "UPDATED");
                        editor.putString("jsonString", jsonString);

                        editor.apply();
                        parseJson();
                        UpdateHargaActivity.this.finish();
                    } catch (JSONException e) {
                        Context context = UpdateHargaActivity.this.getApplicationContext();
                        Toast toast = Toast.makeText(context, "INVALID JSON", Toast.LENGTH_SHORT);
                        toast.show();
                        System.out.println(e);
                    }
                }
            }
        });
        jsonButtonCancel.setOnClickListener(new View.OnClickListener() { // from class: com.kupat.test.UpdateHargaActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                UpdateHargaActivity.this.finish();
            }
        });
        jsonButtonCancel.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.kupat.test.UpdateHargaActivity.3
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View v) {
                UpdateHargaActivity.jsonInput.setText("");
                return true;
            }
        });
        jsonButtonRequest.setOnClickListener(new View.OnClickListener() { // from class: com.kupat.test.UpdateHargaActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                final TextView textView = (TextView) UpdateHargaActivity.this.findViewById(R.id.hargaBaruResponse);


                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient();

                            Request request = new Request.Builder()
                                    .url("http://ourveins.id:8082/get-harga/" + jsonRequestCode.getText().toString())
                                    .build();
                            Response response = client.newCall(request).execute();
                            String reqCode = jsonRequestCode.getText().toString();
                            String resp = response.body().string().replace("\n", "");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    jsonTVResponse.setText(resp);
                                    jsonInput.setText(resp);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });
    }
}
