package utbm.tx52.suiviGPS;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;
 
/**
 * Classe de service permettant de lancer le system de geolocalisation
 * La classe permettra de récuperer les positions et de les envoyer au site web et de les stocker en bdd
 */
public class GeolocalisationService extends Service {
	
	private LocationManager locationMgr = null;
	private float dist2Points = 0;
	private Long time2points = (long) 60000;
	private String adrWeb = "";
	private String idPhone = "";
	private int level;
	private PositionBDD p;

	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){  
	    @Override  
	    public void onReceive(Context arg0, Intent intent) { 
	      level = intent.getIntExtra("level", 0);  
	    }  
	}; 
	
	/**
	 * On créer un locationListener qui permettra de récuperer les positions et les envoyer au site 
	 * dès qu'une nouvelle location est trouvé
	 */
	private LocationListener onLocationChange = new LocationListener() {
		
		 public void onStatusChanged(String provider, int status, Bundle extras)
		 {
		 }
		 
		 public void onProviderEnabled(String provider)
		 {
		 }
		 
		 public void onProviderDisabled(String provider)
		 {
		 }
		 
		 public void onLocationChanged(Location location)
		 {
			 
			 Double latitude = location.getLatitude();
			 Double longitude = location.getLongitude();
		 
			 
			 /**
			  * Crée un toast affichant les coordonnées  
			  */
			 Toast.makeText(getBaseContext(),"Voici les coordonnees de votre telephone : " + latitude + " " + longitude,Toast.LENGTH_LONG).show(); 
			 if(adrWeb!="")
				 sendData(location);
		 }
		 
		/**
		 * Envoie des données au serveur web
		 * On créer un paquet httpclient qui va contenir nos données
		 */
		private void sendData(Location location) {
			
			HttpClient client = new DefaultHttpClient(); 
	        HttpPost post = new HttpPost(adrWeb); 
	        List<NameValuePair> nvps = new ArrayList<NameValuePair>(2); 
	        nvps.add(new BasicNameValuePair("ID_MOBILE", idPhone));
	        Timestamp d = new Timestamp(location.getTime());
	        String test = d.toString();
	        Toast.makeText(getBaseContext(),test,Toast.LENGTH_LONG).show();
	        nvps.add(new BasicNameValuePair("DATE_POS", d.toString() )) ;
	        nvps.add(new BasicNameValuePair("LAT_POS", Double.toString(location.getLatitude()) )); 
	        nvps.add(new BasicNameValuePair("LONG_POS", Double.toString(location.getLongitude()) )); 
	        nvps.add(new BasicNameValuePair("VIT_POS", Double.toString(location.getSpeed() ))); 
	        nvps.add(new BasicNameValuePair("ALT_POS", Double.toString(location.getAltitude() ))); 
	        
	        try {
				post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			}
	        catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} 
			
	        p = new PositionBDD(getApplicationContext());
	        p.open();
	        try {
	        	Position tmp = new Position(1, location.getTime(), location.getLongitude(), location.getLatitude(),location.getAltitude(), location.getSpeed(), level, 1);
				p.insererPosition(tmp);
				client.execute(post);
			} 
	        catch (ClientProtocolException e) {
	        	Position tmp = new Position(1, location.getTime(), location.getLongitude(), location.getLatitude(),location.getAltitude(), location.getSpeed(), level, 0);
				p.insererPosition(tmp);
				e.printStackTrace();
			} 
	        catch (IOException e) {
	        	Position tmp = new Position(1, location.getTime(), location.getLongitude(), location.getLatitude(),location.getAltitude(), location.getSpeed(), level, 0);
				p.insererPosition(tmp);
				e.printStackTrace();
			}
	        catch (Exception e) {
	        	e.printStackTrace();
			}
	        p.close();
	        sendOldData();
		}
		
		/**
		 * Envoie des données non envoyé
		 */
		public void sendOldData() {
			p = new PositionBDD(getApplicationContext());
			p.open();
			List<Position> l = p.getPositionNonEnvoye();
			Iterator<Position> it = l.iterator();
			
			while(it.hasNext()) {
				Position tmp = it.next();
				
				HttpClient client = new DefaultHttpClient(); 
		        HttpPost post = new HttpPost(adrWeb); 
		        List<NameValuePair> nvps = new ArrayList<NameValuePair>(2); 
		        nvps.add(new BasicNameValuePair("ID_MOBILE", idPhone));
		        Timestamp d = new Timestamp(tmp.getDateH());
		        String test = d.toString();
		        Toast.makeText(getBaseContext(),test,Toast.LENGTH_LONG).show();
		        nvps.add(new BasicNameValuePair("DATE_POS", tmp.getDateToString() ));
		        nvps.add(new BasicNameValuePair("LAT_POS", Double.toString(tmp.getLatitude()) )); 
		        nvps.add(new BasicNameValuePair("LONG_POS", Double.toString(tmp.getLongitude()) )); 
		        nvps.add(new BasicNameValuePair("VIT_POS", Double.toString(tmp.getVitesse() ))); 
		        nvps.add(new BasicNameValuePair("ALT_POS", Double.toString(tmp.getAltitude() ))); 
		        
		        try {
					post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
				}
		        catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} 
				
		        try {
					client.execute(post);
					tmp.setEnvoye(1);
					p.updatePosition(tmp);
				} 
		        catch (ClientProtocolException e) {
					e.printStackTrace();
				} 
		        catch (IOException e) {
					e.printStackTrace();
				}
		        catch (Exception e) {
		        	e.printStackTrace();
				}
			}
			p.close();
		}
	};
 
	public IBinder onBind(Intent arg0) {
		return null;
	}
	 
	public void onCreate() {
		super.onCreate();
		 	
	}
 
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		this.registerReceiver(this.mBatInfoReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));  
		 
		Intent thisIntent = intent;
		
		try {
			dist2Points = new Float(thisIntent.getExtras().getString("Dist"));
			time2points = new Long(thisIntent.getExtras().getString("Temps"))*1000;
			adrWeb = thisIntent.getExtras().getString("AdrWeb");
			idPhone = thisIntent.getExtras().getString("IdPhone");
			
			if(dist2Points<0)
				dist2Points=0;
			if(time2points<0)
				time2points=(long) 0;
			
			launchMyService();
		}
		catch (Exception e) {
			//Erreur de format possible
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	 
	public void onDestroy() {
		super.onDestroy();
		try{
			locationMgr.removeUpdates(onLocationChange);
		}
		catch (Exception e)
		{
			
		}
	}
	
	/**
	 * lance le service avec toute les paramétres souhaités
	 * Avec création d'un location manager qui récupere les données via la puce gps
	 */
	public void launchMyService() {
		
		try {
			locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, time2points,dist2Points, onLocationChange);
			//locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, time2points, dist2Points,onLocationChange);
		}
		catch (Exception e) {
			//si jamais bugs dues aux paramétres
		}
	}
}