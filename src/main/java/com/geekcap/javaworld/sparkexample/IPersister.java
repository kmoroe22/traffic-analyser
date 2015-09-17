package com.geekcap.javaworld.sparkexample;

import java.util.concurrent.BlockingQueue;

/**
 * Created by karabo.moroe on 2015-09-15.
 */
public interface IPersister {

    void persist(BlockingQueue<String> messageQueue);
}
