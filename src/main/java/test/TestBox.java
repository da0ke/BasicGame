package test;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.shape.Box;

/**
 * 每个面不同的颜色
 * @author Administrator
 *
 */
public class TestBox extends SimpleApplication {

	@Override
	public void simpleInitApp() {
		cam.setLocation(new Vector3f(5.0675006f, 3.9122248f, 7.5168543f));
		cam.setRotation(new Quaternion(-0.05449508f, 0.93968856f, -0.17876124f, -0.28646138f));
		
		flyCam.setMoveSpeed(10);
		
		 Box box = new Box(1, 1, 1);
	}
	
	public static void main(String[] args) {
		TestBox app = new TestBox();
		app.start();
	}

}
