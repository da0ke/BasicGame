package tutorial.beginner;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

public class HelloPicking extends SimpleApplication {

	public static void main(String[] args) {
		HelloPicking app = new HelloPicking();
		app.start();
	}

	private Node shootables;
	private Geometry mark;
	private static final String SHOOT = "Shoot";

	@Override
	public void simpleInitApp() {
		initCrossHairs();
		initKeys();
		initMark();

		/** create four colored boxes and a floor to shoot at: */
		shootables = new Node("Shootables");
		rootNode.attachChild(shootables);
		shootables.attachChild(makeCube("a Dragon", -2f, 0f, 1f));
		shootables.attachChild(makeCube("a tin can", 1f, -2f, 0f));
		shootables.attachChild(makeCube("the Sheriff", 0f, 1f, -2f));
		shootables.attachChild(makeCube("the Deputy", 1f, 0f, -4f));
		shootables.attachChild(makeFloor());
		shootables.attachChild(makeCharacter());
	}

	/**
	 * 初始化瞄准镜
	 */
	private void initCrossHairs() {
		setDisplayStatView(false);

		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		BitmapText text = new BitmapText(guiFont, false);
		text.setSize(guiFont.getCharSet().getRenderedSize() * 2);
		text.setText("+");
		text.setLocalTranslation(settings.getWidth() / 2 - text.getLineWidth() / 2,
				settings.getHeight() / 2 + text.getLineHeight() / 2, 0);
		guiNode.attachChild(text);
	}

	private void initKeys() {
		inputManager.addMapping(SHOOT, new KeyTrigger(KeyInput.KEY_SPACE),
				new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addListener(new ActionListener() {

			@Override
			public void onAction(String name, boolean isPressed, float tpf) {
				if (name.equals(SHOOT) && !isPressed) {
					// 1.Reset results list
					CollisionResults results = new CollisionResults();

					// 2.Aim the ray from cam loc to cam deirection
					Ray ray = new Ray(cam.getLocation(), cam.getDirection());

					// 3.Collect intersections between Ray and Shootables in results list
					shootables.collideWith(ray, results);

					// 4.Print the results
					System.out.println("----- Collisions? " + results.size() + "-----");
					for (int i = 0; i < results.size(); i++) {
						// For each hit, wo know distance, impact point, name of geometry
						float distance = results.getCollision(i).getDistance();
						Vector3f impactPoint = results.getCollision(i).getContactPoint();
						String hit = results.getCollision(i).getGeometry().getName();

						System.out.println("* Collision #" + i);
						System.out.println("  You shot " + hit + " at " + impactPoint + ", " + distance + " wu away.");

					}
					
					// 5.Use the results (we mark the hit object)
					if(results.size() > 0) {
						CollisionResult closest = results.getClosestCollision();
						mark.setLocalTranslation(closest.getContactPoint());
						rootNode.attachChild(mark);
					} else {
						//No hits?Then remove the red mark
						rootNode.detachChild(mark);
					}
					
				}
			}
		}, SHOOT);
	}

	/** A red ball that marks the last spot that was "hit" by the "shot". */
	private void initMark() {
		Sphere sphere = new Sphere(30, 30, 0.2f);
		mark = new Geometry("BOOM!", sphere);
		Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mark_mat.setColor("Color", ColorRGBA.Red);
		mark.setMaterial(mark_mat);
	}

	private Geometry makeCube(String name, float x, float y, float z) {
		Box box = new Box(1, 1, 1);
		Geometry cube = new Geometry(name, box);
		cube.setLocalTranslation(x, y, z);
		Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat1.setColor("Color", ColorRGBA.randomColor());
		cube.setMaterial(mat1);
		return cube;
	}

	/** A floor to show that the "shot" can go through several objects. */
	private Geometry makeFloor() {
		Box box = new Box(15, .2f, 15);
		Geometry floor = new Geometry("the Floor", box);
		floor.setLocalTranslation(0, -4, -5);
		Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat1.setColor("Color", ColorRGBA.Gray);
		floor.setMaterial(mat1);
		return floor;
	}

	protected Spatial makeCharacter() {
		// load a character from jme3test-test-data
		Spatial golem = assetManager.loadModel("Models/Oto/Oto.mesh.j3o");
		golem.scale(0.5f);
		golem.setLocalTranslation(-1.0f, -1.5f, -0.6f);

		// We must add a light to make the model visible
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
		golem.addLight(sun);
		return golem;
	}

}
