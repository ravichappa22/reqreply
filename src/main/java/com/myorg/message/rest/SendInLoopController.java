package com.myorg.message.rest;

import com.myorg.message.service.RabbitProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendInLoopController {

  @Autowired
  private RabbitProducer rabbitProducer;

  @PutMapping(path = "/api/bycount/{count}")
  public Boolean sendInLoop(@PathVariable int count) {
    return  rabbitProducer.sendMessagesInLoop(count);
  }
}
