package wu.rang.hao.readerwriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.http.util.EncodingUtils;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private Button write;
	private Button read;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		write=(Button)findViewById(R.id.write);
		read=(Button)findViewById(R.id.read);
		write.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				writeDataToSD();
				
			}
		});
		read.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				readDataFromSD();
			}
		});
	}
	public void writeDataToSD(){  
		try{  
		    /* ��ȡFile����ȷ�������ļ�����Ϣ */  
		    //File file  = new File(Environment.getExternalStorageDirectory()+"/f.txt");  
		  File file  = new File(Environment.getExternalStorageDirectory(),"f.txt");  

		   /* �ж�sd���ⲿ����״̬�Ƿ���Զ�д */  
		    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  
		          
		        /* ���Ķ��� *//*  */  
		     FileOutputStream fos = new FileOutputStream(file);  

		        /* ��Ҫд������� */  
		        String message = "�������Ǻܺ�";  

		       /* ���ַ���ת�����ֽ����� */  
		        byte[] buffer = message.getBytes();  
		  
		        /* ��ʼд������ */  
		        fos.write(buffer);  

		       /* �ر�����ʹ�� */  
		        fos.close();  
		      Toast.makeText(MainActivity.this, "�ļ�д��ɹ�", 1000).show();  
		  }  
		}catch(Exception ex){  
		    Toast.makeText(MainActivity.this, "�ļ�д��ʧ��", 1000).show();  
		}  

	}
	
	public void readDataFromSD(){  
		  try{  
		         
		       /* ����File����ȷ����Ҫ��ȡ�ļ�����Ϣ */  
		       File file = new File(Environment.getExternalStorageDirectory(),"f.txt");  
		        
		        /* FileInputSteam �������Ķ��� */    FileInputStream fis = new FileInputStream(file);  
		        /* ׼��һ���ֽ������û�װ������ȡ������ */  
		        byte[] buffer = new byte[fis.available()];  
		        
		      /* ��ʼ�����ļ��Ķ�ȡ */  
		     fis.read(buffer);            
		        /* �ر���  */  
		     fis.close();  
		         
		       /* ���ֽ�����ת�����ַ����� ��ת������ĸ�ʽ */  
		        String res = EncodingUtils.getString(buffer, "UTF-8");  
		          
		       Toast.makeText(MainActivity.this, "�ļ���ȡ�ɹ�������ȡ������Ϊ��"+res, 1000).show();  
		         
		  }catch(Exception ex){  
		       Toast.makeText(MainActivity.this, "�ļ���ȡʧ�ܣ�", 1000).show();  
		   }  
		}  
}
