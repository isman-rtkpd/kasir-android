package com.monopecez.kaskup2;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import java.util.Set;

/* loaded from: classes2.dex */
public class DeviceListActivity extends Activity {
    protected static final String TAG = "TAG";
    private BluetoothAdapter mBluetoothAdapter;
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() { // from class: com.kupat.test.DeviceListActivity.1
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> mAdapterView, View mView, int mPosition, long mLong) {
            try {
                if (ActivityCompat.checkSelfPermission(DeviceListActivity.this.getApplicationContext(), android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                DeviceListActivity.this.mBluetoothAdapter.cancelDiscovery();
                String mDeviceInfo = ((TextView) mView).getText().toString();
                String mDeviceAddress = mDeviceInfo.substring(mDeviceInfo.length() - 17);
                Log.v(DeviceListActivity.TAG, "Device_Address " + mDeviceAddress);
                Bundle mBundle = new Bundle();
                mBundle.putString("DeviceAddress", mDeviceAddress);
                Intent mBackIntent = new Intent();
                mBackIntent.putExtras(mBundle);
                DeviceListActivity.this.setResult(-1, mBackIntent);
                Log.v(DeviceListActivity.TAG, "FINISHHHHHHHHHHHHHHHHHHHHHHHH");
                DeviceListActivity.this.finish();
            } catch (Exception e) {
                Log.v(DeviceListActivity.TAG, "ERRORRRR");
            }
        }
    };
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;

    @Override // android.app.Activity
    protected void onCreate(Bundle mSavedInstanceState) {
        super.onCreate(mSavedInstanceState);
        requestWindowFeature(5);
        setContentView(R.layout.device_list);
        setResult(0);
        this.mPairedDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.device_name);
        ListView mPairedListView = (ListView) findViewById(R.id.paired_devices);
        mPairedListView.setAdapter((ListAdapter) this.mPairedDevicesArrayAdapter);
        mPairedListView.setOnItemClickListener(this.mDeviceClickListener);
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Set<BluetoothDevice> mPairedDevices = this.mBluetoothAdapter.getBondedDevices();
        if (mPairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice mDevice : mPairedDevices) {
                this.mPairedDevicesArrayAdapter.add(mDevice.getName() + "\n" + mDevice.getAddress());
            }
            return;
        }
        this.mPairedDevicesArrayAdapter.add("None Paired");
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            bluetoothAdapter.cancelDiscovery();
        }
    }
}
