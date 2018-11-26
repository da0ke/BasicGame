package test;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

/**
 * 按键发射小球撞击砖墙
 * @author Administrator
 *
 */
public class TestPhysics extends SimpleApplication {
	
	public static void main(String[] args) {
		TestPhysics app = new TestPhysics();
		app.start();
	}
	
	/**
	 * 开火，发射小球，鼠标左键触发
	 */
	public static final String FIRE = "fire";
	
	/**
	 * 显示或隐藏BulletAppState的debug形状，按空格键触发
	 */
	public static final String DEBUG = "debug";
	
	/**
	 * 砖块的尺寸
	 */
	private static final float brickLength = 0.48f;
	private static final float brickWidth = 0.24f;
	private static final float brickHeight = 0.12f;
	
	private BulletAppState bulletAppState;
	
	@Override
	public void simpleInitApp() {
		// 调整摄像机位置
		cam.setLocation(new Vector3f(0, 4f, 6f));
        cam.lookAt(new Vector3f(2, 2, 0), Vector3f.UNIT_Y);

		// 初始化物理引擎
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);

		// 初始化按键
		initKeys();
		
		// 初始化光照
		initLight();
		
		// 初始化场景
		initScene();
	}
	
	private void initKeys() {
		inputManager.addMapping(FIRE, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addMapping(DEBUG, new KeyTrigger(KeyInput.KEY_SPACE));
		
		inputManager.addListener(actionListener, FIRE, DEBUG);
	}
	
	private ActionListener actionListener = new ActionListener() {
		
		@Override
		public void onAction(String name, boolean isPressed, float tpf) {
			if(!isPressed) {
				if(FIRE.equals(name)) {
					shootBall();
				} else if(DEBUG.equals(name)) {
					boolean debugEnabled = bulletAppState.isDebugEnabled();
					bulletAppState.setDebugEnabled(!debugEnabled);
				}
			}
		}
	};
	
	private void initLight() {
		// 环境光
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.3f, 0.3f, 0.3f, 1f));

        // 阳光
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-1, -2, -3).normalizeLocal());

        rootNode.addLight(ambient);
        rootNode.addLight(sun);
	}
	
	private void initScene() {
		makeFloor();
		makeWall();
	}
	
	private void makeFloor() {
		// 网格
		Box mesh = new Box(10f, 0.1f, 5f);
		mesh.scaleTextureCoordinates(new Vector2f(3, 6));
		
		// 材质
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		Texture tex = assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg");
		tex.setWrap(WrapMode.Repeat);
		mat.setTexture("ColorMap", tex);
		
		// 几何体
		Geometry geom = new Geometry("floor", mesh);
		geom.setMaterial(mat);
		geom.setLocalTranslation(0, -0.1f, 0);// 将地板下移一定距离，让表面和xoz平面重合
		
		// 刚体
		RigidBodyControl rigid = new RigidBodyControl(0);
		geom.addControl(rigid);
		rigid.setCollisionShape(new BoxCollisionShape(new Vector3f(10f, 0.1f, 5f)));
		
		rootNode.attachChild(geom);
		bulletAppState.getPhysicsSpace().add(rigid);
		
		
	}
	
	private void makeWall() {
		// 利用for循环生成一堵由众多砖块组成的墙体。
        float startpt = brickLength / 4;
        float height = 0;
        for (int j = 0; j < 15; j++) {
            for (int i = 0; i < 6; i++) {
                Vector3f vt = new Vector3f(i * brickLength * 2 + startpt, brickHeight + height, 0);
                makeBrick(vt);
            }
            startpt = -startpt;
            height += 2 * brickHeight;
        }
	}
	
	/**
	 * 在指定位置放置一块砖块
	 * @param loc
	 */
	private void makeBrick(Vector3f loc) {
		// 网格
        Box box = new Box(brickLength, brickHeight, brickWidth);
        box.scaleTextureCoordinates(new Vector2f(1f, 0.5f));

        // 材质
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex = assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg");
        mat.setTexture("ColorMap", tex);

        // 几何体
        Geometry geom = new Geometry("brick", box);
        geom.setMaterial(mat);
        geom.setLocalTranslation(loc);// 把砖块放在指定位置

        // 刚体
        RigidBodyControl rigidBody = new RigidBodyControl(2f);
        geom.addControl(rigidBody);
        rigidBody.setCollisionShape(new BoxCollisionShape(new Vector3f(brickLength, brickHeight, brickWidth)));

        rootNode.attachChild(geom);
        bulletAppState.getPhysicsSpace().add(rigidBody);
	}
	
	private void shootBall() {
		// 网格
        Sphere sphere = new Sphere(32, 32, 0.4f, true, false);
        sphere.setTextureMode(TextureMode.Projected);

        // 材质
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex = assetManager.loadTexture("Textures/Terrain/Rock/Rock.PNG");
        mat.setTexture("ColorMap", tex);

        // 几何体
        Geometry geom = new Geometry("cannon ball", sphere);
        geom.setMaterial(mat);

        // 刚体
        RigidBodyControl rigidBody = new RigidBodyControl(1f);
        geom.addControl(rigidBody);
        rigidBody.setCollisionShape(new SphereCollisionShape(0.4f));
        rigidBody.setPhysicsLocation(cam.getLocation());// 位置
        rigidBody.setLinearVelocity(cam.getDirection().mult(25));// 初速度

        rootNode.attachChild(geom);
        bulletAppState.getPhysicsSpace().add(rigidBody);
	}
}
