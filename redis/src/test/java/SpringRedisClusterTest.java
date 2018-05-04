import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-cluster.xml")
public class SpringRedisClusterTest {


    @Autowired
    private JedisCluster jedisCluster;


    @Test
    public void save() {
        jedisCluster.set("name","Han Meimei");

    }

    @Test
    public void get() {
        String str = jedisCluster.get("name");
        System.out.println(str);
    }

    @After
    public void shut() throws IOException {
        jedisCluster.close();
    }

}
