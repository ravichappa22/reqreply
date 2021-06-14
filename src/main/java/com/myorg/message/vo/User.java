package com.myorg.message.vo;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@Builder
@ToString
@JsonRootName("user")
public class User implements Serializable {

  private String firstName;
  private String lastName;
  private int age;

}
