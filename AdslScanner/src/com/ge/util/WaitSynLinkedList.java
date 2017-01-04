package com.ge.util;

import java.util.LinkedList;

public class WaitSynLinkedList<T> {

    private long lSize = -1;

    private final LinkedList<T> mList = new LinkedList<>();

    public WaitSynLinkedList(long size) {
        this.lSize = size;
    }

    public void setSize(long lSize) {
        this.lSize = lSize;
    }

    private void check() {
        if (lSize <= 0) {
            return;
        }

        while (mList.size() >= lSize) {
            try {
                mList.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addLast(T o) {
        synchronized (mList) {
            check();
            mList.addLast(o);
            mList.notifyAll();
        }
    }

    public T removeFirst() {
        T obj;
        synchronized (mList) {
            while (mList.isEmpty()) {
                try {
                    mList.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            obj = mList.removeFirst();
            if (lSize > 0) {
                mList.notifyAll();
            }
        }
        return obj;
    }

    public long size() {
        return mList.size();
    }
}
