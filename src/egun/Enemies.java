package egun;

import java.util.*;
import processing.core.*;

class Enemies extends Team
{
	private ArrayList<Enemy> enemyList_;

	private float spawnX_, spawnY_;
	private float spawnXGap_, spawnYGap_;
	private int countOfSpawn_;
	private int moveSpeedX_, moveSpeedY_;
	
	private int enemyPower_;

	public Enemies(float spawnX, float spawnY, float spawnXGap, float spawnYGap, int countOfSpawn,
		int moveSpeedX, int moveSpeedY, int enemyPower)
	{
		enemyList_ = new ArrayList<Enemy>();
		spawnX_ = spawnX;
		spawnY_ = spawnY;
		spawnXGap_ = spawnXGap;
		spawnYGap_ = spawnYGap;
		countOfSpawn_ = countOfSpawn;
		moveSpeedX_ = moveSpeedX;
		moveSpeedY_ = moveSpeedY;
		enemyPower_ = enemyPower;
	}
	
	public void spawnEnemy(PApplet applet)
	{
		for (int i = 0; i < countOfSpawn_; i++)
		{
			float x = spawnX_ + i * spawnXGap_;
			float y = spawnY_ + i * spawnYGap_;
			enemyList_.add(new Enemy(applet, x, y, enemyPower_));
		}
	}
	
	public void doEnemyAction(Base b)
	{
		for (Enemy e : enemyList_)
		{
			if (!e.tryAttack(b))
			{
				e.move(e.getCoordX() + moveSpeedX_, e.getCoordY() + moveSpeedY_);
			}
		}
	}
	
	public int tryHit(float bulletX, float bulletY)
	{
		int hit = 0;

		for (int i = 0; i < enemyList_.size(); )
		{
			Enemy e = enemyList_.get(i);
			if (e.isHitted(bulletX, bulletY))
			{
				enemyList_.remove(i);
				hit++;
			}
			else
			{
				i++;
			}
		}
		
		return hit;
	}

	public void display(PApplet applet)
	{
		for (Enemy e : enemyList_)
			e.display(applet);
	}
}