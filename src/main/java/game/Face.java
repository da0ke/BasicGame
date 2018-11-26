package game;

import com.jme3.math.ColorRGBA;

/**
 * 方块的一个面
 * @author Administrator
 *
 */
public class Face {
	
	public Face(FaceType type, ColorRGBA color) {
		this.type = type;
		this.color = color;
	}

	private FaceType type;
	private ColorRGBA color;

	public FaceType getType() {
		return type;
	}

	public void setType(FaceType type) {
		this.type = type;
	}

	public ColorRGBA getColor() {
		return color;
	}

	public void setColor(ColorRGBA color) {
		this.color = color;
	}

}
