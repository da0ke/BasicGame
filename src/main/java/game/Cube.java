package game;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 * 单个方块
 * 
 * @author Administrator
 *
 */
public class Cube extends Node {
	
	/**
	 * 六个面
	 */
	private Geometry front;
	private Geometry back;
	private Geometry left;
	private Geometry right;
	private Geometry top;
	private Geometry bottom;
	
	private AssetManager assetManager;
	
	public Cube(String name, AssetManager assetManager) {
		this.name = name;
		this.assetManager = assetManager;
	}
	
	public void setFace(Face face) {
		FaceType faceType = face.getType();

		switch (faceType) {
		case Front:
			if(front != null && hasChild(front)) {
				detachChild(front);
			}
			front = makeFace(faceType, face.getColor());
			attachChild(front);
			break;
		case Back:
			if(back != null && hasChild(back)) {
				detachChild(back);
			}
			back = makeFace(faceType, face.getColor());
			attachChild(back);
			break;
		case Left:
			if(left != null && hasChild(left)) {
				detachChild(left);
			}
			left = makeFace(faceType, face.getColor());
			attachChild(left);
			break;
		case Right:
			if(right != null && hasChild(right)) {
				detachChild(right);
			}
			right = makeFace(faceType, face.getColor());
			attachChild(right);
			break;
		case Top:
			if(top != null && hasChild(top)) {
				detachChild(top);
			}
			top = makeFace(faceType, face.getColor());
			attachChild(top);
			break;
		case Bottom:
			if(bottom != null && hasChild(bottom)) {
				detachChild(bottom);
			}
			bottom = makeFace(faceType, face.getColor());
			attachChild(bottom);
			break;
		default:
			break;
		}
	}
	
	private Geometry makeFace(FaceType faceType, ColorRGBA color) {
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		if(color == null) {
			color = ColorRGBA.Black;
		}
		mat.setColor("Color", color);
		
		
		
		Geometry geom;
		if(faceType == FaceType.Front) {
			geom = new Geometry("front", new Box(0.5f, 0.5f, 0.04f));
			geom.setMaterial(mat);
			geom.move(0, 0, 0.5f);
		} else if(faceType == FaceType.Back) {
			geom = new Geometry("back", new Box(0.5f, 0.5f, 0.04f));
			geom.setMaterial(mat);
			geom.move(0, 0, -0.5f);
		} else if(faceType == FaceType.Left) {
			geom = new Geometry("left", new Box(0.04f, 0.5f, 0.5f));
			geom.setMaterial(mat);
			geom.move(-0.5f, 0, 0);
		} else if(faceType == FaceType.Right) {
			geom = new Geometry("left", new Box(0.04f, 0.5f, 0.5f));
			geom.setMaterial(mat);
			geom.move(0.5f, 0, 0);
		} else if(faceType == FaceType.Top) {
			geom = new Geometry("top", new Box(0.5f, 0.04f, 0.5f));
			geom.setMaterial(mat);
			geom.move(0, 0.5f, 0);
		} else if(faceType == FaceType.Bottom) {
			geom = new Geometry("bottom", new Box(0.5f, 0.04f, 0.5f));
			geom.setMaterial(mat);
			geom.move(0, -0.5f, 0);
		} else {
			geom = new Geometry("geom", new Box(0.1f, 0.1f, 0.1f));
			geom.setMaterial(mat);
		}
		
//		if(faceType == FaceType.Front) {
//			geom.setName("front");
//			geom.setLocalTranslation(-0.5f, -0.5f, 0.5f);
//		} else if(faceType == FaceType.Back) {
//			geom.setName("back");
//			geom.setLocalTranslation(0.5f, -0.5f, -0.5f);
//			Quaternion quaternion = new Quaternion();
//			quaternion.fromAngleAxis(FastMath.PI, new Vector3f(0,1,0));
//			geom.setLocalRotation(quaternion);
//		} else if(faceType == FaceType.Left) {
//			geom.setName("left");
//			geom.setLocalTranslation(-0.5f, -0.5f, -0.5f);
//			Quaternion quaternion = new Quaternion();
//			quaternion.fromAngleAxis(-FastMath.PI/2, new Vector3f(0,1,0));
//			geom.setLocalRotation(quaternion);
//		} else if(faceType == FaceType.Right) {
//			geom.setName("right");
//			Quaternion quaternion = new Quaternion();
//			quaternion.fromAngleAxis(FastMath.PI/2, new Vector3f(0,1,0));
//			geom.setLocalRotation(quaternion);
//			geom.move(0.5f, -0.5f, 0.5f);
//		} else if(faceType == FaceType.Top) {
//			geom.setName("top");
//			geom.setLocalTranslation(-0.5f,0.5f, 0.5f);
//			Quaternion quaternion = new Quaternion();
//			quaternion.fromAngleAxis(-FastMath.PI/2, new Vector3f(1,0,0));
//			geom.setLocalRotation(quaternion);
//		} else if(faceType == FaceType.Bottom) {
//			geom.setName("bottom");
//			geom.setLocalTranslation(-0.5f,-0.5f, -0.5f);
//			Quaternion quaternion = new Quaternion();
//			quaternion.fromAngleAxis(FastMath.PI/2, new Vector3f(1,0,0));
//			geom.setLocalRotation(quaternion);
//		}
		
		return geom;
	}

	

}
