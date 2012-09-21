package utbm.tx52.suiviGPS;

import java.util.List;
import java.util.UUID;

import utbm.tx52.suiviGPS.GeolocalisationService;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Activity principale qui va permettre de parametrer et lancer l'application
 */
public class UTBMSuiviGPSActivity extends Activity {
	private Intent intent = null;
	private String deviceId="";
	private Button boutonTemp;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	this.intent = this.getIntent();
        
        /**
         * Permet de manipuler les champs présent dans la vue
         */
        Button boutonToMap;
        Button boutonActiverServiceGPS;
        Button boutonQuitter;
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /**
         * Code relatif au bouton permettant d'accéder à la carte
         */
        OnClickListener ButtonConsulter = new OnClickListener()
        {
	        public void onClick(View ActuelView)
	        {
		        Intent intentConsulter = new Intent(UTBMSuiviGPSActivity.this,SelectionDateActivity.class);
		        Resources res = getResources();
		    	intentConsulter.putExtra("Etat",res.getInteger(R.integer.constConsultation));				
		        startActivity(intentConsulter);
	        }
        };
        boutonToMap = (Button) findViewById(R.id.boutonConsulter);
        boutonToMap.setOnClickListener(ButtonConsulter);

        /**
         * Code relatif au bouton quitter
         */
        OnClickListener ButtonQuitter = new OnClickListener()
        {
	        public void onClick(View ActuelView)
	        {
			     finish();
	        }
        };
        boutonQuitter = (Button) findViewById(R.id.boutonQuitter);
        boutonQuitter.setOnClickListener(ButtonQuitter);

        /**
         * Code relatif au bouton activant le service
         */
        OnClickListener BoutonActiverSuivi = new OnClickListener()
        {
        	public void onClick(View actuelView)
        	{
        		boutonTemp = (Button) findViewById(R.id.boutonActiverService);
    	        if(!isServiceActif("utbm.tx52.suiviGPS.GeolocalisationService"))
        		{
        			activerSuiviGPS();
        			boutonTemp.setText("Arreter le suivi GPS");
        		}
        		else
        		{
        			Intent myintent = new Intent(UTBMSuiviGPSActivity.this, GeolocalisationService.class);
            		stopService(myintent);
        			boutonTemp.setText("Activer le suivi GPS");
        		}
        	}
        	
        };
        boutonActiverServiceGPS = (Button) findViewById(R.id.boutonActiverService);
        boutonActiverServiceGPS.setOnClickListener(BoutonActiverSuivi);
        
       
		
		if(isServiceActif("utbm.tx52.suiviGPS.GeolocalisationService"))
		{
			boutonActiverServiceGPS.setText("Arreter le suivi GPS");
		}
		else
		{
			boutonActiverServiceGPS.setText("Activer le suivi GPS");
		}
     
		chargerParametre();
    }
    
    public void onDestroy()
    {
    	enregistrerParametre();
    	//stopService(intent);
    	super.onDestroy();
    }
    
    /**
     * isServiceActif permet de savoir si le service dont le nom est passÃ© en paramÃ¨tre est actif
     * 
     * @param nomService: 
     * 
     * @return boolean True : si le service est actif ; False : si le service est inactif
     * 				
     */
    protected boolean isServiceActif(String nomService)
    {   
    	
    	final ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);  
        final List<RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);
        
        //Variable temporaire
        int i=0;
    	while(i < services.size()&& !nomService.equals(services.get(i).service.getClassName())) {
    		i++;
    	}
    	
    	return i < services.size();
    }
    
    /**
     * Permet d'enregistrer les paramÃ¨tres dans les prÃ©fÃ©rences
     */
    protected void enregistrerParametre()
    {
    	/**
    	 * Initialisation de la preference
    	 */
		SharedPreferences parametre =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Editor editor= parametre.edit();
		 
    	EditText ZoneTexte ;
		
		/**
		 * Permet de remplir la zone de texte de la distance
		 */
		ZoneTexte = (EditText)findViewById(R.id.editDistance);
		editor.putString("UTBM_Distance", ZoneTexte.getText().toString());
		 
		/**
		 * Permet de remplir la zone de texte du temps
		 */
		ZoneTexte = (EditText)findViewById(R.id.editTemps);
		editor.putString("UTBM_Temps", ZoneTexte.getText().toString());
		
		/**
		 * Permet de remplir la zone de texte du nombre de position sauvegardÃ©e
		 */
		ZoneTexte = (EditText)findViewById(R.id.editNbPosition);
		editor.putString("UTBM_NbPosition", ZoneTexte.getText().toString());
		
		Bundle bundle  = this.getIntent().getExtras();
		if(bundle!=null) 
		{
			if(intent.getStringExtra("AdrWeb")!="") {
				editor.putString("UTBM_AdresseSiteWeb", intent.getStringExtra("AdrWeb"));
			}
		
			if(intent.getStringExtra("IdPhone")!="") {
				editor.putString("UTBM_UUIDMobile", intent.getStringExtra("IdPhone"));
			}
			else {
				createIdPhone();
				editor.putString("UTBM_UUIDMobile", deviceId);
			}
			
			if(intent.getStringExtra("NumEmetteur")!="") {
				editor.putString("UTBM_NumEmetteur", intent.getStringExtra("NumEmetteur"));
			} 
		}
		
		editor.commit();
    }

    /**
     *protected void chargerParametre(): Permet de charger les paramÃ©tres dans les zones de textes
     */
    protected void chargerParametre()
    {
    	EditText ZoneTexte ;
		SharedPreferences parametre = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		/**
		 * Permet de remplir la zone de texte de la distance
		 */
		ZoneTexte = (EditText)findViewById(R.id.editDistance);
		ZoneTexte.setText(parametre.getString("UTBM_Distance",""));
		
		Bundle bundle  = this.getIntent().getExtras();
		if(bundle!=null && intent.getStringExtra("Dist")!="") {
			ZoneTexte.setText(intent.getStringExtra("Dist"));
		}
		
		/**
		 * Permet de remplir la zone de texte du temps
		 */
		ZoneTexte = (EditText)findViewById(R.id.editTemps);
		ZoneTexte.setText(parametre.getString("UTBM_Temps",""));
		
		if(bundle!=null && intent.getStringExtra("Tps")!="") {
			ZoneTexte.setText(intent.getStringExtra("Tps"));
		}
		
		
		/**
		 * Permet de remplir la zone de texte du nombre de position sauvegardÃ©e
		 */
		ZoneTexte = (EditText)findViewById(R.id.editNbPosition);
		ZoneTexte.setText(parametre.getString("UTBM_NbPosition",""));
		
		if(bundle!=null && intent.getStringExtra("NbMemPoints")!="") {
			ZoneTexte.setText(intent.getStringExtra("NbMemPoints"));
		}
		
		enregistrerParametre();

    }
    
    /**
     * Permet d'activer le service qui envoie les positions GPS au site Web
     */
    protected void activerSuiviGPS()
    {
    	SharedPreferences parametre = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
    	try {
    		 
			Intent myintent = new Intent(UTBMSuiviGPSActivity.this, GeolocalisationService.class);
			
			EditText ZoneTexte1 = (EditText)findViewById(R.id.editDistance);
			 
			EditText ZoneTexte2 = (EditText)findViewById(R.id.editTemps);
			
			EditText ZoneTexte3 = (EditText)findViewById(R.id.editNbPosition);
						
			myintent.putExtra("Dist",ZoneTexte1.getText().toString() );
			myintent.putExtra("Temps",ZoneTexte2.getText().toString() );
			myintent.putExtra("AdrWeb",parametre.getString("UTBM_AdresseSiteWeb",""));
			//Toast.makeText(getBaseContext(),parametre.getString("UTBM_Temps",""),Toast.LENGTH_LONG).show();
			myintent.putExtra("nbMemPoints",ZoneTexte3.getText().toString() );
			myintent.putExtra("IdPhone",parametre.getString("UTBM_UUIDMobile","") );
			
			if(ZoneTexte1.getText().toString()!="" && ZoneTexte2.getText().toString()!="" && ZoneTexte3.getText().toString()!="") {
				//on lance notre service avec l'intent contenant nos informations
				startService(myintent);
			}
			else {
				Toast.makeText(getApplicationContext(), "Informations manquante", Toast.LENGTH_LONG).show();
			}
		}
		catch (Exception e) {
			//si jamais on rencontre un probleme on Ã©vite de crash de l'application
			e.getStackTrace();
		}
		
	}
 
    
    /**
     * S'exécuter uniquement lorsque l'on appuie sur le bouton menu du téléphone.
     * Elle crÃ©e un menu 
     */
    public boolean onCreateOptionsMenu(Menu menu) {
 
        /**
         * Permet d'affiche le menu qui est stockÃ© dans un fichier XML
         */
    	MenuInflater inflater = getMenuInflater();
        /**
         * Instanciation du menu XML en un objet menu
         */
        inflater.inflate(R.layout.menu, menu);
 
 
        return true;
     }
 
    
       /**
        * méthode qui se déclenchera au clic sur un item(non-Javadoc)
        * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
        */
      public boolean onOptionsItemSelected(MenuItem item) {
    	 Resources res = getResources();
         /**
          * On regarde quel item a été cliqué grace à son id et on déclenche une action
          */
         switch (item.getItemId()) {
         	case R.id.optMParam:
            	Intent intentParametrer = new Intent(UTBMSuiviGPSActivity.this,ParamActivity.class);
                startActivity(intentParametrer);
            	return true;
         	
         	case R.id.optSsMenuSupprimerSelection:
         		Intent intentSupprimer = new Intent(UTBMSuiviGPSActivity.this,SelectionDateActivity.class);
         		intentSupprimer.putExtra("Etat",res.getInteger(SelectionDateActivity.etatSuppression));
				
		        startActivity(intentSupprimer);
            	return true;
            	
         	case R.id.optSsMenuSupprimerTout:
         		PositionBDD posBDD = new PositionBDD(getApplicationContext());
         		posBDD.open();
         		posBDD.supprimerToutePosition();
         		posBDD.close();
         		break;
            	
         	case R.id.optMQuitter:
         		//Quitte l'application
         		finish();
         		return true;
         }
         return false;
        }
      
      public void onResume()
      {
    	  super.onResume();
    	  //setContentView(R.layout.main);
    	  this.intent = this.getIntent();
    	  
    	  chargerParametre();
    	  if(this.intent.getFlags()==Intent.FLAG_ACTIVITY_NEW_TASK) {
    		  boutonTemp.setText("Arreter le suivi GPS");
    		  activerSuiviGPS();
    	  }
    	 
      }
      private void createIdPhone() {
    		
  		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
  		String tmDevice = null;
  		String tmSerial = null;
  		String androidId = null;
  		
  		tmDevice = "" + tm.getDeviceId();
  	    tmSerial = "" + tm.getSimSerialNumber();
  	    androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

  	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
  	    deviceId = deviceUuid.toString(); 
  	}
}