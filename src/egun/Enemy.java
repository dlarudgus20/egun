package egun;

import processing.core.*;

class Enemy implements IUnit, TimerListener
{
	private static PImage enemyImage, maskImage;

	public static void initImage(PApplet applet)
	{
		enemyImage = applet.loadImage("enemy.png");
		maskImage = applet.loadImage("enemy_mask.png");
		enemyImage.mask(maskImage);
		// for isHittable()
		maskImage.loadPixels();
	}

	private static final int attackPower = 1;

	private int enemyActionTimer_ = -1;
	private static final int enemyActionFreq_ = 500;

	private ITimer timer_;
	private float enemyX, enemyY;

	private float moveSpeedX_, moveSpeedY_;

	private boolean bLive = true;
	private Team team = null;

	public Enemy(ITimer timer, float x, float y, int moveSpeedX, int moveSpeedY)
	{
		timer_ = timer;
		enemyActionTimer_ = timer_.addTimer(enemyActionFreq_, true, this);

		enemyX = x;
		enemyY = y;
		moveSpeedX_ = moveSpeedX;
		moveSpeedY_ = moveSpeedY;
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

	@Override
	public float getWidth() { return enemyImage.width; }
	@Override
	public float getHeight() { return enemyImage.height; }
	@Override
	public float getCoordX() { return enemyX; }
	@Override
	public float getCoordY() { return enemyY; }
	@Override
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
			int hei = PApplet.floor(attackHeight + epsilon);
			int wid = PApplet.floor(attackWidth + epsilon);
			for (int dy = 0; dy <= hei; dy++)
			{
				for (int dx = 0; dx <= wid; dx++)
				{
					int idx = (x0 + dx) + enemyImage.width * (y0 + dy);
					if (idx < maskImage.pixels.length && maskImage.pixels[idx] == 0xffffffff)
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}

	@Override
	public void attacked(IUnit attacker, int damage)
	{
		bLive = false;
		if (isDie())
		{
			timer_.removeTimer(enemyActionTimer_);
		}
	}
	@Override
	public boolean isDie()
	{
		return !bLive;
	}
	
	@Override
	public void display(PApplet applet)
	{
		applet.image(enemyImage, enemyX, enemyY);
	}

	@Override
	public void timerTicked(ITimer timer, int index)
	{
		if (index == enemyActionTimer_)
		{
			int hit = team.tryAttackOppsites(this, attackPower, 2,
					enemyX, enemyY, enemyImage.width, enemyImage.height);
			if (hit == 0)
			{
				enemyX += moveSpeedX_;
				enemyY += moveSpeedY_;
			}
			else
			{
				PApplet.println("aaa");
			}
		}
	}
}
