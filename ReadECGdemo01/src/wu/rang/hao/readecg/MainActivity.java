package wu.rang.hao.readecg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private Button buttonRead;
	
	private SurfaceView sfvECG;
	
	private BTReadThread mReadThread = new BTReadThread(50);//�߳�ʵ����
	private Handler msgHandler;
	private DrawECGWaveForm mECGWF;
	BufferedReader bufferReader;
	StringBuffer strBuffer;
	File file;
	String str=new String();
	Boolean enRead=false;
	
	private String revTmpStr = new String();
	
	public List<Float> ECGDataList = new ArrayList<Float>();
	public boolean ECGDLIsAvailable = true;
	private float ECGData = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		buttonRead=(Button)findViewById(R.id.read);
		sfvECG=(SurfaceView)findViewById(R.id.sfvECG);
		mECGWF = new DrawECGWaveForm(sfvECG);//DrawECGWaveForm�����ʵ����������Ĳ���ΪSrufaceView��ʵ��
	
		Looper lp = Looper.myLooper();
		msgHandler = new MsgHandler(lp);//ʵ����һ��Handler����
		
		// Setting Timer to Draw and Save data
		Timer mDSTimer = new Timer();
		TimerTask mDSTask = new TimerTask(){
			public void run(){
				Message msg = Message.obtain();
				msg.what = 1;
				msgHandler.sendMessage(msg);
			}
		};
		
		// Set Timer
		mDSTimer.schedule(mDSTask,1000,1000);
		buttonRead.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				enRead=true;
				Log.d("MainActivity.this","���Ͽ�ʼ-----------------��д������");
				writeDataToSD();
				Log.d("MainActivity.this","���Ͽ�ʼ--------------����������");
				mReadThread.start();
				
				
			}
		});
	}

	
	
	// MsgHandler class to Update UI
		class MsgHandler extends Handler{
			public MsgHandler(Looper lp){
				super(lp);
			}
			
			@Override
			public void handleMessage(Message msg){
				switch(msg.what){
				case 1:
					if (ECGDataList.size() > 1){
						List<Float> ECGCacheData = new ArrayList<Float>();
						ECGCacheData.addAll(ECGDataList);
						ECGDLIsAvailable = false;
						ECGDataList.clear();
						ECGDLIsAvailable = true;
						// Draw To View
						mECGWF.DrawtoView(ECGCacheData);	//����DrawECGWaveForm���е�DrawtoView�����Խ��ܵ������ݻ�ͼ��ʾ				
					}
					break;
				}

			}
		}
		
		public void writeDataToSD(){  
			try{  
			    /* ��ȡFile����ȷ�������ļ�����Ϣ */  
			    //File file  = new File(Environment.getExternalStorageDirectory()+"/f.txt");  
			  File file1  = new File(Environment.getExternalStorageDirectory(),"100.txt");
			  File file2=new File(Environment.getExternalStorageDirectory(),"wu.txt");
			  Log.d("MainActivity.this","�����ļ��Ѿ�------------------��������");
			   /* �ж�sd���ⲿ����״̬�Ƿ���Զ�д */  
			    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){ 
			    	//���ļ������ݸ�Ϊ������Ҫ�ĸ�ʽ����
			    	BufferedReader bufferReader1=new BufferedReader(new InputStreamReader(new FileInputStream(file1)));
			    	StringBuffer strBuffer1=new StringBuffer();
			    	String str1[];
			    	Log.d("MainActivity.this","���Ͽ�ʼ��ȡ-----------------------������-----------");
					while((str=bufferReader1.readLine())!=null){
							Log.d("MainActivity", "bufferReader��ȡ�������-----------֮ǰ");
							str1=str.split("");
							strBuffer1.append(str1[2]).append(";");
							
					}
			          
			        /* ���Ķ��� *//*  */  
					Log.d("MainActivity.this","���Ͽ�ʼд������--------------------------��");
			     FileOutputStream fos = new FileOutputStream(file2);  
			      byte[] buffer = strBuffer1.toString().getBytes();  
				  
			        /* ��ʼд������ */  
			        fos.write(buffer);  

			       /* �ر�����ʹ�� */  
			        fos.close();  
			        bufferReader1.close();
			        

			        /* ��Ҫд������� */  
			       /* String message = "0.160;0.180;0.170;0.180;0.180;0.170;0.170"
			        		+ ";0.130;0.130;0.100;0.060;0.040;0.010;-0.010;-0.020;"
			        		+ "-0.020;-0.020;-0.030;0.000;0.000;0.000;0.020;0.030;0.040;"
			        		+ "0.030;0.070;0.050;0.060;0.050;0.050;0.050;0.050;0.060;0.050;"
			        		+ "0.050;0.030;0.030;0.030;0.020;0.020;0.020;0.010;0.000;0.000;0.000"
			        		+ ";-0.010;0.000;-0.010;-0.010;-0.010;-0.010;-0.010;-0.020;-0.020;"
			        		+ "-0.010;-0.010;0.000;0.060;0.050;0.090;0.110;0.160;0.130;0.050;0.030;"
			        		+ "-0.090;-0.120;-0.090;-0.080;-0.090;-0.080;-0.080;-0.070;-0.080;0.090;"
			        		+ "0.450;0.790;-1.170;-1.910;-1.640;-1.280;-0.520;-0.150;-0.030;-0.030;0."
			        		+ "010;0.030;0.050;0.060;0.080;0.090;0.100;0.130;0.140;0.160;0.150;0.180;"
			        		+ "0.200;0.220;0.220;0.260;0.280;0.280;0.300;0.290;0.290;0.270;0.240;0.210;"
			        		+ "0.170;0.130;0.100;0.050;0.030;0.040;0.050;0.040;0.050;0.060;0.060;0."
			        		+ "060;0.060;0.050;0.040;0.070;0.060;0.070;0.060;0.080;0.080;"
			        		+ "0.080;0.090;0.100;0.100;0.090;0.090;0.090;0.080;0.070;0.070;0.060;"
			        		+ "0.050;0.050;0.050;0.020;0.040;0.030;0.010;0.010;0.000;0.010;0.000;"
			        		+ "0.000;0.000;0.000;-0.020;-0.010;-0.010;-0.020;-0.020;-0.020;-0.020;"
			        		+ "0.020;0.020;0.060;0.060;0.140;0.100;0.020;-0.020;-0.130;-0.130;-0.130;"
			        		+ "-0.100;-0.100;-0.100;-0.110;-0.110;-0.090;0.160;0.430;0.750;-1.520;-1.930"
			        		+ ";-1.360;-1.110;-0.440;-0.080;-0.060;-0.040;0.000;0.020;0.050;0.060;0.060;"
			        		+ "0.090;0.090;0.100;0.120;0.140;0.140;0.160;0.190;0.210;0.210;0.240;0.250;"
			        		+ "0.250;0.250;0.260;0.240;0.210;0.190;0.170;0.110;0.070;0.050;0.010;"
			        		+ "0.010;-0.010;0.010;0.000;0.010;0.020;0.010;0.010;0.020;0.020;"
			        		+ "0.010;0.010;0.020;0.020;0.020;0.020;0.020;0.020;0.020;0.030;"
			        		+ "0.030;0.020;0.020;0.020;0.030;0.010;0.010;0.000;-0.020;0.000;"
			        		+ "-0.010;-0.020;-0.020;-0.020;-0.020;-0.030;-0.040;-0.050;-0.050;"
			        		+ "-0.060;-0.060;-0.060;-0.060;-0.060;-0.060;-0.060;-0.060;-0.020;"
			        		+ "-0.020;-0.010;0.030;0.080;0.090;0.000;-0.050;-0.130;-0.180;-0.160;"
			        		+ "-0.150;-0.130;-0.130;-0.140;-0.130;-0.140;-0.040;0.340;0.710;-1.050;"
			        		+ "-1.870;-1.580;-1.250;-0.540;-0.170;-0.070;-0.070;-0.030;-0.010;0.010;"
			        		+ "0.020;0.030;0.040;0.040;0.040;0.060;0.080;0.080;0.090;0.100;0.110;0.120;"
			        		+ "0.140;0.150;0.170;0.150;0.160;0.140;0.120;0.120;0.080;0.040;0.030;0.010;"
			        		+ "-0.020;-0.030;-0.010;-0.030;-0.010;0.000;0.010;0.010;0.020;0.020;0.010;"
			        		+ "0.000;0.000;0.010;0.010;0.020;0.010;0.010;0.030;0.030;0.010;0.020;0.010;"
			        		+ "0.030;0.010;-0.010;0.010;0.010;0.000;-0.010;-0.010;-0.010;-0.020;-0.010;"
			        		+ "-0.010;-0.020;-0.050;-0.020;-0.030;-0.020;-0.050;-0.030;-0.040;-0.040;"
			        		+ "-0.010;0.030;-0.020;0.070;0.060;0.130;0.100;0.030;-0.020;-0.120;-0.140;"
			        		+ "-0.150;-0.110;-0.130;-0.130;-0.140;-0.120;-0.140;0.080;0.340;0.670;-0.810;"
			        		+ "-1.640;-1.350;-1.110;-0.610;-0.210;-0.110;-0.090;-0.060;-0.030;-0.030;"
			        		+ "0.000;0.000;0.010;0.020;0.040;0.050;0.040;0.060;0.080;0.080;0.100;0.120;"
			        		+ "0.140;0.150;0.170;0.170;0.170;0.160;0.150;0.140;0.100;0.100;0.060;0.020;"
			        		+ "0.000;0.010;-0.020;-0.020;-0.010;-0.020;0.010;0.000;0.000;0.010;0.010;"
			        		+ "0.010;0.020;0.020;0.030;0.020;0.030;0.030;0.020;0.030;0.020;0.010;"
			        		+ "0.010;0.010;-0.010;0.000;0.000;0.010;-0.020;0.000;-0.010;-0.020;"
			        		+ "-0.010;-0.010;-0.020;-0.020;-0.020;-0.020;-0.020;-0.030;-0.020;"
			        		+ "-0.020;-0.040;-0.020;0.020;0.040;0.040;0.080;0.120;0.150;0.060;0.010;"
			        		+ "-0.050;-0.140;-0.110;-0.120;-0.110;-0.100;-0.100;-0.080;-0.100;-0.030;"
			        		+ "0.350;0.610;-0.460;-1.880;-1.900;-1.250;-0.630;-0.260;-0.030;-0.040;0.000;";*/
			        		

			       /* ���ַ���ת�����ֽ����� */  
			       // byte[] buffer = message.getBytes();  
			  
			        /* ��ʼд������ */  
			        //fos.write(buffer);  

			       /* �ر�����ʹ�� */  
			      //  fos.close();  
			      Toast.makeText(MainActivity.this, "�ļ�д��ɹ�", 1000).show();  
			  }  
			}catch(Exception ex){  
			    Toast.makeText(MainActivity.this, "�ļ�д��ʧ��", 1000).show();  
			}  

		}
	
	
	class BTReadThread extends Thread{//��ȡ���ݵ��߳�
		private int wait = 50;// Time to wait
		public BTReadThread(int wait){
			this.wait = wait;
		}
		/** 
		 * �ж�SDCard�Ƿ���� [��û�����SD��ʱ������ROMҲ��ʶ��Ϊ����sd��] 
		 *  
		 * @return 
		 */  
		public  boolean isSdCardExist() {  
		    return Environment.getExternalStorageState().equals(  
		            Environment.MEDIA_MOUNTED);  
		}
		
		/** 
		 * ��ȡSD����Ŀ¼·�� 
		 *  
		 * @return 
		 */  
		public  String getSdCardPath() {  
		    boolean exist = isSdCardExist();  
		    String sdpath = "";  
		    if (exist) {  
		        sdpath = Environment.getExternalStorageDirectory()  
		                .getAbsolutePath();  
		    } else {  
		        sdpath = "������";  
		    }  
		    return sdpath;  
		  
		} 
		/** 
		 * ��ȡĬ�ϵ��ļ�·�� 
		 *  
		 * @return 
		 */  
		public  String getDefaultFilePath() {  
		    String filepath = "";  
		    File file1 = new File(Environment.getExternalStorageDirectory(),  
		            "ECG1.txt");  
		    if (file1.exists()) {  
		        filepath = file1.getAbsolutePath();  
		    } else {  
		        filepath = "������";  
		    }  
		    Log.d("MainActivity", "�ļ�·����-------"+filepath);
		    return filepath;  
		} 
		
		
		public void run(){
			while(enRead){
				
				//Log.d("MainActivity", "�ļ�·����-------"+Environment.getExternalStorageDirectory());
				//Log.d("MainActivity", "�ļ�·����-------"+Environment.getExternalStorageDirectory().getAbsolutePath());
					file=new File(Environment.getExternalStorageDirectory(),"wu.txt");
				    //file=new File(getDefaultFilePath());
					
				    try {
				    	//Log.d("MainActivity", "bufferReader���󹹽����---------֮ǰ");
						bufferReader=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
						//Log.d("MainActivity", "bufferReader���󹹽����----------֮��");
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
						Log.d("MainActivity", "bufferReader���󹹽����-----------���쳣��");
					}
					 strBuffer=new StringBuffer();
				
					try {
						while((str=bufferReader.readLine())!=null){
							Log.d("MainActivity", "bufferReader��ȡ�������-----------֮ǰ");
							strBuffer.append(str);
							
						}
						//bufferReader.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						Log.d("MainActivity", "bufferReader��ȡ�������-----------���쳣����");
					}
					revTmpStr=strBuffer.toString();
					
					if(revTmpStr.indexOf(';')!=-1){
						try{
							String ECGDataStrs[] = revTmpStr.split(";");
							for (int i = 0; i < ECGDataStrs.length -1; i++){
								try{
									ECGData = Float.parseFloat(ECGDataStrs[i].replace(';',' '));
									ECGDataList.add(ECGData);											
								}catch(Exception e){
									e.printStackTrace();
									continue;
								}

							}
							if (ECGDataStrs[ECGDataStrs.length -1].length()==6 || ECGDataStrs[ECGDataStrs.length -1].length()==7&&ECGDataStrs[ECGDataStrs.length -1].indexOf('-')==0){
								try{
									ECGData = Float.parseFloat(ECGDataStrs[ECGDataStrs.length -1].replace(';',' '));
									ECGDataList.add(ECGData);
								}catch(Exception e){
									e.printStackTrace();
								}
								revTmpStr = "";
							}
							else{
								revTmpStr = ECGDataStrs[ECGDataStrs.length -1];
							}									
							
						}
						catch(Exception e){
							e.printStackTrace();
						}		
				
			}
		}
	}
}
}
/*
��Ҫ�õ��ֻ�����sd����·��������÷���ķ����� 
public List getSDPaths(){//�õ�ȫ���洢��ַ��SD�����ֻ��ڴ棩
List paths = new ArrayList();
StorageManager storageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
try {
Class<?>[] paramClasses = {};
Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", paramClasses);
getVolumePathsMethod.setAccessible(true);
Object[] params = {};
Object invoke = getVolumePathsMethod.invoke(storageManager, params);
for (int i = 0; i < ((String[])invoke).length; i++) {
paths.add(((String[])invoke)[i]);
Log.i("tag1", "·����"+((String[])invoke)[i]);
}
} catch (NoSuchMethodException e1) {
e1.printStackTrace();
} catch (IllegalArgumentException e) {
e.printStackTrace();
} catch (IllegalAccessException e) {
e.printStackTrace();
} catch (InvocationTargetException e) {
e.printStackTrace();
}
return paths;
}
�����ϵĴ�СΪ2���������õ�sd����
�����������·����:list.get(0),���õ�sd����·����list.get(1);*/

