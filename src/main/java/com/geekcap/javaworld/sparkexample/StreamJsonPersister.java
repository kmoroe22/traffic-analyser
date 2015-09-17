package com.geekcap.javaworld.sparkexample;

import java.util.concurrent.BlockingQueue;

/**
 * Created by karabo.moroe on 2015-09-15.
 */
public class StreamJsonPersister implements IPersister{

    private final String file;

    public StreamJsonPersister(String file){
        this.file = file;
    }

    @Override
    public void persist(BlockingQueue<String> msgQueue) {

    }
}
