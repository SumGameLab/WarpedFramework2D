/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.sprite.spriteSheets;

import java.awt.image.BufferedImage;

import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsImage;

public abstract class FrameworkSprites {
	//If these objects are not created by the launcher then you may have some difficult to find bugs
	//Always remember to initialize sprites sheets in the constructor of this class
	
	public static WarpedSpriteSheet dialIcons;	    		
	private static WarpedSpriteSheet standardIcons;		
	public static WarpedSpriteSheet tileTransitions;
	public static WarpedSpriteSheet anglePicker;
	public static WarpedSpriteSheet mouse;
	public static WarpedSpriteSheet mouseLoad;
	public static WarpedSpriteSheet mediaIcons;
	
	public static BufferedImage trayIcon;
	public static BufferedImage loadBackground;
	public static BufferedImage error;
	
	
	public enum KeyboardIcons{
		A,
		B,
		C,
		D,
		E,
		F,
		G,
		H,
		I,
		J,
		K,
		L,
		M,
		N,
		O,
		P,
		Q,
		R,
		S,
		T,
		U,
		V,
		W,
		X,
		Y,
		Z,
		
		a,
		b,
		c,
		d,
		e,
		f,
		g,
		h,
		i,
		j,
		k,
		l,
		m,
		n,
		o,
		p,
		q,
		r,
		s,
		t,
		u,
		v,
		w,
		x,
		y,
		z,
		
		F1,
		F2,
		F3,
		F4,
		F5,
		F6,
		F7,
		F8,
		F9,
		F10,
		F11,
		F12,
		
		ONE,
		TWO,
		THREE,
		FOUR,
		FIVE,
		SIX,
		SEVEN,
		EIGHT,
		NINE, 
		ZERO,
		
		PLUS,
		MINUS,
		EQUALS,
		PERCENTAGE,
		
		EXCLAMATION,
		AT,
		AND,
		APOSTROPHE,
		QUOTE,
		COMMA,
		STOP,
		QUESTION,
		ASTERISK,
		HASHTAG,
		UNDERSCORE,
		COLON,
		SEMI_COLON,
		TILDE,
		PIPE,
		
		FORWARD_SLASH,
		BACK_SLASH,
		BRACKET_LEFT,
		BRACKET_RIGHT,
		PARENTHESIS_LEFT,
		PARENTHESIS_RIGHT,
		BRACE_LEFT,
		BRACE_RIGTH,
		CHEVRON_LEFT,
		CHEVRON_RIGHT,
		
		INS,
		DEL,
		HOME,
		END,
		PAUSE,
		PAGE_UP,
		PAGE_DOWN,
		PRINT,
		BREAK,
		ESC,
		ENTER,
		WIN,
		OPT,
		SPACE,
		BACK_SPACE,
		SCRL_LOCK,
		CAPS_LOCK,
		NUM_LOCK,
		TAB,
		SHIFT,
		SHIFT_L,
		SHIFT_R,
		CTRL,
		CTRL_L,
		CTRL_R,
		ALT,
		ALT_L,
		ALT_R,
		
		ARROW_UP,
		ARROW_DOWN,
		ARROW_LEFT,
		ARROW_RIGHT,
		ARROW_CLOCKWISE,
		ARROW_COUNTER_CLOCKWISE,
		
		ARROW_ALT_UP,
		ARROW_ALT_DOWN,
		ARROW_ALT_LEFT,
		ARROW_ALT_RIGHT,
		ARROW_ALT_CLOCKWISE,
		ARROW_ALT_COUNTER_CLOCKWISE;
	}
	
	public enum EmojiIcons {
		CONFUSED,
		SNEEZE,
		TEMPERATURE,
		DRUNK,
		SICK,
		VOMIT,
		DEAD,
		GRIN,
		HAPPY,
		INDIFFERENT,
		CONCERN,
		UNHAPPY,
		SAD,
		ANGRY,
		HOT,
		COLD,
		SHOCKED,
		LOVE,
		SLEEP,
		DROOL,
		INSPECT,
		SALUTE,
		CONTENT,
		INJURED,
		BLUSH,
		UNCOMFORTABLE,
		SURPRISED,
		MINDBLOWN,
		LIAR,
		ASTONISHED,
		PLEADING,
		CRYING,
		PRESERVING,
		EYEROLL,
		YAWN,
		COOL,
		SHAKING,
		CLOWN,
		DEVIL,
		DEAMON,
		DISGUISE,
		BLANK;
	}
	
	public enum GreekIcons {
		ALPHA,
		BETA,
		GAMMA,
		DELTA,
		EPSILON,
		ZETA,
		ETA,
		THETA,
		IOTA,
		KAPPA,
		LAMBDA,
		MU,
		NU,
		XI,
		OMICRON,
		PI,
		RHO,
		SIGMA,
		TAU,
		UPSILON,
		PHI,
		CHI,
		PSI,
		OMEGA;
	}
	
	public enum StandardIcons {	
		SIGNAL_0,
		SIGNAL_1,
		SIGNAL_2,
		SIGNAL_3,
		SIGNAL_4,
		SIGNAL_5,

		RADIO_CHECKED,
		RADIO_UNCHECKED,
		RADIO_ALT_CHECKED,
		RADIO_ALT_UNCHECKED,
		CHECKBOX_CHECKED,
		CHECKBOX_UNCHECKED,
		
		CLOSE,
		REFRESH,
		TRASH,
		MASS,
		SYSTEM,
		MOVE,
		KEY,
		HOUSE,
		LOCKED,
		UNLOCKED,
		COG,
		SPANNER,
		CALLANDER,
		CLOCK,
		SATELLITE,
		RADIO,
		COLLISION,
		LIGHTNING,
		RESISTOR,
		EXCLAMATION_PIN,
		IMAGE,
		LIST,
		PARTICLES,
		RADIATE,
		FIRE,
		CHAT_BUBBLE,
		CHAT_BUBBLE_ALT,
		SKULL_AND_CROSSBONE,
		INFORMATION,
		FLAG,
		STOPWATCH,
		CLOCK_WALL,
		CLOCK_MOUNT,
		DNA,
		RADIATION,
		RADIATION_ALT,
		RADIATION_WASTE,
		MAP,
		BARCODE,
		QRCODE,
		STAIRS,
		PORTAL,
		
		MAGNET,
		SCREWDRIVER,
		SAW,
		HAMMER,
		AXE,
		PICKAXE,
		SPADE,
		TROWEL,
		BROOM,
		
		GAUGE_OPTIMAL,
		GAUGE_MAX,
		GAUGE_MIN,
		
		THERMOMETER_OPTIMAL,
		THERMOMETER_HOT,
		THERMOEMETER_COLD,
		
		GAUGE_ALT_GOOD,
		GAUGE_ALT_BAD,
		
		BITCOIN,
		DOGECOIN,
		ETHER,
		RUBLE,
		RUPEE,
		YUAN,
		DONG,
		EURO,
		BAHT,
		DOLLAR,
		CENT,
		POUND,
		YEN,
		PESO,
		WON,
		AUSTRAL,
		TENGE,
		
		HEART_BROKEN,
		HEART_MENDED,
		HEART_RED,
		HEART_BLACK,
		HEART_WHITE,
		HEART_GREEN,
		HEART_BLUE,
		HEART_YELLOW,
		
		POINT_UP,
		POINT_DOWN,
		POINT_LEFT,
		POINT_RIGHT,

		THUMBS_UP,
		THUMBS_DOWN,
		
		HANDS_LOVE,
		HANDS_CLAP,
		HANDS_BIRD,
		HANDS_SHAKE,
		HANDS_PUNCH,
		HANDS_KNOCK,
		HANDS_POINT,
		HANDS_RADICAL,
		HANDS_ROCK_AND_ROLL,
		HANDS_SNAP,
		HANDS_CROSS_FINGERS,
		HANDS_PEACE,
		HANDS_PALM,
		HANDS_SPOK,
		HANDS_RECIVE,
		HANDS_OK,
		HANDS_COME_ON,
	
		FOOT,
		LEG,
		ARM,
		EAR,
		NOSE,
		BONE,
		TOOTH,
		LUNG,
		HEART,
		BRAIN,
		EYE,
		
		PAIN_BACK,
		PAIN_EYE,
		PAIN_HEAD,
		PAIN_EAR,
		PAIN_FOOT,
		PAIN_KNEE,
		PAIN_BROKENBONE,
		
		ATOM,
		PLANET,
		PLANET_ALT,
		PLANET_ALIEN,
		STAR,
		SUN,
		MOON_CRESENT,
		MOON_FULL,
		BLACKHOLE,
		SHIP_ALIEN,
		SHIP_SPACE,
		SHIP_SPACE_ALT,
		ANTENA,
		ANTENA_ALT,
		
		FLAG_BROWN,
		FLAG_RED,
		FLAG_GREEN,
		FLAG_BLUE,
		FLAG_PURPLE,
		FLAG_TEAL;		
	}
	
	public static BufferedImage getGreekIcon(GreekIcons greekIcon) {
		switch(greekIcon) {
		case ALPHA:		return standardIcons.getSprite(0,  29); 
		case BETA:      return standardIcons.getSprite(1,  29);
		case GAMMA:     return standardIcons.getSprite(2,  29);
		case DELTA:	    return standardIcons.getSprite(3,  29);
		case EPSILON:   return standardIcons.getSprite(4,  29);
		case ZETA:      return standardIcons.getSprite(5,  29);
		case ETA:       return standardIcons.getSprite(6,  29);
		case THETA:     return standardIcons.getSprite(7,  29);
		case IOTA:      return standardIcons.getSprite(8,  29);
		case KAPPA:     return standardIcons.getSprite(9,  29);
		case LAMBDA:    return standardIcons.getSprite(10, 29);
		case MU:        return standardIcons.getSprite(11, 29);
		case NU:        return standardIcons.getSprite(0,  30);
		case XI:        return standardIcons.getSprite(1,  30);
		case OMICRON:   return standardIcons.getSprite(2,  30);
		case PI:        return standardIcons.getSprite(3,  30);
		case RHO:       return standardIcons.getSprite(4,  30);
		case SIGMA:     return standardIcons.getSprite(5,  30);
		case TAU:       return standardIcons.getSprite(6,  30);
		case UPSILON:   return standardIcons.getSprite(7,  30);
		case PHI:       return standardIcons.getSprite(8,  30);
		case CHI:       return standardIcons.getSprite(9,  30);
		case PSI:       return standardIcons.getSprite(10, 30);
		case OMEGA:     return standardIcons.getSprite(11, 30);
		
		default:
			Console.err("FrameworkSprites -> getGreekIcon() -> invalid case : " + greekIcon);
			return error;
		}
	}
	
	public static BufferedImage getEmogi(EmojiIcons emojiIcon) {
		switch(emojiIcon) {
		case CONFUSED:        return standardIcons.getSprite(0, 14);
		case SNEEZE:          return standardIcons.getSprite(1, 14);
		case TEMPERATURE:     return standardIcons.getSprite(2, 14);
		case DRUNK:           return standardIcons.getSprite(3, 14);
		case SICK:            return standardIcons.getSprite(4, 14);
		case VOMIT:           return standardIcons.getSprite(5, 14);
		case DEAD:            return standardIcons.getSprite(6, 14);
		case GRIN:            return standardIcons.getSprite(0, 15);
		case HAPPY:           return standardIcons.getSprite(1, 15);
		case INDIFFERENT:     return standardIcons.getSprite(2, 15);
		case CONCERN:         return standardIcons.getSprite(3, 15);
		case UNHAPPY:         return standardIcons.getSprite(4, 15);
		case SAD:             return standardIcons.getSprite(5, 15);
		case ANGRY:           return standardIcons.getSprite(6, 15);
		case HOT:             return standardIcons.getSprite(0, 16);
		case SHOCKED:         return standardIcons.getSprite(1, 16);
		case LOVE:            return standardIcons.getSprite(2, 16);
		case SLEEP:           return standardIcons.getSprite(3, 16);
		case DROOL:           return standardIcons.getSprite(4, 16);
		case INSPECT:         return standardIcons.getSprite(5, 16);
		case SALUTE:          return standardIcons.getSprite(6, 16);
		case CONTENT:         return standardIcons.getSprite(0, 17);
		case INJURED:         return standardIcons.getSprite(1, 17);
		case BLUSH:           return standardIcons.getSprite(2, 17);
		case UNCOMFORTABLE:   return standardIcons.getSprite(3, 17);
		case SURPRISED:       return standardIcons.getSprite(4, 17);
		case MINDBLOWN:       return standardIcons.getSprite(5, 17);
		case LIAR:            return standardIcons.getSprite(6, 17);
		case COLD:            return standardIcons.getSprite(0, 18);
		case ASTONISHED:      return standardIcons.getSprite(1, 18);
		case PLEADING:        return standardIcons.getSprite(2, 18);
		case CRYING:          return standardIcons.getSprite(3, 18);
		case PRESERVING:      return standardIcons.getSprite(4, 18);
		case DEAMON:          return standardIcons.getSprite(5, 18);
		case DISGUISE:        return standardIcons.getSprite(6, 18);
		case EYEROLL:         return standardIcons.getSprite(0, 19);
		case YAWN:            return standardIcons.getSprite(1, 19);
		case COOL:            return standardIcons.getSprite(2, 19);
		case SHAKING:         return standardIcons.getSprite(3, 19);
		case CLOWN:           return standardIcons.getSprite(4, 19);
		case DEVIL:           return standardIcons.getSprite(5, 19);
		case BLANK:        	  return standardIcons.getSprite(6, 19);
		default: 
			Console.err("FrameworkSprites -> getEmoji() -> invalid case : " + emojiIcon);
			return error;
		}
	}
	
	public static BufferedImage getKeyboardIcon(KeyboardIcons keyboardIcon) {
		switch(keyboardIcon) {
		case A: return standardIcons.getSprite(0 , 2);
		case B: return standardIcons.getSprite(1 , 2);
		case C: return standardIcons.getSprite(2 , 2);
		case D: return standardIcons.getSprite(3 , 2);
		case E: return standardIcons.getSprite(4 , 2);
		case F: return standardIcons.getSprite(5 , 2);
		case G: return standardIcons.getSprite(6 , 2);
		case H: return standardIcons.getSprite(7 , 2);
		case I: return standardIcons.getSprite(8 , 2);
		case J: return standardIcons.getSprite(9 , 2);
		case K: return standardIcons.getSprite(10, 2);
		case L: return standardIcons.getSprite(11, 2);
		case M: return standardIcons.getSprite(0 , 3);  
		case N: return standardIcons.getSprite(1 , 3);  
		case O: return standardIcons.getSprite(2 , 3);  
		case P: return standardIcons.getSprite(3 , 3);  
		case Q: return standardIcons.getSprite(4 , 3);  
		case R: return standardIcons.getSprite(5 , 3);  
		case S: return standardIcons.getSprite(6 , 3);  
		case T: return standardIcons.getSprite(7 , 3);  
		case U: return standardIcons.getSprite(8 , 3);  
		case V: return standardIcons.getSprite(9 , 3);  
		case W: return standardIcons.getSprite(10, 3); 
		case X: return standardIcons.getSprite(11, 3); 
		case Y: return standardIcons.getSprite(0 , 4); 
		case Z: return standardIcons.getSprite(1 , 4); 
		 
		case a: return standardIcons.getSprite(0 ,5);
		case b: return standardIcons.getSprite(1 ,5);
		case c: return standardIcons.getSprite(2 ,5);
		case d: return standardIcons.getSprite(3 ,5);
		case e: return standardIcons.getSprite(4 ,5);
		case f: return standardIcons.getSprite(5 ,5);
		case g: return standardIcons.getSprite(6 ,5);
		case h: return standardIcons.getSprite(7 ,5);
		case i: return standardIcons.getSprite(8 ,5);
		case j: return standardIcons.getSprite(9 ,5);
		case k: return standardIcons.getSprite(10,5);
		case l: return standardIcons.getSprite(11,5);
		case m: return standardIcons.getSprite(0 ,6);
		case n: return standardIcons.getSprite(1 ,6);
		case o: return standardIcons.getSprite(2 ,6);
		case p: return standardIcons.getSprite(3 ,6);
		case q: return standardIcons.getSprite(4 ,6);
		case r: return standardIcons.getSprite(5 ,6);
		case s: return standardIcons.getSprite(6 ,6);
		case t: return standardIcons.getSprite(7 ,6);
		case u: return standardIcons.getSprite(8 ,6);
		case v: return standardIcons.getSprite(9 ,6);
		case w: return standardIcons.getSprite(10,6);
		case x: return standardIcons.getSprite(11,6);
		case y: return standardIcons.getSprite(0 ,7);
		case z: return standardIcons.getSprite(1 ,7);
		
		case F1:  return standardIcons.getSprite(0 , 11);  
		case F2:  return standardIcons.getSprite(1 , 11);  
		case F3:  return standardIcons.getSprite(2 , 11);  
		case F4:  return standardIcons.getSprite(3 , 11);  
		case F5:  return standardIcons.getSprite(4 , 11);  
		case F6:  return standardIcons.getSprite(5 , 11);  
		case F7:  return standardIcons.getSprite(6 , 11);  
		case F8:  return standardIcons.getSprite(7 , 11);  
		case F9:  return standardIcons.getSprite(8 , 11);  
		case F10: return standardIcons.getSprite(9 , 11);  
		case F11: return standardIcons.getSprite(10, 11); 
		case F12: return standardIcons.getSprite(11, 11); 
		 
		case ONE:   return standardIcons.getSprite(0, 5);  
		case TWO:   return standardIcons.getSprite(1, 5);  
		case THREE: return standardIcons.getSprite(2, 5);  
		case FOUR:  return standardIcons.getSprite(3, 5);  
		case FIVE:  return standardIcons.getSprite(4, 5);  
		case SIX:   return standardIcons.getSprite(5, 5);  
		case SEVEN: return standardIcons.getSprite(6, 5);  
		case EIGHT: return standardIcons.getSprite(7, 5);  
		case NINE:  return standardIcons.getSprite(8, 5);  
		case ZERO:  return standardIcons.getSprite(9, 5);  
		            
		case MINUS:		return standardIcons.getSprite(0, 10);
		case EQUALS:	return standardIcons.getSprite(0, 11);
		case PLUS:   	return standardIcons.getSprite(1, 11);
		case PERCENTAGE:return standardIcons.getSprite(1, 4);
		
		case EXCLAMATION: 	return standardIcons.getSprite(0,  1);
		case AT:			return standardIcons.getSprite(1,  1);
		case HASHTAG:       return standardIcons.getSprite(2,  1);
		case AND:			return standardIcons.getSprite(1,  6);
		case COMMA:			return standardIcons.getSprite(4,  8);
		case APOSTROPHE:    return standardIcons.getSprite(3,  8);
		case QUOTE:         return standardIcons.getSprite(3,  9);
		case STOP:          return standardIcons.getSprite(5,  8);
		case QUESTION:      return standardIcons.getSprite(6,  9);
		case ASTERISK:      return standardIcons.getSprite(7,  1);
		case UNDERSCORE:    return standardIcons.getSprite(10, 1);
		case COLON:         return standardIcons.getSprite(2,  9);
		case SEMI_COLON:    return standardIcons.getSprite(2,  8);
		case TILDE:         return standardIcons.getSprite(1,  10);
		case PIPE:          return standardIcons.getSprite(7,  9);
		
		case FORWARD_SLASH:		return standardIcons.getSprite(6,  8);
		case BACK_SLASH:        return standardIcons.getSprite(7,  8);
		case BRACKET_LEFT:      return standardIcons.getSprite(0,  8);
		case BRACKET_RIGHT:     return standardIcons.getSprite(1,  8);
		case PARENTHESIS_LEFT:  return standardIcons.getSprite(0,  1);
		case PARENTHESIS_RIGHT: return standardIcons.getSprite(1,  1);
		case BRACE_LEFT:        return standardIcons.getSprite(0,  9);
		case BRACE_RIGTH:       return standardIcons.getSprite(1,  9);
		case CHEVRON_LEFT:      return standardIcons.getSprite(4,  9);
		case CHEVRON_RIGHT:     return standardIcons.getSprite(5,  9);
		
		case INS:			return standardIcons.getSprite(2,   7);		
		case DEL:           return standardIcons.getSprite(3,   7);
		case HOME:          return standardIcons.getSprite(4,   7);
		case PAUSE:			return standardIcons.getSprite(2,  10);
		case END:           return standardIcons.getSprite(5,   7);
		case PAGE_UP:       return standardIcons.getSprite(6,   7);
		case PAGE_DOWN:     return standardIcons.getSprite(7,   7);
		case PRINT:         return standardIcons.getSprite(8,   7);
		case SCRL_LOCK:     return standardIcons.getSprite(9,   7);
		case CAPS_LOCK:		return standardIcons.getSprite(8,   9);
		case BREAK:         return standardIcons.getSprite(10,  7);
		case NUM_LOCK:      return standardIcons.getSprite(11,  7);
		case SHIFT:         return standardIcons.getSprite(3,  10);
		case CTRL:          return standardIcons.getSprite(4,  10);
		case ALT:           return standardIcons.getSprite(5,  10);
		case ESC:           return standardIcons.getSprite(6,  10);
		case ENTER:         return standardIcons.getSprite(7,  10);
		case WIN:           return standardIcons.getSprite(8,  10);
		case OPT:           return standardIcons.getSprite(9,  10);
		case BACK_SPACE:    return standardIcons.getSprite(10, 10);
		case SPACE:         return standardIcons.getSprite(11, 10);
		case TAB:           return standardIcons.getSprite(8,   8);
		case SHIFT_L:       return standardIcons.getSprite(9,   8);
		case CTRL_L:        return standardIcons.getSprite(10,  8);
		case ALT_L:         return standardIcons.getSprite(11,  8);
		case SHIFT_R:       return standardIcons.getSprite(9,   9);
		case CTRL_R:        return standardIcons.getSprite(10,  9);
		case ALT_R:         return standardIcons.getSprite(11,  9);
		
		case ARROW_UP:					return standardIcons.getSprite(5,  4);
		case ARROW_RIGHT:				return standardIcons.getSprite(6,  4);
		case ARROW_DOWN:				return standardIcons.getSprite(7,  4);
		case ARROW_LEFT:				return standardIcons.getSprite(8,  4);
		case ARROW_CLOCKWISE:			return standardIcons.getSprite(10, 4);
		case ARROW_COUNTER_CLOCKWISE:	return standardIcons.getSprite(11, 4);
		
		case ARROW_ALT_CLOCKWISE:         return standardIcons.getSprite(0,  26);  
		case ARROW_ALT_UP:                return standardIcons.getSprite(1,  26);  
		case ARROW_ALT_COUNTER_CLOCKWISE: return standardIcons.getSprite(2,  26);  
		case ARROW_ALT_LEFT:              return standardIcons.getSprite(0,  27);  
		case ARROW_ALT_DOWN:              return standardIcons.getSprite(1,  27);  
		case ARROW_ALT_RIGHT:             return standardIcons.getSprite(2,  27);
		
		default:
			Console.err("FrameworkSprites -> getKeyboardIcon() -> invalid case : " + keyboardIcon);
			return error;
		}
	}
	
	public static BufferedImage getStandardIcon(StandardIcons standardIcon) {
		switch(standardIcon) {		
		case SIGNAL_0: return standardIcons.getSprite(6, 25);
		case SIGNAL_1: return standardIcons.getSprite(7, 25);
		case SIGNAL_2: return standardIcons.getSprite(8, 25);
		case SIGNAL_3: return standardIcons.getSprite(9, 25);
		case SIGNAL_4: return standardIcons.getSprite(10, 25);
		case SIGNAL_5: return standardIcons.getSprite(11, 25);
		
		case CHECKBOX_CHECKED:      return standardIcons.getSprite(3, 26);
		case RADIO_CHECKED:      	return standardIcons.getSprite(4, 26);
		case RADIO_ALT_CHECKED:     return standardIcons.getSprite(5, 26);
		case CHECKBOX_UNCHECKED: 	return standardIcons.getSprite(3, 27);	
		case RADIO_UNCHECKED:       return standardIcons.getSprite(4, 27);
		case RADIO_ALT_UNCHECKED:   return standardIcons.getSprite(5, 27);
		
		case INFORMATION:   	  return standardIcons.getSprite(3, 1);
		case CLOSE:               return standardIcons.getSprite(2, 4); 
		case MASS:                return standardIcons.getSprite(3, 4);  
		case REFRESH:             return standardIcons.getSprite(4, 4);  
		case TRASH:               return standardIcons.getSprite(9, 4);  
		case SYSTEM:              return standardIcons.getSprite(0, 12);  
		case MOVE:                return standardIcons.getSprite(1, 12);  
		case KEY:                 return standardIcons.getSprite(2, 12);  
		case HOUSE:               return standardIcons.getSprite(3, 12);  
		case LOCKED:              return standardIcons.getSprite(4, 12);  
		case UNLOCKED:            return standardIcons.getSprite(5, 12);  
		case COG:                 return standardIcons.getSprite(6, 12);  
		case SPANNER:             return standardIcons.getSprite(7, 12);  
		case CALLANDER:           return standardIcons.getSprite(8, 12);  
		case CLOCK:               return standardIcons.getSprite(9, 12);  
		case SATELLITE:           return standardIcons.getSprite(10, 12);  
		case RADIO:               return standardIcons.getSprite(11, 12);  
		case COLLISION:           return standardIcons.getSprite(0, 13);  
		case LIGHTNING:           return standardIcons.getSprite(1, 13);  
		case RESISTOR:            return standardIcons.getSprite(2, 13);  
		case EXCLAMATION_PIN:     return standardIcons.getSprite(3, 13);  
		case IMAGE:               return standardIcons.getSprite(4, 13);  
		case LIST:                return standardIcons.getSprite(5, 13);  
		case PARTICLES:           return standardIcons.getSprite(6, 13);  
		case RADIATE:             return standardIcons.getSprite(10, 13);  
		case FIRE:                return standardIcons.getSprite(11, 13);  
		case CHAT_BUBBLE:         return standardIcons.getSprite(10, 14);  
		case CHAT_BUBBLE_ALT:     return standardIcons.getSprite(2, 25);  
		case SKULL_AND_CROSSBONE: return standardIcons.getSprite(11, 14);               
		case STOPWATCH:           return standardIcons.getSprite(3, 22);  
		case CLOCK_WALL:          return standardIcons.getSprite(4, 22);  
		case CLOCK_MOUNT:         return standardIcons.getSprite(5, 22);  
		case DNA:                 return standardIcons.getSprite(6, 23);  
		case RADIATION:           return standardIcons.getSprite(3, 23);  
		case RADIATION_ALT:       return standardIcons.getSprite(4, 23);  
		case RADIATION_WASTE:     return standardIcons.getSprite(5, 23);  
		case PORTAL:			  return standardIcons.getSprite(0, 25);
		case STAIRS:			  return standardIcons.getSprite(1, 25);	
		case MAP:                 return standardIcons.getSprite(3, 25);  
		case QRCODE:              return standardIcons.getSprite(4, 25);  
		case BARCODE:             return standardIcons.getSprite(5, 25);  
		
		case MAGNET:		return standardIcons.getSprite(6,  22);       
		case SCREWDRIVER:   return standardIcons.getSprite(7,  22);
		case SAW:           return standardIcons.getSprite(8,  22);
		case HAMMER:        return standardIcons.getSprite(9,  22);
		case AXE:           return standardIcons.getSprite(10, 22);
		case PICKAXE:       return standardIcons.getSprite(11, 22);
		case SPADE:         return standardIcons.getSprite(7,  22);
		case TROWEL:        return standardIcons.getSprite(8,  22);
		case BROOM:         return standardIcons.getSprite(9,  22);
		
		case GAUGE_OPTIMAL:    	return standardIcons.getSprite(8, 13);
		case GAUGE_MAX:        	return standardIcons.getSprite(8, 14);
		case GAUGE_MIN:        	return standardIcons.getSprite(8, 15);
		                  
		case THERMOMETER_OPTIMAL: return standardIcons.getSprite(9, 13);  
		case THERMOMETER_HOT:     return standardIcons.getSprite(9, 14);  
		case THERMOEMETER_COLD:   return standardIcons.getSprite(9, 15);  
		                       
		case GAUGE_ALT_GOOD:    return standardIcons.getSprite(10, 13);
		case GAUGE_ALT_BAD:     return standardIcons.getSprite(10, 14);
		                           
		case BITCOIN:   return standardIcons.getSprite(6, 27);          
		case DOGECOIN:  return standardIcons.getSprite(8, 27);           
		case ETHER:     return standardIcons.getSprite(6, 28);           
		case RUBLE:     return standardIcons.getSprite(11, 27);           
		case RUPEE:     return standardIcons.getSprite(7, 28);           
		case YUAN:      return standardIcons.getSprite(7, 26);           
		case DONG:      return standardIcons.getSprite(9, 26);           
		case EURO:      return standardIcons.getSprite(11, 27);           
		case BAHT:      return standardIcons.getSprite(8, 27);           
		case DOLLAR:    return standardIcons.getSprite(7, 28);           
		case CENT:      return standardIcons.getSprite(10, 27);           
		case POUND:     return standardIcons.getSprite(6, 26);
		case YEN:       return standardIcons.getSprite(7, 26);      
		case PESO:		return standardIcons.getSprite(10, 26);
		case WON:		return standardIcons.getSprite(8, 28);
		case AUSTRAL:   return standardIcons.getSprite(9, 27);
		case TENGE:   	return standardIcons.getSprite(9, 28);
		
		case HEART_MENDED:  return standardIcons.getSprite(11, 20);
		case HEART_BLACK:   return standardIcons.getSprite(5, 21);
		case HEART_WHITE:   return standardIcons.getSprite(6, 21);
		case HEART_GREEN:   return standardIcons.getSprite(7, 21);
		case HEART_BLUE:    return standardIcons.getSprite(8, 21);
		case HEART_YELLOW:  return standardIcons.getSprite(9, 21);
		case HEART_RED:     return standardIcons.getSprite(10, 21);
		case HEART_BROKEN:  return standardIcons.getSprite(11, 21);
		
		case THUMBS_UP:				return standardIcons.getSprite( 9, 15);
		case THUMBS_DOWN:     		return standardIcons.getSprite(11, 15);
		case POINT_UP:        		return standardIcons.getSprite(10, 15);
		case HANDS_LOVE:            return standardIcons.getSprite( 7, 16);
		case HANDS_BIRD:     		return standardIcons.getSprite( 8, 16);
		case POINT_LEFT:      		return standardIcons.getSprite( 9, 16);
		case POINT_DOWN:     	 	return standardIcons.getSprite(10, 16);
		case POINT_RIGHT:     		return standardIcons.getSprite(11, 16);
		case HANDS_CLAP:            return standardIcons.getSprite( 7, 17);
		case HANDS_SHAKE:           return standardIcons.getSprite( 8, 17);
		case HANDS_PUNCH:           return standardIcons.getSprite( 9, 17);
		case HANDS_KNOCK:           return standardIcons.getSprite(10, 17);
		case HANDS_POINT:           return standardIcons.getSprite(11, 17);
		case HANDS_RADICAL:         return standardIcons.getSprite( 7, 18);
		case HANDS_ROCK_AND_ROLL:   return standardIcons.getSprite( 8, 18);
		case HANDS_SNAP:            return standardIcons.getSprite( 9, 18);
		case HANDS_CROSS_FINGERS:   return standardIcons.getSprite(10, 18);
		case HANDS_PEACE:           return standardIcons.getSprite(11, 18);
		case HANDS_COME_ON:         return standardIcons.getSprite( 7, 19);
		case HANDS_PALM:            return standardIcons.getSprite( 8, 19);
		case HANDS_SPOK:            return standardIcons.getSprite( 9, 19);
		case HANDS_RECIVE:          return standardIcons.getSprite(10, 19);
		case HANDS_OK:              return standardIcons.getSprite(11, 19);
		
		case FOOT:   return standardIcons.getSprite(0,  20);
		case LEG:    return standardIcons.getSprite(1,  20);
		case ARM:    return standardIcons.getSprite(2,  20);
		case EAR:    return standardIcons.getSprite(3,  20);
		case NOSE:   return standardIcons.getSprite(4,  20);
		case BONE:   return standardIcons.getSprite(5,  20);
		case TOOTH:  return standardIcons.getSprite(6,  20);
		case LUNG:   return standardIcons.getSprite(7,  20);
		case HEART:  return standardIcons.getSprite(8,  20);
		case BRAIN:  return standardIcons.getSprite(9,  20);
		case EYE:    return standardIcons.getSprite(10, 20);
		
		case PAIN_BACK:        return standardIcons.getSprite(5, 24);
		case PAIN_EYE:        return standardIcons.getSprite( 6, 24); 
		case PAIN_HEAD:        return standardIcons.getSprite(7, 24);  
		case PAIN_EAR:         return standardIcons.getSprite(8, 24);  
		case PAIN_FOOT:        return standardIcons.getSprite(9, 24);  
		case PAIN_KNEE:        return standardIcons.getSprite(10, 24);  
		case PAIN_BROKENBONE:  return standardIcons.getSprite(11, 24);
			
		case PLANET:            return standardIcons.getSprite( 0, 21);
		case SUN:               return standardIcons.getSprite( 1, 21);
		case MOON_CRESENT:      return standardIcons.getSprite( 2, 21);
		case PLANET_ALIEN:      return standardIcons.getSprite( 0, 22);
		case STAR:              return standardIcons.getSprite( 1, 22);
		case MOON_FULL:         return standardIcons.getSprite( 2, 22);
		case BLACKHOLE:         return standardIcons.getSprite( 0, 23);
		case PLANET_ALT:        return standardIcons.getSprite( 1, 23);
		case ATOM:				return standardIcons.getSprite( 2, 23);
		case SHIP_ALIEN:        return standardIcons.getSprite( 0, 24);
		case SHIP_SPACE_ALT:    return standardIcons.getSprite( 1, 24);
		case SHIP_SPACE:        return standardIcons.getSprite( 2, 24);
		case ANTENA:            return standardIcons.getSprite( 3, 24);
		case ANTENA_ALT: 	    return standardIcons.getSprite( 4, 24);			
			
		case FLAG_BROWN:	return standardIcons.getSprite(0, 28);	 
		case FLAG_RED: 		return standardIcons.getSprite(1, 28);
		case FLAG_GREEN: 	return standardIcons.getSprite(2, 28);
		case FLAG_BLUE: 	return standardIcons.getSprite(3, 28);
		case FLAG_PURPLE:	return standardIcons.getSprite(4, 28);
		case FLAG_TEAL:  	return standardIcons.getSprite(5, 28);
				
		default:
			Console.err("FrameworkSprites -> getStandardIcon() -> invalid case : " + standardIcon);
			return error;
		}
	}
	
	public static void loadFrameworkSprites() {
		Console.ln("FrameworkSprites -> loadFrameworkSprites() -> loading..");
		standardIcons		= new WarpedSpriteSheet("res/framework/graphics/standard_icons_wf2d_30_30_wf.png");                                                   
		dialIcons	        = new WarpedSpriteSheet("res/framework/graphics/small_compass_wf2d_100_100_wf.png");
		anglePicker 		= new WarpedSpriteSheet("res/framework/graphics/angle_picker_wf2d_200_200_wf.png");
		tileTransitions 	= new WarpedSpriteSheet("res/framework/graphics/transitions_wf2d_64_64_wf.png");		
		mouseLoad		 	= new WarpedSpriteSheet("res/framework/graphics/mouse_load_wf2d_192_192_wf.png");
		mouse				= new WarpedSpriteSheet("res/framework/graphics/mouse_wf2d_100_130_wf.png");
		
		trayIcon 			= UtilsImage.loadBufferedImage("res/framework/graphics/tray_icon.png");
		loadBackground	    = UtilsImage.loadBufferedImage("res/framework/graphics/load_background.png");
		error 				= UtilsImage.loadBufferedImage("res/framework/graphics/error.png");	
	}
	
	public static void loadMediaPlayerSprites() {
		Console.ln("FrameworkSprites -> loadMediaPlayerSprites() -> loading..");
		mediaIcons			= new WarpedSpriteSheet("res/framework/graphics/media_icons_wf2d_38_38_wf.png"); 		
	}
	
	
	
		
		
	 	
		
		
		
		
		
	
	
}
