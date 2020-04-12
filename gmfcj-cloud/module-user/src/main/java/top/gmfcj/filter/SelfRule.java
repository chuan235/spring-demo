package top.gmfcj.filter;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

import java.util.List;

/**
 * 自定义轮训策略,基于Eureka中的轮询策略
 */
public class SelfRule extends AbstractLoadBalancerRule {


    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    public Server choose(ILoadBalancer lb, Object key) {
        List<Server> serverList = lb.getAllServers();
        System.out.println("服务数量 =="+serverList.size());
        for (Server server : serverList) {
            System.out.print("\nhost="+server.getHost());
            System.out.print("\thostPort="+server.getHostPort());
            System.out.print("\tid="+server.getId());
            System.out.print("\tscheme="+server.getScheme());
            System.out.print("\tzone="+server.getZone());
            // metaInfo=com.netflix.niws.loadbalancer.DiscoveryEnabledServer$1@a5113e6
            System.out.print("\tmetaInfo="+server.getMetaInfo());
            System.out.print("\tport="+server.getPort());
            System.out.println(server);
        }
        return serverList.get(0);
    }

    @Override
    public Server choose(Object key) {
        System.out.println("开始选择服务...");
        return choose(getLoadBalancer(), key);
    }
}
