package game;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * 方块的一个面
 * 
 * @author Administrator
 *
 */
public class Face {

	public Face(FaceType type, ColorRGBA color) {
		this.type = type;
		this.color = color;
	}

	public Face(Vector2f leftTop, Vector2f rightTop, Vector2f leftBottom, Vector2f rightBottom) {
		this.leftTop = leftTop;
		this.rightTop = rightTop;
		this.leftBottom = leftBottom;
		this.rightBottom = rightBottom;
	}

	private FaceType type;
	private ColorRGBA color;

	private Vector2f leftTop;
	private Vector2f rightTop;
	private Vector2f leftBottom;
	private Vector2f rightBottom;
	private boolean isPicked;

	public boolean isPicked() {
		return isPicked;
	}

	public void setPicked(boolean isPicked) {
		this.isPicked = isPicked;
	}

	public Vector2f getLeftTop() {
		return leftTop;
	}

	public void setLeftTop(Vector2f leftTop) {
		this.leftTop = leftTop;
	}

	public Vector2f getRightTop() {
		return rightTop;
	}

	public void setRightTop(Vector2f rightTop) {
		this.rightTop = rightTop;
	}

	public Vector2f getLeftBottom() {
		return leftBottom;
	}

	public void setLeftBottom(Vector2f leftBottom) {
		this.leftBottom = leftBottom;
	}

	public Vector2f getRightBottom() {
		return rightBottom;
	}

	public void setRightBottom(Vector2f rightBottom) {
		this.rightBottom = rightBottom;
	}

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
