package com.gangu.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopicConfig {

    private static final String QUEUE = "seckillQueue";
    private static final String EXCHANGE = "seckillExchange";

    @Bean("q")
    public Queue queue(){
        return new Queue(QUEUE);
    }

    @Bean("tE")
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(@Qualifier("q") Queue queue,
                           @Qualifier("tE") TopicExchange topicExchange){
        return BindingBuilder.bind(queue).to(topicExchange).with("seckill.#");
    }
}
