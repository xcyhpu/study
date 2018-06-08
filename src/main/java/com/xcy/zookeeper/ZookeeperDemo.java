package com.xcy.zookeeper;

import org.apache.zookeeper.*;

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


}
