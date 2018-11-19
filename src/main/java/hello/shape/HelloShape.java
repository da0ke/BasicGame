package hello.shape;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;

public class HelloShape extends SimpleApplication {

	@Override
	public void simpleInitApp() {
		flyCam.setMoveSpeed(10);
		
		// 创建一个有10根纬线、16根经线、半径为2的球体
		Geometry geom = new Geometry("球体", new Sphere(10, 16, 2));
		
		// 创建材质，并显示网格线
		Material mat = new Material(assetManager, "MatDefs/Unshaded.j3md");
		mat.getAdditionalRenderState().setWireframe(true);
		geom.setMaterial(mat);
		
		// 将物体添加到场景中
		rootNode.attachChild(geom);
	}
	
	public static void main(String[] args) {
		// 配置参数
		AppSettings settings = new AppSettings(true);
		settings.setTitle("一个方块"); //标题
		settings.setResolution(1024, 768); //分辨率
				
		// 启动程序		
		HelloShape app = new HelloShape();
		app.setSettings(settings);
		app.setShowSettings(true);
		app.start();
	}

}
