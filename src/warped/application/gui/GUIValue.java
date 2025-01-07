/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.ValueType;
import warped.utilities.math.vectors.Vec2d;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.math.vectors.Vec3d;
import warped.utilities.math.vectors.Vec3i;
import warped.utilities.utils.Console;

public class GUIValue extends WarpedGUI {

	private ValueType valueType = ValueType.NULL;
	
	private int tick = 0;
	private int refreshRate = WarpedProperties.WARPED_GAME_REFRESH_RATE;
	
	private Color valueColor  = Color.WHITE;
	private int   valueSize   = 12;
	private int   valueStyle  = Font.PLAIN;
	private Font  valueFont   = new Font("valueFont", valueStyle, valueSize);
	private Vec2i valueOffset = new Vec2i(0, valueSize); //the position that the value will be drawn in the raster
	private String preText = "";
	private String postText = "";
	
	private Integer   integerValue = 0;
	private Double    doubleValue = 0.0;
	private String    stringValue = "0";
	private Vec2d     vec2dValue = new Vec2d();
	private Vec3d     vec3dValue = new Vec3d();
	private Vec2i     vec2iValue = new Vec2i();
	private Vec3i     vec3iValue = new Vec3i();
	
	public GUIValue(Integer value) {setValue(value);}
	public GUIValue(Double  value) {setValue(value);}
	public GUIValue(Vec2d   value) {setValue(value);}
	public GUIValue(Vec3d   value) {setValue(value);}
	public GUIValue(Vec2i   value) {setValue(value);}
	public GUIValue(Vec3i   value) {setValue(value);}
	

	public GUIValue(Vec2d position, Vec2d size) {
		this.position = position;
		setRaster(new BufferedImage((int)size.x, (int)size.y, WarpedProperties.BUFFERED_IMAGE_TYPE));
	}
	
	public GUIValue(Integer value, Vec2d position) {
		this.position = position;
		setRaster(new BufferedImage((int)150, (int)30, WarpedProperties.BUFFERED_IMAGE_TYPE));
		setValue(value);
	}
	public GUIValue(Double  value, Vec2d position) {
		this.position = position;
		setRaster(new BufferedImage((int)150, (int)30, WarpedProperties.BUFFERED_IMAGE_TYPE));
		setValue(value);
	}
	public GUIValue(Vec2d value, Vec2d size, Vec2d position) {
		this.position = position;
		this.size = size;
		setRaster(new BufferedImage((int)150, (int)30, WarpedProperties.BUFFERED_IMAGE_TYPE));
		setValue(value);
	}
	
	public GUIValue(Vec2i   value, Vec2d position) {
		this.position = position;
		setRaster(new BufferedImage((int)150, (int)30, WarpedProperties.BUFFERED_IMAGE_TYPE));
		setValue(value);
	}
	
	public GUIValue(Vec3i   value, Vec2d position) {
		this.position = position;
		setRaster(new BufferedImage((int)150, (int)30, WarpedProperties.BUFFERED_IMAGE_TYPE));
		setValue(value);
	}
	
		
	protected void updateObject()   {return;}
	protected void updatePosition() {return;}
	protected void updateRaster()   {
		tick++;
		if(tick > refreshRate){
			tick = 0;
			updateGraphics();
		}
	}
	
	/**Lower is faster, higher is slower   -> a refresh rate of 0 will update with no delay */
	public void setRefreshRate(int refreshRate) {this.refreshRate = refreshRate;}
	private void setFont() { valueFont = new Font("valueFont", valueStyle, valueSize);}
	public void setValueColor(Color valueColor) {this.valueColor = valueColor;}
	public void setValueSize(int valueSize) {
		this.valueSize = valueSize;
		setFont();
		valueOffset.y = valueSize;
	}
	public void setValueStyle(int valueStyle) {
		this.valueStyle = valueStyle;
		setFont();
	}
	
	public <T> void setValue(T value){
		if(value instanceof Integer) {setValue((Integer)value); return;}
		if(value instanceof Double)  {setValue((Double) value); return;}
		if(value instanceof String)  {setValue((String) value); return;}
		if(value instanceof Vec2d)   {setValue((Vec2d)  value); return;}
		if(value instanceof Vec3d)   {setValue((Vec3d)  value); return;}
		if(value instanceof Vec2i)   {setValue((Vec2i)  value); return;}
		if(value instanceof Vec3i)   {setValue((Vec3i)  value); return;}
		
		Console.err("GUIValue -> setValue(T value) -> tried to set unsuported value type : " + value.getClass().getSimpleName());
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getValue(){
		switch(valueType) {
		case DOUBLE:  return (T) doubleValue;
		case INTEGER: return (T) integerValue;
		case STRING:  return (T) stringValue;
		case VEC2I:   return (T) vec2iValue;
		case VEC3I:   return (T) vec3iValue;
		case VEC_2D:  return (T) vec2dValue;
		case VEC_3D:  return (T) vec3dValue;
		default:
			Console.err("GUIValue -> getValue() -> unsupported value type : " + valueType);
			return null;
		}
	}
	
	protected void updateGraphics() {
		if(valueType != ValueType.NULL) {
			Graphics2D g2d = raster.createGraphics();
			clear(g2d);
			draw(g2d);
			g2d.dispose();
		}
	}
	
	protected void clear(Graphics2D g2d) {
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		g2d.fillRect(0,0, (int)size.x, (int)size.y);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
	}
	protected void draw(Graphics2D g2d) {
		g2d.setFont(valueFont);
		g2d.setColor(valueColor);
		String text = "";
		switch(valueType) {
		case DOUBLE:  text += doubleValue.toString();  break;
		case INTEGER: text += integerValue.toString(); break;
		case STRING:  text += stringValue; 			   break;
		case VEC2I:   text += vec2iValue.getString(); break; 
		case VEC3I:   text += vec3iValue.getPrintln(); break;
		case VEC_2D:  text += vec2dValue.getString(); break;	
		case VEC_3D:  text += vec3dValue.getString(); break;
		default:
			Console.err("GUIValue -> updateGrpahics() -> unsupported switch value : " + valueType);
			break;
		}
		String result = preText + text + postText;
		g2d.setColor(valueColor);
		g2d.drawString(result, valueOffset.x, valueOffset.y);
	}
	
	protected void setValue(Integer value) {
		valueType = ValueType.INTEGER;
		integerValue = value;
		updateGraphics();
	}
	public void setPreText(String preText) {
		this.preText = preText;
		updateGraphics();
	}
	public void setPostText(String postText) {
		this.postText = postText;
		updateGraphics();
	}
	protected void setValue(Double value) {
		valueType = ValueType.DOUBLE;
		doubleValue = value;
		updateGraphics();
	}
	protected void setValue(String value) {
		valueType = ValueType.STRING;
		stringValue = value;
		updateGraphics();
	}
	protected void setValue(Vec2d value) {
		valueType = ValueType.VEC_2D;
		vec2dValue = value;
		updateGraphics();
	}
	protected void setValue(Vec3d value) {
		valueType = ValueType.VEC_3D;
		vec3dValue = value;
		updateGraphics();
	}
	protected void setValue(Vec2i value) {
		valueType = ValueType.VEC2I;
		vec2iValue = value;
		updateGraphics();
	}
	protected void setValue(Vec3i value) {
		valueType = ValueType.VEC3I;
		vec3iValue = value;
		updateGraphics();
	}
		
	protected void mouseEntered() {return;}
	protected void mouseExited() {return;}
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {return;}
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}



}
