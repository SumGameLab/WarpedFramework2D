/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.enums.generalised;

import java.awt.Color;

import warped.utilities.utils.UtilsMath;

public enum Colour {

	BLACK,
	CHRISTMAS_RED,
	EMPTY,
	GREY,
	GREY_DARK,
	GREY_DARK_DARK,
	GREY_ECLIPSE,
	GREY_LIGHT,
	GREY_LIGHT_LIGHT,
	SILVER,
	WHITE,
	CREAM,
	SNOW,
	CORAL,
	BROWN,
	FIREBRICK,
	MAROON,
	RED_DARK,
	RED,
	RED_LIGHT,
	SALMON,
	TOMATO,
	ORANGE,
	ORANGE_DARK,
	SALMON_LIGHT,
	SIENNA,
	SEASHELL,
	CHOCOLATE,
	SADDLE,
	SANDY,
	PEACHPUFF,
	PERU,
	LINEN,
	BISQUE,
	WHEAT,
	GOLDENROD,
	GOLD,
	LEMON,
	KHAKI,
	KHAKI_DARK,
	YELLOW,
	YELLOW_LIGHT,
	YELLOW_DARK,
	YELLOW_GREEN,
	OLIVE,
	OLIVE_DARK,
	LAWN,
	NAVY,
	NIGHT,
	BLUE,
	BLUE_PALE,
	BLUE_DARK,
	BLUE_LIGHT,
	ROYAL,
	GREEN,
	GREEN_DARK,
	GREEN_PALE,
	GREEN_YELLOW,
	HOVER,
	FOREST,
	LIME,
	SEAGREEN,
	SPRING,
	MINTCREAM,
	AQUAMARINE,
	TURQUOISE,
	TEAL,
	CYAN,	
	AQUA,
	SKY_DEEP,
	SKY,
	SKY_LIGHT,
	STEEL,
	SLATE,
	GUN_METAL,
	CORNFLOWER,
	PURPLE,
	PURPLE_DARK,
	PURPLE_PALE,
	VIOLET,
	INDIGO,
	THISTLE,
	PLUM,	
	MAGENTA,
	PINK_HOT,
	PINK_DEEP,
	PINK_PALE,
	CRIMSON;
	
	
	private static final Color black 	 		= new Color(  0,   0,   0, 255);
	private static final Color cream 	 		= new Color(252, 248, 244, 255);    
	private static final Color grey      		= new Color(128, 128, 128, 255);           
	private static final Color greyDark  		= new Color( 60,  60,  60, 255);
	private static final Color greyEclipse		= new Color( 40,  40,  40, 255);   
	private static final Color greyDarkDark		= new Color( 30,  30,  30, 255);   
	private static final Color greyLight 		= new Color(180, 180, 180, 255);    
	private static final Color greyLightLight 	= new Color(210, 210, 210, 255);    
	private static final Color silver    		= new Color(192, 192, 192, 255);         
	private static final Color white 			= new Color(255, 255, 255, 255);          
	private static final Color snow 			= new Color(255, 240, 240, 255);           
	private static final Color coral	 		= new Color(255, 127,  80, 255);             
	private static final Color brown 			= new Color(165,  42,  42, 255);          
	private static final Color fireBrick 		= new Color(178,  34,  34, 255);    
	private static final Color maroon 			= new Color(128,   0,   0, 255);         
	private static final Color redDark 			= new Color( 80,   0,   0, 255);     
	private static final Color red 				= new Color(255,   0,   0, 255);            
	private static final Color redLight 		= new Color(255, 100, 100, 255);      
	private static final Color salmon 			= new Color(255, 128, 114, 255);         
	private static final Color salmonLight 		= new Color(255, 160, 122, 255);   
	private static final Color tomato 			= new Color(255,  90,  71, 255);         
	private static final Color orange 			= new Color(255,  69,   0, 255);                  
	private static final Color orangeDark 		= new Color(216,  57,   0, 255);    
	private static final Color sienna 			= new Color(160,  82,  45, 255);        
	private static final Color seaShell 		= new Color(255, 245, 238, 255);       
	private static final Color chocolate 		= new Color(210, 105,  30, 255);      
	private static final Color saddle 			= new Color(139,  69,  19, 255);         
	private static final Color sandy 			= new Color(244, 164,  96, 255);          
	private static final Color peachPuff 		= new Color(255, 218, 185, 255);     
	private static final Color peru 			= new Color(205, 133,  63, 255);           
	private static final Color linen 			= new Color(250, 240, 230, 255);         
	private static final Color bisque 			= new Color(255, 228, 196, 255);         
	private static final Color wheat 			= new Color(245, 222, 179, 255);          
	private static final Color goldenRod 		= new Color(184, 134,  11, 255);     
	private static final Color gold 			= new Color(255, 215,   0, 255);           
	private static final Color lemon 			= new Color(255, 250, 110, 255);  
	private static final Color khaki 			= new Color(240, 230, 140, 255);          
	private static final Color khakiDark 		= new Color(189, 183, 107, 255);     
	private static final Color yellow 			= new Color(255, 255,   0, 255);         
	private static final Color yellowLight 		= new Color(255, 255, 180, 255);   
	private static final Color yellowDark 		= new Color(200, 200,   0, 255);    
	private static final Color yellowGreen		= new Color(154, 205,  50, 255);   
	private static final Color olive 			= new Color(128, 128,   0, 255);          
	private static final Color oliveDark 		= new Color( 85, 107,  47, 255);     
	private static final Color lawn 			= new Color(124, 252,   0, 255);           
	private static final Color navy 			= new Color(  0,   0, 128, 255);           
	private static final Color blue 			= new Color(  0,   0, 255, 255);           
	private static final Color bluePale 		= new Color( 89,  89, 255, 255);      
	private static final Color blueDark 		= new Color(  0,   0,  60, 255);      
	private static final Color blueLight 		= new Color(110, 110, 255, 255);    
	private static final Color royal 			= new Color( 65, 105, 225, 255);     
	private static final Color green 			= new Color(  0, 200,   0, 255);
	private static final Color greenDark		= new Color(  0, 135,   0, 255);          
	private static final Color greenPale 		= new Color(102, 255, 102, 255);     
	private static final Color greenYellow 		= new Color(173, 255,  47, 255);  
	private static final Color forest 			= new Color( 34, 139,  34, 255);         
	private static final Color lime 			= new Color(  0, 255,   0, 255);           
	private static final Color seaGreen 		= new Color( 46, 139,  87, 255);       
	private static final Color spring 			= new Color(  0, 255, 127, 255);         
	private static final Color mintCream 		= new Color(245, 255, 240, 255);      
	private static final Color aquamarine 		= new Color(127, 255, 212, 255);     
	private static final Color turquoise 		= new Color( 64, 224, 208, 255);      
	private static final Color teal 		 	= new Color(  0, 206, 206, 255);           
	private static final Color cyan 			= new Color(  0, 255, 255, 255);	        
	private static final Color aqua 			= new Color(  0, 140, 220, 255);                 
	private static final Color skyDeep 			= new Color(  0, 191, 255, 255);       
	private static final Color sky 				= new Color(135, 206, 235, 255);            
	private static final Color skyLight 		= new Color(135, 206, 255, 255);               
	private static final Color steel 			= new Color( 70, 130, 180, 255);          
	private static final Color slate 			= new Color(112, 128, 144, 255);          
	private static final Color gunMetal 		= new Color( 70,  80,  90, 255);     
	private static final Color cornflower 		= new Color(100, 149, 237, 255);     
	private static final Color purple 			= new Color(128,   0, 128, 255);         
	private static final Color purpleDark 		= new Color( 80,   0,  80, 255);    
	private static final Color purplePale 		= new Color(128,  80, 128, 255);    
	private static final Color violet 			= new Color(238, 130, 238, 255);     
	private static final Color indigo 			= new Color( 75,   0, 130, 255);         
	private static final Color thistle 			= new Color(216, 191, 216, 255);        
	private static final Color plum 			= new Color(221, 160, 221, 255);	        
	private static final Color magenta 			= new Color(255,   0, 255, 255);        
	private static final Color pinkHot 			= new Color(255, 105, 180, 255);       
	private static final Color pinkDeep 		= new Color(255,  20, 147, 255);      
	private static final Color pinkPale 		= new Color(255, 112, 190, 255);      
	private static final Color crimson 			= new Color(220,  20,  60, 255);
	private static final Color christmasRed 	= new Color(234,  84,  89, 255);
	
	private static final Color night			= new Color(  0,   0,   0, 180);
	private static final Color hover 			= new Color( 60,  60,  60, 60);
	private static final Color empty			= new Color(  0,   0,   0,  0);
	
	
	public final int getRed() {return getRed(this);}
	public static final int getRed(Colour colour) {return colour.getColor().getRed();}
	public final int getGreen() {return getGreen(this);}
	public static final int getGreen(Colour colour) {return colour.getColor().getGreen();}
	public final int getBlue() {return getBlue(this);}
	public static final int getBlue(Colour colour) {return colour.getColor().getBlue();}
	public final int getAlpha() {return getAlpha(this);}
	public static final int getAlpha(Colour colour) {return colour.getColor().getAlpha();}
	
	public static final boolean isEqual(Color color, Colour colour) {
		if(color.getRed()   == colour.getRed()   &&
		   color.getGreen() == colour.getGreen() &&
	  	   color.getBlue()  == colour.getBlue()  &&
		   color.getAlpha() == colour.getAlpha()   ) return true; else return false;
	}
	
	public static final boolean isEqual(int col, Color color) {return color.equals(new Color(col));}
	public final boolean isEqual(int col) {return isEqual(col, this);}
	public static final boolean isEqual(int col, Colour colour) {return Colour.isEqual(new Color(col), colour);}
	
	public static final Color getColor(Colour  colour, int alpha) {
		if(alpha < 0 || alpha > 256) {
			System.err.println("Colour -> getColor() -> alpha is outside of domain 0 <= alpha < 255  : " + alpha);
			return magenta;
		}
		Color base = getColor(colour);
		Color result = new Color(base.getRed(), base.getGreen(), base.getBlue(), alpha);		
		return result;
	}	
	public final Color getBrighter(int val) {return getBrighter(this, val);}
	public static final Color getBrighter(Colour colour, int val) {return Colour.getBrighter(Colour.getColor(colour), val);}
	public static final Color getBrighter(Color color, int val) {return new Color(UtilsMath.clamp(color.getRed() + val, 0, 256), UtilsMath.clamp(color.getGreen() + val, 0, 256), UtilsMath.clamp(color.getBlue() + val, 0, 256), color.getAlpha());}
	
	public final Color getDarker(int val) {return getBrighter(this, val);}
	public static final Color getDarker(Colour colour, int val) {return Colour.getBrighter(Colour.getColor(colour), val);}
	public static final Color getDarker(Color color, int val) {return new Color(UtilsMath.clamp(color.getRed() - val, 0, 256), UtilsMath.clamp(color.getGreen() - val, 0, 256), UtilsMath.clamp(color.getBlue() - val, 0, 256), color.getAlpha());}
	
	public final Color getColor() {return(getColor(this));}
	public final Color getColor(int alpha) {return getColor(this, alpha);}
	public static final Color getColor(Colour colour) {
		switch(colour) {
		case AQUA: return aqua;
		case AQUAMARINE: return aquamarine;
		case BISQUE: return bisque;
		case BLACK: return black;
		case BLUE: return blue;
		case BLUE_DARK: return blueDark;
		case BLUE_LIGHT: return blueLight;
		case BLUE_PALE: return bluePale;
		case CHRISTMAS_RED: return christmasRed;
		case CREAM: return cream;
		case ROYAL: return royal;
		case BROWN: return brown;
		case CHOCOLATE: return chocolate;
		case CORAL: return coral;
		case CORNFLOWER: return cornflower;
		case CRIMSON: return crimson;
		case CYAN: return cyan;
		case FIREBRICK: return fireBrick;
		case FOREST: return forest;
		case GOLD: return gold;
		case GOLDENROD: return goldenRod;
		case GREEN: return green;
		case GREEN_DARK: return greenDark;
		case GREEN_PALE: return greenPale;
		case GREEN_YELLOW: return greenYellow;
		case GREY: return grey;
		case GREY_DARK: return greyDark;
		case GREY_DARK_DARK: return greyDarkDark;
		case GREY_ECLIPSE: return greyEclipse;
		case GREY_LIGHT: return greyLight;
		case GREY_LIGHT_LIGHT: return greyLightLight;
		case GUN_METAL: return gunMetal;
		case INDIGO: return indigo;
		case KHAKI: return khaki;
		case KHAKI_DARK: return khakiDark;
		case LAWN: return lawn;
		case LEMON: return lemon;
		case LIME: return lime;
		case LINEN: return linen;
		case MAGENTA: return magenta;
		case MAROON: return maroon;
		case MINTCREAM: return mintCream;
		case NAVY: return navy;
		case NIGHT: return night;
		case OLIVE: return olive;
		case OLIVE_DARK: return oliveDark;
		case ORANGE: return orange;
		case ORANGE_DARK: return orangeDark;
		case PEACHPUFF: return peachPuff;
		case PERU: return peru;
		case PINK_DEEP: return pinkDeep;
		case PINK_HOT: return pinkHot;
		case PINK_PALE: return pinkPale;
		case PLUM: return plum;
		case PURPLE: return purple;
		case PURPLE_DARK: return purpleDark;
		case PURPLE_PALE: return purplePale;
		case RED: return red;
		case RED_DARK: return redDark;
		case RED_LIGHT: return redLight;
		case SADDLE: return saddle;
		case SALMON: return salmon;
		case SALMON_LIGHT: return salmonLight;
		case SANDY: return sandy;
		case SEAGREEN: return seaGreen;
		case SEASHELL: return seaShell;
		case SIENNA: return sienna;
		case SILVER: return silver;
		case SKY: return sky;
		case SKY_DEEP: return skyDeep;
		case SKY_LIGHT: return skyLight;
		case SLATE: return slate;
		case SNOW: return snow;
		case SPRING: return spring;
		case STEEL: return steel;
		case TEAL: return teal;
		case THISTLE: return thistle;
		case TOMATO: return tomato;
		case TURQUOISE: return turquoise;
		case VIOLET: return violet;
		case WHEAT: return wheat;
		case WHITE: return white;
		case YELLOW: return yellow;
		case YELLOW_DARK: return yellowDark;
		case YELLOW_GREEN: return yellowGreen;
		case YELLOW_LIGHT: return yellowLight;
		
		case HOVER: return hover;
		case EMPTY: return empty; 
		
		default: System.err.println("Colour -> getColor() -> invalid switch case : " + colour); return white;
		}
	}
	
}
