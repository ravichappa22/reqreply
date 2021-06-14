package com.myorg.message.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myorg.message.vo.User;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class RabbitConsumer {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private ObjectMapper objectMapper;

    @RabbitListener(bindings = @QueueBinding(
        value = @org.springframework.amqp.rabbit.annotation.Queue(value = "dispatch", durable = "true"),
        exchange = @Exchange(value = "reqReplyExchange", ignoreDeclarationExceptions = "true"),
        key = "dispatch"))
    public void receiveMessagereqreply(Message receivedMessage) {
      String reply = new String(receivedMessage.getBody()).concat(" sending Reply");
      Message message = new Message(reply.getBytes(), receivedMessage.getMessageProperties());
      rabbitTemplate.convertAndSend(receivedMessage.getMessageProperties().getReplyTo(), message);
  }

  //amq.rabbitmq.reply-to
  @RabbitListener(bindings = @QueueBinding(
        value = @org.springframework.amqp.rabbit.annotation.Queue(value = "reqReply", durable = "true"),
        exchange = @Exchange(value = "reqReplyExchange", ignoreDeclarationExceptions = "true"),
        key = "reqReply"))
  public void receiveMessagereqreplyTo (Message receivedMessage) {
    System.out.println(" received at consumer = " + new String(receivedMessage.getBody()) + " reply to = " + receivedMessage.getMessageProperties().getReplyTo());
    Message replyMessage = null;
    try {
      User user = objectMapper.readValue(new String(receivedMessage.getBody()), User.class);
      replyMessage = new Message(user.getLastName().getBytes(), receivedMessage.getMessageProperties());
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    rabbitTemplate.convertAndSend(receivedMessage.getMessageProperties().getReplyTo(), replyMessage);
  }

}
