package com.shepherdjerred.stteleports.util;

import java.util.LinkedList;

public class TeleportQueue<E> extends LinkedList<E> {

    private final int limit;

    public TeleportQueue(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(E element) {
        super.add(element);
        if (size() > limit) {
            super.remove();
        }
        return true;
    }

}
