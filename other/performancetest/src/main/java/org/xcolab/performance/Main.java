package org.xcolab.performance;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class Main {

    public static void main(String[] args) {
        if(args == null || args.length == 0){
            System.out.println("usage : - numberofthreads");
            return;
        }
        System.out.println("Starting " + args[0]+" threads!");
        int numberOfThreads = new Integer(args[0]);
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();


        // create a thread for each URI
        ThreadTestGroup[] threads = new ThreadTestGroup[numberOfThreads];
        for (int i = 0; i < threads.length; i++) {

            threads[i] = new ThreadTestGroup(httpClient, i);
        }

        // start the threads
        for (int j = 0; j < threads.length; j++) {
            threads[j].start();
        }

        // join the threads
        for (int j = 0; j < threads.length; j++) {
            try {
                threads[j].join();
            }catch (InterruptedException i){

            }
        }
    }
}
