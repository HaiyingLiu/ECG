package wu.rang.hao.ecg_uestc_demo;
/*************************************************************/
/* Project Shmimn 
 *  Mobile Health-care Device
 *  Yuhua Chen
 *  2011-4-24 16:35:21
 */
/*************************************************************/
import java.io.IOException;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.ToggleButton;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class BTConnectActivity extends Activity {
    /** Called when the activity is first created. */
	public static final int REQUEST_ENABLE_BT = 8807;
	public BroadcastReceiver mBTReceiver;
	public static BluetoothSocket mBTSocket;
	public BluetoothAdapter mBTAdapter;
	private Button btnSearchDevices;
	private ToggleButton tBtnBTSwitch;
	private BluetoothDevice mBTDevice;
	private ArrayAdapter<String> adtDvcs;
	private List<String> lstDvcsStr = new ArrayList<String>();	
	private ListView lvDevicesList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.btconnect);
        //��һ������AndroidManifest.xml����������Ȩ��
        // �ڶ��������BluetoothAdapter����
        //Init BluetoothAdapter
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        //�ж�BluetoothAdapter�����Ƿ�Ϊ�գ���Ϊ�գ�����ʾ��û�������豸
        if (mBTAdapter == null){
        	Toast.makeText(BTConnectActivity.this, " No devices supporting Bluetooth! ", Toast.LENGTH_SHORT).show();
        	this.finish();
        }
        
        // Set up BroadCast Receiver
        mBTReceiver = new BroadcastReceiver(){
        	public void onReceive(Context context,Intent intent){
        		String act = intent.getAction();//��ȡ�㲥�е�ACTION
        		// To see whether the action is that already found devices
        		if(act.equals(BluetoothDevice.ACTION_FOUND)){
        			// If found one device, get the device object
        			//������������豸�����ȡ�����豸����
        			BluetoothDevice tmpDvc = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        			// Put the name & address into a string
        			String tmpDvcStr = tmpDvc.getName()+"|"+tmpDvc.getAddress();
        			if (lstDvcsStr.indexOf(tmpDvcStr)==-1){
        				// Avoid duplicate add devices
        				lstDvcsStr.add(tmpDvcStr);
        				adtDvcs.notifyDataSetChanged();
        				Toast.makeText(BTConnectActivity.this, "Find a new device!", Toast.LENGTH_SHORT).show();
        			}
        		}
        		if(act.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
        			Toast.makeText(BTConnectActivity.this, " Searching Complete!", Toast.LENGTH_SHORT).show();
        		}
       		
        		if (act.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)){
    				Toast.makeText(BTConnectActivity.this, "Begin Searching Devices", Toast.LENGTH_SHORT).show();
        		}
         	}
        };
        
        //Register the broadcastReceiver
        //����Ϊ�ڻ��ע��㲥��Ҳ������AndroidManifest.xml�ļ��н���ע��
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBTReceiver,filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(mBTReceiver,filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mBTReceiver,filter); 
        
        //��ȡ�����ؼ���ʵ������--- Init Buttons
        btnSearchDevices = (Button)findViewById(R.id.btnSearchDevice);
        tBtnBTSwitch = (ToggleButton)findViewById(R.id.tBtnBTSwitch);
        lvDevicesList = (ListView)findViewById(R.id.lvDevicesList);
        //�ж������������Ƿ�򿪣����Կؼ���ť���벻ͬ����ʾ
        if(mBTAdapter.getState()==BluetoothAdapter.STATE_OFF)
        	tBtnBTSwitch.setChecked(false);
        if(mBTAdapter.getState()==BluetoothAdapter.STATE_ON)
        	tBtnBTSwitch.setChecked(true);

        // ListView And Data Adapter
        adtDvcs = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lstDvcsStr);
        lvDevicesList.setAdapter(adtDvcs);
        
        // Add Click Listeners to Buttons
        //Ϊ��ť���ü����¼�
        tBtnBTSwitch.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View view){
                // ��������----Open Bluetooth
        		if(tBtnBTSwitch.isChecked()){//�жϰ�ť�Ƿ�Ϊ��״̬
        			if(!mBTAdapter.isEnabled()){//mBTAdapter.isEnabled()����true��ʾ�����Դ򿪣����ã�������������
            			//Open a new dialog to ask user whether wanna open BT
                    	Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    	startActivityForResult(enabler,REQUEST_ENABLE_BT);       				
        			}
        			tBtnBTSwitch.setChecked(true);//���ư�ť��״̬��Ϊtrue
        		}
        		
        		// Close Bluetooth
        		if(!tBtnBTSwitch.isChecked()){//�������������ƣ�ֻ��ִ���෴�Ĳ���
        			if(mBTAdapter.isEnabled()){
            			mBTAdapter.disable();
            			Toast.makeText(BTConnectActivity.this, "Bluetooth has been closed!", Toast.LENGTH_SHORT).show();      				
        			}
        			tBtnBTSwitch.setChecked(false);
        		}
        	}
        });
        
        // �����������ұ�����Χ�������豸----Search Bluetooth Devices
        btnSearchDevices.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View view){
        		if (mBTAdapter.isDiscovering()){
        			Toast.makeText(BTConnectActivity.this, "Already Searching", Toast.LENGTH_SHORT).show();
        		}
    			else{
    				lstDvcsStr.clear();
    				adtDvcs.notifyDataSetChanged();
    				mBTDevice = null;
    				mBTAdapter.startDiscovery();
    			}
        	}
        });
        
        // Create Click listener for the items on devices list
        //ΪListView�е����������¼�
        lvDevicesList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
    				long arg3){
        		if (mBTAdapter == null)
        			Toast.makeText(BTConnectActivity.this, "No devices support BlueTooth", Toast.LENGTH_SHORT).show();
        		else{
        			// stop searching
        			mBTAdapter.cancelDiscovery();
        			// Get address of remote device
        			String str = lstDvcsStr.get(arg2);//����arg2ΪListView�е�������lstDvcsStr�е�λ��
        			String[] dvcValues = str.split("\\|");//[0]��ʾ���������豸�����֣�[1]Ϊ�����豸�ĵ�ַ
        			String dvcAddr = dvcValues[1];
        			//UUID dvcUUID = UUID.randomUUID();
        			//00001101-0000-1000-8000-00805F9B34FB SPP protocal
        			UUID dvcUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        			// Set BT device
        			mBTDevice = mBTAdapter.getRemoteDevice(dvcAddr);//���������ĵ�ַ��ȡ�������豸�Ķ���
        			// Connect Device
        			try{
        				mBTSocket = mBTDevice.createRfcommSocketToServiceRecord(dvcUUID);
        				mBTSocket.connect();
        				Intent mInt = new Intent(BTConnectActivity.this,RTMonitorActivity.class);
        				startActivity(mInt);
        			}
        			catch(IOException e){
        				e.printStackTrace();
        			}
        		}        		
        	}
        });        
    }
    
    public void onActivityResult(int RequestCode, int ResultCode,Intent data){
    	switch(RequestCode){
    	case REQUEST_ENABLE_BT:
    		if(ResultCode == RESULT_OK){
    			Toast.makeText(this.getApplicationContext(), "Bluetooth Launched!", Toast.LENGTH_SHORT).show();
    		}
    		else
    			if(ResultCode == RESULT_CANCELED){
        			Toast.makeText(this.getApplicationContext(), "Launched Bluetoth cancled!", Toast.LENGTH_SHORT).show();   				
    			}
    		break;
    	}
    }    
    @Override
	protected void onDestroy() {
	    this.unregisterReceiver(mBTReceiver);
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}