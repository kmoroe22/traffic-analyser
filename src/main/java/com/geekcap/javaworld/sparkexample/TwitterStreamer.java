package com.geekcap.javaworld.sparkexample;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.Location;
import com.twitter.hbc.core.endpoint.Location.Coordinate;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Created by karabo22 on 10/09/2015.
 */
public class TwitterStreamer {


    private Hosts hosts;
    private Authentication hosebirdAuth;
    private StatusesFilterEndpoint streamEndpoint;
    private BlockingQueue<Event> eventQueue;
    private BlockingQueue<String> msgQueue;
    private Client client;
    private Log log;

    public TwitterStreamer(){
        log = LogFactory.getLog(TwitterStreamer.class);
        setUpAPI();
        createClient();
    }

    private void setUpAPI() {

        msgQueue = new LinkedBlockingQueue<String>(100000);
        eventQueue = new LinkedBlockingQueue<Event>(1000);
        /** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */



        hosts = new HttpHosts(Constants.STREAM_HOST);
        streamEndpoint = new StatusesFilterEndpoint();
        // Optional: set up some followings and track terms
        //List<Long> followings = Lists.newArrayList(1234L, 566788L);
        List<String> terms = Lists.newArrayList("traffic jhb","traffic Johannesburg","traffic jozi",
                "roadblock jhb","roadblock jhb","roadblock jozi");
        //streamEndpoint.followings(followings);
        streamEndpoint.trackTerms(terms);
        Location location = new Location(new Coordinate(27.813432,-26.287627),new Coordinate(28.248765,-25.980637));
        streamEndpoint.locations(Lists.newArrayList(location));
        // These secrets should be read from a config file
        hosebirdAuth = new OAuth1(APIAuthorisation.KEY,
                APIAuthorisation.SECRECT,
                APIAuthorisation.TOKENKEY,
                APIAuthorisation.TOKENSECRET);
    }

    private void createClient(){
        ClientBuilder builder = new ClientBuilder()
                .name("traffic-client")
                .hosts(hosts)
                .authentication(hosebirdAuth)
                .endpoint(streamEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue))
                .eventMessageQueue(eventQueue);
        client = builder.build();
        // Attempts to establish a connection.
    }

    public void collectData() throws InterruptedException {
        client.connect();
        while (!client.isDone()) {
            String msg = msgQueue.take();
            log.info(msg);
        }
    }

    public void collectData(IPersister persister){

    }
}
