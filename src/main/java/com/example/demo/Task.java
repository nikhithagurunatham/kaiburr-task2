package com.example.demo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class Task {
  @Id
  public String id;
  public String name;
  public String owner;
  public String command;
  public List<String> taskExecutions = new ArrayList<>();
}
