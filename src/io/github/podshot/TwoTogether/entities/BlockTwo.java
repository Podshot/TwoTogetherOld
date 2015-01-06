package io.github.podshot.TwoTogether.entities;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import io.github.podshot.TwoTogether.api.entities.Boundable;
import io.github.podshot.TwoTogether.api.entities.Controllable;
import io.github.podshot.TwoTogether.gamestates.LevelOneState;

public class BlockTwo implements Controllable, Boundable {

	private static BlockTwo instance;
	private float y;
	private float x;

	public BlockTwo() {
		instance = this;
	}

	@Deprecated
	@Override
	public void move_up(int delta) {
		if (0 >= Math.round(this.y)) {
			return;
		}
		this.y -= 0.1f*delta;
		if (this.intersectsSomething()) {
			this.y += 0.1f*delta;
		}
	}

	@Override
	public void move_down(int delta) {
		if (560 <= Math.round(this.y)) {
			return;
		}
		this.y += 0.1f*delta;
		if (this.intersectsSomething()) {
			this.y -= 0.1f*delta;
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

	private boolean intersectsSomething() {
		boolean isIntersecting = false;
		BlockOne bo = BlockOne.getInstance();
		if (this.bounds().intersects(bo.bounds())) {
			isIntersecting = true;
		} else {
			for (Shape shape : LevelOneState.getTerrain()) {
				//Rectangle rect = (Rectangle) shape;
				isIntersecting = isIntersecting || this.bounds().intersects(shape);
			}
			//isIntersecting = true;
		}
		return isIntersecting;
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

	@Override
	public void jump() {
	}

	@Override
	public float getX() {
		return this.x;
	}

	@Override
	public float getY() {
		return this.y;
	}

	public static BlockTwo getInstance() {
		if (instance == null) {
			new BlockTwo();
		}
		return instance;
	}

	public Shape bounds() {
		return new Rectangle(this.x, this.y, this.getXBounds(), this.getYBounds());
	}

	@Override
	public float getXBounds() {
		return 10;
		//return 20;
	}

	@Override
	public float getYBounds() {
		return 20;
		//return 40;
	}

	public void setY(float btY) {
		this.y = btY;
	}

	public boolean collided() {
		boolean toReturn = false;
		toReturn = (this.intersectsSomething()
				|| 560 <= Math.round(this.y));
		return toReturn;
	}
	
	public boolean collidedOnTop() {
		boolean toReturn = false;
		Shape bt_bounds = BlockOne.getInstance().bounds();
		Shape bounds = this.bounds();
		toReturn = (bt_bounds.contains(bounds.getMinX(), (bounds.getCenterY()-((this.getYBounds()/2)+1)))
				|| bt_bounds.contains(bounds.getCenterX(), (bounds.getCenterY()-((this.getYBounds()/2)+1)))
				|| bt_bounds.contains(bounds.getMaxX(), (bounds.getCenterY()-((this.getYBounds()/2)+1))));
		for (Shape shape : LevelOneState.getTerrain()) {
			toReturn = toReturn || this.bounds().intersects(shape);
		}
		return toReturn;
	}
	public boolean collidedOnBottom() {
		boolean toReturn = false;
		Shape bt_bounds = BlockOne.getInstance().bounds();
		Shape bounds = this.bounds();
		toReturn = (bt_bounds.contains(bounds.getMinX(), (bounds.getCenterY()+((this.getYBounds()/2)+1)))
				|| bt_bounds.contains(bounds.getCenterX(), (bounds.getCenterY()+((this.getYBounds()/2)+1)))
				|| bt_bounds.contains(bounds.getMaxX(), (bounds.getCenterY()+((this.getYBounds()/2)+1))));
		return toReturn;
	}
}
