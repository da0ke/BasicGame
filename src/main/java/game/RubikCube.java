package game;

import java.util.ArrayList;
import java.util.List;
import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
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
import com.jme3.input.event.MouseMotionEvent;

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
	// 鼠标横向移动距离，从picking开始
	private int dx;
	// 鼠标纵向移动距离，从picking开始
	private int dy;
	
	private float totalTpf;
	
	// pick
	private static final String PICK = "pick";
	// 方块的尺寸
	private static final float UNIT_SIZE = 1f;
	// 方块与方块的间隔
	private static final float UNIT_GAP = 0.03f;

	@Override
	public void simpleInitApp() {
		cam.setLocation(new Vector3f(-4.105831f, 4.6925287f, 5.9258566f));
		cam.setRotation(new Quaternion(0.10000886f, 0.88508904f, -0.22370124f, 0.39569354f));
		flyCam.setMoveSpeed(10);
		flyCam.setEnabled(false);
//		addCoordinates();

		makeRubikCube();

		pivot = new Node("pivot");
		rootNode.attachChild(pivot);

		initKeys();
		
//		System.out.println(FastMath.HALF_PI); //1.5707964
//		System.out.println(FastMath.PI); //3.1415927
//		System.out.println(FastMath.HALF_PI*3); //4.712389
	}

	

	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);
		
		if(dy < -10) { // 向下
			totalTpf = totalTpf + tpf;
			rotateYZ(totalTpf, FaceType.LeftYZ);
			
			if(totalTpf >= 1) {
				isPicking = false;
				dy = 0;
				dx = 0;
				totalTpf = 0;
				rotateYZ(1, FaceType.LeftYZ);
				removeCubeFromPivot(true);
			}
		} else if(dy > 10) { // 向上
			totalTpf = totalTpf + tpf;
			rotateYZ(-totalTpf, FaceType.LeftYZ);
			
			if(totalTpf >= 1) {
				isPicking = false;
				dy = 0;
				dx = 0;
				totalTpf = 0;
				rotateYZ(-1, FaceType.LeftYZ);
				removeCubeFromPivot(false);
			}
		} else if(dx > 10) { // 向右
			totalTpf = totalTpf + tpf;
			rotateXZ(totalTpf, FaceType.LeftXZ);
			
			if(totalTpf >= 1) {
				isPicking = false;
				dy = 0;
				dx = 0;
				totalTpf = 0;
				rotateXZ(1, FaceType.LeftXZ);
				removeCubeFromPivot(true);
			}
		}
	}



	private void initKeys() {
		inputManager.addMapping(PICK, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

		inputManager.addListener(actionListener, PICK);
		
		inputManager.addRawInputListener(new RubikListenser() {

			@Override
			public void onMouseMotionEvent(MouseMotionEvent evt) {
				if(isPicking) {
					dx += evt.getDX();
					dy += evt.getDY();
				}
			}
			
		});
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
					dx = 0;
					dy = 0;
					
				}
			}
		}
	};


	private void rotateYZ(float tpf, FaceType faceType) {
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
		Quaternion pitch90 = new Quaternion();
		pitch90.fromAngleAxis(FastMath.HALF_PI * tpf, new Vector3f(1, 0, 0));
		
		pivot.setLocalRotation(pitch90);
	}

	private void rotateXY(float tpf, FaceType faceType) {
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
		Quaternion pitch90 = new Quaternion();
		pitch90.fromAngleAxis(FastMath.HALF_PI * tpf, new Vector3f(0, 0, 1));
		pivot.setLocalRotation(pitch90);
	}

	private void rotateXZ(float tpf, FaceType faceType) {
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
		Quaternion pitch90 = new Quaternion();
		pitch90.fromAngleAxis(FastMath.HALF_PI * tpf, new Vector3f(0, 1, 0));
		pivot.setLocalRotation(pitch90);
	}


	private void removeCubeFromPivot(boolean positive) {
		Vector3f pivotV3f = pivot.getLocalTranslation();
		Vector3f axis = new Vector3f(pivotV3f.x==0?1:0, pivotV3f.y==0?1:0, pivotV3f.z==0?1:0);
		Quaternion pitch90 = new Quaternion();
		pitch90.fromAngleAxis(FastMath.HALF_PI * (positive?1:-1), axis);
		
		for (Spatial cube : pivot.getChildren()) {
			Vector3f v3f = cube.getWorldTranslation();
			if(v3f.x>0&&v3f.x<0.001) {
				v3f.setX(0);
			}
			if(v3f.y>0&&v3f.y<0.001) {
				v3f.setY(0);
			}
			if(v3f.z>0&&v3f.z<0.001) {
				v3f.setZ(0);
			}
			
			cube.removeFromParent();
			cube.setLocalTranslation(v3f);
			
			cube.setLocalRotation(pitch90.mult(cube.getLocalRotation()));
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
