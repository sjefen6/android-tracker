package org.goldclone.android.tracker;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends ItemizedOverlay
{

    private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
    
    public MyItemizedOverlay(Drawable defaultMarker)
    {
        super(boundCenterBottom(defaultMarker));
    }

    @Override
    protected OverlayItem createItem(int i)
    {
        return overlays.get(i);
    }

    @Override
    public int size()
    {
        return overlays.size();
    }
    
    public void addOverlay(OverlayItem overlay)
    {
        overlays.add(overlay);
        populate();
    }
}