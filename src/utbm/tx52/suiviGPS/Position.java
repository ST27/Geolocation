package utbm.tx52.suiviGPS;

import java.sql.Timestamp;


/**
 * Classe de donnée afin de stocker les informations en bdd
 */
public class Position {
		private int _id;
		private long _dateH;
		private double _longitude;
		private double _latitude;
		private double _altitude;
		private float _vitesse;
		private int _nivBatterie;
		private int _envoye;
		
		/**
		 * 
		 * Constructeur ayant pour paramétre la liste des attributs de l'objet position
		 * 
		 * @param id :int
		 * @param dateH :long
		 * @param longitude :double
		 * @param latitude :double
		 * @param altitude :float
		 * @param vitesse :float
		 * @param nivBatterie :float
		 * @param envoye :float
		 */
		public Position (int id, long dateH, double longitude, double latitude, double altitude,float vitesse, int nivBatterie, int envoye)
		{
			this._id=id;
			this._dateH= dateH;
			this._longitude=longitude;
			this._latitude= latitude;
			this._altitude= altitude;
			this._vitesse= vitesse;
			this._nivBatterie= nivBatterie;
			this._envoye=envoye;	
		}
		
		/**
		 * Constructeur ayant pour paramétre une position
		 * @param p: Position
		 */
		public Position (Position p)
		{
			this._id=p._id;
			this._dateH= p._dateH;
			this._longitude=p._longitude;
			this._latitude= p._latitude;
			this._altitude= p._altitude;
			this._vitesse= p._vitesse;
			this._nivBatterie= p._nivBatterie;
			this._envoye=p._envoye;	
		}
		
		/**
		 * Constructeur par défaut
		 */
		public Position() {
			this._id=0;
			this._dateH= 0;
			this._longitude=0;
			this._latitude= 0;
			this._altitude= 0;
			this._vitesse= 0;
			this._nivBatterie= 0;
			}

		/**
		 * @return the id
		 */
		public int getId() {
			return _id;
		}
		/**
		 * @return the dateH
		 */
		public long getDateH() {
			return _dateH;
		}
		
		public String getDateToString(){
			Timestamp d = new Timestamp(_dateH);
	        return d.toString();
		}
		/**
		 * @return the longitude
		 */
		public double getLongitude() {
			return _longitude;
		}
		/**
		 * @return the latitude
		 */
		public double getLatitude() {
			return _latitude;
		}
		/**
		 * @return the altitude
		 */
		public double getAltitude() {
			return _altitude;
		}
		/**
		 * @return the vitesse
		 */
		public float getVitesse() {
			return _vitesse;
		}
		/**
		 * @return the nivBatterie
		 */
		public float getNivBatterie() {
			return _nivBatterie;
		}
		
		public int getEnvoye(){
			return _envoye;
		}
		/**
		 * @param id the id to set
		 */
		public void setId(int id) {
			this._id = id;
		}
		/**
		 * @param dateH the dateH to set
		 */
		public void setDateH(long dateH) {
			this._dateH = dateH;
		}
		/**
		 * @param longitude the longitude to set
		 */
		public void setLongitude(double longitude) {
			this._longitude = longitude;
		}
		/**
		 * @param latitude the latitude to set
		 */
		public void setLatitude(double latitude) {
			this._latitude = latitude;
		}
		/**
		 * @param altitude the altitude to set
		 */
		public void setAltitude(float altitude) {
			this._altitude = altitude;
		}
		/**
		 * @param vitesse the vitesse to set
		 */
		public void setVitesse(float vitesse) {
			this._vitesse = vitesse;
		}
		/**
		 * @param nivBatterie the nivBatterie to set
		 */
		public void setNivBatterie(int nivBatterie) {
			this._nivBatterie = nivBatterie;
		}
		
		public void setEnvoye(int envoye){
			this._envoye=envoye;
		}
		
		/**
		 * Permet d'afficher la position sous forme de chaine de caractéres
		 */
		public String toString(){
			
			return "ID: "+ _id +
					"\nDate :"+	_dateH +
					"\nLongitude :"+ _longitude+ 
					"\nLatitude :"+ _latitude+
					"\nAltitude :"+ _altitude+
					"\nVitesse :"+ _vitesse+
					"\nNiveau de batterie :" + _nivBatterie+
					"\nEnvoye"+ _envoye;
		}
		
		public String getInformation(){
			
			String temp="";
			if(_envoye==0)
				temp="\n Position non envoyée";
			
			return 	"\nLongitude :"+ _longitude+ 
					"\nLatitude :"+ _latitude+
					"\nAltitude :"+ _altitude+
					"\nVitesse :"+ _vitesse+
					"\nNiveau de batterie :" + _nivBatterie+ temp;
				
		}
}