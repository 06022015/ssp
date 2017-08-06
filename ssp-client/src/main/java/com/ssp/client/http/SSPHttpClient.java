package com.ssp.client.http;

import com.ssp.api.exception.HttpConnectionException;
import com.ssp.api.exception.SSPURLException;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/18/17
 * Time: 8:14 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class SSPHttpClient {

    private static Logger logger = LoggerFactory.getLogger(SSPHttpClient.class);

    public ClientResponse get(final String endpoint) throws SSPURLException {
        ClientRequest request = new ClientRequest(endpoint, ClientMethod.GET);
        HttpURLConnection connection=getConnection(request);
        return connect(connection);
    }

    public ClientResponse post(ClientRequest request) throws SSPURLException {
        HttpURLConnection connection=getConnection(request);
        OutputStream os = null;
        try {
            connection.setDoOutput(true);
            String input = request.getContent();
            os = connection.getOutputStream();
            os.write(input.getBytes());
            os.flush();
        } catch (IOException e) {
            logger.error("Unable to write request:- "+ e.getMessage());
            throw new SSPURLException("Unable to write request", e.getCause(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }finally {
             close(os);
             //connection.disconnect();
        }
        return connect(connection);
    }

    private HttpURLConnection getConnection(ClientRequest request){
        try {
            URL url = new URL(request.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(request.getMethod());
            if(request.getProperty().containsKey(ClientRequest.USE_CACHE_NAME))
                connection.setUseCaches(true);
            connection.setRequestProperty(ClientRequest.CONTENT_TYPE, request.getProperty().get(ClientRequest.CONTENT_TYPE_NAME).toString());
            connection.setRequestProperty(ClientRequest.ACCEPT_TYPE_NAME, request.getProperty().get(ClientRequest.CONTENT_TYPE_NAME).toString());
                if(request.getProperty().containsKey(ClientRequest.CONNECTION_TIMEOUT_NAME))
                    connection.setConnectTimeout((Integer) request.getProperty().get(ClientRequest.CONNECTION_TIMEOUT_NAME));
                if(request.getProperty().containsKey(ClientRequest.READ_TIMEOUT_NAME))
                    connection.setReadTimeout((Integer) request.getProperty().get(ClientRequest.READ_TIMEOUT_NAME));
            return connection;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.error("Unable to form URL:- "+ e.getMessage());
            throw new SSPURLException("URL not formed properly", e.getCause(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            throw new HttpConnectionException(e.getCause(),"Unable to connect to server");
        }
    }

    private ClientResponse connect(HttpURLConnection connection) throws HttpConnectionException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        InputStream read = null;
        int code = 200;
        try {
            //logger.debug("Connecting to dsp server....");
            connection.connect();
            code = connection.getResponseCode();
            read = connection.getInputStream();
            int ret = 0;
            while ((ret = read.read(buf)) > 0) {
                os.write(buf, 0, ret);
            }
        } catch (IOException e) {
            try {
                //logger.debug("failed to connect. Reading error message");
                code = connection.getResponseCode();
                read = connection.getErrorStream();
                int ret = 0;
                while (null != read && (ret = read.read(buf)) > 0) {
                    os.write(buf, 0, ret);
                }
            } catch(IOException ex) {
                ex.printStackTrace();
                throw new HttpConnectionException(ex.getCause(),"Unable to connect to server");
            }finally {
                close(read);
            }
            e.printStackTrace();
        }finally {
            close(read);
            //logger.debug("Disconnecting...");
           // connection.disconnect();
        }
        return new ClientResponse(code, os.toString());
    }

    private void close(Object obj){
        try {
            if(null == obj)
                return;
            if(obj instanceof InputStream){
                InputStream in = (InputStream)obj;
                in.close();
            }else if(obj instanceof OutputStream){
                OutputStream out = (OutputStream)obj;
                out.close();
            }obj = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String args[]) throws InterruptedException {
        for(int i=0;i< 1; i++){
            final int index = i;
            Thread thread = new Thread(){
                public void run(){
                    testSSP(index);
                }
            };
            thread.start();
            Thread.sleep(2);
        }
    }

    public static void testParellel(final int index){
        ExecutorService executorService = Executors.newFixedThreadPool(200);
        List<Callable<String>> callables = new ArrayList<Callable<String>>();
        for(int i = 0; i< 1; i++){
            Callable<String> callable = new Callable<String>() {
                public String call() throws Exception {
                    return testSSP(index);
                }
            };
            callables.add(callable);
        }
        long startTime = Calendar.getInstance().getTimeInMillis();
        System.out.println("Thread Number"+index+" Start time:- "+startTime);
        List<Future<String>> requests = null;
        try {
            requests = executorService.invokeAll(callables);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread Number"+index+" Mid time:- "+Calendar.getInstance().getTimeInMillis());
        executorService.shutdown();
        while (!executorService.isTerminated()){

        }
        long endTime = Calendar.getInstance().getTimeInMillis();
        System.out.println("Thread Number"+index+ " End time:- "+endTime);
        System.out.println("Thread Number"+index+ " Diff:- " + (endTime-startTime));
        for(Future<String> future: requests){
            try {
                future.get();
                //System.out.println(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Complete time:- "+new Date());
    }
    
    public static String testSSP(Integer pubId){
        long startTime = Calendar.getInstance().getTimeInMillis();
        System.out.println("Thread Number"+pubId+" Start time:- "+startTime);
        SSPHttpClient client = new SSPHttpClient();
        ClientRequest clientRequest = new ClientRequest("http://localhost:8080/ssp/ReqAd?pub_id=3&block_id=1&ref=http://www.foobar.com/1234.html", ClientMethod.GET);
        //clientRequest.addProperty(ClientRequest.CONNECTION_TIMEOUT_NAME, 100);
        //clientRequest.addProperty(ClientRequest.READ_TIMEOUT_NAME, 200);
        HttpURLConnection connection = client.getConnection(clientRequest);
        try {
            connection.setDoOutput(true);
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 Firefox/26.0");
            String res = client.connect(connection).getResponse();
            long endTime = Calendar.getInstance().getTimeInMillis();
            System.out.println("Thread Number"+pubId+ " End time:- "+endTime);
            System.out.println("Thread Number"+pubId+ " Diff:- " + (endTime-startTime));
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unable to write request:- "+ e.getMessage());
            throw new SSPURLException("Unable to write request", e.getCause(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }finally {
            if(null != connection)
                connection.disconnect();
        }
    }

}
