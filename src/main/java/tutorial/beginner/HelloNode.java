package tutorial.beginner;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Box;

public class HelloNode extends SimpleApplication {
	
	public static void main(String[] args) {
		HelloNode app = new HelloNode();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		flyCam.setMoveSpeed(10);
		// add coordinates
		addCoordinate();
		
		
		/**
		 * create a blue box at coordinates (1, -1, 1)
		 */
		Box box1 = new Box(1, 1, 1);
		Geometry blue = new Geometry("Box", box1);
		blue.setLocalTranslation(new Vector3f(1, -1, 1));
		Material mat1 = new Material(assetManager, "MatDefs/Unshaded.j3md");
		mat1.setColor("Color", ColorRGBA.Blue);
		blue.setMaterial(mat1);
		
		/**
		 * create a red box straight above the blue one at (1, 3, 1)
		 */
		Box box2 = new Box(1, 1, 1);
		Geometry red = new Geometry("Box", box2);
		red.setLocalTranslation(new Vector3f(1, 3, 1));
		Material mat2 = new Material(assetManager, "MatDefs/ShowNormals.j3md");
//		mat2.setColor("Color", ColorRGBA.Red);
		red.setMaterial(mat2);
		
		/**
		 * create a pivot node at (0, 0, 0) and attach it to the root node
		 */
		Node pivot = new Node("pivot");
		rootNode.attachChild(pivot);
		
		/**
		 * attach the two boxes to the "pivot" node
		 */
		pivot.attachChild(blue);
		pivot.attachChild(red);
		
		/**
		 * rotate the pivot node
		 */
//		pivot.rotate(.4f, .4f, 0);

	}

	
	
	/**
	 * 添加坐标系
	 * 
	 * @param vec3
	 * @param color
	 */
	public void addCoordinate() {
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

}
