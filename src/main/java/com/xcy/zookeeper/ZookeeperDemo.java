package com.xcy.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.apache.zookeeper.Watcher.Event.EventType.*;

/**
 * Created by xuchunyang on 2018/6/8 18点57分
 */
public class ZookeeperDemo {

    public static final String lockPath = "/locks";

    public static AsyncCallback.StringCallback callback = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {

            System.out.println("StringCallBack: "+path+ "   "+ctx + "   "+name);

        }
    };


    public static void main(String[] args) throws Exception {

        final ZooKeeper zk = new ZooKeeper("172.16.10.41:2181,172.16.10.42:2181,172.16.10.43:2181", 2000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {

                System.out.println("Event Trigger : State->"+event.getState() + " : Type->"+event.getType());

                switch (event.getState()) {
                    case SyncConnected: {
                        System.out.println("SyncConnected");
                        break;
                    }
                    case Disconnected: {
                        System.out.println("Disconnected");
                        break;
                    }
                    default:
                        break;
                }

                switch (event.getType()) {
                    case NodeDataChanged: {
                        System.out.println("Node Changed");
                        break;
                    }
                    case NodeCreated: {
                        System.out.println("Node Created");
                        break;
                    }
                    case NodeDeleted: {
                        System.out.println("Node Deleted");
                        break;
                    }
                    case None: {
                        System.out.println("EventType None");
                        break;
                    }
                    default:
                        break;
                }
            }
        }, true);

        createNodes(zk);

//        final int childrenCount = countChildren(zk);
//
//        System.out.println("ChildrenCount : " + childrenCount);

    }

    private static int countChildren(ZooKeeper zk) throws Exception {

        return zk.getChildren("/locks", false).size();

    }

    private static void createNodes(ZooKeeper zk) throws Exception {

        int i = 1;

        while (true) {

//            final String createResult = zk.create(lockPath + "/child"+(i++), null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            zk.create(lockPath + "/child"+(i++), null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, callback, null);

//            System.out.println("创建结果："+createResult);

//            final List<String> children = zk.getChildren("/locks", false);
//
//            children.forEach(child -> {
//                System.out.println(child);
//            });

            Thread.sleep(3000);


        }


    }

    private CountDownLatch countDownLatch;

    public void process(ZooKeeper zk, WatchedEvent event) {
        // TODO Auto-generated method stub
        if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
            System.out.println("watcher received event");
            countDownLatch.countDown();
        }
        System.out.println("回调watcher1实例： 路径" + event.getPath() + " 类型："+ event.getType());
        // 事件类型，状态，和检测的路径
        Watcher.Event.EventType eventType = event.getType();
        Watcher.Event.KeeperState state = event.getState();
        String watchPath = event.getPath();
        switch (eventType) {
            case NodeCreated:
                break;
            case NodeDataChanged:
                break;
            case NodeChildrenChanged:
                try {
                    //处理收到的消息
                    handleMessage(zk, watchPath);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (KeeperException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    public void handleMessage(ZooKeeper zk, String watchPath) throws KeeperException,InterruptedException, UnsupportedEncodingException {
        System.out.println("收到消息");
        //再监听该子节点
        List<String> Children = getChildren(watchPath, zk);
        for (String a : Children) {
            String childrenpath = watchPath + "/" + a;
            byte[] recivedata = zk.getData(childrenpath, false, null);
            String recString = new String(recivedata, "UTF-8");
            System.out.println("receive the path:" + childrenpath + ":data:"+ recString);
            //做完了之后，删除该节点
            zk.delete(childrenpath, -1);
        }
    }

    public List<String> getChildren(String path, ZooKeeper zk) throws KeeperException,InterruptedException {
        //监听该节点子节点的变化情况
        return zk.getChildren(path, false);
    }
    public Stat setData(String path, byte[] data, int version, ZooKeeper zk)throws KeeperException, InterruptedException {
        return zk.setData(path, data, version);
    }


}
