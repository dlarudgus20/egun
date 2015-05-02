package egun;

class Enemies extends Team implements TimerListener
{
	private int spawnEnemyTimer_ = -1;
	private static final int spawnEnemyFreq_ = 2000;

	private ITimer timer_;
	private float spawnX_, spawnY_;
	private float spawnXGap_, spawnYGap_;
	private int countOfSpawn_;
	private int moveSpeedX_, moveSpeedY_;

	public Enemies(ITimer timer,
			float spawnX, float spawnY, float spawnXGap, float spawnYGap, int countOfSpawn,
			int moveSpeedX, int moveSpeedY)
	{
		timer_ = timer;
		spawnEnemyTimer_ = timer_.addTimer(spawnEnemyFreq_, true, this);

		spawnX_ = spawnX;
		spawnY_ = spawnY;
		spawnXGap_ = spawnXGap;
		spawnYGap_ = spawnYGap;
		countOfSpawn_ = countOfSpawn;
		moveSpeedX_ = moveSpeedX;
		moveSpeedY_ = moveSpeedY;
	}
	
	@Override
	public void timerTicked(ITimer timer, int index)
	{
		if (index == spawnEnemyTimer_)
		{
			for (int i = 0; i < countOfSpawn_; i++)
			{
				float x = spawnX_ + i * spawnXGap_;
				float y = spawnY_ + i * spawnYGap_;
				addUnit(new Enemy(timer_, x, y, moveSpeedX_, moveSpeedY_));
			}
		}
	}
}
