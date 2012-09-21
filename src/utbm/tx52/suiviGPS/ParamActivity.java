package utbm.tx52.suiviGPS;

import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Classe d'ui pour l'affichage des données de parametre
 */
public class ParamActivity extends Activity{

	
	public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.param);
		 
		 /**
		  * Permet de revenir a l'ecran d'acceuil sans valider
		  */
		 Button boutonToMain = (Button) findViewById(R.id.boutonAnnuler);
		 boutonToMain.setOnClickListener(new View.OnClickListener()
	     {
			 public void onClick(View actuelView)
		     {
				 finish();
		     }
	     });
		 	
		 /**
		  * Permet de revenir a l'ecran d'acceuil apres avoir valider
		  */
		 Button boutonValider = (Button) findViewById(R.id.boutonParamValider);
		 boutonValider.setOnClickListener(new View.OnClickListener()
		 {
			 public void onClick(View actuelView)
			 {
				 /**
				  * Initialisation de la preference
				  */
				 SharedPreferences parametre = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				 Editor editor= parametre.edit();
				 
				 /**
				  * Insertion dans les preferences de l'adresse du site web
				  */
				 EditText ZoneTexte = (EditText)findViewById(R.id.editAdresseSite);
				 editor.putString("UTBM_AdresseSiteWeb",ZoneTexte.getText().toString());
				 
				 /**
				  * Insertion dans les preferences de l'identifiant du mobile dans le site web 
				  */
				 ZoneTexte = (EditText)findViewById(R.id.editUUIDMobile);
				 editor.putString("UTBM_UUIDMobile",ZoneTexte.getText().toString());

				 if(ZoneTexte.getText().toString().equals("")) {
					 editor.putString("UTBM_UUIDMobile",createIdPhone());
				 }
				 
				 /**
				  * Insertion dans les preferences du numero de mobile capable d'activer le SMS a distance
				  */
				 ZoneTexte = (EditText)findViewById(R.id.editNumEmetteur);
				 editor.putString("UTBM_NumEmetteur",ZoneTexte.getText().toString());
				 
				 /**
				  * Met a jour les modifications
				  */
				 editor.commit();

				 finish();
				 
			 }
		 });
		 
		 /**
		  * Permet d'afficher les preferences 
		  */
		 EditText ZoneTexte ;
		 SharedPreferences parametre = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); 
				 //PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		 ZoneTexte = (EditText)findViewById(R.id.editAdresseSite);
		 ZoneTexte.setText(parametre.getString("UTBM_AdresseSiteWeb",""));
		 
		 /**
		  * Insertion dans les preferences de l'identifiant du mobile dans le site web 
		  */
		 ZoneTexte = (EditText)findViewById(R.id.editUUIDMobile);
		 ZoneTexte.setText(parametre.getString("UTBM_UUIDMobile",""));
		 
		 if(ZoneTexte.getText().toString().equals("")) {
			 ZoneTexte.setText(createIdPhone());
		 }
		 
		 /**
		  * Insertion dans les preferences du numero de mobile capable d'activer le SMS a distance
		  */
		 ZoneTexte = (EditText)findViewById(R.id.editNumEmetteur);
		 ZoneTexte.setText(parametre.getString("UTBM_NumEmetteur",""));
		 
	}
	
	private String createIdPhone() {
		
  		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
  		String tmDevice = null;
  		String tmSerial = null;
  		String androidId = null;
  		
  		tmDevice = "" + tm.getDeviceId();
  	    tmSerial = "" + tm.getSimSerialNumber();
  	    androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

  	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
  	    return deviceUuid.toString(); 
  	}

}
