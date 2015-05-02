package egun;

import java.util.*;
import processing.core.*;

interface TimerListener
{
	public void timerTicked(ITimer timer, int index);
}

interface ITimer
{
	public void update(float timespan);
	public int addTimer(int freq, boolean enabled, TimerListener listener);
	public boolean removeTimer(int index);
	public boolean updateTimer(int index, int freq);
	public boolean enableTimer(int index, boolean enabled, boolean reset);
}

class Timer implements ITimer
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

	@Override
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
					r.listener.timerTicked(this, r.index);
				}
			}
		}
	}
	
	@Override
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
	
	@Override
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
	
	@Override
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
	
	@Override
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

class FakeTimer implements ITimer
{
	private int indexCounter = 0;

	public void update(float timespan) { }
	public int addTimer(int freq, boolean enabled, TimerListener listener) { return indexCounter++; }
	public boolean removeTimer(int index) { return true; }
	public boolean updateTimer(int index, int freq) { return true; }
	public boolean enableTimer(int index, boolean enabled, boolean reset) { return true; }
}
