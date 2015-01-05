package io.github.podshot.TwoTogether.api.entities;

public interface Controllable {

	public void move_up(int delta);
	
	public void move_down(int delta);
	
	public void move_left(int delta);
	
	public void move_right(int delta);
	
	public void jump();

	float getX();

	float getY();
}
