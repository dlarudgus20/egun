package egun;

import processing.core.*;

public class Program
{
	public static void main(String[] args)
	{
		PApplet.main("egun.PMyApplet", args);
	}
	
	public static boolean isIntersect(float x1, float y1, float wid1, float hei1,
			float x2, float y2, float wid2, float hei2)
	{
		return (x1 <= x2 + wid2 &&
				y1 <= y2 + hei2 &&
				x1 + wid1 >= x2 &&
				y1 + hei1 >= y2);
	}
}
