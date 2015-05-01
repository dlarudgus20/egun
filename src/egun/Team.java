package egun;

import java.util.*;

class Team
{
	private static ArrayList<Team> teamList = new ArrayList<Team>();

	public static void ffff()
	{

	}

	private ArrayList<IUnit> unitList = new ArrayList<IUnit>();

	public Team()
	{
		teamList.add(this);
	}

	public void addUnit(IUnit unit)
	{
		unitList.add(unit);
		unit.setTeam(this);
	}
}
