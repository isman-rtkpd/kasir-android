package com.monopecez.kaskup2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class UpdateHargaActivity extends MainActivity {
    static Button jsonButtonCancel;
    static Button jsonButtonOk;
    static Button jsonButtonRequest;
    static EditText jsonInput;
    static TextView jsonTVResponse;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.kupat.test.MainActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(Bundle mSavedInstanceState) {
        super.onCreate(mSavedInstanceState);
//        setContentView(R.layout.update_harga);
//        jsonInput = (EditText) findViewById(R.id.hargaBaruET);
//        jsonButtonOk = (Button) findViewById(R.id.hargaBaruButton);
//        jsonButtonCancel = (Button) findViewById(R.id.hargaBaruButtonCancel);
//        jsonButtonRequest = (Button) findViewById(R.id.hargaBaruButtonRequest);
//        jsonButtonOk.setOnClickListener(new View.OnClickListener() { // from class: com.kupat.test.UpdateHargaActivity.1
//            @Override // android.view.View.OnClickListener
//            public void onClick(View v) {
//                System.out.println("YANG DIINPUT : " + UpdateHargaActivity.jsonInput.getText().toString());
//                String jsonString = UpdateHargaActivity.jsonInput.getText().toString();
//                if (jsonString.length() != 0) {
//                    try {
//                        new JSONObject(jsonString);
//                        UpdateHargaActivity.this.parseJson(jsonString);
//                        SharedPreferences sharedPref = UpdateHargaActivity.this.getSharedPreferences("pricesPreferences", 0);
//                        SharedPreferences.Editor editor = sharedPref.edit();
//                        if (sharedPref.getString("created", "").equals("OK")) {
//                            editor.putString("created", "UPDATED");
//                        }
//                        editor.apply();
//                        UpdateHargaActivity.this.finish();
//                    } catch (JSONException e) {
//                        Context context = UpdateHargaActivity.this.getApplicationContext();
//                        Toast toast = Toast.makeText(context, "INVALID JSON", 0);
//                        toast.show();
//                        System.out.println(e);
//                    }
//                }
//            }
//        });
//        jsonButtonCancel.setOnClickListener(new View.OnClickListener() { // from class: com.kupat.test.UpdateHargaActivity.2
//            @Override // android.view.View.OnClickListener
//            public void onClick(View v) {
//                UpdateHargaActivity.this.finish();
//            }
//        });
//        jsonButtonCancel.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.kupat.test.UpdateHargaActivity.3
//            @Override // android.view.View.OnLongClickListener
//            public boolean onLongClick(View v) {
//                UpdateHargaActivity.jsonInput.setText(MainActivity.defaultJson);
//                return true;
//            }
//        });
//        jsonButtonRequest.setOnClickListener(new View.OnClickListener() { // from class: com.kupat.test.UpdateHargaActivity.4
//            @Override // android.view.View.OnClickListener
//            public void onClick(View v) {
//                final TextView textView = (TextView) UpdateHargaActivity.this.findViewById(R.id.hargaBaruResponse);
//                RequestQueue queue = Volley.newRequestQueue(UpdateHargaActivity.this.getApplicationContext());
//                StringRequest stringRequest = new StringRequest(0, "https://bookshelf-170600.appspot.com/harga", new Response.Listener<String>() { // from class: com.kupat.test.UpdateHargaActivity.4.1
//                    @Override // com.android.volley.Response.Listener
//                    public void onResponse(String response) {
//                        textView.setText("Response is: " + response);
//                        UpdateHargaActivity.jsonInput.setText(response);
//                    }
//                }, new Response.ErrorListener() { // from class: com.kupat.test.UpdateHargaActivity.4.2
//                    @Override // com.android.volley.Response.ErrorListener
//                    public void onErrorResponse(VolleyError error) {
//                        textView.setText("That didn't work!");
//                    }
//                });
//                queue.add(stringRequest);
//            }
//        });
    }
}
