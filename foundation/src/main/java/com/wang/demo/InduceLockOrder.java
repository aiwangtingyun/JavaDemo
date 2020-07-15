package com.wang.demo;

/* 固定锁顺序避免死锁 */
public class InduceLockOrder {
    // 额外的锁、避免两个对象hash值相等的情况
    private static final Object tieLock = new Object();

    class Account{
        private int totalAmount = 0;
        public void debit(int amount) {
            totalAmount += amount;
        }
        public void credit(int amount) {
            totalAmount -= amount;
        }
        public int getBalance() {
            return totalAmount;
        }
    }

    public void transferMoney(Account fromAcct, Account toAcct, int amount) {
        class Helper {
            public void transfer() {
                if (fromAcct.getBalance() < amount) {
                    return;
                } else {
                    fromAcct.debit(amount);
                    toAcct.credit(amount);
                }
            }
        }

        // 得到锁的 hash 值
        int fromHash = System.identityHashCode(fromAcct);
        int toHash = System.identityHashCode(toAcct);

        // 根据 hash 值来上锁
        if (fromHash < toHash) {
            synchronized (fromAcct) {
                synchronized (toAcct) {
                    new Helper().transfer();
                }
            }
        } else if (fromHash > toHash) {
            synchronized (toAcct) {
                synchronized (fromAcct) {
                    new Helper().transfer();
                }
            }
        } else { // 额外的锁，避免两个对象的 hash 值相等的情况
            synchronized (tieLock) {
                synchronized (fromAcct) {
                    synchronized (toAcct) {
                        new Helper().transfer();
                    }
                }
            }
        }
    }
}
