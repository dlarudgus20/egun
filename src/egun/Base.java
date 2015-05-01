package egun;

import processing.core.*;

class Base implements IUnit
{
	private float baseX, baseY, baseWidth, baseHeight;
	private float attackAreaSize;
	private int baseHp, baseMaxHp;

	private Team team = null;

	public Base(float x, float y, float wid, float hei, int maxHp, float attackAreaSz)
	{
		baseX = x;
		baseY = y;
		baseWidth = wid;
		baseHeight = hei;
		baseHp = baseMaxHp = maxHp;
		attackAreaSize = attackAreaSz;
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
	public boolean isDie()
	{
		return (baseHp <= 0);
	}

	@Override
	public boolean isHittable(float attackX, float attackY, float attackWidth, float attackHeight)
	{
		return Program.isIntersect(baseX - attackAreaSize, baseY - attackAreaSize,
				baseWidth + 2 * attackAreaSize, baseHeight + 2 * attackAreaSize,
				attackX, attackY, attackWidth, attackHeight);
	}

	@Override
	public void attacked(int damage)
	{
		baseHp = PApplet.max(baseHp - damage, 0);
	}

	public void display(PApplet applet)
	{
		applet.noStroke();

		// body
		applet.fill(0xff90ff33);
		applet.rect(baseX, baseY, baseWidth, baseHeight);
		
		// HP bar
		float gapX = baseWidth / 10;
		float gapY = PApplet.min(gapX, baseHeight / 10);
		float x = baseX + gapX;
		float y = baseY + gapY;
		float wid = baseWidth - gapX * 2;
		applet.fill(0xff000000);
		applet.rect(x, y, wid, gapY);
		applet.fill(0xffff0000);
		applet.rect(x, y, wid * ((float)baseHp / baseMaxHp), gapY);
	}
}
