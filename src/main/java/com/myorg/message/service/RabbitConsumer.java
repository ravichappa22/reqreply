package com.myorg.message.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitConsumer {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  /*  @RabbitListener(bindings = @QueueBinding(
        value = @org.springframework.amqp.rabbit.annotation.Queue(value = "reqReply", durable = "true"),
        exchange = @Exchange(value = "reqReplyExchange", ignoreDeclarationExceptions = "true"),
        key = "reqReply"))
  public void receiveMessagereqreply(String receivedMessage, MessageProperties properties) {
    System.out.println("receivedMessage = " + receivedMessage);
    rabbitTemplate.convertAndSend(properties.getReplyTo(), receivedMessage);
  }*/

  //amq.rabbitmq.reply-to
  @RabbitListener(bindings = @QueueBinding(
        value = @org.springframework.amqp.rabbit.annotation.Queue(value = "reqReply", durable = "true"),
        exchange = @Exchange(value = "reqReplyExchange", ignoreDeclarationExceptions = "true"),
        key = "reqReply"))
  public void receiveMessagereqreplyTo (Message receivedMessage) {
    System.out.println("receivedMessage reply = " + new String(receivedMessage.getBody()) + " reply to = " + receivedMessage.getMessageProperties().getReplyTo());
    String reply = new String(receivedMessage.getBody()).concat(" sending Reply");
    Message message = new Message(reply.getBytes(), receivedMessage.getMessageProperties());
    rabbitTemplate.convertAndSend(receivedMessage.getMessageProperties().getReplyTo(), message);
  }

}
