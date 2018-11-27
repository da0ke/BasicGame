package game;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.debug.Arrow;
import com.jme3.system.AppSettings;

public class MagicCube extends SimpleApplication {
	

	@Override
	public void simpleInitApp() {
		flyCam.setMoveSpeed(10);
//		flyCam.setDragToRotate(true);

		
		addCoordinates();
		
		makeMagicCube();
	}
	
	
	private void makeMagicCube() {
		for(int k=0;k<3;k++) {
			for(int j=0;j<3;j++) {
				for(int i=0;i<3;i++) {
					Cube cube = new Cube("cube", assetManager);
					cube.setFace(new Face(FaceType.Front, ColorRGBA.White));
					cube.setFace(new Face(FaceType.Back, ColorRGBA.Yellow));
					cube.setFace(new Face(FaceType.Top, ColorRGBA.Red));
					cube.setFace(new Face(FaceType.Bottom, ColorRGBA.Green));
					cube.setFace(new Face(FaceType.Left, ColorRGBA.Blue));
					cube.setFace(new Face(FaceType.Right, ColorRGBA.Orange));

					cube.move(1.03f*i, 1.03f*j, 1.03f*k);
					
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
		createAxes(new Vector3f(5, 0, 0), ColorRGBA.Green);
		createAxes(new Vector3f(0, 5, 0), ColorRGBA.Red);
		createAxes(new Vector3f(0, 0, 5), ColorRGBA.Blue);
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
		settings.setTitle("魔方");
		settings.setResolution(1024, 768);
		
		MagicCube app = new MagicCube();
		app.setSettings(settings);
		app.setShowSettings(false);
		app.start();
	}

}
