package com.myorg.message.config;

import org.springframework.amqp.core.AsyncAmqpTemplate;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {

  @Value("${messaging.host}")
  private String messagingHost;
  @Value("${messaging.port}")
  private Integer messagingPort;
  @Value("${messaging.username}")
  private String messagingUsername;
  @Value("${messaging.password}")
  private String messagingPassword;
  @Value("${messaging.vh}")
  private String messagingVirtualHost;

  @Bean
  public ConnectionFactory connectionFactory() {
    CachingConnectionFactory cachingConnectionFactory =  new CachingConnectionFactory();
    cachingConnectionFactory.setVirtualHost(messagingVirtualHost);
    cachingConnectionFactory.setUsername(messagingUsername);
    cachingConnectionFactory.setUsername(messagingUsername);
    cachingConnectionFactory.setPassword(messagingPassword);
    cachingConnectionFactory.setHost(messagingHost);
    cachingConnectionFactory.setPort(messagingPort);
    return cachingConnectionFactory;
  }

  @Bean
  public RabbitAdmin rabbitAdmin() {
    return new RabbitAdmin(connectionFactory());
  }

  @Bean
  public RabbitTemplate rabbitTemplate () {
    return new RabbitTemplate(connectionFactory());
  }

  @Bean
  public AsyncAmqpTemplate asyncAmqpTemplate() {
    AsyncRabbitTemplate asyncRabbitTemplate = new AsyncRabbitTemplate(rabbitTemplate());
    asyncRabbitTemplate.setReceiveTimeout(60000);
    return new AsyncRabbitTemplate(rabbitTemplate());
  }
}
