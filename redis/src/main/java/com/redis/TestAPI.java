package com.redis;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TestAPI {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);

        jedis.set("k1", "v1");
        jedis.set("k2", "v2");
        jedis.set("k3", "v3");

        Set<String> keys = jedis.keys("*");
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            System.out.println(next);
        }

        System.out.println("redis.exists ===> " + jedis.exists("k2"));
        System.out.println("redis.ttl ===> " + jedis.ttl("k1"));

        // String
        System.out.println("------------- String ----------");
        System.out.println("k1 ==> " + jedis.get("k1"));
        jedis.mset("str1", "strv1", "str2", "strv2", "str3", "strv3");
        System.out.println("mget ===> " + jedis.mget("str1", "str2", "str3"));

        // list
        System.out.println("------------- list ----------");
        if (jedis.exists("mylist")) {
            jedis.del("mylist");
        }
        jedis.lpush("mylist", "1", "2", "3");
        List<String> mylist = jedis.lrange("mylist", 0, -1);
        for (String element : mylist) {
            System.out.println(element);
        }

        // set
        System.out.println("------------- set ----------");
        jedis.sadd("orders", "jd001");
        jedis.sadd("orders", "jd002");
        jedis.sadd("orders", "jd003");
        Set<String> orders = jedis.smembers("orders");
        Iterator<String> iterator1 = orders.iterator();
        while (iterator1.hasNext()) {
            System.out.println(iterator1.next());
        }
        jedis.srem("orders", "jd001");
        System.out.println(jedis.smembers("orders").size());

        // hash
        System.out.println("------------- hash ----------");
        jedis.hset("hash1", "username", "Andy");
        System.out.println(jedis.hget("hash1", "username"));
        HashMap<String, String> map = new HashMap<>();
        map.put("telephone", "1234567");
        map.put("address", "guangzhou");
        map.put("email", "123@163.com");
        jedis.hmset("hash2", map);
        List<String> result = jedis.hmget("hash2", "telephone", "email");
        for (String element : result) {
            System.out.println(element);
        }

        // zset
        System.out.println("------------- zset ----------");
        jedis.zadd("zset01", 10d, "v1");
        jedis.zadd("zset01", 20d, "v2");
        jedis.zadd("zset01", 30d, "v3");
        System.out.println(jedis.zrange("zset01", 0, -1));
    }
}
