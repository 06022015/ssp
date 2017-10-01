package com.ssp.api.entity.jpa;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "djax_win_notice")
public class WinNoticeEntity {

    private static final long serialVersionUID = 3256446889020622647L;

    private Long id;
    private Date createdAt;
    private Integer dspId;
    private Integer publisherId;
    private String requestId;
    private Float publisherShare;
    private Float dspBidAmount;
    private Integer adSpaceId;

    public WinNoticeEntity() {
        this.createdAt = new Date();
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(unique = true, nullable = false, updatable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Column(name = "dsp_id")
    public Integer getDspId() {
        return dspId;
    }

    public void setDspId(Integer dspId) {
        this.dspId = dspId;
    }

    @Column(name = "publisher_id")
    public Integer getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Integer publisherId) {
        this.publisherId = publisherId;
    }

    @Column(name = "request_id")
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Column(name = "publisher_share")
    public Float getPublisherShare() {
        return publisherShare;
    }

    public void setPublisherShare(Float publisherShare) {
        this.publisherShare = publisherShare;
    }

    @Column(name = "dsp_bid_amount")
    public Float getDspBidAmount() {
        return dspBidAmount;
    }

    public void setDspBidAmount(Float dspBidAmount) {
        this.dspBidAmount = dspBidAmount;
    }

    @Column(name = "adspace_id")
    public Integer getAdSpaceId() {
        return adSpaceId;
    }

    public void setAdSpaceId(Integer adSpaceId) {
        this.adSpaceId = adSpaceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WinNoticeEntity that = (WinNoticeEntity) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
