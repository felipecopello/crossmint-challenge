package io.crossmint.runner;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.crossmint.handler.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);
  private final Handler handler;
  private final ConfigurableApplicationContext context;

  public Runner(Handler handler, ConfigurableApplicationContext context) {
    this.handler = handler;
    this.context = context;
  }

  @Override
  public void run(String... args) throws JsonProcessingException {
    LOGGER.info("***** ----- Task 1 started ----- *****");
    handler.handleTaskOne();
    LOGGER.info("***** ----- Task 1 finished ----- *****");

    SpringApplication.exit(context, () -> 0);
  }
}
