/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package test;

import warped.application.entities.WarpedEntitie;
import warped.application.state.WarpedState;
import warped.graphics.sprite.CharacterSprite;
import warped.graphics.sprite.CharacterSprite.AnimationType;
import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.math.vectors.VectorD;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class ExampleEntitie extends WarpedEntitie {


	private double speed = 61.621;
	CharacterSprite characterSprite;
	VectorD velocity = new VectorD(speed, speed);
	
	public ExampleEntitie(CharacterSprite sprite) {
		setSprite(sprite);
		characterSprite = sprite;
	}
	
	@Override
	protected void mouseEntered() {
		//Console.ln("TestEntitie -> mouseEntered()");
	}

	@Override
	protected void mouseExited() {}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {WarpedState.notify.note("Mouse wheel - change speed");}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {
		if(mouseEvent.getWheelRotation() > 0) speed += 0.1;
		else speed -= 0.1;
	}

	@Override
	public void updateObject() {
		if(characterSprite.getAnimation() == AnimationType.MOVE_DOWN) velocity.set(0.0, speed * 1.5);
		if(characterSprite.getAnimation() == AnimationType.MOVE_DOWN_LEFT) velocity.set(-speed, speed);
		if(characterSprite.getAnimation() == AnimationType.MOVE_DOWN_RIGHT) velocity.set(speed, speed);
		if(characterSprite.getAnimation() == AnimationType.MOVE_UP) velocity.set(0.0, -speed * 1.5);
		if(characterSprite.getAnimation() == AnimationType.MOVE_UP_LEFT) velocity.set(-speed, -speed);
		if(characterSprite.getAnimation() == AnimationType.MOVE_UP_RIGHT) velocity.set(speed, -speed);
		if(characterSprite.getAnimation() == AnimationType.MOVE_LEFT) velocity.set(-speed * 1.5, 0.0);
		if(characterSprite.getAnimation() == AnimationType.MOVE_RIGHT) velocity.set(speed, 0.0);
		
		if(x() < -getWidth()) setPosition(1920, y());
		if(x() > 1920) setPosition(- getWidth(), y());
		if(y() < -getHeight()) setPosition(x(), 1080);
		if(y() > 1080) setPosition(x(), -getHeight());
	}

	@Override
	public void updateMid() {
		int rnd = UtilsMath.getDn(8);
		if(rnd == 1)characterSprite.setAnimation(AnimationType.MOVE_DOWN_LEFT);
		if(rnd == 2)characterSprite.setAnimation(AnimationType.MOVE_DOWN);
		if(rnd == 3)characterSprite.setAnimation(AnimationType.MOVE_DOWN_RIGHT);
		if(rnd == 4)characterSprite.setAnimation(AnimationType.MOVE_UP_LEFT);
		if(rnd == 5)characterSprite.setAnimation(AnimationType.MOVE_UP);
		if(rnd == 6)characterSprite.setAnimation(AnimationType.MOVE_UP_RIGHT);
		if(rnd == 7)characterSprite.setAnimation(AnimationType.MOVE_LEFT);
		if(rnd == 8)characterSprite.setAnimation(AnimationType.MOVE_RIGHT);
	}

	@Override
	protected void updatePosition(double deltaTime) {
		move(velocity.x() * deltaTime, velocity.y() * deltaTime);
		
	}
}
