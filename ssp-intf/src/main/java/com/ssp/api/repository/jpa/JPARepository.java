package com.ssp.api.repository.jpa;

import com.ssp.api.entity.jpa.AdBlockInfo;
import com.ssp.api.entity.jpa.DSPInfo;
import com.ssp.api.entity.jpa.WinNoticeEntity;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/19/17
 * Time: 12:05 AM
 * To change this template use File | Settings | File Templates.
 */
public interface JPARepository {

    List<DSPInfo> getAllDSP(String adFormat);

    AdBlockInfo  getAdBlockInfo(Long pubId, Long adBlockId);

    void saveWinningBid(WinNoticeEntity winNotice);

}
