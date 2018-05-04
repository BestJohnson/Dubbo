import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-redis.xml")
public class SpringRedisTest {

    @Autowired
    private JedisPool jedisPool;

    @Test
    public void save() {
        Jedis jedis = jedisPool.getResource();
        //jedis.set("name","lily");

        Map<String,String> map = new HashMap<String, String>();
        map.put("name","lucy");
        map.put("age","23");
        jedis.hmset("user:1",map);
        jedis.close();

    }
}
