package cn.da0ke.box;
import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

public class BasicGame extends SimpleApplication {

	/**
	 * 初始化3D场景，显示一个方块
	 */
	@Override
	public void simpleInitApp() {
		// 创建一个方块形状的网格
		Mesh box = new Box(1, 1, 1);
		
		// 加载一个感光材质
		Material mat = new Material(assetManager, "light/Lighting.j3md");
		
		// 创建一个几何体，应用刚才的网格和材质
		Geometry geom = new Geometry("Box");
		geom.setMesh(box);
		geom.setMaterial(mat);
		
		// 创建一束定向光，并让它斜向下照射，好使我们能够看清那个方块。
		DirectionalLight light = new DirectionalLight();
		light.setDirection(new Vector3f(-1, -2, -3));
		
		// 将方块和光源都添加到场景图中
		rootNode.attachChild(geom);
		rootNode.addLight(light);
		
		// 设置摄像机位置和视角
		cam.setLocation(new Vector3f(4.6257105f, 2.5871375f, 9.046516f));
		cam.setRotation(new Quaternion(-0.032686457f, 0.96426344f, -0.1427495f, -0.22079447f));
		
	}
	
	public static void main(String[] args) {
		// 配置参数
		AppSettings settings = new AppSettings(true);
		settings.setTitle("一个方块"); //标题
		settings.setResolution(1024, 768); //分辨率
		
		
		// 启动程序
		BasicGame app = new BasicGame();
		app.setSettings(settings);
		app.setShowSettings(true);
		app.start();
	}

}
