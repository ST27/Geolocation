package utbm.tx52.suiviGPS;

import java.util.ArrayList;
//import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//import android.widget.Toast;

/**
 * Base de donnée de l'application
 */
public class PositionBDD {

	private static final String COL_ID="Id";
	private static final String COL_DATEH = "DateH";
	private static final String COL_LONGITUDE = "Longitude";
	private static final String COL_LATITUDE = "Latitude";
	private static final String COL_ALTITUDE = "Altitude";
	private static final String COL_VITESSE = "Vitesse";
	private static final String COL_NIVBATTERIE ="NivBatterie";
	private static final String COL_ENVOYE="Envoye";
	
	private static final int NUM_COL_ID=0;
	private static final int NUM_COL_DATEH = 1;
	private static final int NUM_COL_LONGITUDE = 2;
	private static final int NUM_COL_LATITUDE = 3;
	private static final int NUM_COL_ALTITUDE = 4;
	private static final int NUM_COL_VITESSE = 5;
	private static final int NUM_COL_NIVBATTERIE = 6;
	private static final int NUM_COL_ENVOYE=7;
	
	private static final String NOM_TABLE_POSITION = "position";
	private static final String NOM_BDD = "UTBM_positionGPS.db";
	
	private SQLiteDatabase bdd;
	private BDDSQLite bddPosition;
	
	/**
	 * Créée une connnexion à la base de données SQLite dans le contexte passé en paramétre
	 * 
	 * @param context: contexte dans lequel nous voulons placer la base de donnÃ©es
	 */
	
	public PositionBDD(Context context)
	{
		bddPosition = new BDDSQLite(context,NOM_BDD,null);
	}
	
	/**
	 *  Ouvre la connexion à la base de donnÃ©es
	 */
	public void open(){
		bdd= bddPosition.getWritableDatabase();
	}
	
	/**
	 *  Ferme la connexion à la base de donnÃ©es
	 */
	public void close(){
		bdd.close();
	}
	
	/**
	 * 
	 * @return base de données : SQLiteDatabase
	 */
	public SQLiteDatabase getBDD(){
		return bdd;
	}
	
	/**
	 * Insére une position dans la base de données
	 * @param pos : Objet position
	 * @return
	 */
	public long insererPosition(Position pos)
	{
		/**
		 * Création d'un ContentValues
		 */
		ContentValues valeur = new ContentValues();

	    /**
	     * Affectation des valeurs
	     */
		valeur.put(COL_DATEH, pos.getDateH());
		valeur.put(COL_LONGITUDE,pos.getLongitude());
		valeur.put(COL_LATITUDE,pos.getLatitude());
		valeur.put(COL_ALTITUDE,pos.getAltitude());
		valeur.put(COL_VITESSE,pos.getVitesse());
		valeur.put(COL_NIVBATTERIE,pos.getNivBatterie());
		valeur.put(COL_ENVOYE,pos.getEnvoye());
		/**
		 * Insertion de l'objet dans la base de données
		 */
		return bdd.insert(NOM_TABLE_POSITION,null,valeur);
	
	}
	
	/**
	 * Cherche l'identifiant de la premiére position stockée dans la base de données
	 * @return identifiant de la position : int
	 */
	public int chercherIdPremierPosition()
	{
		/**
		 * Récupére l'identifiant de la position
		 */
		int idPos;
		/**
		 * Exécution de la requéte :
		 *	SELECT MIN(ID_POS) FROM POSITION;
		*/
		Cursor c = bdd.query(NOM_TABLE_POSITION,new String[] { "min(" + COL_ID + ")" } ,null,null,null,null,null);
		
		if(c.getCount()==0)
		{
		 return 0;
		}
		
		c.moveToFirst();
		/**
		 * Placement du cursor sur l'unique ligne retournée par la requéte
		 */
		idPos=c.getInt(0);
		
		/**
		 * Feremeture du curseur
		 */
		c.close();
		return idPos;
	}
	
	
	/**
	 * Retourne le nombre de position dans la base de données
	 * @return
	 */
	public int nombrePosition()
	{
		int nbPos;
		Cursor c = bdd.query(NOM_TABLE_POSITION,new String[] { "count(" + COL_ID + ")" } ,null,null,null,null,null);
		
		if(c.getCount()==0)
		{
		 return 0;
		}
		
		/**
		 * Placement du cursor sur l'unique ligne retournée par la requéte
		 */
		c.moveToFirst();
		nbPos=c.getInt(0);
		
		/**
		 * Feremeture du curseur
		 */
		c.close();
		return nbPos;
	}
	
	/**
	 * Supprime la position dont l'identifiant est passée en paramétre
	 * @param id
	 * @return
	 */
	public int supprimerPosition(long debut, long fin)
	{	
		/**
		 * Stocke la close de sÃ©lection
		 */
		String selection=null;
		
		/**
		 * On sélectionne selon une date de début et une date de fin
		 */
		if(debut!=0 && fin!=0)
		{
			selection=COL_DATEH+" >= "+debut  +" AND "+COL_DATEH +" <= "+fin;
		}
		else
		{
			/**
			 * On sélectionne selon une date de début 
			 */
			if(debut!=0)
			{
				selection=COL_DATEH+" >= "+debut;
			}
			else
			{

			/**
			 * On sélectionne selon une date de fin
			 */
				if(fin!=0)
				{
					selection=COL_DATEH +" <= "+fin;
				}
			}
		}
		/**
		 * Suppression des positions dans la BDD
		 */
		return bdd.delete(NOM_TABLE_POSITION, selection ,null);
	}
	
	
	/**
	 * Supprime les position qui se trouvent entre  position dont l'identifiant est passée en paramétre
	 * @param id
	 * @return
	 */
	public int supprimerPosition(int id)
	{	
		//Suppression des positions dans la BDD
		return bdd.delete(NOM_TABLE_POSITION, COL_ID + " = " + id ,null);
	}
	
	/**
	 * Supprime les position qui se trouvent entre  position dont l'identifiant est passée en paramétre
	 * @param id
	 * @return
	 */
	public int supprimerToutePosition()
	{	
		/**
		 * Suppression des positions dans la BDD
		 */
		return bdd.delete(NOM_TABLE_POSITION,null,null);
	}
	
	/**
	 * Met à jout la position envoyé en paramétre
	 * @param pos
	 * @return
	 */
	public int updatePosition(Position pos)
	{	
		ContentValues argument = new ContentValues();
		argument.put(COL_VITESSE,pos.getVitesse());
		argument.put(COL_ALTITUDE,pos.getAltitude());
		argument.put(COL_NIVBATTERIE,pos.getNivBatterie());
		argument.put(COL_ENVOYE,pos.getEnvoye());
		
		/**
		 * Suppression des positions dans la BDD
		 */
		return bdd.update(NOM_TABLE_POSITION, argument, COL_ID + " = " + pos.getId() ,null);
		
	}
	
	
	
	/**
	 * Retourne la premiére position insérée dans la base de donnée
	 * 
	 * @return Position
	 */
	public Position getPremierePosition()
	{
		Cursor c = bdd.query(NOM_TABLE_POSITION,
							 new String[] { COL_ID,
											COL_DATEH,
											COL_LONGITUDE,
											COL_LATITUDE,
											COL_ALTITUDE,
											COL_VITESSE,
											COL_NIVBATTERIE,
							 				COL_ENVOYE} ,null,null,null,null,COL_ID + " ASC");
		/**
		 * Création d'une position 
		 */
		if(c.getCount()==0)
		{
		 c.close();
		 return null;
		 	
		}
		c.moveToFirst();

		Position pos = new Position(cursorToPosition(c));
		/**
		 * Fermeture du cursor
		 */
		c.close();
			
		return pos;
	}
	
	/**
	 * 
	 * Retourne la position s'il elle existe ayant pour id, le paramétre idPos passée en paramétre
	 * 
	 * @param idPos
	 * @return Position
	 */
	public Position getPosition(long idPos)
	{
		Cursor c = bdd.query(NOM_TABLE_POSITION,
							 new String[] { COL_ID,
											COL_DATEH,
											COL_LONGITUDE,
											COL_LATITUDE,
											COL_ALTITUDE,
											COL_VITESSE,
											COL_NIVBATTERIE,
							 				COL_ENVOYE} ,COL_ID + " = "+ idPos,null,null,null,null);

		/**
		 * Création d'une position 
		 */
		if(c.getCount()==0)
		{
			 c.close();
			 return null;
		}
		
		c.moveToFirst();

		Position pos = new Position(cursorToPosition(c));
		/**
		 * Fermeture du cursor
		 */
		c.close();
					
		return pos;

	}

	
	/**
	 * Retourne la liste des positions entre deux dates, 
	 * si l'on ne veut pas de restriction sur la date de début on affecte 0 au paramétre correspondant
	 * 
	 * @param debut :long
	 * @param fin :long
	 * @return
	 */
	public ArrayList<Position> getPosition(long debut,long fin)
	{
		/**
		 * Stocke la close de sélection
		 */
		String selection=null;
		
		/**
		 * On sélectionne selon une date de début et une date de fin
		 */
		if(debut!=0 && fin!=0)
		{
			selection=COL_DATEH+" >= "+debut  +" AND "+COL_DATEH +" <= "+fin;
		}
		else
		{

			/**
			 * On sélectionne selon une date de début 
			 */
			if(debut!=0)
			{
				selection=COL_DATEH+" >= "+debut;
			}
			else
			{

				/**
				 * On sélectionne selon une date de fin
				 */
				if(fin!=0)
				{
					selection=COL_DATEH +" <= "+fin;
				}
			}
		}
		/*
		 * Requéte de sélection de type :
		 * SELECT * FROM POSITION; 
		 */
		Cursor c = bdd.query(NOM_TABLE_POSITION,
							 new String[] { COL_ID,
											COL_DATEH,
											COL_LONGITUDE,
											COL_LATITUDE,
											COL_ALTITUDE,
											COL_VITESSE,
											COL_NIVBATTERIE,
							 				COL_ENVOYE},selection,null,null,null,null);
		
		ArrayList<Position> tabPosition= new ArrayList<Position>();
		
		if(c.moveToFirst())
		{
			do
			{
				tabPosition.add(cursorToPosition(c));			
			}
			while(c.moveToNext());
			
		}
		c.close();
		return tabPosition;
	}
	
	/**
	 * Retourne la liste des positions non envoyé
	 * 
	 * @return
	 */
	public ArrayList<Position> getPositionNonEnvoye()
	{
		
				
		/**
		 * On sélectionne selon une date de début et une date de fin
		 */
	    Cursor c = bdd.query(NOM_TABLE_POSITION,
							 new String[] { COL_ID,
											COL_DATEH,
											COL_LONGITUDE,
											COL_LATITUDE,
											COL_ALTITUDE,
											COL_VITESSE,
											COL_NIVBATTERIE,
							 				COL_ENVOYE},COL_ENVOYE + "= 0",null,null,null,null);
		
		ArrayList<Position> tabPosition= new ArrayList<Position>();
		
		if(c.moveToFirst())
		{
			do
			{
				tabPosition.add(cursorToPosition(c));			
			}
			while(c.moveToNext());
			
		}
		c.close();
		return tabPosition;
	}
		
	Position cursorToPosition(Cursor c)
	{
		if(c.getCount()==0)
			{
			 return null;
			}
		
		Position pos = new Position();
		
		/**
		 * On affecte les informations grace aux infos contenues
		 */
		pos.setId(c.getInt(NUM_COL_ID));
		pos.setDateH(c.getLong(NUM_COL_DATEH));
		pos.setLongitude(c.getDouble(NUM_COL_LONGITUDE));
		pos.setLatitude(c.getDouble(NUM_COL_LATITUDE));
		pos.setAltitude(c.getFloat(NUM_COL_ALTITUDE));
		pos.setVitesse(c.getFloat(NUM_COL_VITESSE));
		pos.setNivBatterie(c.getInt(NUM_COL_NIVBATTERIE));
		pos.setEnvoye(c.getInt(NUM_COL_ENVOYE));
		
		
		/**
		 * On retourne la position
		 */
		return pos;
	}
	

	
	
}
