package xyz.launcel.test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xyz.launcel.log.BaseLogger;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public abstract class AbstractTest extends BaseLogger implements AppTest {
}
