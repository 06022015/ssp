package com.ssp.web.servlet;

import com.scientiamobile.wurfl.core.Device;
import com.scientiamobile.wurfl.core.WURFLEngine;
import com.ssp.api.Constant;
import com.ssp.api.dto.BidData;
import com.ssp.api.exception.SSPException;
import com.ssp.api.service.SSPService;
import com.ssp.web.util.ApplicationContextUtil;
/*import net.sourceforge.wurfl.core.WURFLHolder;*/
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 6/24/17
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class SSPServlet extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(SSPServlet.class);

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException, java.io.IOException {
        long startTime = Calendar.getInstance().getTimeInMillis();
        logger.debug("Request in for pubId:-" + req.getParameter(Constant.PUBLISHER_ID) + " " + startTime);
        String response = "";
        String dvId = req.getParameter(Constant.DIV_ID);
        try{
            Map<String, String> parameter = new HashMap<String, String>();
            String hostIp = req.getRemoteAddr();
            /*if(hostIp.startsWith("127.0.0"))
                parameter.put(Constant.IP, "49.206.255.140");
            else
                parameter.put(Constant.IP, req.getRemoteAddr());*/
            parameter.put(Constant.IP, req.getRemoteAddr());
            parameter.put(Constant.USER_AGENT, req.getHeader("User-Agent"));
            parameter.put(Constant.PUBLISHER_ID, req.getParameter(Constant.PUBLISHER_ID));
            parameter.put(Constant.BLOCK_ID, req.getParameter(Constant.BLOCK_ID));
            parameter.put(Constant.REF_URL, req.getParameter("ref"));
            parameter.put(Constant.DEVICE_LANG, req.getLocale().getLanguage());
            String requestURL = req.getHeader("referer");
            parameter.put(Constant.REQUESTER_URL, StringUtils.isNotEmpty(requestURL)?requestURL:"");
            deviceCap(parameter,req);
            SSPService service = (SSPService) ApplicationContextUtil.getApplicationContext().getBean("sspService");
            logger.debug("Reading service obj:-" + (Calendar.getInstance().getTimeInMillis() - startTime));
            BidData bidData;
            try {
                bidData = service.processRequest(parameter);
                resp.setStatus(HttpStatus.OK.value());
                if(null != bidData)
                    response = buildResponse(bidData, dvId);
                else
                    response = buildEmptyResponse(dvId);
            } catch (SSPException e) {
                logger.error("SSP Exception "+e.getMessage());
                resp.setStatus(e.getCode());
                response = buildEmptyResponse(dvId);
            } catch (IOException e) {
                logger.error("IO Exception "+e.getMessage());
                response = buildEmptyResponse(dvId);
            }catch (NullPointerException e){
                logger.error("Null pointer Exception "+e.getMessage());
                resp.setStatus(HttpStatus.OK.value());
                response = buildEmptyResponse(dvId);
                e.printStackTrace();
            }
            logger.debug("Request out for pubId:-" + parameter.get(Constant.PUBLISHER_ID) + " " + (Calendar.getInstance().getTimeInMillis() - startTime));
        }catch (Exception e){
             e.printStackTrace();
            response = buildEmptyResponse(dvId);
        }
        resp.getWriter().write(response);
    }


    private void deviceCap(Map<String, String> parameter, HttpServletRequest req) {
        /*WURFLHolder wurflHolder = (WURFLHolder) ApplicationContextUtil.getApplicationContext().getBean("wurflHolder");
            Device device = wurflHolder.getWURFLManager().getDeviceForRequest(req);*/
        WURFLEngine wurflEngine = (WURFLEngine) ApplicationContextUtil.getApplicationContext().getBean("wurflEngine");
        Device device = wurflEngine.getDeviceForRequest(req);
        logger.debug("WURFL device := " + device.toString());
        Map<String, String> deviceVirCap = device.getVirtualCapabilities();
        Map<String, String> deviceCap = device.getCapabilities();
        logger.debug("Virtual capabilities" + deviceVirCap.toString());
        logger.debug("Capabilities" + deviceCap.toString());
        String deviceOs = deviceVirCap.get(Constant.DEVICE_ADVERTISE_DEVICE_OS);
          deviceOs = StringUtils.isBlank(deviceOs)?deviceCap.get(Constant.DEVICE_OS):deviceOs;
        parameter.put(Constant.DEVICE_OS, StringUtils.isNotBlank(deviceOs) ? deviceOs : "");
        String deviceOsVersion = deviceVirCap.get(Constant.DEVICE_ADVERTISE_DEVICE_OS_VERSION);
        deviceOsVersion = StringUtils.isBlank(deviceOsVersion)?deviceCap.get(Constant.DEVICE_OS_VERSION):deviceOsVersion;
        parameter.put(Constant.DEVICE_OS_VERSION, StringUtils.isNotBlank(deviceOsVersion) ? deviceOsVersion : "");
        String deviceMake = deviceCap.get(Constant.DEVICE_MAKE);
        parameter.put(Constant.DEVICE_MAKE, StringUtils.isNotBlank(deviceMake) ? deviceMake : "");
        String deviceModel = deviceCap.get(Constant.DEVICE_MODEL);
        parameter.put(Constant.DEVICE_MODEL, StringUtils.isNotBlank(deviceModel) ? deviceModel : "");
        String formFactor = deviceVirCap.get(Constant.FORM_FACTOR);
        parameter.put(Constant.FORM_FACTOR, StringUtils.isNotBlank(formFactor) ? formFactor : "");
        String hwv = deviceCap.get(Constant.DEVICE_HWV);
        parameter.put(Constant.DEVICE_HWV, StringUtils.isNotBlank(hwv) ? hwv : "");
    }

    private String buildResponse(BidData bidData, String diveId){
        StringBuilder response = new StringBuilder("(function(callbackAd){var ao = {}; ao.status='");
        response.append("SUCCESS");
        response.append("'; ao.divid='");
        response.append(diveId);
        response.append("';ao.sessionId='");
        response.append("4abc30a6-dbe8-4510-84c2-104e67ca5a2b");
        response.append("';ao.type='");
        response.append("RICHMEDIA");
        response.append("'; ao.link=''; ao.mediadata=\"");
        response.append(bidData.getAdm());
        response.append("\";ao.target=''; ao.text=''; ao.width='");
        response.append(bidData.getWidth());
        response.append("'; ao.height='");
        response.append(bidData.getHeight());
        response.append("'; ao.beacons=[\"");
        response.append("http://www.google.com");
        response.append("\"];if (callbackAd != null) {callbackAd(ao);}})(AdTag.showAd);");
        return response.toString();
    }

    private String buildEmptyResponse(String divId){
        StringBuilder response = new StringBuilder("(function(callbackAd){var ao = {}; ao.status='");
        response.append("ERROR';");
        response.append("ao.errorCode=42;");
        response.append("ao.divid='");
        response.append(divId);
        response.append("';ao.errorMessage='");
        response.append("Currently no ad available'; ");
        response.append("if (callbackAd != null) {callbackAd(ao);}})(AdTag.showAd);");
        return response.toString();
    }

}
