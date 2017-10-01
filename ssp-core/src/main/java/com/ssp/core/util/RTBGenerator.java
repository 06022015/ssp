package com.ssp.core.util;

import com.codahale.metrics.MetricRegistry;
import com.google.openrtb.OpenRtb.BidResponse;
import com.google.openrtb.OpenRtb.AdPosition;
import com.google.openrtb.OpenRtb.DeviceType;
import com.google.openrtb.OpenRtb.LocationType;
import com.google.openrtb.OpenRtb.CreativeAttribute;
import com.google.openrtb.OpenRtb.BidRequest.Regs;
import com.google.openrtb.OpenRtb.BidRequest;
import com.google.openrtb.OpenRtb.BidRequest.*;
import com.google.openrtb.OpenRtb.ConnectionType;
import com.google.openrtb.OpenRtb.AuctionType;
import com.google.openrtb.json.OpenRtbJsonFactory;
import com.google.openrtb.util.OpenRtbValidator;
import com.maxmind.geoip2.model.CityResponse;
import com.ssp.api.Constant;
import com.ssp.api.entity.jpa.AdBlockInfo;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/28/17
 * Time: 9:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class RTBGenerator {

    private  OpenRtbValidator validator;
    private OpenRtbJsonFactory openrtbJson;

    public RTBGenerator() {
        validator = new OpenRtbValidator(new MetricRegistry());
        openrtbJson = OpenRtbJsonFactory.create();
    }

    public  BidRequest  generate(AdBlockInfo adBlockInfo , CityResponse location, Map<String,String> parameter){
        BidRequest bidRequest = null;
        try{
            bidRequest = BidRequest.newBuilder()
                    .setId(UUID.randomUUID().toString())
                    .setAt(AuctionType.FIRST_PRICE)
                    .setTmax(120)
                    .addCur(parameter.get(Constant.CURRENCY))
                    .setTest(false)
                    .setRegs(Regs.newBuilder()
                            .setCoppa(false))
                    .addImp(BidRequest.Imp.newBuilder()
                            .setInstl(false)
                            .setId("1")
                            .setBidfloor(adBlockInfo.getFloorPrice())
                            .setBanner(Imp.Banner.newBuilder()
                                    //.setBtype()//Not required. based on discussion over skype.
                                    .setW(adBlockInfo.getWidth())
                                    .setH(adBlockInfo.getHeight())
                                    .setPos(AdPosition.forNumber(Integer.parseInt(adBlockInfo.getAdPosition())))
                                    .setTopframe(true)
                                    .addBattr(CreativeAttribute.USER_INTERACTIVE)
                                    .setId("1"))
                            .setTagid("") //Empty value. Confirmed from Hari
                            .setSecure(false))
                    .addAllBcat(new ArrayList<String>(0))//Confirmed from Hari
                    .addAllBadv(new ArrayList<String>(0))//Confirmed from Hari
                    .setSite(Site.newBuilder()
                            .setId(adBlockInfo.getSiteId()+"")
                            .setName(adBlockInfo.getSiteName())
                            .setDomain(adBlockInfo.getSiteURL())
                            .addAllCat(Arrays.asList(adBlockInfo.getSiteCat(), adBlockInfo.getAdBlockCat()))
                            .setPrivacypolicy(true)
                            .setKeywords("") //Empty value. Confirmed from Hari
                            .addAllSectioncat(new ArrayList<String>(0))//Empty value. Confirmed from Hari
                            .addAllPagecat(new ArrayList<String>(0))//Empty value. Confirmed from Hari
                            .setPage(parameter.get(Constant.REQUESTER_URL))  // It should be requester URL
                            .setRef(parameter.get(Constant.REF_URL)) // It shouw ref request param.
                            .setPublisher(Publisher.newBuilder()
                                    .setId(adBlockInfo.getUserId()+"")
                                    .addCat(adBlockInfo.getAdBlockCat())
                                    .setDomain(adBlockInfo.getWebsite())
                                    .setName(adBlockInfo.getFirstName()+ " "+ adBlockInfo.getLastName())))
                    .setDevice(getDeviceBuilder(location, parameter))
                    .setUser(User.newBuilder().setId(adBlockInfo.getUserId()+""))
                    //.setExtension()//Not required on current phase. Based on discussion over skype.
                    .build();

        }catch (Exception e){
            e.printStackTrace();
        }
       return bidRequest;
    }


    private Device.Builder getDeviceBuilder(CityResponse location, Map<String,String> parameter){
        Device.Builder deviceBuilder =  Device.newBuilder()
                .setDnt(false)
                .setLmt(Boolean.parseBoolean(parameter.get(Constant.LMT)))
                .setIp(parameter.get(Constant.IP))
                .setUa(parameter.get(Constant.USER_AGENT))
                .setOs(parameter.get(Constant.DEVICE_OS))
                .setLanguage(parameter.get(Constant.DEVICE_LANG))
                .setMake(parameter.get(Constant.DEVICE_MAKE))
                .setJs(true)
                .setConnectiontype(getConnectionType(location))
                .setDevicetype(getDeviceType(parameter.get(Constant.FORM_FACTOR)))
                .setOsv(parameter.get(Constant.DEVICE_OS_VERSION))
                .setModel(parameter.get(Constant.DEVICE_MODEL))
                .setHwv(parameter.get(Constant.DEVICE_HWV));
        if(null != location){
            deviceBuilder.setGeo(Geo.newBuilder()
                    .setLat((location.getLocation().getLatitude() != null) ? location.getLocation().getLatitude() : 0.000000)
                    .setLon((location.getLocation().getLongitude() != null) ? location.getLocation().getLongitude() : 0.000000)
                    .setCountry((location.getCountry().getIsoCode() != null) ? location.getCountry().getIsoCode() : "")
                    .setCity((location.getCity().getName() != null) ? location.getCity().getName() : "")
                    .setZip((location.getPostal().getCode() != null) ? location.getPostal().getCode() : "")
                    .setMetro(location.getLocation().getMetroCode()+"")
                    .setType(LocationType.IP)
                    .setUtcoffset(200));
            //Region not required . based on discussion over skype with Hari
        }
       return  deviceBuilder;
    }

    private static ConnectionType getConnectionType(CityResponse location){
        ConnectionType connectionType = ConnectionType.CONNECTION_UNKNOWN;
        if(null != location && location.getTraits().getConnectionType() != null){
            if(location.getTraits().getConnectionType().toString().equals("Cellular"))
                connectionType = ConnectionType.CELL_UNKNOWN;
            else if(location.getTraits().getConnectionType().toString().equals("Cable/DSL"))
                connectionType = ConnectionType.ETHERNET;
            else if(location.getTraits().getConnectionType().toString().equals("Corporate"))
                connectionType = ConnectionType.WIFI;
        }
        return connectionType;
    }

    private static DeviceType getDeviceType(String formFactor){
        DeviceType deviceType = DeviceType.MOBILE;
        if(StringUtils.isEmpty(formFactor))
            return deviceType;
        if(formFactor.equalsIgnoreCase("desktop"))
            deviceType = DeviceType.PERSONAL_COMPUTER;
        else if(formFactor.equalsIgnoreCase("tablet"))
            deviceType = DeviceType.TABLET;
        else if(formFactor.equalsIgnoreCase("smartphone"))
            deviceType = DeviceType.MOBILE;
        else if(formFactor.equalsIgnoreCase("smart-tv"))
            deviceType = DeviceType.CONNECTED_TV;
        return deviceType;
    }

    public BidResponse.Builder getBidResponse(String dspResponse) throws IOException {
        return openrtbJson.newReader().readBidResponse(dspResponse).toBuilder();
    }

    public boolean isValid(BidRequest bidRequest, BidResponse.Builder bidResponse) throws IOException {
        return this.validator.validate(bidRequest, bidResponse);
    }

    public String getBidAsString(BidRequest bidRequest)throws IOException{
        return openrtbJson.newWriter().writeBidRequest(bidRequest);
    }

    public static void main(String args[]){
        System.out.println(getDeviceType("smart-tv"));
    }

}
