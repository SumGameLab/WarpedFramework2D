/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.tile;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public enum TileTransitionType {
	
	UP,
	DOWN,
	LEFT,
	RIGHT,
	
	INNER_1,
	INNER_2,
	INNER_3,
	INNER_4,
	
	OUTER_1,
	OUTER_2,
	OUTER_3,
	OUTER_4,
	
	NONE;
	
	private static final int LEFT_GIVER 				= 0;
	private static final int LEFT_INVERT_GIVER			= 1;
	public static  final int LEFT_INVERT_RANDOM_GIVER 	= 2;
	private static final int ABOVE_GIVER 				= 3;
	private static final int PRIMARY_GIVER          	= 4;
	private static final int PRIMARY_RANDOM_GIVER 		= 5;
	private static final int SECONDARY_GIVER			= 6;
	private static final int SECONDARY_RANDOM_GIVER 	= 7;
	
	private static Map<Integer, TileTransitionType> map = new HashMap<>();
	static {
		for (TileTransitionType type : TileTransitionType.values()) {
            map.put(type.ordinal(), type);
        }
	}
	
	public static TileTransitionType get(int index) {return (TileTransitionType) map.get(index);}
	public static int size() {return map.size();}

	public BufferedImage getTileTransition() {return getTileTransition(this);}
	public static BufferedImage getTileTransition(TileTransitionType transitionType) {
		switch(transitionType) {
		case DOWN:	  return FrameworkSprites.tileTransitions.getSprite(0,0);
		case UP:	  return FrameworkSprites.tileTransitions.getSprite(0,1);
		case LEFT:	  return FrameworkSprites.tileTransitions.getSprite(1,1);
		case RIGHT:	  return FrameworkSprites.tileTransitions.getSprite(1,0);
			
		case INNER_1: return FrameworkSprites.tileTransitions.getSprite(2,0);			
		case INNER_2: return FrameworkSprites.tileTransitions.getSprite(3,0);
		case INNER_3: return FrameworkSprites.tileTransitions.getSprite(3,1); 
		case INNER_4: return FrameworkSprites.tileTransitions.getSprite(2,1);
					
		case OUTER_1: return FrameworkSprites.tileTransitions.getSprite(4,0);
		case OUTER_2: return FrameworkSprites.tileTransitions.getSprite(5,0);
		case OUTER_3: return FrameworkSprites.tileTransitions.getSprite(5,1);		
		case OUTER_4: return FrameworkSprites.tileTransitions.getSprite(4,1);
		default:
			Console.err("TileTransitionType -> getTileTransition() -> invalid case : " + transitionType);
			return null;
		}
	}

	/**Does a transition of the input type in the input relative position connect to the tile*/
	public boolean connects(TileTransitionType relativePos) {return connects(this, relativePos);}
	public static boolean connects(TileTransitionType transition, TileTransitionType relativePos) {
		if(relativePos == LEFT) {
			switch(transition) {
			case DOWN: 	  return true;	
			case INNER_1: return true;
			case INNER_4: return true;
			case OUTER_1: return true;
			case OUTER_4: return true;
			case UP: 	  return true; 
			
			case NONE:    return false;
			case INNER_2: return false;
			case INNER_3: return false;
			case LEFT:	  return false;
			case OUTER_2: return false;
			case OUTER_3: return false;
			case RIGHT:   return false;
			default:
				Console.err("TileTransitionType -> connects() -> relativePos : LeftTile -> invalid case : " + transition);
				return false;
			}
		}
		if(relativePos == UP) {
			switch(transition) {
			case LEFT:    return true;
			case RIGHT:   return true;
			case INNER_1: return true;
			case INNER_2: return true;
			case OUTER_1: return true;
			case OUTER_2: return true;

			case NONE:    return false;
			case UP:      return false;
			case DOWN: 	  return false;
			case INNER_3: return false;
			case INNER_4: return false;
			case OUTER_3: return false;
			case OUTER_4: return false;
			default:
				Console.err("TileTransitionType -> connects() -> relativePos : AboveTile -> invalid case : " + transition);
				return false;
			}
		}		
		Console.err("TileTransitionType -> connects() -> relativePos is not valid : " + relativePos);
		return false;
	}
		

	/*
	public static TileCelestial getConnectingTile(WarpedTile leftTile, WarpedTile aboveTile, WarpedTile aboveRightTile) {
		if(leftTile == null && aboveTile == null) {
			Console.err("TileTransitionType -> getConnectingTile() -> leftTile && aboveTile is null");
			return null;
		}
		
		boolean isAllMatching = true;
		boolean isAboveMatching = true;
		boolean isInverseType = true;
		
		if(leftTile == null || aboveTile == null) isInverseType = false;
		else isInverseType = leftTile.isInverseType(aboveTile);
		if(leftTile  != null && !leftTile.isPureTile()  && aboveRightTile != null && !aboveRightTile.isPureTile()) if(!leftTile.isMatchingType(aboveRightTile)) isAllMatching = false;
		if(aboveTile != null && !aboveTile.isPureTile() && aboveRightTile != null && !aboveRightTile.isPureTile()) if(!aboveTile.isMatchingType(aboveRightTile)) {
			isAllMatching = false; 
			isAboveMatching = false; 
		}
		
		TileTransitionType leftTransition = null;
		TileTransitionType aboveTransition = null;
		TileTransitionType aboveRightTransition = null;
		
		if(aboveTile == null) aboveTransition = getPossibleAbove(leftTile.getTransitionType());
		else aboveTransition = aboveTile.getTransitionType();
		
		if(leftTile == null) leftTransition = getPossibleLeft(aboveTile.getTransitionType()); 
		else leftTransition = leftTile.getTransitionType();
		
		if(aboveRightTile == null) aboveRightTransition = getPossibleRight(aboveTransition);
		else aboveRightTransition = aboveRightTile.getTransitionType();
		
		TileTransitionType resultTransition = getConnectingTransition(leftTransition, aboveTransition, aboveRightTransition, isAllMatching, isAboveMatching, isInverseType);
		int typeGiver = getTileTypeGiver(resultTransition, leftTransition, aboveTransition, aboveRightTransition, isInverseType);
		
		TileType primaryType = null;;
		TileType secondaryType = null;
		
		WarpedTile tileType = null;
		
		switch(typeGiver) {
		case LEFT_GIVER:    
			if(leftTile == null) tileType = aboveTile;
			else tileType  = leftTile;
			primaryType   = tileType.getPrimaryType();
			secondaryType = tileType.getSecondaryType();
			break;
		case LEFT_INVERT_GIVER:
			if(leftTile == null) tileType = aboveTile;
			else tileType  = leftTile;
			primaryType   = tileType.getSecondaryType();
			secondaryType = tileType.getPrimaryType();
			break;
		case LEFT_INVERT_RANDOM_GIVER:
			if(leftTile == null) tileType = aboveTile;
			else tileType  = leftTile;
			primaryType   = tileType.getSecondaryType();
			secondaryType = tileType.getParent().getRandomTransition(primaryType);
			break;
		case ABOVE_GIVER:       
			if(aboveTile == null) tileType = leftTile;
			else tileType = aboveTile;
			primaryType =   tileType.getPrimaryType();
			secondaryType = tileType.getSecondaryType();
			break;
		case PRIMARY_GIVER:
			if(leftTile == null) tileType = aboveTile;
			else tileType  = leftTile;
			primaryType = tileType.getPrimaryType();
			secondaryType = primaryType;
			break;
		case SECONDARY_GIVER:
			if(leftTile == null) tileType = aboveTile;
			else tileType  = leftTile;
			primaryType = tileType.getSecondaryType();
			secondaryType = primaryType;
			break;
		case PRIMARY_RANDOM_GIVER:
			if(leftTile == null) tileType = aboveTile;
			else tileType  = leftTile;
			primaryType = tileType.getPrimaryType();
			secondaryType = tileType.getParent().getRandomTransition(primaryType);
			break;
		case SECONDARY_RANDOM_GIVER:
			if(leftTile == null) tileType = aboveTile;
			else tileType  = leftTile;
			secondaryType = tileType.getSecondaryType();
			primaryType = tileType.getParent().getRandomTransition(secondaryType);
			break;
		default:
			Console.err("TileTransitionType -> getConnectingTile -> invalid type givver : " + typeGiver);
			break;		
		}
		
		//Console.ln("TIleTransitionType -> getConnectingTile() -> (primaryType, secondaryType, transition) :  ( " + primaryType + ", " + secondaryType + ", " +  resultTransition + ") ");
		WarpedTileSet set = tileType.getParent();
		return set.generateCelestialTile(parent, primaryType, secondaryType, resultTransition);		
	}
	*/
	
	private static int errorCount = 0;
	public static TileTransitionType getConnectingTransition(TileTransitionType left, TileTransitionType above, TileTransitionType aboveRight, boolean isAllMatching, boolean isAboveMatching, boolean isInverseType) {
		switch(left) {
		case DOWN:
			switch(above) {
			case DOWN:
				return DOWN;
			case OUTER_3: case OUTER_4:
				if(isAboveMatching) return DOWN;
				else return INNER_2;
			case LEFT: case OUTER_1: case INNER_2: case RIGHT: case INNER_1: case OUTER_2:
				return OUTER_3;
			case UP: case INNER_3: case INNER_4: case NONE:    
				if(isAllMatching && UtilsMath.coinFlip()) return DOWN;
				return INNER_2;
			default:
				errorCount++;
				Console.err("TileTransitionType -> getConnectionTransiton() ->  left : " + left + " -> above : " + above + " -> isMatching : " + isAllMatching + " -> error count : " + errorCount);
				return NONE;
			}

		case RIGHT:
			switch(above) {
			case UP:
				//this is a test
				return NONE;
			case INNER_4:
				if(isAllMatching && UtilsMath.coinFlip()) return INNER_1;
				else return NONE;
			case INNER_3:
				if(isAllMatching && !aboveRight.connects(UP) && UtilsMath.coinFlip()) return INNER_1;
				else return NONE;
			case RIGHT:
				if(isAllMatching && UtilsMath.coinFlip()) return INNER_4;
				else return RIGHT; 
			case INNER_1:
				if(isAllMatching && UtilsMath.coinFlip()) return INNER_4;
				else return RIGHT;
			case NONE:
				if(isAllMatching && !aboveRight.connects(UP) && UtilsMath.coinFlip()) return INNER_1;
				else return NONE;
			case DOWN: case OUTER_3: case OUTER_4: 
				if(UtilsMath.coinFlip()) return NONE;
				else return OUTER_1;
			case LEFT: 
				if(isAboveMatching && UtilsMath.coinFlip()) return OUTER_4;
				return LEFT;
			case INNER_2: case OUTER_1:
				if(isAllMatching && UtilsMath.coinFlip()) return OUTER_4;
				return LEFT;
			default:
				errorCount++;
				Console.err("TileTransitionType -> getConnectionTransiton() ->  left : " + left + " -> above : " + above + " -> isMatching : " + isAllMatching + " -> error count : " + errorCount);
				return NONE;
			}
			
		case LEFT:
			switch(above) {
			case OUTER_4: case DOWN:
				//THIS IS A TEST
				return NONE;
			case INNER_2: case LEFT:
				if(isAllMatching && UtilsMath.coinFlip()) return OUTER_4;
				else return LEFT;
			case RIGHT: case INNER_1: case OUTER_2: 
				if(isAboveMatching && UtilsMath.coinFlip()) return INNER_4;
				return RIGHT;
			case UP: case INNER_3: case INNER_4: case NONE:   
				if(isAllMatching && UtilsMath.coinFlip()) return INNER_1;
				return NONE;
			default:
				Console.err("TileTransitionType -> getConnectionTransiton() ->  left : " + left + " -> above : " + above + " -> isMatching : " + isAllMatching);
				return NONE;
			}
			
		case UP:
			switch(above) {
			case OUTER_2:
				return INNER_3;
				
			case RIGHT:	case INNER_1:
				return OUTER_3;
			case NONE:
				if(isAllMatching && UtilsMath.coinFlip()) return UP;
				else return INNER_2;	
			case DOWN: case OUTER_3: case OUTER_4: case INNER_4: case INNER_3: case UP:
				if(isAllMatching && UtilsMath.coinFlip()) return UP;
				else return OUTER_2;	
			case LEFT: case INNER_2: case OUTER_1: 
				return INNER_3;			
			default:
				errorCount++;
				Console.err("TileTransitionType -> getConnectionTransiton() ->  left : " + left + " -> above : " + above + " -> isMatching : " + isAllMatching + " -> error count : " + errorCount);
				return NONE;
			}
			
		case INNER_1:
			switch(above) {
			case INNER_2:
				//THIS IS A TEST
				return NONE;
			case DOWN:
				if(isAllMatching && UtilsMath.coinFlip()) return UP;
				else return INNER_2;
			case LEFT:
				if(isInverseType) return OUTER_3;
				if(UtilsMath.coinFlip()) return LEFT;
				else return INNER_3;
			case OUTER_1:
				if(isInverseType) return INNER_3;
				if(UtilsMath.coinFlip()) return LEFT;
				else return OUTER_4;
			case RIGHT: case INNER_1: case OUTER_2:
				return OUTER_3;
			case UP: case INNER_3: case INNER_4: case OUTER_4: case OUTER_3: case NONE:    
				if(isAllMatching && UtilsMath.coinFlip()) return DOWN;
				else return INNER_2;
			
			default:
				errorCount++;
				Console.err("TileTransitionType -> getConnectionTransiton() ->  left : " + left + " -> above : " + above + " -> isMatching : " + isAllMatching + " -> error count : " + errorCount);
				return NONE;
			}
			
		case INNER_2:
			switch(above) {
			case LEFT: case OUTER_1:
				if(isAllMatching && UtilsMath.coinFlip()) return OUTER_4;
				else return LEFT;
			case OUTER_3: case OUTER_4:	case DOWN:
				if(isAllMatching && UtilsMath.coinFlip()) return INNER_1;
				else return NONE;
			case INNER_2:
				if(isAllMatching && UtilsMath.coinFlip()) return OUTER_4;
				else return LEFT;
			case RIGHT: case INNER_1: case OUTER_2: 
				if(isAllMatching && UtilsMath.coinFlip()) return INNER_4;
				else return RIGHT;
			case UP: case INNER_3: case INNER_4: case NONE:
				if(UtilsMath.coinFlip()) return NONE;
				return INNER_1;			
			default:
				errorCount++;
				Console.err("TileTransitionType -> getConnectionTransiton() ->  left : " + left + " -> above : " + above + " -> isMatching : " + isAllMatching + " -> error count : " + errorCount);
				return NONE;
			}
			
		case INNER_3:
			switch(above) {
			case OUTER_4: case DOWN: case OUTER_3:
				//this is a test
				return NONE;
			case INNER_2:
				if(isAllMatching && UtilsMath.coinFlip()) return OUTER_4;
				else return LEFT;
			case LEFT:
				return LEFT;
			case RIGHT: case INNER_1: case OUTER_2: 
				if(isAllMatching && UtilsMath.coinFlip()) return INNER_4;
				else return RIGHT;
			case UP: case INNER_3: case INNER_4: case NONE:    
				if(UtilsMath.coinFlip()) return NONE;
				return INNER_1;		
			default:
				errorCount++;
				Console.err("TileTransitionType -> getConnectionTransiton() ->  left : " + left + " -> above : " + above + " -> isMatching : " + isAllMatching + " -> error count : " + errorCount);
				return NONE;
			}
			
		case INNER_4:
			switch(above) {
			case INNER_3:
				if(isAboveMatching && UtilsMath.coinFlip()) return DOWN;
				else return INNER_2;
				
			case RIGHT:
				return OUTER_3;
			case INNER_1:
				return INNER_3;
			case INNER_4:
				return INNER_2;
			case NONE: case UP: 
				if(isAllMatching && UtilsMath.coinFlip()) return UP;
				else return INNER_2;
			case DOWN: case OUTER_3: case OUTER_4: 
				if(isAllMatching && UtilsMath.coinFlip()) return UP;
				else return OUTER_2;
			case LEFT: case INNER_2: case OUTER_1: 
				return INNER_3;
			default:
				errorCount++;
				Console.err("TileTransitionType -> getConnectionTransiton() ->  left : " + left + " -> above : " + above + " -> isMatching : " + isAllMatching + " -> error count : " + errorCount);
				return NONE;
			}
			
		case OUTER_1:
			switch(above) {
				
			case INNER_4:
				if(isAllMatching && UtilsMath.coinFlip()) return DOWN;
				else return INNER_2;
			case NONE: case INNER_3: case UP:
				if(isAllMatching && UtilsMath.coinFlip()) {
					if(isInverseType) return DOWN;
					else return UP;
				}
				else return INNER_2;
			case DOWN: case OUTER_3: case OUTER_4:
				if(aboveRight.connects(UP) && !isAllMatching) return OUTER_2;
				else if(isAllMatching && UtilsMath.coinFlip()) return UP;
				else return OUTER_2;
			case LEFT: case INNER_2: case OUTER_1: 
				return INNER_3;
			default:
				errorCount++;
				Console.err("TileTransitionType -> getConnectionTransiton() ->  left : " + left + " -> above : " + above + " -> isMatching : " + isAllMatching + " -> error count : " + errorCount);
				return NONE;
			}
			
		case OUTER_2:
			switch(above) {
			case OUTER_2:
				return RIGHT;
			case INNER_4: case INNER_3: case UP: 
				//this is a test
				return NONE;
			case INNER_1: case RIGHT: 
				if(isAllMatching && UtilsMath.coinFlip()) return INNER_4;
				else return RIGHT;
			case NONE:
				if(isAllMatching && UtilsMath.coinFlip()) return INNER_1;
				else return NONE;
			case DOWN: case OUTER_3: case OUTER_4: 
				if(UtilsMath.coinFlip()) return NONE;
				else return OUTER_1;
			case LEFT: case INNER_2: case OUTER_1:
				if(isAllMatching && UtilsMath.coinFlip()) return OUTER_4;
				return LEFT;
			default:
				errorCount++;
				Console.err("TileTransitionType -> getConnectionTransiton() ->  left : " + left + " -> above : " + above + " -> isMatching : " + isAllMatching + " -> error count : " + errorCount);
				return NONE;
			}
			
		case OUTER_3:
			switch(above) {		
				
			case INNER_4: case UP: case INNER_3:
				//THIS IS A TEST
				return NONE;
			case INNER_1: case RIGHT:
				if(isAllMatching && UtilsMath.coinFlip()) return INNER_4;
				else return RIGHT;
			case NONE:
				if(isAllMatching && UtilsMath.coinFlip()) return INNER_1;
				else return NONE;
			case DOWN: case OUTER_3: case OUTER_4:  
				if(UtilsMath.coinFlip()) return NONE;
				else return OUTER_1;
			case LEFT: case INNER_2: case OUTER_1: case OUTER_2:
				if(isAllMatching && UtilsMath.coinFlip()) return OUTER_4;
				return LEFT;
			default:
				errorCount++;
				Console.err("TileTransitionType -> getConnectionTransiton() ->  left : " + left + " -> above : " + above + " -> isMatching : " + isAllMatching + " -> error count : " + errorCount);
				return NONE;
			}
			
		case OUTER_4:
			switch(above) {
			case DOWN:
				//this is a test
				return NONE;
			case OUTER_4:
				if(isAboveMatching && UtilsMath.coinFlip()) return UP;
				else return OUTER_2;
			case INNER_2:
				return INNER_3;
			case LEFT:
				return INNER_3;
			case RIGHT: case INNER_1: case OUTER_2:
				return OUTER_3;
			case UP: case INNER_3: case INNER_4: case NONE:    
				if(isAllMatching && UtilsMath.coinFlip()) return DOWN;
				else return INNER_2;
			default:
				errorCount++;
				Console.err("TileTransitionType -> getConnectionTransiton() ->  left : " + left + " -> above : " + above + " -> isMatching : " + isAllMatching + " -> error count : " + errorCount);
				return NONE;
			}
			
		case NONE:
			switch(above) {
			case DOWN: case OUTER_3: case OUTER_4:
				if(UtilsMath.coinFlip()) return NONE;
				else return OUTER_1;
			case LEFT: case INNER_2: case OUTER_1:
				if(isAllMatching && UtilsMath.coinFlip()) return OUTER_4;
				else return LEFT;
			case RIGHT: case INNER_1: case OUTER_2:
				if(isAllMatching && UtilsMath.coinFlip()) return INNER_4;
				else return RIGHT;
			case UP: case INNER_3: case INNER_4: case NONE:   
				if(UtilsMath.coinFlip()) return INNER_1;
				else return NONE;
			default:
				errorCount++;
				Console.err("TileTransitionType -> getConnectionTransiton() ->  left : " + left + " -> above : " + above + " -> isMatching : " + isAllMatching + " -> error count : " + errorCount);
				return NONE;
			}
			
		default:
			Console.err("TileTransitionType -> getConnectionTransiton() ->  left : " + left + " -> above : " + above + " -> isMatching : " + isAllMatching);
			return NONE;
		
		}		
	}
	
	public static int getTileTypeGiver(TileTransitionType transition, TileTransitionType left, TileTransitionType above, TileTransitionType aboveRight, boolean isInverseType) {
		switch(left) {
		case DOWN: 
			switch(above) {
			case OUTER_1:
				if(transition == OUTER_3) return LEFT_GIVER;
				return ABOVE_GIVER;	
			case UP:
				break;
			default:
				return LEFT_GIVER;		
			}

		case OUTER_4:
			switch(above) {
			case OUTER_4: case INNER_2:
				return ABOVE_GIVER;
			case LEFT:
				if(transition == INNER_3) return ABOVE_GIVER; 
			default:
				return LEFT_GIVER;			
			}

		case INNER_1:
			switch(above) {
			case LEFT:
				if(transition == UP) return ABOVE_GIVER;
				if(isInverseType) return LEFT_GIVER;
				else return ABOVE_GIVER;
			case DOWN:
				if(transition == INNER_2) return LEFT_GIVER;
				if(transition == UP) return ABOVE_GIVER;
				else return ABOVE_GIVER;		
			case INNER_2: case OUTER_1:
				return ABOVE_GIVER;			
			default: return LEFT_GIVER;
			
			}

		case UP: 
			switch(above) {
			case NONE:
				if(transition == INNER_2) return LEFT_INVERT_GIVER;
				else return LEFT_GIVER;
			case INNER_4:
				if(transition == OUTER_2 || transition == UP) return LEFT_GIVER;
				else return ABOVE_GIVER;
			case INNER_2:	case INNER_1: case RIGHT:
				return ABOVE_GIVER;
			default:
				return LEFT_GIVER;
			}	
			
		case INNER_4: case OUTER_1:
			switch(above) {
			case RIGHT:
				return ABOVE_GIVER;
			case NONE: case INNER_3:
				if(transition == INNER_2) return LEFT_INVERT_GIVER;
				else return LEFT_GIVER;
			case INNER_4: 
				if(isInverseType) return ABOVE_GIVER;
				if(transition == INNER_2) return LEFT_INVERT_GIVER;
				else return LEFT_GIVER;
			case INNER_2:
				return ABOVE_GIVER;
			case UP:
				return LEFT_INVERT_GIVER;
			case INNER_1: 
			default:
				return LEFT_GIVER;
			}		
			
		case OUTER_2:	
			switch(above) {
			case OUTER_2:
				return ABOVE_GIVER;
			case INNER_1: case RIGHT: case INNER_3:
				case INNER_4: 
				return ABOVE_GIVER;
			case NONE:
				return LEFT_INVERT_GIVER;
			case DOWN: case OUTER_3: case OUTER_4:
				if(transition == OUTER_1  && !aboveRight.connects(TileTransitionType.UP)) return SECONDARY_RANDOM_GIVER;
				else return SECONDARY_GIVER;
			case LEFT: case INNER_2: case OUTER_1: case UP:
				return ABOVE_GIVER;
			default: break;
			}
			
		case RIGHT: case OUTER_3:
			switch(above) {
			case OUTER_2:
				return LEFT_GIVER;
			case INNER_1: case RIGHT: case INNER_3:
				case INNER_4: 
				return ABOVE_GIVER;
			case NONE:
				return LEFT_INVERT_GIVER;
			case DOWN: case OUTER_3: case OUTER_4:
				if(transition == OUTER_1  && !aboveRight.connects(TileTransitionType.UP)) return SECONDARY_RANDOM_GIVER;
				else return SECONDARY_GIVER;
			case LEFT: case INNER_2: case OUTER_1: case UP:
				return ABOVE_GIVER;
			default: break;
			}
			
		case LEFT: case INNER_2: case INNER_3:
			switch(above) {
			case LEFT:
				return ABOVE_GIVER;
			case OUTER_3: case OUTER_4: case DOWN:
				if(transition == INNER_1  && !aboveRight.connects(TileTransitionType.UP)) return PRIMARY_RANDOM_GIVER;
				else return PRIMARY_GIVER;
			case RIGHT: case INNER_1: case OUTER_2: case INNER_2: 
				return ABOVE_GIVER;
			case UP: case INNER_3: case INNER_4: case NONE:
				if(transition == INNER_1  && !aboveRight.connects(TileTransitionType.UP)) return PRIMARY_RANDOM_GIVER;
				else return PRIMARY_GIVER;
			default: break;
			}
			
		case NONE:
			switch(above) {
			case RIGHT: case LEFT: case INNER_1: case INNER_2: case OUTER_1: case OUTER_2:
				return ABOVE_GIVER;
			case DOWN: case OUTER_3: case OUTER_4:
				if(transition == OUTER_1 && !aboveRight.connects(TileTransitionType.UP)) return SECONDARY_RANDOM_GIVER;
				else return SECONDARY_GIVER;
			case UP: case INNER_3: case INNER_4: case NONE:
				if(transition == INNER_1 && !aboveRight.connects(TileTransitionType.UP)) return PRIMARY_RANDOM_GIVER;
				else return PRIMARY_GIVER;								
			default: break;
			}
		default:
			Console.err("TileTransitionType -> getTileTypeGiver() -> invalid case -> left : " + left + " -> above : " + above);
			return -1;
		
		}
	}
	
	public static TileTransitionType getPossibleAbove(TileTransitionType left) {
		ArrayList<TileTransitionType> possible = new ArrayList<>();
		switch(left) {
		case DOWN: case LEFT: case INNER_1: case INNER_2: case INNER_3:	case OUTER_4: case NONE:
			possible.add(RIGHT);
			possible.add(UP);
			possible.add(INNER_1);
			possible.add(INNER_3);
			possible.add(INNER_4);
			possible.add(OUTER_2);
			possible.add(NONE);
			break;
		case RIGHT: case UP: case INNER_4: case OUTER_1: case OUTER_2: case OUTER_3:
			possible.add(DOWN);
			possible.add(LEFT);
			possible.add(INNER_2);
			possible.add(OUTER_1);
			possible.add(OUTER_3);
			possible.add(OUTER_4);
			break;
		default:
			Console.err("TileTransitionType -> getPossibleAbove() -> invalid case : " + left);
			break;
		}
		return possible.get(UtilsMath.random(possible.size()));
	}
	
	public static TileTransitionType getPossibleLeft(TileTransitionType above) {
		ArrayList<TileTransitionType> possible = new ArrayList<>();
		switch(above) {
		case DOWN: case LEFT: case INNER_2: case OUTER_1: case OUTER_3: case OUTER_4:
			possible.add(RIGHT);
			possible.add(UP);
			possible.add(INNER_4);
			possible.add(OUTER_1);
			possible.add(OUTER_2);
			possible.add(OUTER_3);
			break;
		case RIGHT: case UP: case INNER_1: case INNER_3: case INNER_4: case OUTER_2: case NONE: 
			possible.add(DOWN);
			possible.add(LEFT);
			possible.add(INNER_1);
			possible.add(INNER_2);
			possible.add(INNER_3);
			possible.add(OUTER_4);
			possible.add(NONE);
			break;
		default:
			Console.err("TileTransitionType -> getPossibleLeft() -> invalid case : " + above);
			return null;
		}
		
		return possible.get(UtilsMath.random(possible.size()));
	}
	
	
	public static TileTransitionType getPossibleRight(TileTransitionType above) {
		ArrayList<TileTransitionType> possible = new ArrayList<>();
		switch(above) {
		case DOWN: case INNER_1: case OUTER_4:
			possible.add(DOWN);
			possible.add(INNER_2);
			possible.add(OUTER_3);
			break;
		case RIGHT:
			possible.add(LEFT);
			possible.add(OUTER_1);
			possible.add(OUTER_2);
			possible.add(NONE);
			break;
		case LEFT: case INNER_2: case INNER_3: case NONE:
			possible.add(NONE);
			possible.add(RIGHT);
			possible.add(INNER_1);
			possible.add(INNER_4);
			break;
		case UP: case INNER_4: case OUTER_1:
			possible.add(UP);
			possible.add(OUTER_2);
			possible.add(INNER_3);
			break;	
		case OUTER_2: case OUTER_3:
			possible.add(OUTER_4);
			possible.add(OUTER_1);
			possible.add(NONE);
			break;
		default:
			Console.err("TileTransitionType -> getPossibleRight() -> invalid case : " + above);
			break;
		}
		return possible.get(UtilsMath.random(possible.size()));
	}

}
