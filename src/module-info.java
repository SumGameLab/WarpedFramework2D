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
	exports warped.audio;
	exports warped.graphics.camera;
	exports warped.graphics.window;
	exports warped.graphics.sprite;
	exports warped.graphics.animation;
	exports warped.graphics.sprite.spriteSheets;
	exports warped.user.keyboard;
	exports warped.user.mouse;
	exports warped.user.actions;
	exports warped.application.assemblys;
	exports warped.application.depthFields;
	exports warped.application.entities;
	exports warped.application.entities.item;
	exports warped.application.entities.item.ammunition;
	exports warped.application.entities.item.artifact;
	exports warped.application.entities.item.container;
	exports warped.application.entities.item.drink;
	exports warped.application.entities.item.element;
	exports warped.application.entities.item.equipment;
	exports warped.application.entities.item.equipment.apparel;
	exports warped.application.entities.item.equipment.tool;
	exports warped.application.entities.item.food;
	exports warped.application.entities.item.resource;
	exports warped.application.entities.projectile;
	exports warped.application.gui;
	exports warped.application.gui.textBox;
	exports warped.application.gui.lists;
	exports warped.application.object;
	exports warped.application.player;
	exports warped.application.prop;
	exports warped.application.prop.animation;
	exports warped.application.quest;
	exports warped.application.state;
	exports warped.application.state.groups;
	exports warped.application.state.managers;
	exports warped.application.state.managers.gameObjectManagers;
	exports warped.application.tile;
	exports warped.utilities.math;
	exports warped.utilities.math.vectors;
	exports warped.utilities.math.geometry.curves.bezier;
	exports warped.utilities.utils;
	exports warped.utilities.utils.path;
	exports warped.utilities.enums;
	exports warped.utilities.enums.generalised;

	
	
}