package hello.node;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;

public class HelloNode extends SimpleApplication {
	
	private Spatial spatial;

	@Override
	public void simpleInitApp() {
		flyCam.setMoveSpeed(10);
		
		// 球体网格
		Mesh mesh = new Sphere(16, 24, 1);
		
		// 创建2个球体
		Geometry redGeom = new Geometry("红色气球", mesh);
		redGeom.setMaterial(newLightingMaterial(ColorRGBA.Red));
		
		Geometry cyanGeom = new Geometry("青色气球", mesh);
		cyanGeom.setMaterial(newLightingMaterial(ColorRGBA.Cyan));
		
		// 将2个气球添加到1个Node节点中
		Node node = new Node("原点");
		node.attachChild(redGeom);
		node.attachChild(cyanGeom);
		
		// 设置2个气球的相对位置
		redGeom.setLocalTranslation(-1, 3, 0);
		cyanGeom.setLocalTranslation(1.5f, 2, 0);
		
		// 缩放2个气球的大小
		node.scale(0.5f);
		
		// 将这个节点添加到场景图中
		rootNode.attachChild(node);
		
		// 添加光源
		addLight();
		
		this.spatial = node;
		

		toggleWireframe(true);
	}
	
	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);
		
		if(spatial != null) {
			// 绕Y轴旋转
			spatial.rotate(0, 3.1415926f * tpf, 0);
		}
		
	}
	
	/**
	 * 创建一个感光材质
	 * @param color
	 * @return
	 */
	private Material newLightingMaterial(ColorRGBA color) {
		// 加载一个感光材质
		Material mat = new Material(assetManager, "MatDefs/Lighting.j3md");
		mat.setColor("Diffuse", color);// 在漫射光照射下反射的颜色
		mat.setColor("Ambient", color);// 在环境光照射下，反射的颜色
		mat.setColor("Specular", ColorRGBA.White);// 镜面反射时，高光的颜色
        mat.setFloat("Shininess", 24);// 光泽度，取值范围1~128
        mat.setBoolean("UseMaterialColors", true); // 使用上面设置的Diffuse、Ambient、Specular等颜色
        
		return mat;
	}
	
	/**
	 * 添加光源
	 */
	private void addLight() {
		// 定向光
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-1, -2, -3));

        // 环境光
        AmbientLight ambient = new AmbientLight();

        // 调整光照亮度
        ColorRGBA lightColor = new ColorRGBA();
        sun.setColor(lightColor.mult(0.8f));
        ambient.setColor(lightColor.mult(0.2f));
        
        // #3 将模型和光源添加到场景图中
        rootNode.addLight(sun);
        rootNode.addLight(ambient);
	}

	public static void main(String[] args) {
		// 配置参数
		AppSettings settings = new AppSettings(true);
		settings.setTitle("Hello node"); // 标题
		settings.setResolution(1024, 768); // 分辨率

		// 启动程序
		HelloNode app = new HelloNode();
		app.setSettings(settings);
		app.setShowSettings(true);
		app.start();
		
	}

	private void toggleWireframe(final boolean flag) {
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
