package test;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Box;

/**
 * 实现鼠标拖曳旋转
 * @author Administrator
 *
 */
public class TestBox extends SimpleApplication {
	
	// Mouse movement: Up
	private static final String UP = "up";
	// Mouse movement: Down
	private static final String DOWN = "down";
	// Mouse movement: Left
	private static final String LEFT = "left";
	// Mouse movement: Right
	private static final String RIGHT = "right";
	// pick
	private static final String PICK = "pick";
	
	private boolean isPicking = false;
	
	
	private Geometry geom;

	@Override
	public void simpleInitApp() {
		cam.setLocation(new Vector3f(4.060241f, 2.9458878f, 11.016832f));
		cam.setRotation(new Quaternion(-0.013292809f, 0.98208433f, -0.0759166f, -0.1719602f));
		
		flyCam.setEnabled(false);
		
		addCoordinates();
		
		geom = new Geometry("Box", new Box(1, 1, 1));
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Orange);
		geom.setMaterial(mat);
		
		rootNode.attachChild(geom);
	
		initKeys();
	}
	
	private void initKeys() {
		inputManager.addMapping(PICK, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addMapping(UP, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
		inputManager.addMapping(DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
		inputManager.addMapping(LEFT, new MouseAxisTrigger(MouseInput.AXIS_X, true));
		inputManager.addMapping(RIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, false));
		
		inputManager.addListener(analogListener, UP, DOWN, LEFT, RIGHT);
		inputManager.addListener(actionListener, PICK);
	}
	
	private ActionListener actionListener = new ActionListener() {
		
		@Override
		public void onAction(String name, boolean isPressed, float tpf) {
			isPicking = false;
			if(name.equals(PICK) && isPressed) {
				// Reset results list.
		        CollisionResults results = new CollisionResults();
		        // Convert screen click to 3d position
		        Vector2f click2d = inputManager.getCursorPosition();
		        Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
		        Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
		        // Aim the ray from the clicked spot forwards.
		        Ray ray = new Ray(click3d, dir);
		        // Collect intersections between ray and all nodes in results list.
		        rootNode.collideWith(ray, results);
		        // (Print the results so we see what is going on:)
		        for (int i = 0; i < results.size(); i++) {
		          // (For each “hit”, we know distance, impact point, geometry.)
		          float dist = results.getCollision(i).getDistance();
		          Vector3f pt = results.getCollision(i).getContactPoint();
		          String target = results.getCollision(i).getGeometry().getName();
//		          System.out.println("Selection #" + i + ": " + target + " at " + pt + ", " + dist + " WU away.");
		        }
		        // Use the results -- we rotate the selected geometry.
		        if (results.size() > 0) {
		          // The closest result is the target that the player picked:
		          Geometry target = results.getClosestCollision().getGeometry();
		          // Here comes the action:
		          if (target.getName().equals("Box")) {
		            isPicking = true;
		          }
		        }
			}
		}
	};
	
	private AnalogListener analogListener = new AnalogListener() {
		
		@Override
		public void onAnalog(String name, float intensity, float tpf) {
			if(isPicking) {
				switch (name) {
				case UP: 
					geom.rotate(-intensity*speed*4, 0, 0);
					break;
				case DOWN:
					geom.rotate(intensity*speed*4, 0, 0);
					break;
				case LEFT:
					geom.rotate(0, -intensity*speed*4, 0);
					break;
				case RIGHT:
					geom.rotate(0, intensity*speed*4, 0);
					break;
				default:
					break;
				}
			}
			
		}
	};
	
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
		TestBox app = new TestBox();
		app.start();
	}

}
