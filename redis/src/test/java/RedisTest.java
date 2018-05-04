import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisTest {


    @Test
    public void string() {

        Jedis jedis = new Jedis("172.16.1.233",6379);
        jedis.set("name","tom");
        String name = jedis.get("name");
        jedis.close();
        System.out.println(name);

    }

    @Test
    public void hash() {
        Jedis jedis = new Jedis("172.16.1.233",6379);
        /*jedis.hset("user:1","name","hanks");
        String str = jedis.hget("user:1","name");
        System.out.println(str);*/

        //jedis.hmset("user:2",);

        jedis.close();

    }

    @Test
    public void list() {
        Jedis jedis = new Jedis("172.16.1.233",6379);

        jedis.lpush("user:1:address","zhengzhou","shanghai","beijing");

        /*List<String> list = jedis.lrange("user:1:address",0,-1);
        for(String str :list) {
            System.out.println(str);
        }*/
        String str = jedis.lindex("user:1:address",1);
        System.out.println(str);

        jedis.close();
    }

    @Test
    public void set() {
        Jedis jedis = new Jedis("172.16.1.233",6379);
        Set set = jedis.smembers("user:3:voter");

        jedis.close();
    }

}
