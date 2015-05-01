package egun;

import processing.core.*;

class Ball
{
	private float mainRadius;
	private int prevMaxCount;
	
	private int ballColor;
	private float alphaFactor, radiusFactor;
	
	private float[] prevX, prevY;
	private int prevCount;
	
	private float ballX, ballY;
	
	public Ball(float x, float y, float radius, int prev,
		int c, float aFactor, float rFactor)
	{
		mainRadius = radius;
		prevMaxCount = prev;
		
		ballColor = c;
		alphaFactor = aFactor;
		radiusFactor = rFactor;
		
		prevX = new float[prev];
		prevY = new float[prev];
		prevCount = 0;
		
		ballX = x;
		ballY = y;
	}
	
	public boolean createAfterImage(float new_x, float new_y)
	{
		if (prevCount < prevMaxCount)
		{
			prevX[prevCount] = ballX;
			prevY[prevCount] = ballY;
			prevCount++;
			
			ballX = new_x;
			ballY = new_y;
			
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean deleteAfterImage()
	{
		if (prevCount > 0)
		{
			prevCount--;
			for (int i = 0; i < prevCount; i++)
			{
				prevX[i] = prevX[i + 1];
				prevY[i] = prevY[i + 1];
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}

	public void display(PApplet applet)
	{
		applet.noStroke();

		float alpha = applet.alpha(ballColor);
		float radius = mainRadius;

		applet.fill(applet.red(ballColor), applet.green(ballColor), applet.blue(ballColor), alpha);
		applet.ellipse(ballX, ballY, radius, radius);
	
		for (int i = prevCount - 1; i >= 0; i--)
		{
			alpha *= alphaFactor;
			radius *= radiusFactor;
			applet.fill(applet.red(ballColor), applet.green(ballColor), applet.blue(ballColor), alpha);
			applet.ellipse(prevX[i], prevY[i], radius, radius);
		}
	}
}
