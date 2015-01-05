package io.github.podshot.TwoTogether.entities;

import io.github.podshot.TwoTogether.Game;
import io.github.podshot.TwoTogether.api.entities.Boundable;
import io.github.podshot.TwoTogether.api.entities.Controllable;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class BlockOne implements Controllable, Boundable {

	private static BlockOne instance;
	private float x;
	private float y;

	public BlockOne() {
		instance = this;
		x = 100;
		y = 100;
	}

	@Deprecated
	@Override
	public void move_up(int delta) {
		if (0 >= Math.round(this.y)) {
			return;
		}
		this.y = this.y - 0.1f;
		if (this.intersectsSomething()) {
			this.y = this.y + 0.1f;
		}
	}

	//@SuppressWarnings("unused")
	private boolean intersectsSomething() {
		boolean isIntersecting = false;
		BlockTwo bt = BlockTwo.getInstance();
		if (this.bounds().intersects(bt.bounds())) {
			isIntersecting = true;
		} else {
			for (Shape shape : Game.getTerrain()) {
				isIntersecting = (isIntersecting || this.bounds().intersects(shape));
			}
			//isIntersecting = true;
		}
		return isIntersecting;
	}

	@Override
	public void move_down(int delta) {
		if (580 <= Math.round(this.y)) {
			return;
		}
		this.y = this.y + 0.1f;
		if (this.intersectsSomething()) {
			this.y = this.y - 0.1f;
		}
	}

	@Override
	public void move_left(int delta) {
		if (0 >= Math.round(this.x)) {
			return;
		}
		this.x -= 0.1f*delta;
		if (this.intersectsSomething()) {
			this.x += 0.1f*delta;
		}
	}

	@Override
	public void move_right(int delta) {
		if (790 <= Math.round(this.x)) {
			return;
		}
		this.x += 0.1f*delta;
		if (this.intersectsSomething()) {
			this.x -= 0.1f*delta;
		}
	}

	public static BlockOne getInstance() {
		if (instance == null) {
			new BlockOne();
		}
		return instance;
	}

	@Override
	public float getX() {
		return this.x;
	}

	@Override
	public float getY() {
		return this.y;
	}

	@Override
	public void jump() {

	}

	@Override
	public float getXBounds() {
		return 10;
		//return 20;
	}

	public Shape bounds() {
		return new Rectangle(this.x, this.y, this.getXBounds(), this.getYBounds());
	}

	@Override
	public float getYBounds() {
		return 10;
		//return 20;
	}

	public void setY(float boY) {
		this.y = boY;
	}

	public boolean collided() {
		boolean toReturn = false;
		toReturn = (this.intersectsSomething()
				|| 580 <= Math.round(this.y));
		return toReturn;
	}

	public boolean collidedOnTop() {
		boolean toReturn = false;
		Shape bt_bounds = BlockTwo.getInstance().bounds();
		Shape bounds = this.bounds();
		toReturn = (bt_bounds.contains(bounds.getMinX(), (bounds.getCenterY()-this.getYBounds()))
				|| bt_bounds.contains(bounds.getCenterX(), (bounds.getCenterY()-this.getYBounds()))
				|| bt_bounds.contains(bounds.getMaxX(), (bounds.getCenterY()-this.getYBounds())));
		for (Shape shape : Game.getTerrain()) {
			toReturn = toReturn || this.bounds().intersects(shape);
		}
		return toReturn;
	}
	public boolean collidedOnBottom() {
		boolean toReturn = false;
		Shape bt_bounds = BlockTwo.getInstance().bounds();
		Shape bounds = this.bounds();
		toReturn = (bt_bounds.contains(bounds.getMinX(), (bounds.getCenterY()+this.getYBounds()))
				|| bt_bounds.contains(bounds.getCenterX(), (bounds.getCenterY()+this.getYBounds()))
				|| bt_bounds.contains(bounds.getMaxX(), (bounds.getCenterY()+this.getYBounds())));
		//System.out.println("Bottom: "+test_bool);
		return toReturn;
	}
}
