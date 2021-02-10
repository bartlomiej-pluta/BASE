/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.bartlomiejpluta.base.game;

import com.bartlomiejpluta.base.core.engine.GameEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.bartlomiejpluta.base")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class App implements ApplicationRunner {
    private final GameEngine gameEngine;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting game engine");
        gameEngine.start();
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
