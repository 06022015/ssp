package com.ssp.core.util;

import com.maxmind.db.CHMCache;
import com.maxmind.db.Reader;
import com.maxmind.geoip2.DatabaseProvider;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/28/17
 * Time: 7:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class GeoLocationService {

    private DatabaseProvider provider;
    private String dbLocation;
    private boolean cache;

    public GeoLocationService(String dbLocation, boolean cache) throws IOException {
        this.dbLocation = dbLocation;
        this.cache = cache;
        init();
    }

    private void init() throws IOException {
        File dbFile = new File(dbLocation);
        DatabaseReader.Builder geoBuilder =  new DatabaseReader.Builder(dbFile);
        if(cache){
            geoBuilder.withCache(new CHMCache());
            geoBuilder.fileMode(Reader.FileMode.MEMORY_MAPPED);
        }
        provider = geoBuilder.build();
    }
    
    public CityResponse getLocation(String ip) throws IOException, GeoIp2Exception {
        InetAddress inetAddress = InetAddress.getByName(ip);
        return this.provider.city(inetAddress);
    }
}
