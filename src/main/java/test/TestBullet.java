package test;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 * 使用Bullet物理引擎
 * 
 * @author Administrator
 *
 */
public class TestBullet extends SimpleApplication {

	public static void main(String[] args) {
		TestBullet app = new TestBullet();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		cam.setLocation(new Vector3f(-18.317675f, 16.480816f, 13.418682f));
		cam.setRotation(new Quaternion(0.13746259f, 0.86010045f, -0.3107305f, 0.38049686f));

		// 初始化物理引擎
		BulletAppState bulletAppState = new BulletAppState(new Vector3f(-100, -100, -100), new Vector3f(100, 100, 100));
		stateManager.attach(bulletAppState);

		// 获得Bullet的物理空间，它代表了运转物理规则的世界
		PhysicsSpace physicsSpace = bulletAppState.getPhysicsSpace();

		/**
		 * 创建地板的刚体对象，尺寸为长28m，宽15m，厚0.1m
		 * 刚体的质量设为0，这样地板就不会受到任何力的作用
		 */
		RigidBodyControl floor = new RigidBodyControl(0);// 质量为0时，这个物体就会成为静态物体
		floor.setCollisionShape(new BoxCollisionShape(new Vector3f(14f, 0.05f, 7.5f)));
		floor.setRestitution(0.8f); // 弹性系数
		physicsSpace.add(floor);
		
		/**
		 * 创建球形刚体，质量为0.65kg，半径为0.123m
		 */
		RigidBodyControl ball = new RigidBodyControl(0.65f);
		ball.setCollisionShape(new SphereCollisionShape(0.123f));
		ball.setPhysicsLocation(new Vector3f(-10, 1, 0)); // 在物理世界中的坐标
		ball.setLinearVelocity(new Vector3f(8, 5, 0)); // 线速度
		ball.setFriction(0.2f);// 摩擦系数
		ball.setRestitution(0.8f);// 弹性系数
		physicsSpace.add(ball);// 把小球添加到物理空间中
		
		
		/**
		 * 创建挡板的刚体对象，质量为0.2kg，尺寸为横宽1.8m * 竖高1.05m * 厚0.03m
		 */
		RigidBodyControl board = new RigidBodyControl(0.2f);
		board.setCollisionShape(new BoxCollisionShape(new Vector3f(0.015f, 0.525f, 0.9f)));
		
		/**
		 * 克隆10个挡板，从半场开始，间隔一米摆放。
		 */
		for(int i = 0;i < 10;i++) {
			RigidBodyControl b = (RigidBodyControl) board.jmeClone();
			b.setPhysicsLocation(new Vector3f(i * 1, 0.52f, 0f));
			physicsSpace.add(b);
		}

		// 开启调试模式，这样能够可视化观察物体的运动情况
		bulletAppState.setDebugEnabled(true);
	}

}
