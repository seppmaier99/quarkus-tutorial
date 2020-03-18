package com.example;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.Dependent;

@Dependent
public class FruitService {
  @ConfigProperty(name = "greetings.message")
  String message;
  public String get(String text) {
    return String.format("%s %s", message, text);
  }
}
