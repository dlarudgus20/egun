package egun;

import processing.core.*;
import processing.event.*;

public class PMyApplet extends PApplet implements TimerListener
{
	private static final long serialVersionUID = -1176777433010750858L;

	private int afterImageDelTimer_ = -1;
	private static final int afterImageDelFreq_ = 333;

	private Timer timer_;
	private FakeTimer fakeTimer_;

	private Team playerTeam_;
	private Enemies enemies_;

	private static final int gunPower_ = 100;
	private Gun gun_;
	private Base base_;
	private Shop shop_;

	private int score_ = 0;
	private ItemCreator2 buyingItem_ = null;

	@Override
	public void setup()
	{
		size(600, 435);
		Enemy.initImage(this);

		timer_ = new Timer();
		afterImageDelTimer_ = timer_.addTimer(afterImageDelFreq_, false, this);

		playerTeam_ = new Team();
		enemies_ = new Enemies(timer_, 10, 10, 0, 90, 4, 10, 0);

		gun_ = new Gun();
		base_ = new Base(430, 10, 160, 380, 1000, 10);
		playerTeam_.addUnit(base_);

		shop_ = new Shop(10, 380);
		fakeTimer_ = new FakeTimer();
		shop_.addItem(new Boomerang(fakeTimer_, 0, 0), 300, new ItemCreator() {
			@Override
			public IUnit create(Timer timer, float x, float y)
			{
				return new Boomerang(timer, x, y);
			}
		});
	}

	@Override
	public void draw()
	{
		background(180);

		if (mousePressed && buyingItem_ == null)
		{
			Gun.FireInfo fi = gun_.fire(this, mouseX, mouseY);
			if (fi.fired)
			{
				timer_.enableTimer(afterImageDelTimer_, true, true);

				// gun is fired
				int hit = playerTeam_.tryAttackOppsites(
						null, gunPower_, Integer.MAX_VALUE,
						fi.x, fi.y, 0, 0);
				if (hit > 0)
				{
					int point = hit * 20 + (hit - 1) * (hit - 1);
					score_ += point;
					shop_.earnMoney(point);
					println(hit + " killed!! [score:" + score_ + "]");
				}
			}
		}
		else
		{
			gun_.releaseCrosshair();
		}

		timer_.update(1000.0f / frameRate);

		if (base_.isDie())
		{
			println(">>> you lose <<<");
			noLoop();
		}

		shop_.display(this);
		playerTeam_.display(this);
		enemies_.display(this);

		if (buyingItem_ == null)
			gun_.display(this, mouseX, mouseY);
		else
			buyingItem_.display(this, mouseX, mouseY);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getButton() == PApplet.LEFT)
		{
			if (buyingItem_ != null)
			{
				IUnit unit = buyingItem_.create(timer_, e.getX(), e.getY());
				playerTeam_.addUnit(unit);
				buyingItem_ = null;
			}
			else
			{
				ItemCreator2 buyingItem = shop_.onClick(e.getX(), e.getY());
				if (buyingItem != null)
				{
					buyingItem_ = buyingItem;
				}
			}
		}
		else if (e.getButton() == PApplet.RIGHT)
		{
			if (buyingItem_ != null)
			{
				buyingItem_.cancel();
				buyingItem_ = null;
			}
		}
	}

	@Override
	public void timerTicked(ITimer timer, int index)
	{
		if (index == afterImageDelTimer_)
		{
			if (!gun_.deleteCrosshairAfterImage())
			{
				timer_.enableTimer(afterImageDelTimer_, false, true);
			}
		}
	}
}
