/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.HashMap;

import warped.application.state.WarpedFramework2D;

public class FontMatrix {

	private static final int MIN_SIZE = 1;   //inclusive
	private static final int MAX_SIZE = 500; //inclusive
						
	private static Font dynamicFont;
	private static FontAlphabetType alphabetType = FontAlphabetType.LATIN;
	private FontAlphabetType alphabetOverride;
	
	private static int defaultSize				 = 14;
	private static FontStyleType defaultStyleType 	 = FontStyleType.REGULAR;
	
	private static HashMap<FontType, Font> defaultFonts = new HashMap<>();
	private HashMap<FontType, Font> fontOverrides = new HashMap<>();

	
	public enum FontType{
		SYMBOLIC_REGULAR,
	
		LATIN_REGULAR,
		LATIN_BOLD,
		LATIN_THIN,
		LATIN_ITALIC,
		
		DEVANAGARI_BOLD,
		DEVANAGARI_THIN,
		DEVANAGARI_REGULAR,
		
		CYRILLIC_BOLD,
		CYRILLIC_ITALIC,
		CYRILLIC_REGULAR,
		
		GREEK_BOLD,
		GREEK_ITALIC,
		GREEK_REGULAR,
		
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
		
		CHINESE_SIMPLE_REGULAR,
		CHINESE_SIMPLE_BOLD,
		CHINESE_SIMPLE_THIN,
		
		
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
	
	public enum FontAlphabetType{
		LATIN,
		CYRILLIC,
		GREEK,
		DEVANAGARI,
		BENGALI,
		JAPANESE,
		KOREAN,
		CHINESE_TRAD,
		CHINESE_SIMPLE,
		TAMIL,
		GURMUKHI,
		SYMBOLIC,
	}
		
	public static FontType getFontType(FontAlphabetType alphabet, FontStyleType style) {
		switch(alphabet) {
		case GREEK:
			switch(style) {
			case BOLD:		return FontType.GREEK_BOLD;
			case REGULAR:	return FontType.GREEK_REGULAR;
			case ITALIC:	return FontType.GREEK_ITALIC;
			default:
				Console.err("UtilsFont -> getFont() -> invalid case : " + alphabet + ", " + style);
				return FontType.GREEK_REGULAR;
			}
		case DEVANAGARI:
			switch(style) {
			case BOLD:		return FontType.DEVANAGARI_BOLD;		
			case REGULAR:	return FontType.DEVANAGARI_REGULAR;		
			case THIN:		return FontType.DEVANAGARI_THIN;		
			default:
				Console.err("UtilsFont -> getFont() -> invalid case : " + alphabet + ", " + style);
				return FontType.DEVANAGARI_REGULAR;
			}
		case CYRILLIC:
			switch(style) {
			case BOLD:		return FontType.CYRILLIC_BOLD;
			case REGULAR:	return FontType.CYRILLIC_REGULAR;		
			case ITALIC:	return FontType.CYRILLIC_ITALIC;		
			default:
				Console.err("UtilsFont -> getFont() -> invalid case : " + alphabet + ", " + style);
				return FontType.CYRILLIC_REGULAR;
			}
		case BENGALI:
			switch(style) {
			case BOLD:		return FontType.BENGALI_BOLD;
			case REGULAR:	return FontType.BENGALI_REGULAR;	
			case THIN:		return FontType.BENGALI_THIN;
			default:
				Console.err("UtilsFont -> getFont() -> invalid case : " + alphabet + ", " + style);
				return FontType.BENGALI_REGULAR;
			}
		case CHINESE_TRAD:
			switch(style) {
			case BOLD:		return FontType.CHINESE_TRAD_BOLD;			
			case REGULAR:	return FontType.CHINESE_TRAD_REGULAR;
			case THIN:		return FontType.CHINESE_TRAD_THIN;		
			default:
				Console.err("UtilsFont -> getFont() -> invalid case : " + alphabet + ", " + style);
				return FontType.CHINESE_TRAD_REGULAR;
			}
		case CHINESE_SIMPLE:
			switch(style) {
			case BOLD:		return FontType.CHINESE_SIMPLE_BOLD;			
			case REGULAR:	return FontType.CHINESE_SIMPLE_REGULAR;
			case THIN:		return FontType.CHINESE_SIMPLE_THIN;		
			default:
				Console.err("UtilsFont -> getFont() -> invalid case : " + alphabet + ", " + style);
				return FontType.CHINESE_TRAD_REGULAR;
			}
		case GURMUKHI:
			switch(style) {
			case BOLD:		return FontType.GURMUKHI_BOLD;			
			case REGULAR:	return FontType.GURMUKHI_REGULAR;		
			case THIN:		return FontType.GURMUKHI_THIN;
			default:
				Console.err("UtilsFont -> getFont() -> invalid case : " + alphabet + ", " + style);
				return FontType.GURMUKHI_REGULAR;
			}
		case JAPANESE:
			switch(style) {
			case BOLD:		return FontType.JAPANESE_BOLD;		
			case REGULAR:	return FontType.JAPANESE_REGULAR;
			case THIN:		return FontType.JAPANESE_THIN;		
			default:
				Console.err("UtilsFont -> getFont() -> invalid case : " + alphabet + ", " + style);
				return FontType.JAPANESE_REGULAR;
			}
		case KOREAN:
			switch(style) {
			case BOLD:		return FontType.KOREAN_BOLD;
			case REGULAR:	return FontType.KOREAN_REGULAR;
			case THIN:  	return FontType.KOREAN_THIN;
			default:
				Console.err("UtilsFont -> getFont() -> invalid case : " + alphabet + ", " + style);
				return FontType.KOREAN_REGULAR;
			}
		case LATIN:
			switch(style) {
			case BOLD:		return FontType.LATIN_BOLD;		
			case ITALIC:	return FontType.LATIN_ITALIC;		
			case REGULAR:	return FontType.LATIN_REGULAR;		
			case THIN:		return FontType.LATIN_THIN;		
			default:
				Console.err("UtilsFont -> getFont() -> invalid case : " + alphabet + ", " + style);
				return FontType.LATIN_REGULAR;
			}
		case TAMIL:
			switch(style) {
			case BOLD:		return FontType.TAMIL_BOLD;		
			case REGULAR:	return FontType.TAMIL_REGULAR;		
			case THIN:		return FontType.TAMIL_THIN;		
			default:
				Console.err("UtilsFont -> getFont() -> invalid case : " + alphabet + ", " + style);
				return FontType.TAMIL_REGULAR;
			}
		case SYMBOLIC:
			return FontType.SYMBOLIC_REGULAR;
		default:
			Console.err("UtilsFont -> getFont() -> invalid case : " + alphabet + ", " + style);
			return FontType.LATIN_REGULAR;
		}
	}
	
	public static FontAlphabetType getAlphabet(String languageCode) {
		switch(languageCode) {
		case "bn": return FontAlphabetType.BENGALI;
		case "ja": return FontAlphabetType.JAPANESE;
		case "pa": return FontAlphabetType.GURMUKHI;
		case "zh": 
		case "zh_CN": return FontAlphabetType.CHINESE_SIMPLE;
		case "zh_TW":
		case "zh_HK": return FontAlphabetType.CHINESE_TRAD;
		case "ko": return FontAlphabetType.KOREAN;
		case "ta": return FontAlphabetType.TAMIL;
		case "ru": return FontAlphabetType.CYRILLIC;
		case "el": return FontAlphabetType.GREEK;
		case "hi": return FontAlphabetType.DEVANAGARI;
		case "da":
		case "nl":
		case "fr":
		case "fi":
		case "de":
		case "id": 
		case "it":
		case "ms":
		case "no":
		case "pt":
		case "es":
		case "sv":
		case "tr": 
		case "en":  return FontAlphabetType.LATIN;
		case "sym": return FontAlphabetType.SYMBOLIC;
		default:
			return FontAlphabetType.LATIN;
		}
	}
	
	public static FontStyleType getStyle(FontType font) {
		switch(font) {
		case DEVANAGARI_BOLD:
		case CYRILLIC_BOLD:
		case GREEK_BOLD:
		case BENGALI_BOLD:			
		case CHINESE_TRAD_BOLD:		
		case GURMUKHI_BOLD:			
		case JAPANESE_BOLD:			
		case KOREAN_BOLD:			
		case LATIN_BOLD:			
		case CHINESE_SIMPLE_BOLD:
		case TAMIL_BOLD:			return FontStyleType.BOLD;
		case BENGALI_THIN:			
		case CHINESE_TRAD_THIN:		
		case GURMUKHI_THIN:			
		case JAPANESE_THIN:			
		case KOREAN_THIN:			
		case LATIN_THIN:			
		case DEVANAGARI_THIN:
		case CHINESE_SIMPLE_THIN:
		case TAMIL_THIN:			return FontStyleType.THIN;
		case BENGALI_REGULAR:		
		case CHINESE_TRAD_REGULAR:	
		case GURMUKHI_REGULAR:		
		case JAPANESE_REGULAR:		
		case KOREAN_REGULAR:		
		case LATIN_REGULAR:			
		case DEVANAGARI_REGULAR:
		case CYRILLIC_REGULAR:
		case GREEK_REGULAR:
		case CHINESE_SIMPLE_REGULAR:
		case SYMBOLIC_REGULAR:
		case TAMIL_REGULAR:			return FontStyleType.REGULAR;			
		case CYRILLIC_ITALIC:
		case GREEK_ITALIC:
		case LATIN_ITALIC:			return FontStyleType.ITALIC;
		default:
			Console.err("UtilsFont -> getStyle() -> invalid case : " + font);
			return FontStyleType.REGULAR;
		}
	}
	
	public FontAlphabetType getAlphabet(FontType font) {
		switch(font) {
		case BENGALI_BOLD:			
		case BENGALI_REGULAR:		
		case BENGALI_THIN:			return FontAlphabetType.BENGALI;
		case CHINESE_TRAD_BOLD:		
		case CHINESE_TRAD_REGULAR:	
		case CHINESE_TRAD_THIN:		return FontAlphabetType.CHINESE_TRAD;
		case CHINESE_SIMPLE_BOLD:
		case CHINESE_SIMPLE_THIN:
		case CHINESE_SIMPLE_REGULAR: return FontAlphabetType.CHINESE_SIMPLE;
		case GURMUKHI_BOLD:			
		case GURMUKHI_REGULAR:		
		case GURMUKHI_THIN:			return FontAlphabetType.GURMUKHI;
		case JAPANESE_BOLD:			
		case JAPANESE_REGULAR:		
		case JAPANESE_THIN:			return FontAlphabetType.JAPANESE;
		case KOREAN_BOLD:			
		case KOREAN_REGULAR:		
		case KOREAN_THIN:			return FontAlphabetType.KOREAN;
		case LATIN_BOLD:			
		case LATIN_ITALIC:			
		case LATIN_REGULAR:			
		case LATIN_THIN:			return FontAlphabetType.LATIN;
		case TAMIL_BOLD:			
		case TAMIL_REGULAR:			
		case TAMIL_THIN:			return FontAlphabetType.TAMIL;
		case CYRILLIC_BOLD:
		case CYRILLIC_REGULAR:
		case CYRILLIC_ITALIC:		return FontAlphabetType.CYRILLIC;
		case DEVANAGARI_BOLD:
		case DEVANAGARI_REGULAR:
		case DEVANAGARI_THIN:		return FontAlphabetType.DEVANAGARI;
		case GREEK_BOLD:
		case GREEK_REGULAR:
		case GREEK_ITALIC:			return FontAlphabetType.GREEK;
		case SYMBOLIC_REGULAR:		return FontAlphabetType.SYMBOLIC;
		default:
			Console.err("UtilsFont -> getLanguage() -> invalid case : " + font);
			return FontAlphabetType.LATIN;		
		}
	}
		
	static {		
		try {
			defaultFonts.put(FontType.LATIN_REGULAR, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/ChironSungHKLatin-Regular.ttf")));
			defaultFonts.put(FontType.LATIN_BOLD, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/ChironSungHKLatin-Bold.ttf")));
			defaultFonts.put(FontType.LATIN_THIN, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/ChironSungHKLatin-Light.ttf")));
			defaultFonts.put(FontType.LATIN_ITALIC, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/ChironSungHKLatin-Italic.ttf")));
			defaultFonts.put(FontType.DEVANAGARI_REGULAR, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/HindDevanagari-Regular.ttf")));
			defaultFonts.put(FontType.DEVANAGARI_BOLD, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/HindDevanagari-Bold.ttf")));
			defaultFonts.put(FontType.DEVANAGARI_THIN, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/HindDevanagari-Light.ttf")));
			defaultFonts.put(FontType.CYRILLIC_REGULAR, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/LoraCyrillic-Regular.ttf")));
			defaultFonts.put(FontType.CYRILLIC_BOLD, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/LoraCyrillic-Bold.ttf")));
			defaultFonts.put(FontType.CYRILLIC_ITALIC, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/LoraCyrillic-Italic.ttf")));
			defaultFonts.put(FontType.GREEK_REGULAR, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/TinosGreek-Regular.ttf")));
			defaultFonts.put(FontType.GREEK_BOLD, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/TinosGreek-Bold.ttf")));
			defaultFonts.put(FontType.GREEK_ITALIC, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/TinosGreek-Italic.ttf")));
			defaultFonts.put(FontType.BENGALI_REGULAR, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansBengali-Regular.ttf")));
			defaultFonts.put(FontType.BENGALI_BOLD, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansBengali-Bold.ttf")));
			defaultFonts.put(FontType.BENGALI_THIN, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansBengali-Thin.ttf")));
			defaultFonts.put(FontType.JAPANESE_REGULAR, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansJapanese-Regular.ttf")));
			defaultFonts.put(FontType.JAPANESE_BOLD, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansJapanese-Bold.ttf")));
			defaultFonts.put(FontType.JAPANESE_THIN, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansJapanese-Thin.ttf")));
			defaultFonts.put(FontType.KOREAN_REGULAR, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansKorean-Regular.ttf")));
			defaultFonts.put(FontType.KOREAN_BOLD, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansKorean-Bold.ttf")));
			defaultFonts.put(FontType.KOREAN_THIN, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansKorean-Thin.ttf")));
			defaultFonts.put(FontType.TAMIL_REGULAR, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansTamil-Regular.ttf")));
			defaultFonts.put(FontType.TAMIL_BOLD, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansTamil-Bold.ttf")));
			defaultFonts.put(FontType.TAMIL_THIN, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansTamil-Thin.ttf")));
			defaultFonts.put(FontType.CHINESE_TRAD_REGULAR, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansChineseTrad-Regular.ttf")));
			defaultFonts.put(FontType.CHINESE_TRAD_BOLD, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansChineseTrad-Bold.ttf")));
			defaultFonts.put(FontType.CHINESE_TRAD_THIN, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansChineseTrad-Thin.ttf")));
			defaultFonts.put(FontType.CHINESE_SIMPLE_REGULAR, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansChineseSimple-Regular.ttf")));
			defaultFonts.put(FontType.CHINESE_SIMPLE_BOLD, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansChineseSimple-Bold.ttf")));
			defaultFonts.put(FontType.CHINESE_SIMPLE_THIN, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansChineseSimple-Light.ttf")));
			defaultFonts.put(FontType.GURMUKHI_REGULAR, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansGurmukhi-Regular.ttf")));
			defaultFonts.put(FontType.GURMUKHI_BOLD, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansGurmukhi-Bold.ttf")));
			defaultFonts.put(FontType.GURMUKHI_THIN, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/NotoSansGurmukhi-Thin.ttf")));
			defaultFonts.put(FontType.SYMBOLIC_REGULAR, Font.createFont(Font.TRUETYPE_FONT, WarpedFramework2D.class.getResourceAsStream("/framework/fonts/Yarndings20-Regular.ttf")));
			Console.ln("UtilsFont -> loaded framework fonts");
		} catch(FontFormatException | IOException  e) {
			Console.stackTrace(e);
		}
		updateDynamicFont();
	}
	
	/**A new font matrix.
	 * */
	public FontMatrix() {}
	
	/**A new font matrix with the specified font override
	 * @param fontType - the type of font to override.
	 * @param font - the font to use.
	 * @author 5som3 */
	public FontMatrix(FontType fontType, Font font) {setFontOverride(fontType, font);}
	
	/**Set a font type to a new font.
	 * @param fontType - the type of font to replace.
	 * @param font - the font to set as the new font.
	 * @apiNote It is vital to use the correct alphabet type when replacing a font. 
	 * @apiNote example: You must replace Latin fonts with other Latin fonts, otherwise the correct characters will not be available when the language is changed.  
	 * @author 5som3*/
	public static void setDefault(FontType fontType, Font font) {defaultFonts.put(fontType, font);}
		
	/**Set a font to override the default font type.
	 * @param fontType - the type of font to override.
	 * @param font - the font to use.
	 * @apiNote Use this when a specific font should be used instead of the default font
	 * @apiNote This font will be prioritised when getting a dynamic font of the specified type.
	 * @author 5som3*/
	public void setFontOverride(FontType fontType, Font font) {fontOverrides.put(fontType, font);}
	
	/**Clear any font overrides.
	 * @author 5som3*/
	public void clearFontOverrides() {fontOverrides.clear();}
	
	/**Set the alphabet override.
	 * @param type - The type of alphabet that this matrix should use
	 * @apiNote Will override the default alphabet.
	 * @author 5som3*/
	public void setAlphabetOverride(FontAlphabetType type) {alphabetOverride = type;}
	
	/**Set the alphabet override.
	 * @param languageCode - The langaugeCode corresponding to the type of alphabet that this matrix should use
	 * @apiNote Will override the default alphabet.
	 * @author 5som3*/
	public void setAlphabetOverride(String languageCode) {setAlphabetOverride(getAlphabet(languageCode));}
	
	/**Clear the alphabet override.
	 * @author 5som3*/
	public void clearAlphabetOverride() {alphabetOverride = null;}
	
	/**Get the font corresponding to the parameters.
	 * @param alphabet - the alphabet to get the font for. 
	 * @param style - the style of the language to get. NOTE: not all languages have all styles i.e. Chinese does not have italic.
	 * @param size - the size of the font. 
	 * @return Font - The font corresponding to the language code with the specified style and size.
	 * @apiNote Use this when you need a fixed font, fixed types will not be altered when the language is changed.
	 * @author 5som3*/
	public static Font getStatic(FontAlphabetType alphabet, FontStyleType style, int size) {
		if(isSizeOutOfBounds(size)) return dynamicFont;
		else return defaultFonts.get(getFontType(alphabet, style)).deriveFont(Font.PLAIN, size);
	}
	
	/**Get the font corresponding to the parameters.
	 * @param languageCode - the language locale to get the font for. 
	 * @param styleType - the style of the language to get. NOTE: not all languages have all styles i.e. Chinese does not have italic.
	 * @param size - the size of the font. 
	 * @return Font - The font corresponding to the language code with the specified style and size.
	 * @apiNote Use this when you need a fixed font, fixed types will not be altered when the language is changed.
	 * @author 5som3*/
	public static Font getStatic(String languageCode, FontStyleType styleType, int size) {
		if(isSizeOutOfBounds(size)) return dynamicFont;
		else return getStatic(getFontType(getAlphabet(languageCode), styleType), size);
	}
	
	/**Get a font based on the FontType.
	 * @param fontType - the type of font to get.
	 * @return Font - get the specified font.
	 * @apiNote Use this when you need a fixed font, fixed types will not be altered when the language is changed. 
	 * @author 5som3*/
	public static Font getStatic(FontType fontType) {
		return defaultFonts.get(fontType).deriveFont(Font.PLAIN, defaultSize);
	}
	
	/**Get a font of a specific size based on the font type.
	 * @param fontType - the type of font to get.
	 * @param size - the size of the font.
	 * @return Font - the font with the specified parameters.
	 * @apiNote Use this when you need a fixed font, fixed types will not be altered when the language is changed.
	 * @author 5som3*/
	public static Font getStatic(FontType fontType, int size) {
		if(isSizeOutOfBounds(size)) return dynamicFont;		
		return defaultFonts.get(fontType).deriveFont(Font.PLAIN, size);
	}	
	
	/**Get the default font in the specified style and size.
	 * @param styleType - the style of font to get.
	 * @param size - the size of the font.
	 * @return Font - the default font with the specified parameters.
	 * @apiNote Use this when you need a fixed font, fixed types will not be altered when the language is changed.
	 * @author 5som3*/
	public static Font getStatic(FontStyleType styleType, int size) {
		if(isSizeOutOfBounds(size)) return dynamicFont;
		return getStatic(getFontType(alphabetType, styleType), size);
	}
	
	/**Get the default font size.
	 * @return int - the default size of font to use.
	 * @author 5som3*/
	public static int getDefaultSize() {return defaultSize;}

	/**Set the alphabet to be used when getting the default font.
	 * @param alphabetType - the type of alphabet to use by default.
	 * @apiNote Use this when changing the language to ensure that the correct alphabet is used.
	 * @author 5som3*/
	public static void setAlphabetType(FontAlphabetType alphabetType) {
		FontMatrix.alphabetType = alphabetType;
		updateDynamicFont();
	}
	
	/**Set the alphabet to be used when getting the dynamic font.
	 * @param languageLocale - get the alphabet type corresponding to this language locale.
	 * @apiNote Use this when changing the language to ensure that the correct alphabet is used.
	 * @author 5som3*/
	public static void setAlphabetType(String languageLocale) {
		FontMatrix.alphabetType = getAlphabet(languageLocale);
		updateDynamicFont();
	}
	
	public FontAlphabetType getAlphabetType() {
		if(alphabetOverride != null) return alphabetOverride;
		else return alphabetType;
	}
	
	/**Set the default font size.
	 * @param size - the size of the default font.
	 * @author 5som3*/
	public static void setDefaultSize(int size) {
		if(isSizeOutOfBounds(size)) return;
		else defaultSize = size;
		updateDynamicFont();
	}
	
	/**Get the font based on the set alphabet type.
	 * @return Font - the default font in the default style and size.
	 * @apiNote Use dynamic font if you support internationalisation, default font is based on the set alphabet type. 
	 * @author 5som3*/
	public Font getDynamic() {return dynamicFont;}
	
	/**Get the font based on the set alphabet type in the specified size.
	 * @param size - the size of the font.
	 * @return Font - the default font type in the specified size.
	 * @apiNote Use dynamic font if you support internationalisation, default font is based on the set alphabet type.
	 * @author 5som3*/
	public Font getDynamic(int size) {
		if(isSizeOutOfBounds(size)) return getDynamic();
		else return getDynamic(defaultStyleType, size);
	}
	
	/**Get the font based on the set alphabet type in the specified size.
	 * @param styleType - the style of the font.
	 * @param size - the size of the font. 
	 * @return Font - the default font type in the specified size.
	 * @apiNote Use dynamic font if you support internationalisation, default font is based on the set alphabet type.
	 * @author 5som3*/
	public Font getDynamic(FontStyleType styleType, int size) {
		if(isSizeOutOfBounds(size)) return getDynamic();
		else {
			FontType fontType;
			if(alphabetOverride == null) fontType = getFontType(alphabetType, styleType);
			else fontType = getFontType(alphabetOverride, styleType);
			if(fontOverrides.containsKey(fontType)) 
				return fontOverrides.get(fontType).deriveFont(Font.PLAIN, size);
			else return defaultFonts.get(fontType).deriveFont(Font.PLAIN, size);
		}
	}
	
	private static void updateDynamicFont() {dynamicFont =  getStatic(alphabetType, defaultStyleType, defaultSize);}
	private static boolean isSizeOutOfBounds(int size) {
		if(size < MIN_SIZE || size > MAX_SIZE) {
			Console.err("UtilsFont -> isSizeOutOfBounds() -> size is out of bounds : " + size);
			return true;
		} else return false;
	}
			
	
}
