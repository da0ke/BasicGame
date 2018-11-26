package game;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Quad;

public class MagicCube extends SimpleApplication {
	
	

	@Override
	public void simpleInitApp() {
		flyCam.setMoveSpeed(10);
		
		addCoordinates();
		
		Cube cube1 = new Cube(assetManager);
		cube1.setFace(new Face(FaceType.Front, ColorRGBA.White));
		cube1.setFace(new Face(FaceType.Left, ColorRGBA.Yellow));
		cube1.setFace(new Face(FaceType.Top, ColorRGBA.Green));
		
		Cube cube2 = new Cube(assetManager);
		cube2.setFace(new Face(FaceType.Front, ColorRGBA.White));
		cube2.setFace(new Face(FaceType.Left, ColorRGBA.Yellow));
		cube2.setFace(new Face(FaceType.Top, ColorRGBA.Green));
		cube2.move(0, 1.1f, 0);
		
		
		Cube cube3 = new Cube(assetManager);
		cube3.setFace(new Face(FaceType.Front, ColorRGBA.White));
		cube3.setFace(new Face(FaceType.Left, ColorRGBA.Yellow));
		cube3.setFace(new Face(FaceType.Top, ColorRGBA.Green));
		cube3.move(0, 2.2f, 0);
		
		
		
		rootNode.attachChild(cube1);
		rootNode.attachChild(cube2);
		rootNode.attachChild(cube3);
	}

	
	
	/**
	 * 添加坐标系
	 * 
	 * @param vec3
	 * @param color
	 */
	public void addCoordinates() {
		// 创建X、Y、Z方向的箭头，作为参考坐标系
		createAxes(new Vector3f(5, 0, 0), ColorRGBA.Green);
		createAxes(new Vector3f(0, 5, 0), ColorRGBA.Red);
		createAxes(new Vector3f(0, 0, 5), ColorRGBA.Blue);
	}

	/**
	 * 创建轴线
	 * 
	 * @param vec3
	 * @param color
	 */
	private void createAxes(Vector3f vec3, ColorRGBA color) {
		// 创建材质，设定箭头的颜色
		Material mat = new Material(assetManager, "MatDefs/Unshaded.j3md");
		mat.setColor("Color", color);

		// 创建几何物体，应用箭头网格
		Geometry geom = new Geometry("箭头", new Arrow(vec3));
		geom.setMaterial(mat);

		// 添加到场景中
		rootNode.attachChild(geom);
	}
	
	public static void main(String[] args) {
		MagicCube app = new MagicCube();
		app.start();
	}

}
