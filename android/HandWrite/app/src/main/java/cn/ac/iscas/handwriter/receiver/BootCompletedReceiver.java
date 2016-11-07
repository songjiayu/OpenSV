package cn.ac.iscas.handwriter.receiver;
 
import cn.ac.iscas.handwriter.service.AppLockService;
import android.content.BroadcastReceiver; 
import android.content.Context; 
import android.content.Intent; 
 
public class BootCompletedReceiver extends BroadcastReceiver { 
	
	@Override 
	public void onReceive(Context context, Intent intent) { 
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) 
		{
			AppLockService.hasoffed = true;
			Intent sevice = new Intent(context, AppLockService.class);
			context.startService(sevice);
	        }       
	}
 
	 
}
