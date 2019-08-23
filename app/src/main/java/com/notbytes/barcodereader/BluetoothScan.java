package com.notbytes.barcodereader;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.notbytes.constants.constant;
import com.notbytes.model.ProductAddResponse;
import com.notbytes.rest.ApiClient;
import com.notbytes.rest.ApiInterface;
import com.notbytes.utils.AppUtil;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BluetoothScan extends AppCompatActivity implements View.OnClickListener
{
    String TAG = "BLuetoothscan";
    TextView tv_product_id;

    Button btn_add_product,btn_assign_lefthand,btn_assign_righhand;

    private boolean mScanning;
    private Handler mHandler;

    private BluetoothAdapter mBluetoothAdapter;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    //list bluetooth device, bounded devices
    private Spinner device_select_spinner;
    private TextView tv_left_hand_device_selected,tv_right_hand_device_selected;


    LinearLayout ll_lefthand_id,ll_righthand_id;


    /*left and right hand device and product id*/
    public String leftHandDeviceAddress ="";
    public String rightHandDeviceAddress = "";


    public String leftHandDeviceName= "";
    public String rightHandDeviceName = "";

    String get_product_Id = "";

    /*consta*/

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_scan);
        getSupportActionBar().setTitle("        SELECT BLUETOOTH DEVICES        ");

        Initilization();


    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void Initilization()
    {
        tv_product_id = findViewById(R.id.tv_product_id);
        btn_add_product = findViewById(R.id.btn_add_product);
        device_select_spinner = findViewById(R.id.device_select_spinner);           //select device spinner



        mHandler = new Handler();


        btn_assign_lefthand = findViewById(R.id.btn_assign_lefthand);
        btn_assign_righhand = findViewById(R.id.btn_assign_righhand);

        tv_left_hand_device_selected = findViewById(R.id.tv_left_hand_device_selected);
        tv_right_hand_device_selected = findViewById(R.id.tv_right_hand_device_selected);


        /*linear layout left and righ hand*/
        ll_lefthand_id = this.findViewById(R.id.ll_lefthand_id);
        ll_righthand_id = this.findViewById(R.id.ll_righthand_id);

        ll_lefthand_id.setVisibility(LinearLayout.GONE);
        ll_righthand_id.setVisibility(LinearLayout.GONE);

        get_product_Id = getIntent().getStringExtra("product_id");
        tv_product_id.setText(get_product_Id);                                           //getting product id


        // selectively disable BLE-related features.
        // Use this check to determine whether BLE is supported on the device.  Then you can
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
        {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter =  bluetoothManager.getAdapter();
        // Checks if Bluetooth is supported on the device.
        if(mBluetoothAdapter == null)
        {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }



        btn_add_product.setOnClickListener(this);
        btn_assign_lefthand.setOnClickListener(this);
        btn_assign_righhand.setOnClickListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onResume()
    {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, constant.REQUEST_ENABLE_BT);
        }

        // Make sure we have access coarse location enabled, if not, prompt the user to enable it
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect peripherals.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener()
                {
                    @Override
                    public void onDismiss(DialogInterface dialog)
                    {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, constant.PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }

        // Initializes list view adapter
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        device_select_spinner.setAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);                 //scan device




    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onPause()
    {
        super.onPause();
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)

    private void scanLeDevice(final boolean enable)
    {
        if(enable)
        {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable()
            {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public void run()
                {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            },constant.SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);

        }
        else
        {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main,menu);
        if(!mScanning)
        {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        }
        else
        {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(R.layout.actionbarprogress);
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_scan:
                mLeDeviceListAdapter.clear();
                scanLeDevice(true);
                break;
            case R.id.menu_stop:
                scanLeDevice(false);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_add_product:

                /*show dialog to confirm the device*/

                //building retrofit object
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<ProductAddResponse> responseCall =apiInterface.getproductresponse(get_product_Id,leftHandDeviceAddress,rightHandDeviceAddress);

                responseCall.enqueue(new Callback<ProductAddResponse>()
                {
                    @Override
                    public void onResponse(Call<ProductAddResponse> call, Response<ProductAddResponse> response)
                    {
                        Log.d("RESPONSE", response.body().toString());
                        Toast.makeText(getApplicationContext(),"Added successfully"+response.message(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ProductAddResponse> call, Throwable t)
                    {
                        Log.e("ERROROROR", t.getMessage());
                        Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });



                break;

            case R.id.btn_assign_lefthand:

                leftHandDeviceAddress = ((BluetoothDevice) device_select_spinner.getSelectedItem()).getAddress();
                leftHandDeviceName = ((BluetoothDevice) device_select_spinner.getSelectedItem()).getName();
                tv_left_hand_device_selected.setText(leftHandDeviceAddress);


                btn_assign_lefthand.setVisibility(View.GONE);
                ll_lefthand_id.setVisibility(LinearLayout.VISIBLE);




                break;

            case R.id.btn_assign_righhand:

                rightHandDeviceAddress = ((BluetoothDevice) device_select_spinner.getSelectedItem()).getAddress();
                rightHandDeviceName = ((BluetoothDevice) device_select_spinner.getSelectedItem()).getName();


                tv_right_hand_device_selected.setText(rightHandDeviceAddress);


                btn_assign_righhand.setVisibility(View.GONE);
                ll_righthand_id.setVisibility(LinearLayout.VISIBLE);


                break;


            case R.id.btn_reset:
                tv_left_hand_device_selected.setText(" ");
                tv_right_hand_device_selected.setText(" ");
        }

    }



    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter
    {
        public ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        //constructor for le device scan intilization
        public LeDeviceListAdapter()
        {
            super();
            this.mLeDevices = new ArrayList<BluetoothDevice>();;
            this.mInflator = BluetoothScan.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device)
        {
            if(!mLeDevices.contains(device))
            {
                mLeDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position)
        {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }


        @Override
        public int getCount()
        {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i)
        {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            ViewHolder viewHolder;          //instead of findview by id used for fast rendering

            // General ListView optimization code.
            if(view == null)
            {
                view = mInflator.inflate(R.layout.listitem_device,null);
                viewHolder = new ViewHolder();          //object created for view holder
                viewHolder.deviceAddress = view.findViewById(R.id.device_address);
                viewHolder.deviceName = view.findViewById(R.id.device_name);

                view.setTag(viewHolder);            //Tags can also be used to store data within a view without resorting to another data structure.
            }
            else
            {
                viewHolder = (ViewHolder) view.getTag();
            }
            BluetoothDevice device = mLeDevices.get(i);

            final String deviceName = device.getName();
            final String deviceAddress = device.getAddress();

            /*check if device name is not null*/
            if(deviceName !=null && deviceName.length() >0)
            {
                viewHolder.deviceName.setText(deviceName);
                viewHolder.deviceAddress.setText(deviceAddress);
            }
            else
            {
                viewHolder.deviceName.setText("Unknown device");
                viewHolder.deviceAddress.setText("unknown device address");
            }

            return view;
        }
    }
    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback()
    {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {


                    mLeDeviceListAdapter.addDevice(device);
                    mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });


        }
    };


    /*static View Holder*/
    static class ViewHolder
    {
        TextView deviceName;
        TextView deviceAddress;
    }
}
