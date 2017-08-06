package com.ssp.api.dto;

public class BidData {

    private String nurl;
    private String iurl;
    private Double auctionPrice;
    private String auctionId;
    private String auctionBidId;
    private String auctionImpId;
    private String auctionSeatId;
    private String auctionAdId;
    private String auctionCurrency;
    private String adm;
    private Integer height;
    private Integer width;

    public String getNurl() {
        return nurl;
    }

    public void setNurl(String nurl) {
        this.nurl = nurl;
    }

    public String getIurl() {
        return iurl;
    }

    public void setIurl(String iurl) {
        this.iurl = iurl;
    }

    public Double getAuctionPrice() {
        return auctionPrice;
    }

    public void setAuctionPrice(Double auctionPrice) {
        this.auctionPrice = auctionPrice;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public String getAuctionBidId() {
        return auctionBidId;
    }

    public void setAuctionBidId(String auctionBidId) {
        this.auctionBidId = auctionBidId;
    }

    public String getAuctionImpId() {
        return auctionImpId;
    }

    public void setAuctionImpId(String auctionImpId) {
        this.auctionImpId = auctionImpId;
    }

    public String getAuctionSeatId() {
        return auctionSeatId;
    }

    public void setAuctionSeatId(String auctionSeatId) {
        this.auctionSeatId = auctionSeatId;
    }

    public String getAuctionAdId() {
        return auctionAdId;
    }

    public void setAuctionAdId(String auctionAdId) {
        this.auctionAdId = auctionAdId;
    }

    public String getAuctionCurrency() {
        return auctionCurrency;
    }

    public void setAuctionCurrency(String auctionCurrency) {
        this.auctionCurrency = auctionCurrency;
    }

    public String getAdm() {
        return adm;
    }

    public void setAdm(String adm) {
        this.adm = adm;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getFullNURL(){
         String completeNURL = nurl.replace("${AUCTION_ID}", getAuctionId());
        completeNURL = completeNURL.replace("${AUCTION_BID_ID}", getAuctionBidId());
        completeNURL = completeNURL.replace("${AUCTION_PRICE}", getAuctionPrice()+"");
        completeNURL = completeNURL.replace("${AUCTION_IMP_ID}", getAuctionImpId());
        completeNURL = completeNURL.replace("${AUCTION_SEAT_ID}", getAuctionSeatId());
        completeNURL = completeNURL.replace("${AUCTION_AD_ID}", getAuctionAdId());
        completeNURL = completeNURL.replace("${AUCTION_CURRENCY}", getAuctionCurrency());
         return completeNURL;
    }

    @Override
    public String toString() {
        return "BidData{" +
                "url='" + nurl + '\'' +
                ", auctionPrice=" + auctionPrice +
                ", auctionId='" + auctionId + '\'' +
                ", auctionBidId='" + auctionBidId + '\'' +
                ", auctionImpId='" + auctionImpId + '\'' +
                ", auctionSeatId='" + auctionSeatId + '\'' +
                ", auctionAdId='" + auctionAdId + '\'' +
                ", auctionCurrency='" + auctionCurrency + '\'' +
                '}';
    }
}
