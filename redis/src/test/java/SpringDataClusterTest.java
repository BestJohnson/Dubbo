import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-data-cluster.xml")
public class SpringDataClusterTest {

    private RedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisTemplate.setKeySerializer( new StringRedisSerializer());
        this.redisTemplate.setValueSerializer(new StringRedisSerializer());
    }


    @Test
    public void save() {

        //redisTemplate.opsForValue().set("customer:1","dageda");
        Map<String,String> map = new HashMap<String, String>();
        map.put("name:","lily");
        map.put("age","23");
        map.put("address","jiaozuo");
        //redisTemplate.opsForHash().put("vistor","name:","obama");
        redisTemplate.opsForHash().putAll("student:2",map);
    }

    @Test
    public void get() {

        //String str = (String) redisTemplate.opsForValue().get("customer:1");
        Map<String,String> map = new HashMap<String, String>();
        map = redisTemplate.opsForHash().entries("student:2");
        Set set = map.entrySet();
        Iterator it = set.iterator();
        while(it.hasNext()) {
            Map.Entry me = (Map.Entry) it.next();
            String str = (String) me.getKey();
            String str1 = (String) me.getValue();
            System.out.println(str + "--->" + str1);

        }
    }



}
