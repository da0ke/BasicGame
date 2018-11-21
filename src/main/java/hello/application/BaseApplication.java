package hello.application;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.system.AppSettings;

public abstract class BaseApplication extends SimpleApplication {

	/**
	 * 添加坐标系
	 * 
	 * @param vec3
	 * @param color
	 */
	public void addCoordinate() {
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

	public void start(String title) {
		// 配置参数
		AppSettings settings = new AppSettings(true);
		settings.setTitle(title); // 标题
		settings.setResolution(1024, 768); // 分辨率

		setSettings(settings);
		setShowSettings(true);

		start();
	}
	
	public void toggleWireframe(final boolean flag) {
		// 深度优先遍历
		rootNode.depthFirstTraversal(new SceneGraphVisitor() {
			
			@Override
			public void visit(Spatial spatial) {
				if(spatial instanceof Geometry) {
					Geometry geom = (Geometry)spatial;
					
					Material mat = geom.getMaterial();
	                if (mat != null) {
	                    mat.getAdditionalRenderState().setWireframe(flag);
	                }
				}
			}
		});
	}

}
