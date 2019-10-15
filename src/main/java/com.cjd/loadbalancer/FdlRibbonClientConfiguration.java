package com.cjd.loadbalancer;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class FdlRibbonClientConfiguration {

    public static final List<Server> listOfServers = new ArrayList(){{
        add(new Server("localhost:8081"));
        add(new Server("localhost:8082"));
        add(new Server("localhost:8083"));
    }};





    @Bean
    public ServerList<Server> ribbonServers(){

        return new ServerList<Server>() {
            @Override
            public List<Server> getInitialListOfServers() {
                return listOfServers;
            }

            @Override
            public List<Server> getUpdatedListOfServers() {
                return listOfServers;
            }
        };

    }
}
