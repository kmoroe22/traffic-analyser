package com.geekcap.javaworld.sparkexample;

import com.geekcap.javaworld.sparkexample.TwitterStreamer;

/**
 * Created by karabo22 on 10/09/2015.
 */
public class TwitterMain {

    public static void main( String[] args )
    {
        TwitterStreamer streamer = new TwitterStreamer();
        try {
            streamer.collectData();
        } catch (InterruptedException e) {
            System.out.println("oops");
        }
    }
}
