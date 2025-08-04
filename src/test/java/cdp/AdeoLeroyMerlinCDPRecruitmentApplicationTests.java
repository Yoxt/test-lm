package cdp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest
@ComponentScan(basePackages = "adeo.leroymerlin.cdp")
class AdeoLeroyMerlinCDPRecruitmentApplicationTests {

    @Test
    void contextLoads() {
        /* Test de lancement spring-boot: Test OK si pas d'exception générée. */
    }
}
