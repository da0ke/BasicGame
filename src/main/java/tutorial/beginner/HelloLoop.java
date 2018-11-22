package tutorial.beginner;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class HelloLoop extends SimpleApplication {
	
	
	
	public static void main(String[] args) {
		HelloLoop app = new HelloLoop();
		app.start();
	}
	
	private Geometry player;

	@Override
	public void simpleInitApp() {
		flyCam.setMoveSpeed(10);
		
		/**
		 * this blue box is our player character
		 */
		Box b = new Box(1, 1, 1);
		player = new Geometry("blue cube", b);
		Material mat = new Material(assetManager,
		          "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Blue);
		player.setMaterial(mat);
		rootNode.attachChild(player);
	}

	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);
		
		// make the player rotate
		player.rotate(0, 2*tpf, 0);
	}
	
	

}
