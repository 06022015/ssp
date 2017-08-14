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
            //connection.setRequestProperty(ClientRequest.ACCEPT_TYPE_NAME, request.getProperty().get(ClientRequest.CONTENT_TYPE_NAME).toString());
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
                throw new HttpConnectionException(ex.getCause(), "Unable to connect to server");
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
            //Thread.sleep(1);
        }
    }

    public static void testParellel(final int index) {
        ExecutorService executorService = Executors.newFixedThreadPool(200);
        List<Callable<String>> callables = new ArrayList<Callable<String>>();
        for (int i = 0; i < 1; i++) {
            Callable<String> callable = new Callable<String>() {
                public String call() throws Exception {
                    return testSSP(index);
                }
            };
            callables.add(callable);
        }
        long startTime = Calendar.getInstance().getTimeInMillis();
        System.out.println("Thread Number" + index + " Start time:- " + startTime);
        List<Future<String>> requests = null;
        try {
            requests = executorService.invokeAll(callables);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread Number" + index + " Mid time:- " + Calendar.getInstance().getTimeInMillis());
        executorService.shutdown();
        while (!executorService.isTerminated()) {

        }
        long endTime = Calendar.getInstance().getTimeInMillis();
        System.out.println("Thread Number" + index + " End time:- " + endTime);
        System.out.println("Thread Number" + index + " Diff:- " + (endTime - startTime));
        for (Future<String> future : requests) {
            try {
                //future.get();
                System.out.println(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Complete time:- " + new Date());
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
        ClientRequest clientRequest = new ClientRequest("http://182.72.85.2:8010/development/yayimla_dev/dsp/dreamSSP.php?ex=186", ClientMethod.POST);
        clientRequest.put(ClientRequest.CONNECTION_TIMEOUT_NAME, 100);
        clientRequest.put(ClientRequest.READ_TIMEOUT_NAME, 200);
        clientRequest.setContent("{  \n" +
                "   \"id\":\"22eae5d8-8ce9-49dd-858c-a0b34554203d\",\n" +
                "   \"imp\":[  \n" +
                "      {  \n" +
                "         \"id\":\"1\",\n" +
                "         \"banner\":{  \n" +
                "            \"w\":300,\n" +
                "            \"h\":250,\n" +
                "            \"id\":\"1\",\n" +
                "            \"battr\":[  \n" +
                "               13\n" +
                "            ],\n" +
                "            \"pos\":1,\n" +
                "            \"topframe\":1\n" +
                "         },\n" +
                "         \"bidfloor\":0.5199999809265137,\n" +
                "         \"secure\":0\n" +
                "      }\n" +
                "   ],\n" +
                "   \"site\":{  \n" +
                "      \"id\":\"1\",\n" +
                "      \"name\":\"W3 Schools\",\n" +
                "      \"domain\":\"https://www.w3schools.com\",\n" +
                "      \"cat\":[  \n" +
                "         \"IAB1\",\n" +
                "         \"IAB4-1\"\n" +
                "      ],\n" +
                "      \"page\":\"http://www.foobar.com/1234.html\",\n" +
                "      \"ref\":\"http://referringsite.com/referringpage.htm\",\n" +
                "      \"privacypolicy\":1,\n" +
                "      \"publisher\":{  \n" +
                "         \"id\":\"3\",\n" +
                "         \"name\":\"dreamajax adserver\"\n" +
                "      }\n" +
                "   },\n" +
                "   \"device\":{  \n" +
                "      \"ua\":\"Mozilla/5.0 Firefox/26.0\",\n" +
                "      \"geo\":{  \n" +
                "         \"lat\":20.0,\n" +
                "         \"lon\":77.0,\n" +
                "         \"type\":2,\n" +
                "         \"country\":\"IN\",\n" +
                "         \"city\":\"\",\n" +
                "         \"zip\":\"\",\n" +
                "         \"utcoffset\":200\n" +
                "      },\n" +
                "      \"ip\":\"49.206.255.140\",\n" +
                "      \"devicetype\":1,\n" +
                "      \"js\":1,\n" +
                "      \"language\":\"en\",\n" +
                "      \"connectiontype\":0\n" +
                "   },\n" +
                "   \"test\":0,\n" +
                "   \"at\":1,\n" +
                "   \"tmax\":120,\n" +
                "   \"cur\":[  \n" +
                "      \"USD\"\n" +
                "   ],\n" +
                "   \"regs\":{  \n" +
                "      \"coppa\":0\n" +
                "   }\n" +
                "}");
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

    public static String testForeAlertUser(Integer count) {
        long startTime = Calendar.getInstance().getTimeInMillis();
        System.out.println("Thread Number" + count + " Start time:- " + startTime);
        SSPHttpClient client = new SSPHttpClient();
        ClientRequest clientRequest = new ClientRequest("http://localhost:8080/forealert/services/v1/user", ClientMethod.POST);
        clientRequest.setContent("{  \n" +
                "   \"uuId\":\"asdfajhsgf2342hjasdf768df"+count+"\",\n" +
                "   \"name\":\"Rizwan Qureshi"+count+"\",\n" +
                "   \"email\":\"rizwanqureshi"+count+"@gmail.com\",\n" +
                "   \"username\":\"rubinaqureshi"+count+"\",\n" +
                "   \"mobile\":\"9886327"+count+"\",\n" +
                "   \"location\":{  \n" +
                "      \"latitude\":"+2342.33*count+",\n" +
                "      \"longitude\":"+3424.23*count+",\n" +
                "      \"altitude\":"+24*count+",\n" +
                "      \"radius\":"+23*count+"\n" +
                "   }\n" +
                "}");
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

    public static String testForeAlertReportAnIssue(Integer count) {
        long startTime = Calendar.getInstance().getTimeInMillis();
        System.out.println("Thread Number" + count + " Start time:- " + startTime);
        SSPHttpClient client = new SSPHttpClient();
        ClientRequest clientRequest = new ClientRequest("http://localhost:8080/forealert/services/v1/message/report/issue", ClientMethod.POST);
        clientRequest.setContent("{  \n" +
                "   \"title\":\"Accident\",\n" +
                "   \"message\":\"Accident happened in Indira Nagar\",\n" +
                "   \"app\":\"EMG_APP\",\n" +
                "   \"senderUUId\":\"asdfajhsgf2342hjasdf76"+count+"\",\n" +
                "   \"messageLocation\":{  \n" +
                "      \"latitude\":23.33"+count+",\n" +
                "      \"longitude\":"+34.24*count+",\n" +
                "      \"altitude\":"+23*count+",\n" +
                "      \"radius\":"+23*count+",\n" +
                "      \"warningRadius\":"+23*count+"\n" +
                "   },\n" +
                "   \"senderLocation\":{  \n" +
                "      \"latitude\":"+2342.33*count+",\n" +
                "      \"longitude\":"+3424.23*count+",\n" +
                "      \"altitude\":"+23*count+",\n" +
                "      \"radius\":"+23*count+",\n" +
                "      \"warningRadius\":"+23*count+"\n" +
                "   },\n" +
                "   \"status\":\"A\",\n" +
                "   \"type\":\"ZONE\",\n" +
                "   \"isUserGEOMessage\":true,\n" +
                "   \"device\":\"IOS\"\n" +
                "}");
        //DailyRollingFileAppender p =null;
        //p.setL
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
