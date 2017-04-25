package com.example.ecgdisplay;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


import java.util.Timer;  
import java.util.TimerTask;  
  
import android.os.AsyncTask;  
import android.os.Bundle;  
import android.util.Log;  
import android.view.View;  
import android.widget.Button;  
import android.app.Activity;  
  
public class MainActivity extends Activity {  
    public static final String TAG = "TAG_droidpaint_heartcurve.MainActivity";  
    public static final boolean DEBUG = true;  
  
    public static void myLog(String str) {  
        if (DEBUG) {  
            Log.d(TAG, str);  
        }  
    }  
  
    public static void myLogE(String str) {  
        if (DEBUG) {  
            Log.e(TAG, str);  
        }  
    }  
  
    SampleView m_sv = null;  
    MyTask mytask = null;  
  
    private class MyTask extends AsyncTask<String, Integer, String> {  
        // onPreExecute����������ִ�к�̨����ǰ��һЩUI����  
        @Override  
        protected void onPreExecute() {  
            Log.i(TAG, "onPreExecute() called");  
  
        }  
  
        // doInBackground�����ڲ�ִ�к�̨����,�����ڴ˷������޸�UI  
        @Override  
        protected String doInBackground(String... params) {  
            Log.i(TAG, "doInBackground(Params... params) called");  
            try {  
                while (true) {  
                    Thread.sleep(1000);  
                    publishProgress(0);  
                }  
            } catch (InterruptedException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
            return null;  
        }  
  
        // onProgressUpdate�������ڸ��½�����Ϣ  
        @Override  
        protected void onProgressUpdate(Integer... progresses) {  
            Log.i(TAG, "onProgressUpdate(Progress... progresses) called");  
  
            m_sv.append_data(m_sv.lines / 4);  
        }  
  
        // onPostExecute����������ִ�����̨��������UI,��ʾ���  
        @Override  
        protected void onPostExecute(String result) {  
            Log.i(TAG, "onPostExecute(Result result) called");  
  
        }  
  
        // onCancelled����������ȡ��ִ���е�����ʱ����UI  
        @Override  
        protected void onCancelled() {  
            Log.i(TAG, "onCancelled() called");  
  
        }  
    }  
  
    void start_timer() {  
        myLog("start_timer()");  
        mytask = new MyTask();  
        mytask.execute("haha task");  
    }  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        myLog("onCreate()");  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
  
        preprare_xml();  
  
        start_timer();  
    }  
  
    void preprare_xml() {  
        myLog("preprare_xml()");  
        m_sv = (SampleView) findViewById(R.id.sv_test);  
  
        m_sv.setOnClickListener(new View.OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
                myLog("m_sv clicked");  
                // TODO Auto-generated method stub  
                m_sv.append_data(m_sv.lines / 4);  
                m_sv.invalidate();  
            }  
        });  
  
    }  
  
}  
