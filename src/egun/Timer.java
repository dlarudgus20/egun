package egun;

import java.util.*;
import processing.core.*;

interface TimerListener
{
	public void timerTicked(int index);
}

class Timer
{
	private float deciTime = 0;
	private int nowTime = 0;

	private int indexCounter = 0;
	
	private static class Record
	{
		int index;
		TimerListener listener;
		int freq, fireTime;
		boolean enabled;
	}
	private ArrayList<Record> recordPool = new ArrayList<Record>();
	private ArrayList<Record> recordList = new ArrayList<Record>();
	
	public void update(float timespan)
	{
		if (recordList.size() != 0)
		{
			deciTime += timespan;

			int dt = (int)deciTime;
			nowTime += dt;
			deciTime -= dt;

			for (int i = 0; i < recordList.size(); i++)
			{
				Record r = recordList.get(i);
				if (r.enabled && r.fireTime <= nowTime)
				{
					r.fireTime += r.freq;
					r.listener.timerTicked(r.index);
				}
			}
		}
	}
	
	public int addTimer(int freq, boolean enabled, TimerListener listener)
	{
		Record r;

		int poolsz = recordPool.size();
		if (poolsz == 0)
		{
			r = new Record();
		}
		else
		{
			r = recordPool.get(poolsz - 1);
			recordPool.remove(poolsz - 1);
		}

		r.index = indexCounter++;
		r.listener = listener;
		r.freq = freq;

		if (enabled)
			r.fireTime = nowTime + freq;
		else
			r.fireTime = freq;
		r.enabled = enabled;

		recordList.add(recordList.size(), r);
		return r.index;
	}
	
	public boolean removeTimer(int index)
	{
		for (int i = 0; i < recordList.size(); i++)
		{
			if (recordList.get(i).index == index)
			{
				recordPool.add(recordList.remove(i));
				return true;
			}
		}
		
		return false;
	}
	
	public boolean updateTimer(int index, int freq)
	{
		for (int i = 0; i < recordList.size(); i++)
		{
			Record r = recordList.get(i);
			if (r.index == index)
			{
				r.fireTime = PApplet.max(r.fireTime - r.freq + freq, nowTime);
				r.freq = freq;
				return true;
			}
		}
		
		return false;
	}
	
	public boolean enableTimer(int index, boolean enabled, boolean reset)
	{
		for (int i = 0; i < recordList.size(); i++)
		{
			Record r = recordList.get(i);
			if (r.index == index)
			{
				if (r.enabled != enabled)
				{
					r.enabled = enabled;
					if (enabled)
					{
						// enable timer
						// reset parameter is ignored
						r.fireTime += nowTime;
					}
					else
					{
						// disable timer
						if (!reset)
							r.fireTime -= nowTime;
						else
							r.fireTime = r.freq;
					}
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		
		return false;
	}
}
