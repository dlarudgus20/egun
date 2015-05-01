package egun;

import processing.core.*;

class Enemy implements IUnit, TimerListener
{
	private PImage enemyImage, maskImage;
	private int attackPower;
	private float enemyX, enemyY;

	private boolean bLive = true;
	private Team team = null;

	public Enemy(PApplet applet, float x, float y, int power)
	{
		enemyImage = applet.loadImage("enemy.png");
		maskImage = applet.loadImage("enemy_mask.png");
		enemyImage.mask(maskImage);
		// for isHittable()
		maskImage.loadPixels();

		attackPower = power;
		enemyX = x;
		enemyY = y;
	}

	@Override
	public Team getTeam()
	{
		return team;
	}
	@Override
	public void setTeam(Team t)
	{
		team = t;
	}

	public float getCoordX()
	{
		return enemyX;
	}
	public float getCoordY()
	{
		return enemyY;
	}
	
	public void move(float x, float y)
	{
		enemyX = x;
		enemyY = y;
	}

	@Override
	public boolean isHittable(float attackX, float attackY, float attackWidth, float attackHeight)
	{
		// first check - rectangle
		if (Program.isIntersect(
				enemyX, enemyY, enemyImage.width, enemyImage.height,
				attackX, attackY, attackWidth, attackHeight))
		{
			// second check - region
			final float epsilon = 1e-3f;
			int x0 = PApplet.ceil(attackX - enemyX - epsilon);
			int y0 = PApplet.ceil(attackY - enemyY - epsilon);
			for (int dy = 0; dy < (int)attackHeight; dy++)
			{
				for (int dx = 0; dx < (int)attackWidth; dx++)
				{
					if (maskImage.pixels[(x0 + dx) + enemyImage.width * (y0 + dy)] == 0xffffffff)
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}

	@Override
	public void attacked(int damage)
	{
		bLive = false;
	}
	@Override
	public boolean isDie()
	{
		return !bLive;
	}

	@Override
	public void timerTicked(int index)
	{
		if (b.isHittable(enemyX, enemyY, enemyImage.width, enemyImage.height))
		{
			PApplet.println("attacked by " + this);
			b.attacked(attackPower);
		}
	}
	
	public void display(PApplet applet)
	{
		applet.image(enemyImage, enemyX, enemyY);
	}
}
