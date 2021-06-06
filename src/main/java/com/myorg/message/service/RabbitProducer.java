package com.myorg.message.service;


import org.springframework.amqp.core.AsyncAmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@EnableScheduling
public class RabbitProducer {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private AsyncAmqpTemplate asyncAmqpTemplate;

  //@Scheduled(fixedDelay = 5000, initialDelay = 15000)
  public void sendMessagereqreply() {
    System.out.println("sending message sync no reply");
    //rabbitTemplate.convertAndSend("reqReplyExchange","reqReply", "Hello Ravi " + Instant.now(), new CorrelationData());
    rabbitTemplate.convertAndSend("sameple");
  }

  @Scheduled(fixedDelay = 5000, initialDelay = 15000)
  public String asyncsendAndReceiveMessage() throws InterruptedException, ExecutionException, TimeoutException {
    System.out.println("sending message async with reply queue 1");
    ListenableFuture<String> objectListenableFuture = asyncAmqpTemplate.convertSendAndReceive("reqReplyExchange","reqReply", "Hello Ravi 1 " + Instant.now());
    String receivedMessage = objectListenableFuture.get();
    System.out.println("received back message async 1= " + receivedMessage);
    return receivedMessage;
  }

  @Scheduled(fixedDelay = 5000, initialDelay = 15000)
  public String asyncsendAndReceiveMessage2() throws InterruptedException, ExecutionException, TimeoutException {
    System.out.println("sending message async with reply queue 2");
    ListenableFuture<String> objectListenableFuture = asyncAmqpTemplate.convertSendAndReceive("reqReplyExchange","reqReply", "Hello Ravi 2 " + Instant.now());
    String receivedMessage = objectListenableFuture.get();
    System.out.println("received back message async 2 = " + receivedMessage);
    return receivedMessage;
  }

}
