package com.cjd.loadbalancer;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.Server;

import java.util.List;

public class AviliableResponseBalancer  extends AbstractLoadBalancerRule {
    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
        System.out.println(iClientConfig);
    }

    @Override
    public Server choose(Object o) {
        List<Server> serverList1 = this.getLoadBalancer().getReachableServers();
        List<Server> serverList =  this.getLoadBalancer().getAllServers();
        for (Server server:serverList){
            System.out.println(server.getId() + server.isAlive());
            System.out.println(server.getHost() + ":" +server.isAlive());
            if (server.isAlive()){
                return server;
            }
        }
        return null;
    }
}
