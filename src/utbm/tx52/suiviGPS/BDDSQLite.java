package utbm.tx52.suiviGPS;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * La classe BDDSQLite va permettre de stocker les données des coordonées lors 
 * de l'utilisation de l'application
 */
public class BDDSQLite extends SQLiteOpenHelper{
	
	private static final int DATABASE_VERSION = 1;
	
	private static final String POSITION_TABLE_NAME = "position";
	private static final String COL_ID="Id";
	private static final String COL_NIVBATTERIE ="NivBatterie";
	private static final String COL_VITESSE = "Vitesse";
	private static final String COL_ALTITUDE = "Altitude";
	private static final String COL_LATITUDE = "Latitude";
	private static final String COL_LONGITUDE = "Longitude";
	private static final String COL_DATEH = "DateH";
	private static final String COL_ENVOYE = "Envoye";
	
	private static final String POSITION_TABLE_CREATE = "CREATE TABLE "+
					POSITION_TABLE_NAME +
			" (" +
				COL_ID	 		+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
				COL_DATEH	 	+ " INTEGER, "+
				COL_LONGITUDE	+ " DOUBLE, "+
				COL_LATITUDE	+ " DOUBLE, "+
				COL_ALTITUDE	+ " REAL, "+
				COL_VITESSE	 	+ " REAL, "+
				COL_NIVBATTERIE	+ " REAL, "+
				COL_ENVOYE  + " INTEGER "+
			 " );";
				
	/**
	 * Constructeur de BDDSQLite
	 */
	public BDDSQLite(Context contexte,String nomBDD,CursorFactory curseur)
	{
		super(contexte,nomBDD, curseur,DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase bdd){
		/**
		 * Création de la table à  partir de la requéte ci-dessus
		 */
		bdd.execSQL(POSITION_TABLE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase bdd,int version, int inutile)
	{
		bdd.execSQL("DROP_TABLE "+ POSITION_TABLE_NAME + " ;");
		onCreate(bdd);
	}
	

}
