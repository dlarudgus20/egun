package egun;

import processing.core.*;

class Crosshair
{
	private float crossGap, crossLength;
	private float maxRejection;

	private float rejection;
	private float incOfReject;
	private float decOfReject;
	private float frame;

	public Crosshair(float shr, float sha, float shb, float shf,
		float maxReject, float gap, float len)
	{
		crossGap = gap;
		crossLength = len;
		maxRejection = maxReject;

		rejection = shr;
		incOfReject = sha;
		decOfReject = shb;
		frame = shf;
	}
	
	public float getRejection()
	{
		return rejection;
	}
	
	public boolean fire()
	{
		if (frame==0)
		{
			rejection = PApplet.min(rejection + incOfReject, maxRejection);
			frame += 8;
			return true;
		}
		else
		{
			rejection -= decOfReject;
			frame -= 1;
			return false;
		}
	}

	public void release()
	{
		frame = 0;
		if (rejection > 0)
		{
			rejection -= 1;
		}
	}

	public void display(PApplet applet, float x, float y)
	{
		applet.stroke(0);
		applet.line(x - crossLength - rejection, y, x - crossGap - rejection, y);
		applet.line(x + crossLength + rejection, y, x + crossGap + rejection, y);
		applet.line(x, y - crossLength - rejection, x, y - crossGap - rejection);
		applet.line(x, y + crossLength + rejection, x, y + crossGap + rejection);
	}
}
