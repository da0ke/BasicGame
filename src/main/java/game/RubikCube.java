package game;

import java.util.ArrayList;
import java.util.List;
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
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.system.AppSettings;

/**
 * Rubik魔方
 * 
 * 本游戏术语解释 rubik：魔方 
 * cube：魔方中的小方块 
 * face：面，指魔方的面，也可指小方块的面
 * faceType：面的类型，以Z轴方向为前面，Y轴方向为上面，X轴方向为右面
 * 
 * @author Administrator
 *
 */
public class RubikCube extends SimpleApplication {

	// A temp pivot for rotate cube
	private Node pivot;
	
	private Cube pickedCube;

	private List<Cube> cubeList = new ArrayList<>();

	private boolean isPicking = false;
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
	// 方块的尺寸
	private static final float UNIT_SIZE = 1f;
	// 方块与方块的间隔
	private static final float UNIT_GAP = 0.03f;

	@Override
	public void simpleInitApp() {
		cam.setLocation(new Vector3f(-9.749997f, 8.143939f, 10.95721f));
		cam.setRotation(new Quaternion(0.08476704f, 0.89286304f, -0.1893601f, 0.399691f));
		flyCam.setMoveSpeed(10);
		flyCam.setEnabled(false);
		addCoordinates();

		makeRubikCube();

		pivot = new Node("pivot");
		rootNode.attachChild(pivot);

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
			if (name.equals(PICK) && isPressed) {
				// Reset results list.
				CollisionResults results = new CollisionResults();
				// Convert screen click to 3d position
				Vector2f click2d = inputManager.getCursorPosition();
				Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
				Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d)
						.normalizeLocal();
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
				}
				// Use the results -- we rotate the selected geometry.
				if (results.size() > 0) {
					// The closest result is the target that the player picked:
					Geometry target = results.getClosestCollision().getGeometry();
					// Here comes the action:
					pickedCube = (Cube) target.getParent();
					isPicking = true;
					System.out.println("select " + pickedCube.getName() + " " + pickedCube.getLocalTranslation());
				}
			}
		}
	};

	private AnalogListener analogListener = new AnalogListener() {

		@Override
		public void onAnalog(String name, float intensity, float tpf) {
			if (isPicking) {
				System.out.println(intensity);
				switch (name) {
				case UP:
					if(pickedCube.getLocalTranslation().x == 0) {
						
					}
//					geom.rotate(-intensity*speed*4, 0, 0);
					break;
				case DOWN:
//					geom.rotate(intensity*speed*4, 0, 0);
					break;
				case LEFT:
//					geom.rotate(0, -intensity*speed*4, 0);
					break;
				case RIGHT:
//					geom.rotate(0, intensity*speed*4, 0);
					break;
				default:
					break;
				}
			}

		}
	};

	private void rotateYZ(float tpf, FaceType faceType) {
		removeCubeFromPivot();

		List<Cube> list = new ArrayList<>();
		float x = 0;
		float y = 0;
		float z = 0;
		if (FaceType.LeftYZ == faceType) {
			x = 0;
			y = UNIT_SIZE + UNIT_GAP;
			z = UNIT_SIZE + UNIT_GAP;

		} else if (FaceType.MiddleYZ == faceType) {
			x = UNIT_SIZE + UNIT_GAP;
			y = UNIT_SIZE + UNIT_GAP;
			z = UNIT_SIZE + UNIT_GAP;

		} else if (FaceType.RightYZ == faceType) {
			x = (UNIT_SIZE + UNIT_GAP) * 2;
			y = UNIT_SIZE + UNIT_GAP;
			z = UNIT_SIZE + UNIT_GAP;
		}

		// 获取需要旋转的面,共9个方块
		for (Cube cube : cubeList) {
			if (cube.getLocalTranslation().x == x) {
				list.add(cube);
			}
		}

		// 设置旋转支点坐标
		pivot.setLocalTranslation(x, y, z);

		// 将所需面的方块加入旋转支点
		for (Cube cube : list) {
			if (cube.getParent() != pivot) {
				cube.removeFromParent();
				pivot.attachChild(cube);
				cube.move(-x, -y, -z);
			}
		}

		// 旋转
		Quaternion pitch45 = new Quaternion();
		pitch45.fromAngleAxis(FastMath.PI / 2 * tpf, new Vector3f(1, 0, 0));
		pivot.setLocalRotation(pivot.getLocalRotation().mult(pitch45));
	}

	private void rotateXY(float tpf, FaceType faceType) {
		removeCubeFromPivot();

		List<Cube> list = new ArrayList<>();
		float x = 0;
		float y = 0;
		float z = 0;
		if (FaceType.LeftXY == faceType) {
			x = UNIT_SIZE + UNIT_GAP;
			y = UNIT_SIZE + UNIT_GAP;
			z = 0;

		} else if (FaceType.MiddleXY == faceType) {
			x = UNIT_SIZE + UNIT_GAP;
			y = UNIT_SIZE + UNIT_GAP;
			z = UNIT_SIZE + UNIT_GAP;

		} else if (FaceType.RightXY == faceType) {
			x = UNIT_SIZE + UNIT_GAP;
			y = UNIT_SIZE + UNIT_GAP;
			z = (UNIT_SIZE + UNIT_GAP) * 2;
		}

		// 获取需要旋转的面,共9个方块
		for (Cube cube : cubeList) {
			if (cube.getLocalTranslation().z == z) {
				list.add(cube);
			}
		}

		// 设置旋转支点坐标
		pivot.setLocalTranslation(x, y, z);

		// 将所需面的方块加入旋转支点
		for (Cube cube : list) {
			if (cube.getParent() != pivot) {
				cube.removeFromParent();
				pivot.attachChild(cube);
				cube.move(-x, -y, -z);
			}
		}

		// 旋转
		Quaternion pitch45 = new Quaternion();
		pitch45.fromAngleAxis(FastMath.PI / 2 * tpf, new Vector3f(0, 0, 1));
		pivot.setLocalRotation(pivot.getLocalRotation().mult(pitch45));
	}

	private void rotateXZ(float tpf, FaceType faceType) {
		removeCubeFromPivot();

		List<Cube> list = new ArrayList<>();
		float x = 0;
		float y = 0;
		float z = 0;
		if (FaceType.LeftXZ == faceType) {
			x = UNIT_SIZE + UNIT_GAP;
			y = 0;
			z = UNIT_SIZE + UNIT_GAP;

		} else if (FaceType.MiddleXZ == faceType) {
			x = UNIT_SIZE + UNIT_GAP;
			y = UNIT_SIZE + UNIT_GAP;
			z = UNIT_SIZE + UNIT_GAP;

		} else if (FaceType.RightXZ == faceType) {
			x = UNIT_SIZE + UNIT_GAP;
			y = (UNIT_SIZE + UNIT_GAP) * 2;
			z = UNIT_SIZE + UNIT_GAP;
		}

		// 获取需要旋转的面,共9个方块
		for (Cube cube : cubeList) {
			if (cube.getLocalTranslation().y == y) {
				list.add(cube);
			}
		}

		// 设置旋转支点坐标
		pivot.setLocalTranslation(x, y, z);

		// 将所需面的方块加入旋转支点
		for (Cube cube : list) {
			if (cube.getParent() != pivot) {
				cube.removeFromParent();
				pivot.attachChild(cube);
				cube.move(-x, -y, -z);
			}
		}

		// 旋转
		Quaternion pitch45 = new Quaternion();
		pitch45.fromAngleAxis(FastMath.PI / 2 * tpf, new Vector3f(0, 1, 0));
		pivot.setLocalRotation(pivot.getLocalRotation().mult(pitch45));
	}


	private void removeCubeFromPivot() {
		Vector3f pivotV3f = pivot.getLocalTranslation();
		Quaternion pivotRotation = pivot.getLocalRotation();
		for (Spatial cube : pivot.getChildren()) {
			cube.removeFromParent();

			cube.move(pivotV3f);
			cube.setLocalRotation(pivotRotation);

			rootNode.attachChild(cube);
		}
	}

	private void makeRubikCube() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					String name = i + "_" + j + "_" + k;

					Cube cube = new Cube(name, assetManager);
					cube.setFace(new Face(FaceType.Front, ColorRGBA.White));
					cube.setFace(new Face(FaceType.Back, ColorRGBA.Yellow));
					cube.setFace(new Face(FaceType.Top, ColorRGBA.Red));
					cube.setFace(new Face(FaceType.Bottom, ColorRGBA.Green));
					cube.setFace(new Face(FaceType.Left, ColorRGBA.Blue));
					cube.setFace(new Face(FaceType.Right, ColorRGBA.Orange));

					cube.move((UNIT_SIZE + UNIT_GAP) * i, (UNIT_SIZE + UNIT_GAP) * j, (UNIT_SIZE + UNIT_GAP) * k);
					cubeList.add(cube);

					rootNode.attachChild(cube);
				}
			}
		}

	}

	/**
	 * 添加坐标系
	 * 
	 * @param vec3
	 * @param color
	 */
	public void addCoordinates() {
		// 创建X、Y、Z方向的箭头，作为参考坐标系
		createAxes(new Vector3f(7, 0, 0), ColorRGBA.Green);
		createAxes(new Vector3f(0, 7, 0), ColorRGBA.Red);
		createAxes(new Vector3f(0, 0, 7), ColorRGBA.Blue);
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
		AppSettings settings = new AppSettings(true);
		settings.setResolution(1024, 768);
		settings.setTitle("魔方");

		RubikCube app = new RubikCube();
		app.setSettings(settings);
		app.setShowSettings(false);
		app.start();
	}

}
