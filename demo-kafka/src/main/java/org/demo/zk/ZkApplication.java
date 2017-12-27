package org.demo.zk;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * CreateMode.PERSISTENT	永久性节点
CreateMode.PERSISTENT_SEQUENTIAL	永久性序列节点
CreateMode.EPHEMERAL	临时节点，会话断开或过期时会删除此节点
CreateMode.PERSISTENT_SEQUENTIAL	临时序列节点，会话断开或过期时会删除此节点
 * @author liujianquan
 *
 */
public class ZkApplication {
	// 创建一个与服务器的连接
	public static ZooKeeper zk;
	static {
		try {
			zk = new ZooKeeper("172.19.10.5:2181", 6000, new Watcher() {
				// 监控所有被触发的事件
				public void process(WatchedEvent event) {
					System.out.println("已经触发了" + event.getType() + "事件！" + event.getPath());
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void client() {
		// 创建一个目录节点
		try {
			zk.delete("/testRootPath", -1);
			zk.exists("/testRootPath", new Watcher() {

				public void process(WatchedEvent event) {
					System.out.println("监控目录数据修改  " + event.getPath() + "  --  " + event.getState().values());
				}

			});
			zk.create("/testRootPath", "testRootData".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			zk.setData("/testRootPath", "test".getBytes(), -1);
			zk.setData("/testRootPath", "bbst".getBytes(), -1);

		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	static String root = "/prop";
	static Object mutex = new Object();

	static boolean produce(int i) throws KeeperException, InterruptedException {
//		zk.create("/prop", "testRootData".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		ByteBuffer b = ByteBuffer.allocate(4);
		byte[] value;
		b.putInt(i);
		value = b.array();
		zk.create(root + "/element", value, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
		return true;
	}

	static int consume() throws KeeperException, InterruptedException {
		int retvalue = -1;
		Stat stat = null;
		while (true) {
			synchronized (mutex) {
				List<String> list = zk.getChildren(root, true);
				System.out.println(list);
				if (list.size() == 0) {
					mutex.wait();
				} else {
					Integer min = new Integer(list.get(0).substring(7));
					
					for (String s : list) {
						Integer tempValue = new Integer(s.substring(7));
						if (tempValue < min)
							min = tempValue;
					}
					System.out.println(min);
//					min=1;
					byte[] b = zk.getData(root + "/element000000000" + min, false, stat);
					zk.delete(root + "/element000000000" + min, 0);
					ByteBuffer buffer = ByteBuffer.wrap(b);
					retvalue = buffer.getInt();
					System.out.println("value:"+retvalue);
					return retvalue;
				}
			}
		}
	}
	
	public static void main(String[] args) {
//		client();
		try {
//			produce(4);
			consume();
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
