package utbm.tx52.suiviGPS;


import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

/**
 * Classe qui va gerer les points sur la carte
 */
public class PosItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> _tabMarqueur=new ArrayList<OverlayItem>();
	private Context _contexte;
	
	public PosItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public PosItemizedOverlay(Drawable defaultMarker,Context c) {
		this(defaultMarker);
		this._contexte=c;
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		return _tabMarqueur.get(i);
	}

	@Override
	public int size() {
		return _tabMarqueur.size();
	}
	
	protected boolean onTap(int index)
	{
		OverlayItem iPosition = _tabMarqueur.get(index);
		AlertDialog.Builder information = new AlertDialog.Builder(_contexte);
		
		information.setTitle(iPosition.getTitle());
		information.setMessage(iPosition.getSnippet());
		information.show();
		
		return true;
		
	}
	
	public void addOverlay(Position p)
	{
		GeoPoint geoPosition = new GeoPoint((int)(p.getLatitude()*1E6),(int)(p.getLongitude()*1E6));
		OverlayItem overI=new OverlayItem(geoPosition,p.getDateToString(),p.getInformation());
		_tabMarqueur.add(overI);
		this.populate();
	}

	public void addOverlay(OverlayItem o)
	{
		_tabMarqueur.add(o);
		this.populate();
	}
	
	@Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow)
    {
        if(!shadow)
        {
            super.draw(canvas, mapView, false);
        }
    }
}