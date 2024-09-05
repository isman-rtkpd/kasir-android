package com.monopecez.kaskup2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    static int[] qtyList, hargaList;
    static String[] printNameList, nameList;
    static int menuSize;

    private EscPos escpos;
    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private List<TextView> harTot;

    protected static final String TAG = "TAG";
    private ProgressDialog mBluetoothConnectProgressDialog;
    static AlertDialog.Builder builder;
    TextView totalText;
    private BluetoothSocket mBluetoothSocket = null;
    private Handler mHandler = new Handler() { // from class: com.kupat.test.MainActivity.74
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            MainActivity.this.mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(MainActivity.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
        }
    };
    private BluetoothDevice mBluetoothDevice;
    private BluetoothAdapter mBluetoothAdapter;

    private Button settingButton;

    private String defaultJson = "{\"nameList\": [\"kupat\", \"kari\", \"lontong\"], \"printNameList\": [\"kupat\", \"kari\", \"lontong\"], \"hargaList\": [100,200,300]}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parseJson();

        setContentView(R.layout.activity_main);

        totalText = findViewById(R.id.totalharga);

        ConstraintLayout mainConstraintLayout = findViewById(R.id.mainConstraintLayout);

        ConstraintLayout temp = new ConstraintLayout(this);

        harTot = new ArrayList<>();

        qtyList = new int[menuSize];
        for (int i = 0; i < menuSize; i++) {
            if (i == 0) {
                temp = buildBlock(R.id.header2, nameList[i], hargaList[i], true, i);
            } else {
                temp = buildBlock(temp.getId(), nameList[i], hargaList[i], false, i);
            }
            mainConstraintLayout.addView(temp);
            qtyList[i] = 0;

        }

        Button printButton = findViewById(R.id.print);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    printReceipt();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        settingButton = findViewById(R.id.menu_option);
        settingButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.setOnMenuItemClickListener(this::onMenuItemClick);
            popup.getMenuInflater().inflate(R.menu.main, popup.getMenu());
            popup.show();
        });
        //registerForContextMenu(settingButton);

    }


    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_connect_printer) {
            attempDisconnect();
            scanBluetooth();
            return true;
        } else if (itemId == R.id.menu_update_price) {
            updateHarga();
            return true;
        } else if (itemId == R.id.menu_clear_setting) {
            clearContent(1);
            return true;
        } else {
            return false;
        }
    }


    private void calculateTotal() {
        int total = 0;
        for (int i = 0; i < menuSize; i++) {
            total = total + (qtyList[i] * hargaList[i]);
        }
        Log.d("calculate total", "MENU SIZE: " + menuSize);
        Log.d("calculate total", "QTY LIST: " + Arrays.toString(qtyList));
        Log.d("calculate total", "HARGA LIST: " + Arrays.toString(hargaList));
        Log.d("calculate total", "TOTAL: " + total);
        totalText.setText(String.valueOf(total));
    }

    private ConstraintLayout buildBlock(int beforeComponent, String menuNameInput, int harga, boolean isEven, int idx) {
        float dpCoeficient = this.getResources().getDisplayMetrics().density;

        int background = 0;
        if (isEven) {
            background = 15263976; // "#e8e8e8";
        } else {
            background = 0; // "#ffffff";
        }
        ConstraintLayout outLayout = new ConstraintLayout(this);
        int layoutId = View.generateViewId();

        ConstraintLayout.LayoutParams outLayoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        outLayoutParams.topToBottom = beforeComponent;
        outLayout.setLayoutParams(outLayoutParams);
        outLayout.setBackgroundColor(background);
        outLayout.setId(layoutId);


        // TEXT VIEW
        TextView menuName = new TextView(this);
        menuName.setText(menuNameInput);
        menuName.setTypeface(null, Typeface.BOLD);
        menuName.setId(View.generateViewId());
        menuName.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        menuName.setGravity(Gravity.CENTER);

        ConstraintLayout.LayoutParams menuNameParams = new ConstraintLayout.LayoutParams((int) (dpCoeficient * 128 * 1.15), 0);
        menuNameParams.verticalBias = .857f;
        menuNameParams.topToTop = outLayout.getId();
        menuNameParams.bottomToBottom = outLayout.getId();
        menuNameParams.setMarginStart(16);

        // BUTTON KURANG
        Button menuKurang = new Button(this);
        menuKurang.setText("-");
        menuKurang.setId(View.generateViewId());

        ConstraintLayout.LayoutParams menuKurangParams = new ConstraintLayout.LayoutParams((int) (dpCoeficient * 46), (int) (dpCoeficient * 46));
        menuKurangParams.setMarginStart(8);

        // BUTTON TAMBAH
        Button menuTambah = new Button(this);
        menuTambah.setText("+");
        menuTambah.setId(View.generateViewId());

        ConstraintLayout.LayoutParams menuTambahParams = new ConstraintLayout.LayoutParams((int) (dpCoeficient * 46), (int) (dpCoeficient * 46));
        menuTambahParams.setMarginStart(8);


        // TEXT VIEW TOTAL
        TextView menuTotal = new TextView(this);
        menuTotal.setText("0 | 0");
        menuTotal.setTypeface(null, Typeface.BOLD);
        menuTotal.setGravity(Gravity.END);
        menuTotal.setId(View.generateViewId());
        harTot.add(menuTotal);

        ConstraintLayout.LayoutParams menuTotalParams = new ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        menuTotalParams.topToTop = outLayout.getId();
        menuTotalParams.bottomToBottom = outLayout.getId();

        menuNameParams.startToStart = outLayout.getId();
        menuKurangParams.startToEnd = menuName.getId();
        menuTambahParams.startToEnd = menuKurang.getId();
        menuTotalParams.endToEnd = outLayout.getId();
        menuTotalParams.setMarginEnd((int) (8 * dpCoeficient));

        menuName.setLayoutParams(menuNameParams);
        menuKurang.setLayoutParams(menuKurangParams);
        menuTambah.setLayoutParams(menuTambahParams);
        menuTotal.setLayoutParams(menuTotalParams);

        outLayout.addView(menuName);
        outLayout.addView(menuKurang);
        outLayout.addView(menuTambah);
        outLayout.addView(menuTotal);

        ConstraintSet cs = new ConstraintSet();
        cs.clone(outLayout);
        cs.applyTo(outLayout);

        menuTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = qtyList[idx] + 1;
                qtyList[idx] = qty;
                menuTotal.setText(qty + " | " + (qty * harga));
                calculateTotal();
            }
        });

        menuKurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = qtyList[idx];
                if (qty >= 1) {
                    qty--;
                }
                qtyList[idx] = qty;
                menuTotal.setText(qty + " | " + (qty * harga));
                calculateTotal();
            }
        });

        return outLayout;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        try {
            if (this.mBluetoothSocket != null) {
                this.mBluetoothSocket.close();
            }
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int mRequestCode, int mResultCode, Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);
        if (mRequestCode != 1) {
            if (mRequestCode == 2) {
                if (mResultCode != -1) {
                    Toast.makeText(this, "Message", Toast.LENGTH_SHORT).show();
                    return;
                }
                listPairedDevices();
                Intent connectIntent = new Intent(this, (Class<?>) DeviceListActivity.class);
                startActivityForResult(connectIntent, 1);
                return;
            }
            return;
        }
        if (mResultCode == -1) {
            Bundle mExtra = mDataIntent.getExtras();
            String mDeviceAddress = mExtra.getString("DeviceAddress");
            Log.v(TAG, "Coming incoming address " + mDeviceAddress);
            this.mBluetoothDevice = this.mBluetoothAdapter.getRemoteDevice(mDeviceAddress);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                Log.v(TAG, "MASUK KE IF BLOCK!!!!");
            }
            Log.v(TAG, "SIAP SIAP CONNECT!!!!");

            this.mBluetoothConnectProgressDialog = ProgressDialog.show(this, "Connecting...", this.mBluetoothDevice.getName() + " : " + this.mBluetoothDevice.getAddress(), true, true);
            Thread mBlutoothConnectThread = new Thread();
            Log.v(TAG, "Starting thread!!!!");
            mBlutoothConnectThread.start();
            Log.v(TAG, "Thread running!!!!");

            run();

        }
    }

    private void listPairedDevices() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, 2);
        }
        if (this.mBluetoothAdapter != null){
            Set<BluetoothDevice> mPairedDevices = this.mBluetoothAdapter.getBondedDevices();
            if (mPairedDevices.size() > 0) {
                for (BluetoothDevice mDevice : mPairedDevices) {
                    Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  " + mDevice.getAddress());
                }
            }
        }
    }

    public void scanBluetooth() {
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
                startActivityForResult(enableBtIntent, 2);
            } else {
                Intent connectIntent = new Intent(this, (Class<?>) DeviceListActivity.class);
                startActivityForResult(connectIntent, 1);
            }
        }
    }

    public void updateHarga() {
        Intent updateHargaIntent = new Intent(this, (Class<?>) UpdateHargaActivity.class);
        startActivity(updateHargaIntent);
        finish();
    }

    public void attempDisconnect() {
        try {
            if (this.mBluetoothSocket != null) {
                this.mBluetoothSocket.close();
            }
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    //@Override // java.lang.Runnable
    public void run() {
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, 2);// TODO: Consider calling
                return;
            }
            this.mBluetoothSocket = this.mBluetoothDevice.createRfcommSocketToServiceRecord(this.applicationUUID);
            this.mBluetoothAdapter.cancelDiscovery();
            this.mBluetoothSocket.connect();
            this.mHandler.sendEmptyMessage(0);
            Log.d("Tag","BERHASIL KONEK GESS");
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(this.mBluetoothSocket);
        } catch (Exception e){
            Log.d(TAG, "ERROR: " + e);
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException e) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    public void clearContent(int num) {
        for (int i = 0; i < menuSize ; i++){
            qtyList[i] = 0;
            harTot.get(i).setText("0 | 0");
        }
        calculateTotal();
    }

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();
        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = 0x" + UnicodeFormatter.byteToHex(b[k]));
        }
        return b[3];
    }

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }

    public boolean printReceipt() throws InterruptedException {
        Log.d("TAG", "PRINTINGGGGGGG");
        if (this.mBluetoothSocket == null) {
            Log.d("TAG", "PRINTINGGGGGGG NULLLLL");

            Toast.makeText(this, "Silakan hubungkan printer terlebih dahulu", Toast.LENGTH_SHORT).show();
            return false;
        }
        Log.d("TAG", "blutoothsocket: " + this.mBluetoothSocket);

        Thread t = new Thread() { // from class: com.kupat.test.MainActivity.76
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {

                    Log.d("TAG", "START PRINT");
                    OutputStream os = MainActivity.this.mBluetoothSocket.getOutputStream();
                    MainActivity.this.escpos = new EscPos(os);
                    Style title = new Style().setFontSize(Style.FontSize._3, Style.FontSize._3).setJustification(EscPosConst.Justification.Center);
                    MainActivity.this.escpos.feed(1);
                    MainActivity.this.escpos.writeLF(title, "KUPAT TAHU");
                    MainActivity.this.escpos.writeLF(title, "LONTONG KARI");
                    Style title2 = new Style().setJustification(EscPosConst.Justification.Center);
                    MainActivity.this.escpos.writeLF(title2, "CICENDO - 1967 | 085108253545");
                    MainActivity.this.escpos.writeLF(title2, "--------------------");
                    MainActivity.this.escpos.writeLF("A/N: -");
                    MainActivity.this.escpos.writeLF("No : -");

                    MainActivity.this.escpos.feed(1);
                    MainActivity.this.ReceiptBuilder(MainActivity.this.escpos, true);
                    MainActivity.this.escpos.writeLF(title2, "--------------------");
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());
                    MainActivity.this.escpos.writeLF(title2, "TERIMA KASIH :)");
                    MainActivity.this.escpos.writeLF(title2, formattedDate);
                    MainActivity.this.escpos.writeLF(title2, "--------------------");
                    MainActivity.this.escpos.writeLF(title2, "Semoga sehat selalu");
                    MainActivity.this.escpos.feed(2);

                    Log.d("TAG", "END PRINT");
                } catch (Exception e) {
                    Log.e("MainActivity", "Exe ", e);
                }
            }
        };
        Log.d("TAG", "STARTING THREAD");

        t.start();
        t.join();
        Log.d("TAG", "Thread END");

        return true;
    }

    public void ReceiptBuilder(EscPos escpos, boolean first) throws IOException {
        String hargaTotal = totalText.getText().toString();
        Style rightJust = new Style().setJustification(EscPosConst.Justification.Right);
        if (!hargaTotal.equals("0")) {
            for (int i = 0; i < menuSize; i++) {
                if (qtyList[i] > 0) {
                    escpos.writeLF(qtyList[i] + " x " + nameList[i] +" @" + hargaList[i]);
                    if (first) {
                        escpos.writeLF(rightJust, "" + (hargaList[i] * qtyList[i]));
                    }
                }
            }
        }
        escpos.writeLF(rightJust, "Total: " + "123000");
    }

    public void dialogClearReceipt() {
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Hapus data sebelumnya?").setPositiveButton("Ya, hapus", new DialogInterface.OnClickListener() { // from class: com.kupat.test.MainActivity.78
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int id) {
                MainActivity.this.clearContent(0);
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() { // from class: com.kupat.test.MainActivity.77
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.show();
    }

    public void parseJson() {
        defaultJson = "{\"nameList\": [\"kupatxxx\", \"kari\", \"lontong\"], \"printNameList\": [\"kupat\", \"kari\", \"lontong\"], \"hargaList\": [100,200,30000]}";
        String text = defaultJson;
        SharedPreferences sharedPref = getSharedPreferences("pricesPreferences", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        try {
            Log.d("TAG", "PARSING HARGAAAA");
            JSONObject JSo = new JSONObject(text);
            JSONArray tempName = JSo.getJSONArray("nameList");
            JSONArray tempPrintName = JSo.getJSONArray("printNameList");
            JSONArray tempHarga = JSo.getJSONArray("hargaList");
            nameList = new String[tempName.length()];
            printNameList = new String[tempName.length()];
            hargaList = new int[tempName.length()];

            for (int i = 0; i < tempName.length(); ++i) {
                nameList[i] = tempName.getString(i);
                printNameList[i] = tempPrintName.getString(i);
                hargaList[i] = tempHarga.getInt(i);
            }

            System.out.println(Arrays.toString(nameList));
            System.out.println(Arrays.toString(printNameList));
            System.out.println(Arrays.toString(hargaList));
            menuSize = hargaList.length;


        } catch (JSONException e) {
            System.out.println("PARSING ERRRPORRR");
            Toast.makeText(getApplicationContext(), "WRONG JSON!! REVERT", Toast.LENGTH_SHORT);
            editor.putString("created", "NOTOK");
            //parseJson(sharedPref.getString("jsonSebelum", defaultJson));
            System.out.println(e);
            editor.apply();
        }
    }
}