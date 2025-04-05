package com.windbell.mm;


import com.windbell.mm.Communications.NettyServerConfig;
import com.windbell.mm.converter.MessageConverter;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;

@SpringBootApplication
@RequiredArgsConstructor
@MapperScan("com.windbell.mm.mapper")
public class MessageMain8080 implements CommandLineRunner {

    private final ConnectionFactory connectionFactory;
    private final MessageConverter messageConverter;

    public static void main(String[] args) {
        SpringApplication.run(MessageMain8080.class, args);
        System.out.println("(●'◡'●) 应用已启动 /(ㄒoㄒ)/~~");
    }

    @Async
    @Override
    public void run(String... args) throws Exception {
        //RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        new NettyServerConfig().NettyServerBind(8001);
    }
}
