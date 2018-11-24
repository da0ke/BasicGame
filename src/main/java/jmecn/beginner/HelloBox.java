package jmecn.beginner;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;

public class HelloBox extends BaseApplication {
	
	private Geometry geom;

	/**
	 * 初始化3D场景，显示一个方块
	 */
	@Override
	public void simpleInitApp() {
		// 创建一个方块形状的网格
		Mesh box = new Box(1, 1, 1);
		
		// 加载一个感光材质
		Material mat = new Material(assetManager, "MatDefs/Lighting.j3md");
		
		// 创建一个几何体，应用刚才的网格和材质
		geom = new Geometry("Box");
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
	
	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);
		
		// 旋转速度：每秒90°
		float speed = FastMath.PI/2;
		
		// 让方块匀速旋转
		geom.rotate(0, tpf * speed, 0);
	}
	
	public static void main(String[] args) {
		// 启动程序
		new HelloBox().start("一个方块");
	}

}
