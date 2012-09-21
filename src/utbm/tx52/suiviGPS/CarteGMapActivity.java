package utbm.tx52.suiviGPS;

import java.util.ArrayList;
import java.util.List;

import utbm.tx52.suiviGPS.R.id;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

/**
 * La classe CarteGMapActivity permet l'affichage de la carte avec les points repertoriés
 */
public class CarteGMapActivity extends MapActivity {
	
	/**
	 * MapView qui va contenir notre carte
	 */
	private MapView _carte;	
	
	/**
	 * controlleur qui permettra d'interagir avec la map
	 */
	private MapController _carteControleur;
	
	/**
	 * listes des points à relier
	 */
	private ArrayList<GeoPoint> myPolyline;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.carte_google);
		_carte = (MapView) this.findViewById(id.view_carte);
		_carte.setBuiltInZoomControls(true);
		
		Bundle bundle  = this.getIntent().getExtras();
		if(bundle!=null && bundle.containsKey("DateDebut")&& bundle.containsKey("DateFin"))
		 {
			/**
			 * on récupére les positions stocké par l'application
			 */
			PositionBDD posBDD= new PositionBDD(getApplicationContext());
			posBDD.open();
			long dDebut=this.getIntent().getLongExtra("DateDebut",0);
			long dFin =this.getIntent().getLongExtra("DateFin",0);
			ArrayList<Position> tabPos=new ArrayList<Position>(posBDD.getPosition(dDebut,dFin));
			posBDD.close();
			if(tabPos.size()>0)
		    {	
		    	/*_carte = (MapView) this.findViewById(id.view_carte);
				_carte.setBuiltInZoomControls(true);*/
			   
				List<Overlay> mapOverlays = _carte.getOverlays();
			   
				Drawable drawable = this.getResources().getDrawable(R.drawable.gmap_marker);
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
				
				PosItemizedOverlay itemizedOverlay = new PosItemizedOverlay(drawable,this);
			    //Permet de centrer la carte sur la première position
				GeoPoint point = new GeoPoint((int)(tabPos.get(0).getLatitude()*1E6),(int)(tabPos.get(0).getLongitude()*1E6));
				_carteControleur = _carte.getController();
				_carteControleur.animateTo(point);
				_carteControleur.setZoom(4);
				
				/**
				 * ajout des polyline pour relier les points
				 */
				myPolyline = new ArrayList<GeoPoint>();
				myPolyline.add(point);
				
				/*point = new GeoPoint((int)6.42*1000000,(int)48.10*1000000);
				myPolyline.add(point);
				point = new GeoPoint((int)8.42*1000000,(int)50.10*1000000);
				myPolyline.add(point);
				point = new GeoPoint((int)10.42*1000000,(int)48.10*1000000);
				myPolyline.add(point);*/
				
				for(int i=0;i<tabPos.size();i++)
				{
					point = new GeoPoint((int)(tabPos.get(i).getLatitude()*1E6),(int)(tabPos.get(i).getLongitude()*1E6));
					myPolyline.add(point);
					
					itemizedOverlay.addOverlay(tabPos.get(i));
					mapOverlays.add(itemizedOverlay);
				}
				
			    mapOverlays.add(new MapOverlay(myPolyline));  
				
                
		    }
			else
			{
				Toast.makeText(getApplicationContext(), "Aucun point ne correspond a la selection", Toast.LENGTH_LONG).show();
				finish();
			}
		 }
		 else
		 {
			Toast.makeText(getApplicationContext(), "Aucun point ne correspond a la selection", Toast.LENGTH_LONG).show();
			finish();
		 }
	}	 
	
	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}

	/**
	 * Classe qui va permettre de gerer l'affichage des tracés entre les points
	 */
	public class MapOverlay extends Overlay {

	    ArrayList<GeoPoint> route;

	    public MapOverlay(ArrayList<GeoPoint> r) {
	            route = new ArrayList<GeoPoint>();
	            for (GeoPoint p: r) {
	                    route.add(p);
	            }
	    }

	    public void draw(Canvas canvas, MapView mapv, boolean shadow) {
	            super.draw(canvas, mapv, false);

	            Paint mPaint = new Paint();
	            mPaint.setDither(true);
	            mPaint.setColor(Color.rgb(128, 136, 231));
	            mPaint.setAlpha(100);
	            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	            mPaint.setStrokeJoin(Paint.Join.ROUND);
	            mPaint.setStrokeCap(Paint.Cap.ROUND);
	            mPaint.setStrokeWidth(2);

	            Path path = new Path();

	            GeoPoint start = route.get(0);
	            for (int i = 1; i < route.size(); ++i) {
	                    Point p1 = new Point();
	                    Point p2 = new Point();

	                    Projection projection = mapv.getProjection();
	                    projection.toPixels(start, p1);
	                    projection.toPixels(route.get(i), p2);

	                    path.moveTo(p2.x, p2.y);
	                    path.lineTo(p1.x, p1.y);

	                    start = route.get(i);
	            }
	            canvas.drawPath(path, mPaint);
	    }
	}

}
