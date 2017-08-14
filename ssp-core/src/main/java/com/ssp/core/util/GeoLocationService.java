package com.ssp.core.util;

import com.maxmind.db.CHMCache;
import com.maxmind.db.Reader;
import com.maxmind.geoip2.DatabaseProvider;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.*;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    private boolean cache = true;
    private Map<String,CityResponse> cacheMap;

    public GeoLocationService(String dbLocation, boolean cache) throws IOException {
        this.dbLocation = dbLocation;
        this.cache = cache;
        init();
        cacheMap = new ConcurrentHashMap<String, CityResponse>();
    }

    private void init() throws IOException {
        File dbFile = new File(dbLocation);
        DatabaseReader.Builder geoBuilder =  new DatabaseReader.Builder(dbFile)
                .fileMode(Reader.FileMode.MEMORY);
        if(cache){
            geoBuilder.withCache(new CHMCache());
        }
        provider = geoBuilder.build();
    }
    
    public CityResponse getLocation(String ip) throws IOException, GeoIp2Exception {
        InetAddress inetAddress = InetAddress.getByName(ip);
        return this.provider.city(inetAddress);
    }
}
