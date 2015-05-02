package egun;

import java.util.*;

import processing.core.PApplet;

class Team
{
	private static ArrayList<Team> teamList_ = new ArrayList<Team>();

	protected ArrayList<IUnit> unitList_ = new ArrayList<IUnit>();

	public Team()
	{
		teamList_.add(this);
	}

	public void closeTeam()
	{
		teamList_.remove(this);
	}

	public final List<IUnit> getUnitList()
	{
		return unitList_;
	}

	public void addUnit(IUnit unit)
	{
		unitList_.add(unit);
		unit.setTeam(this);
	}

	public int tryAttackOppsites(IUnit attacker, int damage, int maxHit,
			float attackX, float attackY, float attackWidth, float attackHeight)
	{
		int hit = 0;
		for (Team team : teamList_)
		{
			if (team == this)
				continue;

			for (int i = 0; i < team.unitList_.size(); i++)
			{
				IUnit unit = team.unitList_.get(i);
				if (unit.isHittable(attackX, attackY, attackWidth, attackHeight))
				{
					unit.attacked(attacker, damage);
					if (unit.isDie())
					{
						team.unitList_.remove(i);
						i--;
					}
					if (++hit >= maxHit)
					{
						return hit;
					}
				}
			}
		}
		return hit;
	}

	public void display(PApplet applet)
	{
		for (IUnit unit : unitList_)
			unit.display(applet);
	}
}
