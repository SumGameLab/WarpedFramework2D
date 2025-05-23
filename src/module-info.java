/**
 * 
 */
/**
 * @author SomeKid
 *
 */
module WarpedFramework2D {
	requires transitive java.desktop;
	requires transitive javafx.swing;
	requires transitive javafx.media;
	requires transitive javafx.base;
	requires transitive javafx.controls;
	requires transitive javafx.graphics;	
	
	exports warped;
	exports warped.application.actionWrappers;
	exports warped.application.assemblys;
	exports warped.application.depthFields;
	exports warped.application.entities;
	exports warped.application.entities.item;
	exports warped.application.entities.projectile;
	exports warped.application.gui;
	exports warped.application.player;
	exports warped.application.quest;
	exports warped.application.state;
	exports warped.application.state.managers;
	exports warped.application.state.managers.gameObjectManagers;
	exports warped.application.tile;
	exports warped.audio;
	exports warped.functionalInterfaces;
	exports warped.graphics;
	exports warped.graphics.animation;
	exports warped.graphics.sprite;
	exports warped.graphics.sprite.spriteSheets;	
	exports warped.graphics.window;
	exports warped.user;
	exports warped.user.keyboard;
	exports warped.user.mouse;
	exports warped.utilities.enums;
	exports warped.utilities.enums.biology;
	exports warped.utilities.enums.biology.animal;
	exports warped.utilities.enums.biology.bacteria;
	exports warped.utilities.enums.biology.fungi;
	exports warped.utilities.enums.biology.plant;
	exports warped.utilities.enums.biology.protozoa;
	exports warped.utilities.enums.generalised;
	exports warped.utilities.math;
	exports warped.utilities.math.geometry.bezier;
	exports warped.utilities.math.geometry.circle;
	exports warped.utilities.math.geometry.ellipse;
	exports warped.utilities.math.geometry.ellipsoid;
	exports warped.utilities.math.geometry.sphere;
	exports warped.utilities.math.matrices;
	exports warped.utilities.math.vectors;
	exports warped.utilities.timers;
	exports warped.utilities.utils;
	exports warped.utilities.utils.path;

	
	
}