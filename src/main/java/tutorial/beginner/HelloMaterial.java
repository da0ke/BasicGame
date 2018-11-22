package tutorial.beginner;

import javax.swing.plaf.ColorUIResource;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.jme3.util.TangentBinormalGenerator;

public class HelloMaterial extends SimpleApplication {

	public static void main(String[] args) {
		HelloMaterial app = new HelloMaterial();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		flyCam.setMoveSpeed(10);

		/**
		 * A simple textured cube -- in good MIP map quality
		 */
		Box cube1Mesh = new Box(1, 1, 1);
		Geometry cube1Geom = new Geometry("My Textured Box", cube1Mesh);
		cube1Geom.setLocalTranslation(-3f, 1.1f, 0f);
		Material cube1Mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		Texture cube1Tex = assetManager.loadTexture("Interface/Logo/Monkey.jpg");
		cube1Mat.setTexture("ColorMap", cube1Tex);
		cube1Geom.setMaterial(cube1Mat);
		rootNode.attachChild(cube1Geom);

		/**
		 * A translucent/transparent texture, similar to a window frame
		 */
		Box cube2Mesh = new Box(1f, 1f, 0.01f);
		Geometry cube2Geom = new Geometry("window frame", cube2Mesh);
		Material cube2Mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		cube2Mat.setTexture("ColorMap", assetManager.loadTexture("Textures/ColoredTex/Monkey.png"));
		cube2Mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		cube2Geom.setQueueBucket(Bucket.Transparent);
		cube2Geom.setMaterial(cube2Mat);
		rootNode.attachChild(cube2Geom);

		/**
		 * A bumpy rock with a shiny light effect
		 */
		Sphere sphereMesh = new Sphere(32, 32, 2);
		Geometry sphereGeom = new Geometry("Shiny rock", sphereMesh);
		sphereMesh.setTextureMode(Sphere.TextureMode.Projected); // better quality on spheres
		TangentBinormalGenerator.generate(sphereMesh); // for lighting effect
		Material sphereMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		sphereMat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg"));
		sphereMat.setTexture("NormalMap", assetManager.loadTexture("Textures/Terrain/Pond/Pond_normal.png"));
		sphereMat.setBoolean("UseMaterialColors",true);
	    sphereMat.setColor("Diffuse",ColorRGBA.White);
	    sphereMat.setColor("Specular",ColorRGBA.White);
	    sphereMat.setFloat("Shininess", 64f);  // [0,128]
	    sphereGeom.setMaterial(sphereMat);
	    sphereGeom.setLocalTranslation(0,2,-2); // Move it a bit
	    sphereGeom.rotate(1.6f, 0, 0);          // Rotate it a bit
	    rootNode.attachChild(sphereGeom);
	    
	    /**
	     * Must add a light to make the lit object visible
	     */
	    DirectionalLight sun = new DirectionalLight();
	    sun.setDirection(new Vector3f(1, 0, -2).normalizeLocal());
	    sun.setColor(ColorRGBA.White);
	    rootNode.addLight(sun);
	}

}
