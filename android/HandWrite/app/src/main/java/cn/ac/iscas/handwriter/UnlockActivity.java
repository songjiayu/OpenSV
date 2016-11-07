package cn.ac.iscas.handwriter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import cn.ac.iscas.handwriter.views.SignaturePad;
import cn.ac.iscas.handwriter.utils.HandWriter;
//add
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
//end


public class UnlockActivity extends Activity {

    private final String TAG = "UnlockActivity";

    private SignaturePad mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;
    private TextView mSignaturepadDescription;

    //add
    private Button mOptionbtn;
    private int count;
    
    private static ArrayList mrecords=new ArrayList();

    private HandWriter mhandwriter = HandWriter.GetInstance();
    //end
    
    @Override
    public void onAttachedToWindow() {
		 this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | 
		   WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | 
		   WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | 
		   WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON ,
		   WindowManager.LayoutParams.FLAG_FULLSCREEN | 
		   WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | 
		   WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | 
		   WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		 super.onAttachedToWindow();
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
    fullScreenDisplay();
    count=0;

    mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
        @Override
        public void onStartSigning() { //保留添加二级密码功能
            if (/*mCurrentUsername == null*/false) {
                new AlertDialog.Builder(UnlockActivity.this)
                    .setTitle(R.string.no_username_dialog_title)
                    .setMessage(R.string.no_username_dialog_message)
                    .setIcon(android.R.drawable.stat_sys_warning)
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                 @Override
                                                 public void onClick(DialogInterface dialog, int which) {
                        mSignaturePad.clear();
                        // clear old records.
                        mSignaturePad.getMotionEventRecord().clear();
                        dialog.dismiss();
                    }
                        }).create().show();
            } else {
                fullScreenDisplay();
            }
        }

        @Override
        public void onSigned() {
        mSaveButton.setEnabled(true);
        mClearButton.setEnabled(true);
        count=0;
        }

        @Override
        public void onClear() {
        mSaveButton.setEnabled(false);
        //mClearButton.setEnabled(false);
        }
    });

    mClearButton = (Button) findViewById(R.id.clear_button);
    mSaveButton = (Button) findViewById(R.id.save_button);
    mOptionbtn = (Button) findViewById(R.id.option_btn);
    mOptionbtn.setVisibility(View.INVISIBLE);

    mSignaturepadDescription = (TextView) findViewById(R.id.signature_pad_description);
    mSignaturepadDescription.setText(getString(R.string.hint_info3));

    mClearButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        mSignaturePad.clear();
        // clear old records.
        mSignaturePad.getMotionEventRecord().clear();
        if(count==10)
        {
            Intent intent = new Intent("cn.ac.iscas.FACE_UNLOCK");
		    sendBroadcast(intent);
            Toast.makeText(UnlockActivity.this, "解锁成功!", Toast.LENGTH_SHORT).show();
            finish();
        }
        count++;

        }
    });

    mSaveButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        // show dialog, let user tell us whether this signature is true.
        mrecords.clear();
        mrecords.add(mSignaturePad.getMotionEventRecord());
        mSignaturePad.clear();
        if (mhandwriter.check())
        {
            Intent intent = new Intent("cn.ac.iscas.FACE_UNLOCK");
		    sendBroadcast(intent);
            Toast.makeText(UnlockActivity.this, "解锁成功!", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            Toast.makeText(UnlockActivity.this, "解锁失败", Toast.LENGTH_SHORT).show();
        }
        count=0;
        mSignaturePad.clear();
        mSignaturePad.getMotionEventRecord().clear();
        fullScreenDisplay();
        }
    });
    

    }


    //选项按钮    
   


    private void fullScreenDisplay (){
    if (mSignaturePad != null) {
        // full screen setting, make our sign UI fullscreen.
        mSignaturePad.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
    }
    
    
    
    //add
    public static ArrayList getrecords(){
            return mrecords;
    }

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
		if(KeyEvent.KEYCODE_BACK == event.getKeyCode())  
		{  
			return true; 
		}  
		return super.onKeyDown(keyCode, event); 
	}
    //end

}
