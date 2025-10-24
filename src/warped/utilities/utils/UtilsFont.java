/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.utils;

import java.awt.Font;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import warped.application.state.WarpedFramework2D;

public class UtilsFont {

	private static final int MIN_SIZE = 1;   //inclusive
	private static final int MAX_SIZE = 500; //inclusive
						
	//private static Color preferredColor				 = Color.WHITE;

	private static int defaultSize				 = 14;
	private static FontLanguageType languageType = FontLanguageType.LATIN;
	private static Font defaultFont;
	private static FontType defaultFontType = FontType.LATIN_REGULAR;	
	private static HashMap<FontType, Font> fonts = new HashMap<>();
	
	public enum FontType{
		LATIN_REGULAR,
		LATIN_BOLD,
		LATIN_THIN,
		LATIN_ITALIC,
		
		BENGALI_REGULAR,
		BENGALI_BOLD,
		BENGALI_THIN,
		
		JAPANESE_REGULAR,
		JAPANESE_BOLD,
		JAPANESE_THIN,
		
		KOREAN_REGULAR,
		KOREAN_BOLD,
		KOREAN_THIN,
		
		CHINESE_TRAD_REGULAR,
		CHINESE_TRAD_BOLD,
		CHINESE_TRAD_THIN,
		
		TAMIL_REGULAR,
		TAMIL_BOLD,
		TAMIL_THIN,
		
		GURMUKHI_REGULAR,
		GURMUKHI_BOLD,
		GURMUKHI_THIN
	}
	
	public enum FontStyleType{
		REGULAR,
		THIN,
		BOLD,
		ITALIC,
	}
	
	public enum FontLanguageType{
		LATIN,
		BENGALI,
		JAPANESE,
		KOREAN,
		CHINESE_TRAD,
		TAMIL,
		GURMUKHI
	}
		
	public static FontType getFontType(FontLanguageType language, FontStyleType style) {
		switch(language) {
		case BENGALI:
			switch(style) {
			case BOLD:		return FontType.BENGALI_BOLD;
			case REGULAR:	return FontType.BENGALI_REGULAR;	
			case THIN:		return FontType.BENGALI_THIN;
			default:
				Console.err("UtilsFont -> getFont() -> invalid case : " + language + ", " + style);
				return FontType.BENGALI_REGULAR;
			}
		case CHINESE_TRAD:
			switch(style) {
			case BOLD:				break;
			case REGULAR:			break;
			case THIN:				break;
			default:
				Console.err("UtilsFont -> getFont() -> invalid case : " + language + ", " + style);
				return FontType.CHINESE_TRAD_REGULAR;
			}
		case GURMUKHI:
			switch(style) {
			case BOLD:				break;
			case REGULAR:			break;
			case THIN:				break;
			default:
				Console.err("UtilsFont -> getFont() -> invalid case : " + language + ", " + style);
				return FontType.GURMUKHI_REGULAR;
			}
		case JAPANESE:
			switch(style) {
			case BOLD:				break;
			case REGULAR:			break;
			case THIN:				break;
			default:
				Console.err("UtilsFont -> getFont() -> invalid case : " + language + ", " + style);
				return FontType.JAPANESE_REGULAR;
			}
		case KOREAN:
			switch(style) {
			case BOLD:				break;
			case REGULAR:			break;
			case THIN:				break;
			default:
				Console.err("UtilsFont -> getFont() -> invalid case : " + language + ", " + style);
				return FontType.KOREAN_REGULAR;
			}
		case LATIN:
			switch(style) {
			case BOLD:				break;
			case ITALIC:			break;
			case REGULAR:			break;
			case THIN:				break;
			default:
				Console.err("UtilsFont -> getFont() -> invalid case : " + language + ", " + style);
				return FontType.LATIN_REGULAR;
			}
		case TAMIL:
			switch(style) {
			case BOLD:				break;
			case REGULAR:			break;
			case THIN:				break;
			default:
				Console.err("UtilsFont -> getFont() -> invalid case : " + language + ", " + style);
				return FontType.TAMIL_REGULAR;
			}
		default:
			Console.err("UtilsFont -> getFont() -> invalid case : " + language + ", " + style);
			return FontType.LATIN_REGULAR;
		}
	}
	
	public static FontLanguageType getLanguage(String languageCode) {
		switch(languageCode) {
		case "bn": return FontLanguageType.BENGALI;
		case "ja": return FontLanguageType.JAPANESE;
		case "pa": return FontLanguageType.GURMUKHI;
		case "zh": return FontLanguageType.CHINESE_TRAD;
		case "ko": return FontLanguageType.KOREAN;
		case "ta": return FontLanguageType.TAMIL;
		default:
			return FontLanguageType.LATIN;
		}
	}
	
	public static FontStyleType getStyle(FontType font) {
		switch(font) {
		case BENGALI_BOLD:			
		case CHINESE_TRAD_BOLD:		
		case GURMUKHI_BOLD:			
		case JAPANESE_BOLD:			
		case KOREAN_BOLD:			
		case LATIN_BOLD:			
		case TAMIL_BOLD:			return FontStyleType.BOLD;
		case BENGALI_THIN:			
		case CHINESE_TRAD_THIN:		
		case GURMUKHI_THIN:			
		case JAPANESE_THIN:			
		case KOREAN_THIN:			
		case LATIN_THIN:			
		case TAMIL_THIN:			return FontStyleType.THIN;
		case BENGALI_REGULAR:		
		case CHINESE_TRAD_REGULAR:	
		case GURMUKHI_REGULAR:		
		case JAPANESE_REGULAR:		
		case KOREAN_REGULAR:		
		case LATIN_REGULAR:			
		case TAMIL_REGULAR:			return FontStyleType.REGULAR;			
		case LATIN_ITALIC:			return FontStyleType.ITALIC;
		default:
			Console.err("UtilsFont -> getStyle() -> invalid case : " + font);
			return FontStyleType.REGULAR;
		}
	}
	
	public FontLanguageType getLanguage(FontType font) {
		switch(font) {
		case BENGALI_BOLD:			
		case BENGALI_REGULAR:		
		case BENGALI_THIN:			return FontLanguageType.BENGALI;
		case CHINESE_TRAD_BOLD:		
		case CHINESE_TRAD_REGULAR:	
		case CHINESE_TRAD_THIN:		return FontLanguageType.CHINESE_TRAD;
		case GURMUKHI_BOLD:			
		case GURMUKHI_REGULAR:		
		case GURMUKHI_THIN:			return FontLanguageType.GURMUKHI;
		case JAPANESE_BOLD:			
		case JAPANESE_REGULAR:		
		case JAPANESE_THIN:			return FontLanguageType.JAPANESE;
		case KOREAN_BOLD:			
		case KOREAN_REGULAR:		
		case KOREAN_THIN:			return FontLanguageType.KOREAN;
		case LATIN_BOLD:			
		case LATIN_ITALIC:			
		case LATIN_REGULAR:			
		case LATIN_THIN:			return FontLanguageType.LATIN;
		case TAMIL_BOLD:			
		case TAMIL_REGULAR:			
		case TAMIL_THIN:			return FontLanguageType.TAMIL;
		default:
			Console.err("UtilsFont -> getLanguage() -> invalid case : " + font);
			return FontLanguageType.LATIN;		
		}
	}
	
	public UtilsFont() {
		
	}
	
	static {		
		try {
			//URL url = WarpedFramework2D.class.getResource("/framework/fonts/NotoSansLatin-Regular.ttf");
			//URI uri = WarpedFramework2D.class.getResource("/framework/fonts/NotoSansLatin-Regular.ttf").getPath()).;
			//String path = WarpedFramework2D.class.getResource("/framework/fonts/NotoSansLatin-Regular.ttf").getPath();
			
			//InputStream is = WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansLatin-Regular.ttf");
			
			//String path = ClassLoader.getSystemResource("res/framework/fonts/NotoSansLatin-Regular.ttf").getPath();
						
			//Console.ln("UtilsFont -> loading font resource from : " + path);
			//File latinReg = new File(path);
			//File latinBold = new File("res/framework/fonts/NotoSansLatin-Bold.ttf");
			//File latinThin = new File("res/framework/fonts/NotoSansLatin-Thin.ttf");
			//File latinItalic = new File("res/framework/fonts/NotoSansLatin-Italic.ttf");
			
			//File bengaliReg = new File("res/framework/fonts/NotoSansBengali-Regular.ttf");
			//File bengaliBold = new File("res/framework/fonts/NotoSansBengali-Bold.ttf");
			//File bengaliThin = new File("res/framework/fonts/NotoSansBengali-Thin.ttf");
			
			//File japaneseReg = new File("res/framework/fonts/NotoSansJapanese-Regular.ttf");
			//File japaneseBold = new File("res/framework/fonts/NotoSansJapanese-Bold.ttf");
			//File japaneseThin = new File("res/framework/fonts/NotoSansJapanese-Thin.ttf");
			
			//File koreanReg = new File("res/framework/fonts/NotoSansKorean-Regular.ttf");
			//File koreanBold = new File("res/framework/fonts/NotoSansKorean-Bold.ttf");
			//File koreanThin = new File("res/framework/fonts/NotoSansKorean-Thin.ttf");
			
			//File tamilReg = new File("res/framework/fonts/NotoSansTamil-Regular.ttf");
			//File tamilBold = new File("res/framework/fonts/NotoSansTamil-Bold.ttf");
			//File tamilThin = new File("res/framework/fonts/NotoSansTamil-Thin.ttf");
			
			//File tradChineseReg = new File("res/framework/fonts/NotoSansTradChinese-Regular.ttf");
			//File tradChineseBold = new File("res/framework/fonts/NotoSansTradChinese-Bold.ttf");
			//File tradChineseThin = new File("res/framework/fonts/NotoSansTradChinese-Thin.ttf");
			//File gurmukhiReg = new File("res/framework/fonts/NotoSansGurmukhi-Regular.ttf");
			//File gurmukhiBold = new File("res/framework/fonts/NotoSansGurmukhi-Bold.ttf");
			//File gurmukhiThin = new File("res/framework/fonts/NotoSansGurmukhi-Thin.ttf");
			fonts.put(FontType.LATIN_REGULAR, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansLatin-Regular.ttf")));
			fonts.put(FontType.LATIN_BOLD, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansLatin-Bold.ttf")));
			fonts.put(FontType.LATIN_THIN, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansLatin-Thin.ttf")));
			fonts.put(FontType.LATIN_ITALIC, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansLatin-Italic.ttf")));
			fonts.put(FontType.BENGALI_REGULAR, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansBengali-Regular.ttf")));
			fonts.put(FontType.BENGALI_BOLD, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansBengali-Bold.ttf")));
			fonts.put(FontType.BENGALI_THIN, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansBengali-Thin.ttf")));
			fonts.put(FontType.JAPANESE_REGULAR, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansJapanese-Regular.ttf")));
			fonts.put(FontType.JAPANESE_BOLD, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansJapanese-Bold.ttf")));
			fonts.put(FontType.JAPANESE_THIN, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansJapanese-Thin.ttf")));
			fonts.put(FontType.KOREAN_REGULAR, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansKorean-Regular.ttf")));
			fonts.put(FontType.KOREAN_BOLD, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansKorean-Bold.ttf")));
			fonts.put(FontType.KOREAN_THIN, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansKorean-Thin.ttf")));
			fonts.put(FontType.TAMIL_REGULAR, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansTamil-Regular.ttf")));
			fonts.put(FontType.TAMIL_BOLD, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansTamil-Bold.ttf")));
			fonts.put(FontType.TAMIL_THIN, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansTamil-Thin.ttf")));
			fonts.put(FontType.CHINESE_TRAD_REGULAR, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansTradChinese-Regular.ttf")));
			fonts.put(FontType.CHINESE_TRAD_BOLD, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansTradChinese-Bold.ttf")));
			fonts.put(FontType.CHINESE_TRAD_THIN, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansTradChinese-Thin.ttf")));
			fonts.put(FontType.GURMUKHI_REGULAR, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansGurmukhi-Regular.ttf")));
			fonts.put(FontType.GURMUKHI_BOLD, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansGurmukhi-Bold.ttf")));
			fonts.put(FontType.GURMUKHI_THIN, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansGurmukhi-Thin.ttf")));
			Console.ln("UtilsFont -> loaded framework fonts");
		} catch(Exception e) {
			Console.stackTrace(e);
		}
		
		updateDefaultFont();
	}
	
	public static Font getFont(String languageCode, FontStyleType styleType, int size) {
		return getFont(getFontType(getLanguage(languageCode), styleType), size);
	}
	
	public static Font getFont(FontType fontType) {
		return fonts.get(fontType).deriveFont(Font.PLAIN, defaultSize);
	}
	
	public static Font getFont(FontType fontName, int size) {
		if(isSizeOutOfBounds(size)) return getDefault();		
		return fonts.get(fontName).deriveFont(Font.PLAIN, size);
	}	
	
	public static Font getFont(FontStyleType styleType, int size) {
		if(isSizeOutOfBounds(size)) return getDefault();
		return getFont(getFontType(languageType, styleType), size);
	}
	
	//public static void setPreferredColor(Color preferredColor) {UtilsFont.preferredColor = preferredColor;}
	//public static Color getPreferredColor() {return preferredColor;}
	public static int getDefaultSize() {return defaultSize;}

	public static void setLanguage(FontLanguageType languageType) {UtilsFont.languageType = languageType;}
	public static void setLanguage(String languageLocale) {UtilsFont.languageType = getLanguage(languageLocale);}
	
	public static void setDefaultFont(String languageLocale) {
		defaultFontType = getFontType(getLanguage(languageLocale), FontStyleType.REGULAR);
		updateDefaultFont();
		
	}
	public static void setDefaultFont(FontType fontType){
		defaultFontType = fontType;
		updateDefaultFont();
	}

	public static void setPreferredSize(int size) {
		if(isSizeOutOfBounds(size)) return;
		else defaultSize = size;
		updateDefaultFont();
	}
	
	
	private static void updateDefaultFont() {defaultFont = fonts.get(defaultFontType).deriveFont(Font.PLAIN, defaultSize);}
	public static Font getDefault() {return defaultFont;}
	public static Font getDefault(int size) {
		if(isSizeOutOfBounds(size)) {
			Console.err("UtilsFont -> getPreferred() -> size is out of bounds");
			return getDefault();
		}
		else return fonts.get(defaultFontType).deriveFont(Font.PLAIN, size);
	}
	
	private static boolean isSizeOutOfBounds(int size) {
		if(size < MIN_SIZE || size > MAX_SIZE) {
			Console.err("UtilsFont -> isSizeOutOfBounds() -> size is out of bounds : " + size);
			return true;
		} else return false;
	}
			
	
}
