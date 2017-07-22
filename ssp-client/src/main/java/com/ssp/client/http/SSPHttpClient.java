package com.ssp.client.http;

import com.ssp.api.exception.HttpConnectionException;
import com.ssp.api.exception.SSPURLException;
import org.apache.commons.httpclient.HttpStatus;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
        HttpConnectionProperty property = new HttpConnectionProperty(endpoint, ClientMethod.GET.name());
        HttpURLConnection connection=getConnection(property);
        return connect(connection);
    }

    public ClientResponse post(ClientRequest request) throws SSPURLException {
        HttpURLConnection connection=getConnection(request.getProperty());
        OutputStream os = null;
        try {
            connection.setDoOutput(true);
            String input = request.getContent().toString();
            os = connection.getOutputStream();
            os.write(input.getBytes());
            os.flush();
        } catch (IOException e) {
            logger.error("Unable to write request:- "+ e.getMessage());
            throw new SSPURLException("Unable to write request", e.getCause(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }finally {
             if(null != os){
                 try {
                     os.close();
                     os = null;
                 } catch (IOException e) {

                 }
             }
            if(null != connection)
                connection.disconnect();
        }
        return connect(connection);
    }


    
    /*public ClientResponse get(final String endpoint) throws SSPURLException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HttpURLConnection connection=null;
        byte[] buf = new byte[4096];
        InputStream read = null;
        int code = 200;
        try {
            HttpConnectionProperty property = new HttpConnectionProperty(endpoint, HTTPMethod.GET.name());
            connection = getConnection(property);
            connection.connect();
            read = connection.getInputStream();
            int ret = 0;
            while ((ret = read.read(buf)) > 0) {
                os.write(buf, 0, ret);
            }
        } catch (MalformedURLException e) {
            throw new SSPURLException("URL not formed properly", e.getCause(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            try {
                if(null == connection)
                    throw new HttpConnectionException(e.getCause(),"Connection not available");
                    code = connection.getResponseCode();
                    read = connection.getErrorStream();
                    int ret = 0;
                    while ((ret = read.read(buf)) > 0) {
                        os.write(buf, 0, ret);
                    }
            } catch(IOException ex) {
                throw new HttpConnectionException(ex.getCause(),"Unable to connect to server");
            }
        }finally {
            try {
                if(null != read)
                    read.close();
            } catch (IOException e) {
                throw new HttpConnectionException(e.getCause(),"Unable to close connection");
            }
        }
        return new ClientResponse(code, os.toString());
    }*/

    private HttpURLConnection getConnection(HttpConnectionProperty property){
        try {
            URL url = new URL(property.getUrl());
            HttpURLConnection connection  = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(property.getMethod());
            //connection.setConnectTimeout(property.getConnectTimeOut());
            //connection.setReadTimeout(property.getReadTimeOut());
            connection.setUseCaches(property.isUseCache());
            connection.setRequestProperty("Content-Type", property.getContentType());
            connection.setRequestProperty("Accept", property.getAcceptType());
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
            e.printStackTrace();
            try {
                //logger.debug("failed to connect. Reading error message");
                code = connection.getResponseCode();
                read = connection.getErrorStream();
                int ret = 0;
                while ((ret = read.read(buf)) > 0) {
                    os.write(buf, 0, ret);
                }
            } catch(IOException ex) {
                ex.printStackTrace();
                throw new HttpConnectionException(ex.getCause(),"Unable to connect to server");
            }
        }finally {
            try {
                if(null != read)
                    read.close();
                read =null;
            } catch (IOException e) {
               // throw new HttpConnectionException(e.getCause(),"Unable to close connection");
            }
            //logger.debug("Disconnecting...");
            connection.disconnect();
        }
        return new ClientResponse(code, os.toString());
    }
    
    
    public static void main(String args[]) throws InterruptedException {

        for(int i=0;i< 3000; i++){
            final int index = i;
            Thread thread = new Thread(){
                public void run(){
                    testSSP(index);
                }
            };
            thread.start();
            Thread.sleep(1);
        }
    }

    public static void testParellel(final int index){
        ExecutorService executorService = Executors.newFixedThreadPool(200);
        List<Callable<String>> callables = new ArrayList<Callable<String>>();
        for(int i = 0; i< 1; i++){
            Callable<String> callable = new Callable<String>() {
                @Override
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
        HttpURLConnection connection = client.getConnection(new HttpConnectionProperty("http://localhost:8080/ssp?pubId="+pubId, ClientMethod.POST.toString()));
        try {
            connection.setDoOutput(true);
            connection.addRequestProperty("pubId", pubId+"");
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
