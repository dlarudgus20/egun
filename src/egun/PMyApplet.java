package egun;

import processing.core.*;

public class PMyApplet extends PApplet implements TimerListener
{
	private static final long serialVersionUID = -1176777433010750858L;

	private int AfterImageDelTimer = -1;
	private static final int AfterImageDelFreq = 333;
	private int SpawnEnemyTimer = -1;
	private static final int SpawnEnemyFreq = 2000;
	private int EnemyActionTimer = -1;
	private static final int EnemyActionFreq = 500;

	private static final int EnemyPower = 5;

	private Gun gun;

	private Base base;
	private Enemies enemies;

	private Timer timer;

	private int score = 0;

	@Override
	public void setup()
	{
		size(600, 400);

		gun = new Gun();
		base = new Base(430, 10, 160, 380, 100, 10);
		enemies = new Enemies(10, 10, 0, 90, 4, 10, 0, EnemyPower);
		timer = new Timer();

		SpawnEnemyTimer = timer.addTimer(SpawnEnemyFreq, true, this);
		EnemyActionTimer = timer.addTimer(EnemyActionFreq, true, this);
		AfterImageDelTimer = timer.addTimer(AfterImageDelFreq, false, this);
	}

	@Override
	public void draw()
	{
		background(180);

		if (mousePressed)
		{
			Gun.FireInfo fi = gun.fire(this, mouseX, mouseY);
			if (fi.fired)
			{
				timer.enableTimer(AfterImageDelTimer, true, true);
				
				// gun is fired
				int hit = enemies.tryHit(fi.x, fi.y);
				if (hit > 0)
				{
					score += hit * 100 + (hit - 1) * (hit - 1);
					println(hit + " killed!! [score:" + score + "]");
				}
			}
		}
		else
		{
			gun.releaseCrosshair();
		}

		timer.update(1000.0f / frameRate);

		base.display(this);
		enemies.display(this);

		gun.display(this, mouseX, mouseY);
	}

	@Override
	public void timerTicked(int index)
	{
		if (index == AfterImageDelTimer)
		{
			if (!gun.deleteCrosshairAfterImage())
			{
				timer.enableTimer(AfterImageDelTimer, false, true);
			}
		}
		else if (index == SpawnEnemyTimer)
		{
			enemies.spawnEnemy(this);
		}
		else if (index == EnemyActionTimer)
		{
			enemies.doEnemyAction(base);
		}
	}
}
