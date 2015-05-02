package egun;

import processing.core.*;

interface IUnit extends IMovable
{
	public Team getTeam();
	public void setTeam(Team team);

	public boolean isHittable(float attackX, float attackY, float attackWidth, float attackHeight);
	public boolean isDie();
	public void attacked(IUnit attacker, int damage);

	public float getWidth();
	public float getHeight();
	public void display(PApplet applet);
}
