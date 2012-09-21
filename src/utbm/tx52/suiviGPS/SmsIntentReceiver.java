package utbm.tx52.suiviGPS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Classe de receiver pour activer l'application lors de l'envoi de sms
 */
public class SmsIntentReceiver extends BroadcastReceiver 
{
	private static String DIST="dist";
	private static String TPS="tps";
	private static String ADR="adr";
	private static String MEM="mem";
	private static String ID="idP";
	private static String NUM="num";
	
	private String t_str1="", t_str2="", t_str3="", t_str4="", t_str5="", t_str6="";
	
	private void triggerAppLaunch(Context context)
	{
		Intent broadcast = new Intent("utbm.tx52.suiviGPS.WAKE_UP");
		
		broadcast.putExtra("Dist", t_str1);
		broadcast.putExtra("Tps", t_str2);
		broadcast.putExtra("AdrWeb", t_str3);
		broadcast.putExtra("NbMemPoints", t_str4);
		broadcast.putExtra("IdPhone", t_str5);
		broadcast.putExtra("NumEmetteur", t_str6);
        
		broadcast.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 	
		context.startActivity(new Intent(broadcast));
	}
	
	private SmsMessage[] getMessagesFromIntent(Intent intent)
	{
		SmsMessage retMsgs[] = null;
		Bundle bdl = intent.getExtras();
		try{
			Object pdus[] = (Object [])bdl.get("pdus");
			retMsgs = new SmsMessage[pdus.length];
			for(int n=0; n < pdus.length; n++)
			{
				byte[] byteData = (byte[])pdus[n];
				retMsgs[n] = SmsMessage.createFromPdu(byteData);
			}	
			
		}catch(Exception e)
		{
			Log.e("GetMessages", "fail", e);
		}
		return retMsgs;
	}
	
	public void onReceive(Context context, Intent intent) 
	{
		
		if(!intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
		{
			return;
		}
		SmsMessage msg[] = getMessagesFromIntent(intent);
		
		/**
		 * analyse du message afin de traiter les opérations en fonction
		 */
		for(int i=0; i < msg.length; i++)
		{
			String message = msg[i].getDisplayMessageBody();
			if(message != null && message.length() > 0)
			{
				Log.i("MessageListener:",  message);
				
				if(message.startsWith("launchAPP")) 
				{
					if(message.contains(DIST)) 
					{
						int indexD=message.indexOf(DIST)+DIST.length()+1;
						int indexF=message.indexOf("#1");
					
						t_str1 = message.substring(indexD, indexF);
					}
					if(message.contains(ADR)) 
					{
						int indexD=message.indexOf(ADR)+ADR.length()+1;
						int indexF=message.indexOf("#1");
					
						t_str2 = message.substring(indexD, indexF);
					}
					if(message.contains(TPS)) 
					{
						int indexD=message.indexOf(TPS)+TPS.length()+1;
						int indexF=message.indexOf("#1");
					
						t_str3 = message.substring(indexD, indexF);
						
					}
					if(message.contains(MEM)) 
					{
						int indexD=message.indexOf(MEM)+MEM.length()+1;
						int indexF=message.indexOf("#1");
					
						t_str4 = message.substring(indexD, indexF);
					}
					if(message.contains(ID)) 
					{
						int indexD=message.indexOf(ID)+ID.length()+1;
						int indexF=message.indexOf("#1");
					
						t_str5 = message.substring(indexD, indexF);
					}
					if(message.contains(NUM)) 
					{
						int indexD=message.indexOf(NUM)+NUM.length()+1;
						int indexF=message.indexOf("#1");
					
						t_str6 = message.substring(indexD, indexF);
					}
					
					triggerAppLaunch(context);
				}
			}
		}
	}
}