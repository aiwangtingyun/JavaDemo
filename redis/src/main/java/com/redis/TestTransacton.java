package com.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class TestTransacton {

    private boolean transactonMethod() throws InterruptedException {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        int balance;               // 可用余额
        int debp;                   // 欠额
        int amtToSubtract = 10;     // 实刷额度

        // 加锁监视
        jedis.watch("balance");

        // 休眠过程中调用 set balance 5
        Thread.sleep(7000);

        balance = Integer.parseInt(jedis.get("balance"));
        if (balance < amtToSubtract) {
            jedis.unwatch();
            System.out.println("balance has modified!");
            return false;
        } else {
            System.out.println("--------- start transacton ---------");
            Transaction transaction = jedis.multi();
            transaction.decrBy("balance", amtToSubtract);
            transaction.incrBy("debt", amtToSubtract);
            transaction.exec();
            System.out.println("balance ==> " + jedis.get("balance"));
            System.out.println("debt ==> " + jedis.get("debt"));
            return true;
        }
    }

    /**
     * 通俗点讲，watch命令就是标记一个键，如果标记了一个键，
     * 在提交事务前如果该键被别人修改过，那事务就会失败，这种情况通常可以在程序中
     * 重新再尝试一次。
     * 首先标记了键balance，然后检查余额是否足够，不足就取消标记，并不做扣减；
     * 足够的话，就启动事务进行更新操作，
     * 如果在此期间键balance被其它人修改， 那在提交事务（执行exec）时就会报错，
     * 程序中通常可以捕获这类错误再重新执行一次，直到成功。
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        TestTransacton test = new TestTransacton();
        boolean result = test.transactonMethod();
        System.out.println(result);
    }
}
