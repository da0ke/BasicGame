package jmecn.beginner;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

public class HelloMaterial extends BaseApplication {

	@Override
	public void simpleInitApp() {
		flyCam.setMoveSpeed(10);
		
		// 添加物体
		addUnshadedBox();
		addLightingBox();
		
		addUnshadedSphere();
		addLightingSphere();
		
		// 添加光源
		addLight();
		
		// 把窗口背景改成淡蓝色
		viewPort.setBackgroundColor(new ColorRGBA(0.6f, 0.7f, 0.9f, 1));
	}
	
	/**
	 * 创建一个方块，应用无光材质
	 */
	private void addUnshadedBox() {
		// 加载一个无光材质
		Material mat = new Material(assetManager, "MatDefs/Unshaded.j3md");
		
		// 加载一个纹理贴图， 设置给这个材质
		Texture tex = assetManager.loadTexture("Textures/BrickWall/BrickWall.jpg");
		mat.setTexture("ColorMap", tex);
		
		// 创造1个方块，应用此材质
		Geometry geom = new Geometry("普通方块", new Box(1, 1, 1));
        geom.setMaterial(mat);
        
        geom.move(4, 0, 0);
        rootNode.attachChild(geom);
	}
	
	/**
	 * 创建一个方块，应用受光材质
	 */
	private void addLightingBox() {
		// 加载一个受光材质
		Material mat = new Material(assetManager, "MatDefs/Lighting.j3md");
		
		// #2 设置纹理贴图
        // 漫反射贴图
        Texture tex = assetManager.loadTexture("Textures/BrickWall/BrickWall.jpg");
        mat.setTexture("DiffuseMap", tex);
        
        // 法线贴图
        tex = assetManager.loadTexture("Textures/BrickWall/BrickWall_normal.jpg");
        mat.setTexture("NormalMap", tex);
        
        // 视差贴图
        tex = assetManager.loadTexture("Textures/BrickWall/BrickWall_height.jpg");
        mat.setTexture("ParallaxMap", tex);
        
        // 设置反光度
        mat.setFloat("Shininess", 2.0f); //光泽度，取值范围1~128
        
        // 创造1个方块，应用此材质。
        Geometry geom = new Geometry("文艺方块", new Box(1, 1, 1));
        geom.setMaterial(mat);
        
        rootNode.attachChild(geom);
	}
	
	/**
	 * 创建一个红色的小球，应用无光材质
	 */
	private void addUnshadedSphere() {
		// 加载一个无光材质
		Material mat = new Material(assetManager, "MatDefs/Unshaded.j3md");
		
		// 设置颜色
		mat.setColor("Color", ColorRGBA.Red);
		
		// 创建一个球体，应用此材质
		Geometry geom = new Geometry("球体", new Sphere(20, 40, 1));
		geom.setMaterial(mat);
		
		geom.move(4, 3, 0);
		rootNode.attachChild(geom);
	}
	
	/**
	 * 创建一个红色的小球，应用受光材质
	 */
	private void addLightingSphere() {
		// 加载一个受光材质
		Material mat = new Material(assetManager, "MatDefs/Lighting.j3md");
		
		// 设置参数
		mat.setColor("Diffuse", ColorRGBA.Red);// 在漫射光照射下反射的颜色
		mat.setColor("Ambient", ColorRGBA.Red);// 在环境光照射下，反射的颜色
		mat.setColor("Specular", ColorRGBA.White);// 镜面反射时，高光的颜色
		
		// 反光度越低，光斑越大，亮度越低。
        mat.setFloat("Shininess", 32);// 光泽度，取值范围1~128
        
        // 使用上面设置的Diffuse、Ambient、Specular等颜色
        mat.setBoolean("UseMaterialColors", true);
        
        //创造1个球体，应用此材质。
        Geometry geom = new Geometry("文艺小球", new Sphere(20, 40, 1));
        geom.setMaterial(mat);
        
        geom.move(0, 3, 0);
        rootNode.attachChild(geom);
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
		// 启动程序
		new HelloMaterial().start("材质");
	}

}
