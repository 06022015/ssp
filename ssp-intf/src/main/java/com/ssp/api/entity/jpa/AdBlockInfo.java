package com.ssp.api.entity.jpa;

import javax.persistence.Column;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/29/17
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdBlockInfo {

    private Integer userId;
    private String firstName;
    private String lastName;
    private String website;
    private Integer siteId;
    private String siteName;
    private String siteURL;
    private String appType;
    private String siteCat;
    private String adFormat;
    private Integer width;
    private Integer height;
    private String adPosition;
    private float floorPrice;
    private String adBlockCat;

    public AdBlockInfo(Integer userId, String firstName, String lastName, String website, Integer siteId, String siteName, String siteURL, Character appType, String siteCat, Character adFormat, Integer width, Integer height, Character adPosition, float floorPrice, String adBlockCat) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.website = website;
        this.siteId = siteId;
        this.siteName = siteName;
        this.siteURL = siteURL;
        this.appType = appType.toString();
        this.siteCat = siteCat;
        this.adFormat = adFormat.toString();
        this.width = width;
        this.height = height;
        this.adPosition = adPosition.toString();
        this.floorPrice = floorPrice;
        this.adBlockCat = adBlockCat;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteURL() {
        return siteURL;
    }

    public void setSiteURL(String siteURL) {
        this.siteURL = siteURL;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getSiteCat() {
        return siteCat;
    }

    public void setSiteCat(String siteCat) {
        this.siteCat = siteCat;
    }

    public String getAdFormat() {
        return adFormat;
    }

    public void setAdFormat(String adFormat) {
        this.adFormat = adFormat;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getAdPosition() {
        return adPosition;
    }

    public void setAdPosition(String adPosition) {
        this.adPosition = adPosition;
    }

    public float getFloorPrice() {
        return floorPrice;
    }

    public void setFloorPrice(float floorPrice) {
        this.floorPrice = floorPrice;
    }

    public String getAdBlockCat() {
        return adBlockCat;
    }

    public void setAdBlockCat(String adBlockCat) {
        this.adBlockCat = adBlockCat;
    }
}
