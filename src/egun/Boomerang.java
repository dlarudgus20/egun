package egun;

import processing.core.*;

public class Boomerang implements IUnit, TimerListener
{
	private static final float unitSize_ = 20;
	private static final int unitColor_ = 0xffeee314;
	private static final int decoColor_ = 0x80314ceb;
	private static final int attackPower_ = 10;
	private static final int attackRange_ = 60;
	private static final int attackMotionSpeed_ = 10;

	private static final float maxHp_ = 100;

	private int attackTimer_ = -1;
	private static final int attackFreq_ = 2000;
	private int attackMotionTimer_ = -1;
	private static final int attackMotionFreq_ = 75;

	private ITimer timer_;
	private Team team_ = null;
	private float x_, y_;
	private float hp_;

	private static final int attackBallRadius_ = 3;
	private Ball attackBall_= new Ball(0, 0, 10, attackBallRadius_, 0xf0ffff00, 0.5f, 1);
	private int ballMovedSize_ = 0;

	private static enum AttackFlag { ATT_FORWARD, ATT_BACKWARD, NO }
	private AttackFlag attackingFlag_ = AttackFlag.NO;

	public Boomerang(ITimer timer, float x, float y)
	{
		timer_ = timer;
		attackTimer_ = timer_.addTimer(attackFreq_, true, this);
		attackMotionTimer_ = timer_.addTimer(attackMotionFreq_, false, this);

		x_ = x;
		y_ = y;
		hp_= maxHp_;
	}

	@Override
	public Team getTeam()
	{
		return team_;
	}
	@Override
	public void setTeam(Team team)
	{
		team_ = team;
	}

	@Override
	public float getWidth() { return unitSize_; }
	@Override
	public float getHeight() { return unitSize_; }
	@Override
	public float getCoordX() { return x_; }
	@Override
	public float getCoordY() { return y_; }
	@Override
	public void move(float x, float y)
	{
		x_ = x;
		y_ = y;
	}

	@Override
	public boolean isHittable(float attackX, float attackY, float attackWidth, float attackHeight)
	{
		return Program.isIntersect(
				x_, y_, unitSize_, unitSize_,
				attackX, attackY, attackWidth, attackHeight);
	}

	@Override
	public boolean isDie()
	{
		return (hp_ <= 0);
	}

	@Override
	public void attacked(IUnit attacker, int damage)
	{
		hp_ = PApplet.max(hp_ - damage, 0);
		if (isDie())
		{
			timer_.removeTimer(attackTimer_);
			timer_.removeTimer(attackMotionTimer_);
		}
	}

	@Override
	public void display(PApplet applet)
	{
		applet.noStroke();
		applet.fill(unitColor_);
		applet.rect(x_, y_, unitSize_, unitSize_);
		applet.fill(decoColor_);
		applet.ellipseMode(PApplet.CORNER);
		applet.ellipse(x_ + 1, y_ + 1, unitSize_ - 2, unitSize_ - 2);

		if (attackingFlag_ != AttackFlag.NO)
			attackBall_.display(applet);
	}

	private float getFireX() { return x_; }
	private float getFireY() { return y_; }
	@Override
	public void timerTicked(ITimer timer, int index)
	{
		if (index == attackTimer_)
		{
			attackBall_.moveWithoutAfterImage(getFireX(), getFireY());
			attackingFlag_ = AttackFlag.ATT_FORWARD;
			ballMovedSize_ = 0;
			timer_.enableTimer(attackMotionTimer_, true, true);
		}
		else if (index == attackMotionTimer_)
		{
			if (attackingFlag_ == AttackFlag.ATT_FORWARD)
			{
				ballMovedSize_ += attackMotionSpeed_;
				float new_x = getFireX() - ballMovedSize_;

				team_.tryAttackOppsites(this, attackPower_, 1,
						new_x, getFireY(),
						ballMovedSize_, attackBallRadius_ * 2);

				attackBall_.createAfterImage(new_x, getFireY());

				if (ballMovedSize_ >= attackRange_)
					attackingFlag_ = AttackFlag.ATT_BACKWARD;
			}
			else if (attackingFlag_ == AttackFlag.ATT_BACKWARD)
			{
				ballMovedSize_ -= attackMotionSpeed_;
				float new_x = getFireX() - ballMovedSize_;

				team_.tryAttackOppsites(this, attackPower_, 2,
						attackBall_.getCoordX(), getFireY(),
						new_x - attackBall_.getCoordX(), attackBallRadius_ * 2);

				attackBall_.createAfterImage(new_x, getFireY());

				if (ballMovedSize_ <= 0)
					attackingFlag_ = AttackFlag.NO;
			}
			else if (attackingFlag_ == AttackFlag.NO)
			{
				// delete remain afterimages
				if (!attackBall_.deleteAfterImage())
				{
					timer_.enableTimer(attackMotionTimer_, false, true);
				}
			}
		}
	}
}
