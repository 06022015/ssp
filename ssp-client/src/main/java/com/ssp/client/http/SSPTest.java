package com.ssp.client.http;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Calendar;
import java.util.Scanner;

public class SSPTest {

    private HttpURLConnection getConnection(String endPoint, String method) {
        try {
            URL url = new URL(endPoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            return connection;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("Unable to form URL:- " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static class SSPRes{
        int code;
        String content;

        public SSPRes(int code, String content) {
            this.code = code;
            this.content = content;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    private SSPRes connect(HttpURLConnection connection) throws Exception {
        int code = 200;
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            connection.connect();
            code = connection.getResponseCode();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String output = null;
            while ((output = reader.readLine()) != null) {
                sb.append(output);
            }
        } catch (SocketTimeoutException e) {
            System.out.println("DSP connection timed out:- ");
            code = 504;
        } catch (IOException e) {
            try {
                System.out.println("failed to connect. Reading error message");
                code = connection.getResponseCode();
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String output = null;
                while ((output = reader.readLine()) != null) {
                    sb.append(output);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                close(reader);
            }
            e.printStackTrace();
        } finally {
            close(reader);
        }
        return new SSPRes(code, sb.toString());
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
        Scanner scan = new Scanner(System.in);
        int size = scan.nextInt();
        for (int i = 0; i < size; i++) {
            final int index = i;
            Thread thread = new Thread() {
                public void run() {
                    testSSP(index);
                }
            };
            thread.start();
            Thread.sleep(1);
        }
    }

    public static void testSSP(Integer pubId) {
        long startTime = Calendar.getInstance().getTimeInMillis();
        System.out.println("Thread Number" + pubId + " Start time:- " + startTime);
        SSPTest client = new SSPTest();
        //HttpURLConnection connection = client.getConnection("http://34.225.131.159:8080/ssp/ReqAd?pub_id=3&block_id=1&ref=http://www.foobar.com/1234.html","GET");
        HttpURLConnection connection = client.getConnection("http://localhost:8080/ssp/ReqAd?pub_id=3&block_id=1&ref=http://www.foobar.com/1234.html","GET");
        try {
            connection.setDoOutput(true);
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 Firefox/26.0");
            String res = client.connect(connection).getContent();
            long endTime = Calendar.getInstance().getTimeInMillis();
            System.out.println("Thread Number" + pubId + " End time:- " + endTime);
            System.out.println("Thread Number" + pubId + " Diff:- " + (endTime - startTime));
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to write request:- " + e.getMessage());
        } finally {
            if (null != connection)
                connection.disconnect();
        }
    }


}
