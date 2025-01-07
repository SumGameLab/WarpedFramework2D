/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;

public class UtilsFont {

	private static final int MIN_SIZE = 1;   //inclusive
	private static final int MAX_SIZE = 300; //inclusive
						
	private static Font preferredFont;
	private static Font preferredTitleFont;
	private static Font preferredParagraphFont;
	
	private static Color preferredColor				 = Color.WHITE;
	private static Color preferredTitleColor 		 = Color.YELLOW;
	private static Color preferredParagraphColor 	 = Color.WHITE;
	private static int preferredSize				 = 14;
	private static int preferredTitleSize			 = 16;
	private static int preferredParagraphSize		 = 14;
	private static int preferredStyle				 = Font.PLAIN;
	private static int preferredTitleStyle			 = Font.PLAIN;
	private static int preferredParagraphStyle 		 = Font.PLAIN;
	private static String preferredFontName 		 = Font.SANS_SERIF;
	private static String preferredTitleFontName 	 = Font.SERIF;
	private static String preferredParagraphFontName = Font.SANS_SERIF;
	
	private static ArrayList<String> availableFonts = new ArrayList<>();
	
	static {
		String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for(String fontName : fontNames) {availableFonts.add(fontName);}	
		
		updatePreferredFont();
		updatePreferredTitleFont();
		updatePreferredParagraphFont();
	}
	
	public static Font getFont(String fontName, int style, int size) {
		if(isStyleOutOfBounds(style) || isSizeOutOfBounds(size)) return getPreferred();		
		if(!isFontInstalled(fontName)) return getPreferred(style, size);
		return new Font(fontName, style, size);
	}	
	
	
	//--------
	//--------------- Access --------
	//--------
	public static void setPreferredColor(Color preferredColor) {UtilsFont.preferredColor = preferredColor;}
	public static void setPreferredTitleColor(Color preferredTitleColor) {UtilsFont.preferredTitleColor = preferredTitleColor;}
	public static void setPreferredParagraphColor(Color preferredParagraphColor) {UtilsFont.preferredParagraphColor = preferredParagraphColor;}
	
	public static Color getPreferredColor() {return preferredColor;}
	public static Color getPreferredTitleColor() {return preferredTitleColor;}
	public static Color getPreferredParagraphColor() {return preferredParagraphColor;}
	        
	public static int getPreferredSize() {return preferredSize;}
	public static int getPreferredTitleSize() {return preferredTitleSize;}
	public static int getPreferredParagraphSize() {return preferredParagraphSize;}
	       
	public static int getPreferredStyle() {return preferredStyle;}
	public static int getPreferredTitleStyle() {return preferredTitleStyle;}
	public static int getPreferredParagraphStyle() {return preferredParagraphStyle;}
	
	//-------- Font --------
	public static void setPreferredFont(String fontName){
		if(!isFontInstalled(fontName)) return;
		else preferredFontName = fontName;
		updatePreferredFont();
	}
	
	public static void setPreferredTitleFont(String fontName) {
		if(!isFontInstalled(fontName)) return;
		else preferredTitleFontName = fontName;
		updatePreferredTitleFont();
	}
	
	public static void setPreferredParagraphFont(String fontName) {
		if(!isFontInstalled(fontName)) return;
		else preferredParagraphFontName = fontName;
		updatePreferredParagraphFont();
	}

	//------- Size --------
	public static void setPreferredSize(int size) {
		if(isSizeOutOfBounds(size)) return;
		else preferredSize = size;
		updatePreferredFont();
	}
	
	public static void setPreferredTitleSize(int size) {
		if(isSizeOutOfBounds(size)) return; 
		else preferredTitleSize = size;
		updatePreferredFont();
	}
	
	public static void setPreferredParagraphSize(int size) {
		if(isSizeOutOfBounds(size)) return; 
		else preferredParagraphSize = size;
		updatePreferredFont();
	}
	
	//-------- Style --------
	public static void setPreferredStyle(int style) {
		if(isStyleOutOfBounds(style)) return;
		else preferredStyle = style;
		updatePreferredFont();
	}
	
	public static void setPreferredTitleStyle(int style) {
		if(isStyleOutOfBounds(style)) return;
		else preferredTitleStyle = style;
		updatePreferredTitleFont();
	}
	
	public static void setPreferredParagraphStyle(int style) {
		if(isStyleOutOfBounds(style)) return;
		else preferredParagraphStyle = style;
		updatePreferredParagraphFont();		
	}
	
	
	//--------
	//--------------- Update  --------
	//--------
	private static void updatePreferredFont() {preferredFont = new Font(preferredFontName, preferredStyle, preferredSize);}
	private static void updatePreferredTitleFont() {preferredTitleFont = new Font(preferredFontName, preferredTitleStyle, preferredTitleSize);}	
	private static void updatePreferredParagraphFont() {preferredParagraphFont = new Font(preferredFontName, preferredParagraphStyle, preferredParagraphSize);}
	
	
	//--------
	//--------------- Preferred Font --------
	//--------
	public static Font getPreferred() {return preferredFont;}
	public static Font getPreferredBold() {return new Font(preferredFontName, Font.BOLD, preferredSize);}
	public static Font getPreferredItalic() {return new Font(preferredFontName, Font.ITALIC, preferredSize);}
	public static Font getPreferred(int size) {
		if(isSizeOutOfBounds(size)) {
			Console.err("UtilsFont -> getPreferred() -> size is out of bounds");
			return getPreferred();
		}
		else return new Font(preferredFontName, preferredStyle, size);
	}
	public static Font getPreferred(int style, int size) {
		if(isSizeOutOfBounds(size) || isStyleOutOfBounds(style)) {
			Console.err("UtilsFont -> getPreferred() -> size or style is out of bounds");
			return getPreferred();
		} else return new Font(preferredFontName, style, size);
	}
	
	//-------- Preferred Title Font
	public static Font getPreferredTitle() {return preferredTitleFont;}
	public static Font getPreferredTitle(int size) {
		if(isSizeOutOfBounds(size)) {
			Console.err("UtilsFont -> getPreferredTitle() -> size is out of bounds");
			return getPreferredTitle();
		}
		else return new Font(preferredTitleFontName, preferredTitleStyle, size);
	}
	public static Font getPreferredTitle(int style, int size) {
		if(isSizeOutOfBounds(size) || isStyleOutOfBounds(style)) {
			Console.err("UtilsFont -> getPreferredTitle() -> size or style is out of bounds");
			return getPreferredTitle();
		} else return new Font(preferredTitleFontName, style, size);
	}
	
	//-------- Preferred Paragraph Font -------
	public static Font getPreferredParagraph() {return preferredParagraphFont;}
	public static Font getPreferredParagraph(int size) {
		if(isSizeOutOfBounds(size)) return getPreferredParagraph();
		else return new Font(preferredParagraphFontName, preferredParagraphStyle, size);
	}
	public static Font getPreferredParagraph(int style, int size) {
		if(isSizeOutOfBounds(size) || isStyleOutOfBounds(style)) {
			Console.err("UtilsFont -> getPreferredTitle() -> size or style is out of bounds");
			return getPreferredParagraph();
		} else return new Font(preferredParagraphFontName, style, size);
	}
	
	//--------- Validation --------
	private static boolean isSizeOutOfBounds(int size) {
		if(size < MIN_SIZE || size > MAX_SIZE) {
			Console.err("UtilsFont -> isSizeOutOfBounds() -> size is out of bounds : " + size);
			return true;
		} else return false;
	}
	
	private static boolean isStyleOutOfBounds(int style) {
		if(style != Font.PLAIN && style != Font.BOLD && style != Font.ITALIC) {
			Console.err("UtilsFont -> isStyleOutOfBounds() -> style is out of bounds : " + style);
			return true;
		} else return false;
	}
	
	private static boolean isFontInstalled(String fontName) {
		if(!availableFonts.contains(fontName)) {
			Console.err("UtilsFont -> isFontInstalled() -> font is not installed : " + fontName);
			return false;
		} else return true;
	}
		
	
}
