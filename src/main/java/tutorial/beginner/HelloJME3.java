package tutorial.beginner;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class HelloJME3 extends SimpleApplication {

	public static void main(String[] args) {
		HelloJME3 app = new HelloJME3();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		flyCam.setMoveSpeed(10);
		
		// create cube shape
		Box b = new Box(1, 1, 1);
		// create cube geometry from the shape
		Geometry geom = new Geometry("Box", b);
		// create a simple material
		Material mat = new Material(assetManager, "MatDefs/Unshaded.j3md");
		// set color of material to blue
		mat.setColor("Color", ColorRGBA.Blue);

		// set the cube's material
		geom.setMaterial(mat);

		// make the cube appear in the scene
		rootNode.attachChild(geom);

	}

}
