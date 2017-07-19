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
            //connection.setUseCaches(property.isUseCache());
            //connection.setRequestProperty("Content-Type", property.getContentType());
            //connection.setRequestProperty("Accept", property.getAcceptType());
            return connection;
        } catch (MalformedURLException e) {
            logger.error("Unable to form URL:- "+ e.getMessage());
            throw new SSPURLException("URL not formed properly", e.getCause(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            throw new HttpConnectionException(e.getCause(),"Unable to connect to server");
        }
    }

    private ClientResponse connect(HttpURLConnection connection) throws HttpConnectionException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        InputStream read = null;
        int code = 200;
        try {
            logger.debug("Connecting to dsp server....");
            connection.connect();
            code = connection.getResponseCode();
            read = connection.getInputStream();
            int ret = 0;
            while ((ret = read.read(buf)) > 0) {
                os.write(buf, 0, ret);
            }
        } catch (IOException e) {
            try {
                logger.debug("failed to connect. Reading error message");
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
                read =null;
            } catch (IOException e) {
               // throw new HttpConnectionException(e.getCause(),"Unable to close connection");
            }
            logger.debug("Disconnecting...");
            connection.disconnect();
        }
        return new ClientResponse(code, os.toString());
    }
    
    
    public static void main(String args[]){
        SSPHttpClient client = new SSPHttpClient();
        System.out.println(client.get("http://google.com").getResponse());
    }
}
