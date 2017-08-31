package com.ssp.client.http;

import com.ssp.api.exception.HttpConnectionException;
import com.ssp.api.exception.SSPURLException;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.zip.GZIPInputStream;

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
        HttpURLConnection connection = getConnection(request);
        return connect(connection);
    }

    public ClientResponse post(ClientRequest request) throws SSPURLException {
        HttpURLConnection connection = getConnection(request);
        OutputStream os = null;
        try {
            connection.setDoOutput(true);
            if (request.isCompressed()) {
                connection.setRequestProperty("Accept-Encoding", "gzip");
            }
            String input = request.getContent();
            os = connection.getOutputStream();
            os.write(input.getBytes());
            os.flush();
        } catch (IOException e) {
            logger.error("Unable to write request:- " + e.getMessage());
            throw new SSPURLException("Unable to write request", e.getCause(), HttpStatus.SC_NOT_FOUND);
        } finally {
            close(os);
            //connection.disconnect();
        }
        return connect(connection);
    }

    private HttpURLConnection getConnection(ClientRequest request) {
        try {
            URL url = new URL(request.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(request.getMethod());
            if (request.getProperty().containsKey(ClientRequest.USE_CACHE_NAME))
                connection.setUseCaches(true);
            connection.setRequestProperty(ClientRequest.ACCEPT_LANGUAGE_NAME, ClientRequest.ACCEPT_LANGUAGE);
            connection.setRequestProperty(ClientRequest.CONTENT_TYPE_NAME, ClientRequest.CONTENT_TYPE);
            connection.setRequestProperty(ClientRequest.ACCEPT_TYPE_NAME, request.getProperty().get(ClientRequest.CONTENT_TYPE_NAME).toString());
            if (request.getProperty().containsKey(ClientRequest.CONNECTION_TIMEOUT_NAME))
                connection.setConnectTimeout((Integer) request.getProperty().get(ClientRequest.CONNECTION_TIMEOUT_NAME));
            if (request.getProperty().containsKey(ClientRequest.READ_TIMEOUT_NAME))
                connection.setReadTimeout((Integer) request.getProperty().get(ClientRequest.READ_TIMEOUT_NAME));
            return connection;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.error("Unable to form URL:- " + e.getMessage());
            throw new SSPURLException("URL not formed properly", e.getCause(), HttpStatus.SC_PRECONDITION_FAILED);
        } catch (IOException e) {
            e.printStackTrace();
            throw new HttpConnectionException("Unable to connect to server",e.getCause(),  HttpStatus.SC_BAD_REQUEST);
        }
    }

    private ClientResponse connect(HttpURLConnection connection) throws HttpConnectionException {
        int code;
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            //logger.debug("Connecting to dsp server....");
            connection.connect();
            code = connection.getResponseCode();
            if ("gzip".equals(connection.getContentEncoding())) {
                reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream())));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            String output = null;
            while ((output = reader.readLine()) != null) {
                sb.append(output);
            }
        } catch (SocketTimeoutException e) {
            logger.error("DSP connection timed out:- ");
            code = HttpStatus.SC_GATEWAY_TIMEOUT;
        } catch (FileNotFoundException e) {
            logger.error(" File not found:- " + connection.getURL().toString());
            code = HttpStatus.SC_NO_CONTENT;
        } catch (IOException e) {
            try {
                logger.debug("failed to connect. Reading error message");
                code = connection.getResponseCode();
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String output = null;
                while ((output = reader.readLine()) != null) {
                    sb.append(output);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new HttpConnectionException("Unable to connect to server",ex.getCause(),  404);
            } finally {
                close(reader);
            }
            e.printStackTrace();
        } finally {
            close(reader);
        }
        return new ClientResponse(code, sb.toString());
    }

    private void close(Object obj) {
        try {
            if (null == obj)
                return;
            if (obj instanceof InputStream) {
                InputStream in = (InputStream) obj;
                in.close();
            } else if (obj instanceof OutputStream) {
                OutputStream out = (OutputStream) obj;
                out.close();
            }
            obj = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws InterruptedException {
        for (int i = 0; i < 1; i++) {
            final int index = i;
            Thread thread = new Thread() {
                public void run() {
                    testSSP(index);
                }
            };
            thread.start();
            Thread.sleep(2);
        }
    }

    public static String testSSP(Integer pubId) {
        long startTime = Calendar.getInstance().getTimeInMillis();
        System.out.println("Thread Number" + pubId + " Start time:- " + startTime);
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
            System.out.println("Thread Number" + pubId + " End time:- " + endTime);
            System.out.println("Thread Number" + pubId + " Diff:- " + (endTime - startTime));
            System.out.println(res);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unable to write request:- " + e.getMessage());
            throw new SSPURLException("Unable to write request", e.getCause(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
        } finally {
            if (null != connection)
                connection.disconnect();
        }
    }

    public static String testDSP(Integer count) {
        long startTime = Calendar.getInstance().getTimeInMillis();
        System.out.println("Thread Number" + count + " Start time:- " + startTime);
        SSPHttpClient client = new SSPHttpClient();
        ClientRequest clientRequest = new ClientRequest(" http://34.199.35.125/dsp1/dsp/dreamSSP.php?ex=186", ClientMethod.POST);
        //clientRequest.put(ClientRequest.CONNECTION_TIMEOUT_NAME, 100);
        //clientRequest.put(ClientRequest.READ_TIMEOUT_NAME, 200);
        clientRequest.setContent("{\"id\":\"3e86d02c-c93d-429c-8f6d-5f0a716e69e6\",\"imp\":[{\"id\":\"1\",\"banner\":{\"w\":300,\"h\":250,\"id\":\"1\",\"battr\":[13],\"pos\":1,\"topframe\":1},\"bidfloor\":0.5199999809265137,\"secure\":0}],\"site\":{\"id\":\"1\",\"name\":\"W3 Schools\",\"domain\":\"https://www.w3schools.com\",\"cat\":[\"IAB1\",\"IAB4-1\"],\"page\":\"http://www.foobar.com/1234.html\",\"ref\":\"http://referringsite.com/referringpage.htm\",\"privacypolicy\":1,\"publisher\":{\"id\":\"3\",\"name\":\"dreamajax adserver\"}},\"device\":{\"ua\":\"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:54.0) Gecko/20100101 Firefox/54.0\",\"geo\":{\"lat\":20.0,\"lon\":77.0,\"type\":2,\"country\":\"IN\",\"city\":\"\",\"zip\":\"\",\"utcoffset\":200},\"ip\":\"49.206.255.140\",\"devicetype\":1,\"js\":1,\"language\":\"en\",\"connectiontype\":0},\"test\":0,\"at\":1,\"tmax\":120,\"cur\":[\"USD\"],\"regs\":{\"coppa\":0}}");
        //HttpURLConnection connection = client.getConnection(clientRequest);
        try {
            ClientResponse response = client.post(clientRequest);
            long endTime = Calendar.getInstance().getTimeInMillis();
            System.out.println("Thread Number" + count + " End time:- " + endTime);
            System.out.println("Thread Number" + count + " Diff:- " + (endTime - startTime));
            System.out.println(response.getResponse());
            return response.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unable to write request:- " + e.getMessage());
            throw new SSPURLException("Unable to write request", e.getCause(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
