package egun;

import java.util.*;

import processing.core.*;

interface ItemCreator
{
	public IUnit create(Timer timer, float x, float y);
}
interface ItemCreator2 extends ItemCreator
{
	public void cancel();
	public void display(PApplet applet, float x, float y);
}

class Shop
{
	private static final float displayGap_ = 5;

	private static class Item
	{
		IUnit unit;
		int price;
		ItemCreator creator;
	}
	private ArrayList<Item> itemList_ = new ArrayList<Item>();

	private int money_ = 99990;
	private float x_, y_;

	public Shop(float x, float y)
	{
		x_ = x;
		y_ = y;
	}

	public void addItem(IUnit unit, int price, ItemCreator creator)
	{
		int sz = itemList_.size();
		if (sz > 0)
		{
			Item last = itemList_.get(sz - 1);
			unit.move(last.unit.getCoordX() + displayGap_, y_);
		}
		else
		{
			unit.move(x_, y_);
		}
		Item item = new Item();
		item.unit = unit;
		item.price = price;
		item.creator = creator;
		itemList_.add(item);
	}

	public void earnMoney(int point)
	{
		money_ += point;
	}

	public ItemCreator2 onClick(float x, float y)
	{
		for (final Item item : itemList_)
		{
			if (item.unit.getCoordX() <= x && x <= item.unit.getCoordX() + item.unit.getWidth()
				&& item.unit.getCoordY() <= y && y <= item.unit.getCoordY() + item.unit.getHeight())
			{
				if (item.price <= money_)
				{
					final float oldX = item.unit.getCoordX();
					final float oldY = item.unit.getCoordY();
					return new ItemCreator2() {
						@Override
						public IUnit create(Timer timer, float x, float y)
						{
							money_ -= item.price;
							IUnit ret = item.creator.create(timer, x, y);
							cancel();
							return ret;
						}
						@Override
						public void cancel()
						{
							item.unit.move(oldX, oldY);
						}
						@Override
						public void display(PApplet applet, float x, float y)
						{
							item.unit.move(x, y);
							// will be displayed by Shop.display()
						}
					};
				}
				else
				{
					PApplet.println("money is not enough");
					return null;
				}
			}
		}
		return null;
	}

	public void display(PApplet applet)
	{
		applet.fill(0xff000000);
		applet.textAlign(PApplet.LEFT, PApplet.TOP);
		for (Item item : itemList_)
		{
			item.unit.display(applet);
			applet.text("$" + item.price, item.unit.getCoordX(), item.unit.getCoordY() + item.unit.getHeight());
		}
		applet.textAlign(PApplet.LEFT, PApplet.BOTTOM);
		applet.text("money: $" + money_, x_, y_);
	}
}
