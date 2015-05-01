package egun;

interface IUnit
{
	public Team getTeam();
	public void setTeam(Team team);

	public boolean isHittable(float attackX, float attackY, float attackWidth, float attackHeight);
	public boolean isDie();
	public void attacked(int damage);
}
