package tutorial.beginner;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class HelloAnimation extends SimpleApplication implements AnimEventListener {
	
	public static void main(String[] args) {
		HelloAnimation app = new HelloAnimation();
		app.start();
	}
	
	private AnimChannel channel;
	private AnimControl control;
	private Node player;
	private static final String ACTION_WALK = "Walk";
	private static final String ACTION_STAND = "stand";
	private static final String ACTION_DODGE = "Dodge";
	
	@Override
	public void simpleInitApp() {
		viewPort.setBackgroundColor(ColorRGBA.LightGray);
		initKeys();
		
		DirectionalLight light = new DirectionalLight();
		light.setDirection(new Vector3f(-0.1f, -1f, -1f).normalizeLocal());
		rootNode.addLight(light);
		
		player = (Node) assetManager.loadModel("Models/Oto/Oto.mesh.j3o");
		player.setLocalScale(0.5f);
		rootNode.attachChild(player);
		control = player.getControl(AnimControl.class);
		control.addListener(this);
		channel = control.createChannel();
		channel.setAnim(ACTION_STAND);
		
	}
	
	@Override
	public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
		if(animName.equals(ACTION_WALK)) {
			channel.setAnim(ACTION_DODGE, 0.5f);
			channel.setLoopMode(LoopMode.DontLoop);
			channel.setSpeed(0.1f);
		}
	}

	@Override
	public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
		
	}
	
	private void initKeys() {
		inputManager.addMapping(ACTION_WALK, new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addListener(actionListener, ACTION_WALK);
	}
	
	private ActionListener actionListener = new ActionListener() {
		
		@Override
		public void onAction(String name, boolean isPressed, float tpf) {
			if(name.equals(ACTION_WALK) && !isPressed) {
				if(!channel.getAnimationName().equals(ACTION_WALK)) {
					channel.setAnim(ACTION_WALK, 0.5f);
					channel.setLoopMode(LoopMode.Loop);
				}
			}
		}
	};

}
