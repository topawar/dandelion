package com.topawar.web.test;

import com.topawar.web.model.entity.Generator;
import com.topawar.web.service.GeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author topawar
 */
@SpringBootTest()
public class GeneratorServiceImplTest {

    @Resource
    private GeneratorService generatorService;

    @Test
    void insertBatch() throws CloneNotSupportedException {
        Generator generator = generatorService.getById(9);
        generator.setId(null);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CompletableFuture[] completableFuture = new CompletableFuture[10];
        for (int i = 0; i < 10; i++) {
            int finalIndex = i;
            completableFuture[i] = CompletableFuture.runAsync(() -> {
                List<Generator> generators = new ArrayList<>(10000);
                for (int j = 0; j < 10000; j++) {
                    Generator newGenerator = null;
                    try {
                        newGenerator = generator.clone();
                    } catch (CloneNotSupportedException e) {
                        throw new RuntimeException(e);
                    }
                    generators.add(newGenerator);
                }
                System.out.println("thread" + finalIndex + "insert数据");
                generatorService.saveBatch(generators);
            }, executorService);
        }
        CompletableFuture.allOf(completableFuture).join();
        executorService.shutdown();
    }
}
