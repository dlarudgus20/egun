package egun;

import processing.core.*;

class Gun
{
	private Crosshair cross = new Crosshair(0, 12, 1, 0, 50, 44, 11);
	private Ball track = null;

	private boolean hideCross_ = false;

	public class FireInfo
	{
		float x, y;
		boolean fired;
	}
	public FireInfo fire(PApplet applet, float x, float y)
	{
		FireInfo ret = new FireInfo();

		if (cross.fire())
		{
			ret.fired = true;

			float fireError = 0.5f * cross.getRejection();

			ret.x = x + applet.random(-fireError, fireError);
			ret.y = y + applet.random(-fireError, fireError);
	
			if (track == null)
			{
				track = new Ball(ret.x, ret.y, 5, 25,
					0xffff0000, 0.75f, 1);
			}
			else
			{
				if (!track.createAfterImage(ret.x, ret.y))
				{
					track.deleteAfterImage();
					track.createAfterImage(ret.x, ret.y);
				}
			}
		}
		else
		{
			ret.fired = false;
		}
		
		return ret;
	}
	
	public void releaseCrosshair()
	{
		cross.release();
	}
	
	public boolean deleteCrosshairAfterImage()
	{
		if (track.deleteAfterImage())
		{
			return true;
		}
		else
		{
			track = null;
			return false;
		}
	}

	public void hideCrosshair(boolean hide)
	{
		hideCross_ = hide;
	}

	public void display(PApplet applet, float x, float y)
	{
		if (track != null)
			track.display(applet);

		if (!hideCross_)
			cross.display(applet, x, y);
	}
}
