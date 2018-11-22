package tutorial.beginner;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * How to map keys and mouse buttons to actions
 * @author Administrator
 *
 */
public class HelloInput extends SimpleApplication {
	
	public static void main(String[] args) {
		HelloInput app = new HelloInput();
		app.start();
	}
	
	private Geometry player;
	private boolean isRunning = true;
	private static final String ACTION_PAUSE = "Pause";
	private static final String ACTION_LEFT = "Left";
	private static final String ACTION_RIGHT = "Right";
	private static final String ACTION_ROTATE = "Rotate";
	private static final String ACTION_UP = "Up";
	private static final String ACTION_DOWN = "Down";

	@Override
	public void simpleInitApp() {
		flyCam.setEnabled(false);
		
		Box b = new Box(1, 1, 1);
		player = new Geometry("Player", b);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Blue);
		player.setMaterial(mat);
		rootNode.attachChild(player);
		
		initKeys();
	}
	
	/**
	 * Custom Keybinding: Map named actions to inputs
	 */
	private void initKeys() {
		// Your can map one or several inputs to one named action
		inputManager.addMapping(ACTION_PAUSE, new KeyTrigger(KeyInput.KEY_P));
		inputManager.addMapping(ACTION_LEFT, new KeyTrigger(KeyInput.KEY_J));
		inputManager.addMapping(ACTION_RIGHT, new KeyTrigger(KeyInput.KEY_K));
		inputManager.addMapping(ACTION_UP, new KeyTrigger(KeyInput.KEY_H)
				, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
		inputManager.addMapping(ACTION_DOWN, new KeyTrigger(KeyInput.KEY_L)
				, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
		inputManager.addMapping(ACTION_ROTATE, new KeyTrigger(KeyInput.KEY_SPACE)
				, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		
		// Add the names to the action listener
		inputManager.addListener(actionListener, ACTION_PAUSE);
		inputManager.addListener(analogListener, ACTION_LEFT, ACTION_RIGHT, ACTION_ROTATE, ACTION_UP, ACTION_DOWN);
	}
	
	private final ActionListener actionListener = new ActionListener() {
		
		@Override
		public void onAction(String name, boolean isPressed, float tpf) {
			if(name.equals(ACTION_PAUSE) && !isPressed) {
				isRunning = !isRunning;
			}
		}
	};
	
	private final AnalogListener analogListener = new AnalogListener() {
		
		@Override
		public void onAnalog(String name, float value, float tpf) {
			if(isRunning) {
				if(name.equals(ACTION_ROTATE)) {
					player.rotate(0, value*speed, 0);
				}
				if(name.equals(ACTION_RIGHT)) {
					Vector3f v = player.getLocalTranslation();
					player.setLocalTranslation(v.x+value*speed, v.y, v.z);
				}
				if(name.equals(ACTION_LEFT)) {
					Vector3f v = player.getLocalTranslation();
					player.setLocalTranslation(v.x-value*speed, v.y, v.z);
				}
				if(name.equals(ACTION_UP)) {
					Vector3f v = player.getLocalTranslation();
					player.setLocalTranslation(v.x, v.y+value*speed, v.z);
				}
				if(name.equals(ACTION_DOWN)) {
					Vector3f v = player.getLocalTranslation();
					player.setLocalTranslation(v.x, v.y-value*speed, v.z);
				}
			} else {
				System.out.println("Press P to unpause");
			}
		}
	};
	

}
