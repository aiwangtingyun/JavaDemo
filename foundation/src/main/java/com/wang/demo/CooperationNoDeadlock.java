package com.wang.demo;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/* 开放调用避免死锁 */
public class CooperationNoDeadlock {

    class Taxi {
        private Point location;
        private Point destinatin;
        private Dispatcher dispatcher;

        public Taxi(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        public synchronized Point getLocation() {
            return this.location;
        }

        public synchronized void setLocation(Point location) {
            boolean reachedDestination;

            // 加 Taxi 内置锁
            synchronized (this) {
                this.location = location;
                reachedDestination = location.equals(destinatin);
            }

            // 执行同步代码块后释放锁
            if (reachedDestination) {
                dispatcher.notifyAvailable(this);
            }
        }

        public synchronized Point getDestinatin() {
            return this.destinatin;
        }

        public synchronized void setDestinatin(Point destinatin) {
            this.destinatin = destinatin;
        }
    }

    class Dispatcher {
        private final Set<Taxi> taxis;
        private final Set<Taxi> availableTaxis;

        public Dispatcher() {
            taxis = new HashSet<Taxi>();
            availableTaxis = new HashSet<Taxi>();
        }

        public synchronized void notifyAvailable(Taxi taxi) {
            availableTaxis.add(taxi);
        }

        public Image getImage() {
            Set<Taxi> copy;

            // Dispatcher 内置锁
            synchronized (this) {
                copy = new HashSet<Taxi>(taxis);
            }

            // 执行同步代码块后释放锁
            Image image = new Image();
            for (Taxi t : copy) {
                image.drawMarker(t.getDestinatin());
            }

            return image;
        }
    }

    class Image {
        public void drawMarker(Point p) {
            System.out.println("DrawMarker : " + p.getX() + " " + p.getY());
        }
    }
}
