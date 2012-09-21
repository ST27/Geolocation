package utbm.tx52.suiviGPS;

import java.text.SimpleDateFormat;
import java.util.Date;

import utbm.tx52.suiviGPS.R.id;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
;

/**
 * Permet de supprimer ou d'afficher les positions qui correspondent aux critéres de sélection
 * 
 */

public class SelectionDateActivity extends Activity{
	private int _etat;
	public static final int etatSuppression = 1;
	public static final int etatConsultation = 2;
	
	public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.select_date);
		 
		 Button boutonValider = (Button) findViewById(R.id.boutonValiderSelectDate);
		 Button boutonAnnuler = (Button) findViewById(R.id.boutonAnnulerSelectDate);
		 
		 Bundle bundle  = this.getIntent().getExtras();
		 if(bundle!=null && bundle.containsKey("Etat"))
		 {
			 
			 _etat=this.getIntent().getIntExtra("Etat",0);
			 switch(_etat)
			 {
			 	case etatSuppression:
				    boutonValider.setText("Supprimer");
				    break;
				    
			 	case etatConsultation:
			 		boutonValider.setText("Consulter");
			 		break;
				
				default:

					 Toast.makeText(getApplicationContext(), "Une erreur s'est produite lors de la sÃ©lection des dates",Toast.LENGTH_LONG).show();
					 finish();
				break;
				  
			 }
		 		
		 }
		 else
		 {
			 Toast.makeText(getApplicationContext(), "Une erreur s'est produite lors de la sÃ©lection des dates",Toast.LENGTH_LONG).show();
			 finish();
		 }
		 
		 /**
		  * Permet de revenir à l'écran d'acceuil sans valider
		  */
		 OnClickListener buttonAnnuler =new OnClickListener() 
		 {
			 public void onClick(View actuelView)
		     {
				 finish();
		     }
	     };
	     boutonAnnuler.setOnClickListener(buttonAnnuler);
	     
		 /**
		  * Action exécutée après l'appui du bouton valider
		  */
	     OnClickListener buttonValiderDate= new OnClickListener() 
		 {
			 public void onClick(View actuelView)
			 {
				 if(verifierDate()==true)
				 {
					 EditText zoneTexte;
					 switch(_etat)
					 {
					 	case etatConsultation:
						 
					 		Intent intentCarte = new Intent(SelectionDateActivity.this,CarteGMapActivity.class);
					     
					 		zoneTexte = (EditText) findViewById(id.editDateDebut);
					 		intentCarte.putExtra("DateDebut",dateToLong(zoneTexte.getText().toString()));
						 	
					 		zoneTexte = (EditText) findViewById(id.editDateFin);
					 		intentCarte.putExtra("DateFin",dateToLong(zoneTexte.getText().toString()));
						 
					 		startActivity(intentCarte);
					 		break;

					 	case etatSuppression:
					 		zoneTexte = (EditText) findViewById(id.editDateDebut);
							long debut=dateToLong(zoneTexte.getText().toString());	
							zoneTexte = (EditText) findViewById(id.editDateFin);
							long fin=dateToLong(zoneTexte.getText().toString());	
							 
							PositionBDD posBDD=new PositionBDD(getApplicationContext());
							 
							posBDD.open();
							posBDD.supprimerPosition(debut,fin);
							posBDD.close();
							 
							break;
						
						default:

							 Toast.makeText(getApplicationContext(), "Une erreur s'est produite lors de la sÃ©lection des dates",Toast.LENGTH_LONG).show();
							 finish();
						break;
					 }
				 }
				 else
				 {
					 Toast.makeText(getApplicationContext(), "Les dates spÃ©cifiÃ©es ne sont pas dans le bon format",Toast.LENGTH_LONG).show(); 
				 }
			 }
		 };
		 boutonValider.setOnClickListener(buttonValiderDate);
			 
	}

	public boolean verifierDate()
	{
		SimpleDateFormat sdfDebut= new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
		SimpleDateFormat sdfFin= new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
		
		EditText editDebut=(EditText) findViewById(R.id.editDateDebut);
		EditText editFin=(EditText) findViewById(R.id.editDateFin);
		
		Date debut=new Date();
		Date fin=new Date();
		
		 
		try 
		{
			String d="";
			String f="";
			
			if(editDebut.getText().toString().length()>0)
			{
				debut=sdfDebut.parse(editDebut.getText().toString());
				d=sdfDebut.format(debut);
			}
			if(editFin.getText().toString().length()>0)
			{
				fin=sdfDebut.parse(editFin.getText().toString());
				f=sdfFin.format(fin);
			}
			
			return d.compareTo(editDebut.getText().toString())==0 && f.compareTo(editFin.getText().toString())==0;
			
		}
		catch(Exception e)
		{	
			return false;
		}
		
	}
	
	public long dateToLong(String s)
	{
		if(s=="")
		{
			return 0;
		}
		else
		{
			SimpleDateFormat sdf= new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
			try
			{
				Date d=sdf.parse(s);
				return d.getTime();
			}
			catch(Exception e)
			{
				return 0;
			}
		}	
		
	}
}

