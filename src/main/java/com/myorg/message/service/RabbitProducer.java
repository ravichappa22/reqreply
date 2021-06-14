package com.myorg.message.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myorg.message.vo.User;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@EnableScheduling
@Slf4j
public class RabbitProducer {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private AsyncAmqpTemplate asyncAmqpTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  //@Scheduled(fixedDelay = 5000, initialDelay = 15000)
  public void sendMessagereqreply() {
    System.out.println("sending message sync no reply");
    User user = User.builder().firstName("Ravi").lastName("Chappa").age(40).build();
    rabbitTemplate.convertAndSend("reqReplyExchange","reqReply", user, new CorrelationData());
  }

 // @Scheduled(fixedDelay = 1000, initialDelay = 15000)
  public ListenableFuture<String> asyncsendAndReceiveMessage() throws InterruptedException, ExecutionException, TimeoutException {
    System.out.println("sending message async with reply queue 1");
    User user = User.builder().firstName("Ravi").lastName("Chappa").age(40).build();
    ListenableFuture<String> objectListenableFuture = null;
    try {
      String requestMessage = objectMapper.writeValueAsString(user);
      objectListenableFuture = asyncAmqpTemplate.convertSendAndReceive("reqReplyExchange", "reqReply", requestMessage);
    } catch (JsonProcessingException e) {
      log.error("JsonProcessingException");
    }
    return objectListenableFuture;
  }

  @Scheduled(fixedDelay = 5000, initialDelay = 15000)
  public void asyncsendAndReceiveMessageTwo() throws InterruptedException, ExecutionException, TimeoutException {
    System.out.println("sending message async with reply queue 2");
    ListenableFuture<String> objectListenableFuture = asyncAmqpTemplate.convertSendAndReceive("reqReplyExchange","dispatch", "Hello Ravi 2 " + Instant.now());
    String receivedMessage = objectListenableFuture.get();
    System.out.println("received back message async 2 = " + receivedMessage);
  }

  public Boolean sendMessagesInLoop(Integer count) {
    List<ListenableFuture<String>> listenableFutureList = new ArrayList<>();
    int i = 0;
   do{
     try {
       listenableFutureList.add(this.asyncsendAndReceiveMessage());
     } catch (InterruptedException e) {
       e.printStackTrace();
     } catch (ExecutionException e) {
       e.printStackTrace();
     } catch (TimeoutException e) {
       e.printStackTrace();
     }
     i++;
    }while(i <count);

    listenableFutureList.forEach( f -> {
      try {
        System.out.println("received back message async 1= " + f.get());
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    });

    return true;
  }
}
