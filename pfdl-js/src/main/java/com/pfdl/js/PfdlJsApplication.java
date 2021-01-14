package com.pfdl.js;

import com.pfdl.common.netty.JSConstants;
import com.pfdl.js.netty.server.NettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.Async;
import java.net.InetSocketAddress;


@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class PfdlJsApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(PfdlJsApplication.class);
    public static void main(String[] args) {
        /**
         * 启动springboot
         */
        SpringApplication app = new SpringApplication(PfdlJsApplication.class);
        app.run(args);
        log.info("SpringBoot ----  启动成功 ");
    }

    @Async
    @Override
    public void run(String... args) throws Exception {
        /**
         * Server包下
         */
        InetSocketAddress address = new InetSocketAddress(JSConstants.SOCKET_SERVER_IP,JSConstants.SOCKET_SERVER_PROT);
        NettyServer nettyServer = new NettyServer();
        nettyServer.start(address);
    }



}
