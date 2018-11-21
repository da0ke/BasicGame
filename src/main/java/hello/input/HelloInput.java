package hello.input;

import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;

public class HelloInput extends SimpleApplication {

	/**
	 * 开火消息
	 */
	public static final String FIRE = "Fire";

	/**
	 * 加载模型
	 */
	public static final String LOAD = "Load";

	@Override
	public void simpleInitApp() {
		// 检测输入设备
		System.out.printf("Mouse: %b\nKeyboard: %b\nJoystick: %b\nTouch: %b\n", mouseInput != null, keyInput != null,
				joyInput != null, touchInput != null);

		flyCam.setMoveSpeed(10);
		
		
		// 绑定消息和触发器
		inputManager.addMapping(FIRE, new KeyTrigger(KeyInput.KEY_SPACE),
				new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

		inputManager.addMapping(LOAD, new KeyTrigger(KeyInput.KEY_L));

		// 绑定消息和监听器
		inputManager.addListener(new ActionListener() {

			@Override
			public void onAction(String name, boolean isPressed, float tpf) {
				switch (name) {
				case FIRE: {
					if (isPressed) {
						fire();
					}
					break;
				}
				case LOAD: {
					if (isPressed) {
						System.out.println("load model");
						loadModel();
					}

					break;
				}

				}
			}
		}, FIRE, LOAD);

		// 原始输入监听器
		inputManager.addRawInputListener(new RawInputListener() {

			@Override
			public void onTouchEvent(TouchEvent evt) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onMouseMotionEvent(MouseMotionEvent evt) {
				int x = evt.getX();
				int y = evt.getY();

				// 打印鼠标的坐标
				System.out.println("x=" + x + " y=" + y);
			}

			@Override
			public void onMouseButtonEvent(MouseButtonEvent evt) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onKeyEvent(KeyInputEvent evt) {
				int keyCode = evt.getKeyCode();
				boolean isPressed = evt.isPressed();

				// 当玩家按下Y键时，输出“Yes!”
				if (isPressed && keyCode == KeyInput.KEY_Y) {
					System.out.println("Yes!");
				}
			}

			@Override
			public void onJoyButtonEvent(JoyButtonEvent evt) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onJoyAxisEvent(JoyAxisEvent evt) {
				// TODO Auto-generated method stub

			}

			@Override
			public void endInput() {
				// TODO Auto-generated method stub

			}

			@Override
			public void beginInput() {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 开火
	 */
	private void fire() {
		System.out.println("bang!");
	}

	/**
	 * 加载模型
	 */
	private void loadModel() {
		new Thread() {
			public void run() {
				// 在子线程中加载模型
				// 加载一个无光材质
				Material mat = new Material(assetManager, "MatDefs/Unshaded.j3md");
				mat.setColor("Color", ColorRGBA.Red);

				// 加载一个纹理贴图， 设置给这个材质
				Texture tex = assetManager.loadTexture("Textures/BrickWall/BrickWall.jpg");
				mat.setTexture("ColorMap", tex);

				// 创造1个方块，应用此材质
				Geometry geom = new Geometry("普通方块", new Box(1, 1, 1));
				geom.setMaterial(mat);

				
				// 通知主线程，将模型添加到场景图中
				enqueue(new Runnable() {
					
					@Override
					public void run() {
						rootNode.attachChild(geom);
					}
				});
				
			}
		}.start();
		
	}

	public static void main(String[] args) {
		// 配置参数
		AppSettings settings = new AppSettings(true);
		settings.setTitle("Hello node"); // 标题
		settings.setResolution(1024, 768); // 分辨率

		// 启动程序
		HelloInput app = new HelloInput();
		app.setSettings(settings);
		app.setShowSettings(true);
		app.start();
	}

}
