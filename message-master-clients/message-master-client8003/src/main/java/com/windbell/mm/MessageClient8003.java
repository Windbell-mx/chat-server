package com.windbell.mm;

import com.windbell.mm.communication.NettyClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.Async;

import java.net.InetAddress;


@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class MessageClient8003 implements CommandLineRunner {

    @Value("${server.port}")
    private int port;

    public static void main(String[] args) {
        SpringApplication.run(MessageClient8003.class, args);
    }

    @Async
    @Override
    public void run(String... args) throws Exception {
        System.out.println("NettyClient start running --> : " + port + "(●'◡'●)");
        new NettyClientConfig().connect(InetAddress.getLocalHost().getHostAddress(), 8001);
    }
}