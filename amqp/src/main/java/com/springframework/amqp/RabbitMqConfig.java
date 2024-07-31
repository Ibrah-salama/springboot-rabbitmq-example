package com.springframework.amqp;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class RabbitMqConfig {

    /*
    * Fist we need a connectionFactory for the rabbitmq to use
    * Then we need to configure message converter -> here we can pass objectMapper or leave it is as it because its so simple here in the project
    * Add all to the rabbitTemplate
    * [NOTE] wen rabbitMQ sends the messages to the exchange it sends it as jsonBLOB and from the queue we need to change the jsonBLOB to our object
    * */

    /* inject connectionFactory */
    private final ConnectionFactory connectionFactory;

    /* Inject object mapper */
    private final ObjectMapper objectMapper;

    /* specify a message converter */
    @Bean
    public MessageConverter jacksonConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    /* specify Amqp template for rabbitMQ */
    @Bean
    public AmqpTemplate amqpTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jacksonConverter());
        return rabbitTemplate;
    }

    /* setup rabbitMQ listener */
    @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jacksonConverter());
        return factory;
    }
}
