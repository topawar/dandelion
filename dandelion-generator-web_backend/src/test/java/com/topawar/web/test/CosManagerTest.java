package com.topawar.web.test;

import com.topawar.web.manager.CosManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;

@SpringBootTest
class CosManagerTest {

    @Resource
    private CosManager cosManager;

    @Test
    void deleteObject() {
		cosManager.deleteSingleObject("/test/logo.jpg");
    }

    @Test
    void deleteObjects() {
        cosManager.deleteObjects(Arrays.asList("test/鱼聪明AI绘画 (1).jpg",
                "test/鱼聪明AI绘画.jpg"
                ));
    }

    @Test
    void deleteDir() {
        cosManager.deleteDir("/test/");
    }
}