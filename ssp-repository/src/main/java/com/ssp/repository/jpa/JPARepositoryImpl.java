package com.ssp.repository.jpa;

import com.ssp.api.entity.jpa.AdBlockInfo;
import com.ssp.api.entity.jpa.DSPInfo;
import com.ssp.api.repository.jpa.JPARepository;
import org.hibernate.*;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/19/17
 * Time: 12:05 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("jpaRepository")
public class JPARepositoryImpl implements JPARepository {

    private static Logger logger = LoggerFactory.getLogger(JPARepositoryImpl.class);

    private static String DSP_DETAIL_QUERY = "SELECT u.id as user_id, d.ping_url, d.qps, d.request_format, d.compress_request FROM djax_users as u LEFT JOIN djax_dsp as d ON u.id = d.user_id WHERE u.user_type = 'dsp' AND d.request_format = :adFormat AND u.is_approved = 1 AND u.is_blocked = 0 AND u.is_deleted = 0";

    private static String PUBLISHER_INFO_QUERY = "call publisherInfo(:userId, :adBlockId)";

    @Autowired
    private SessionFactory sessionFactory;

    public List<DSPInfo> getAllDSP(String adFormat) {
        SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(DSP_DETAIL_QUERY);
        sqlQuery.setParameter("adFormat", adFormat)
                .setResultTransformer(new AliasToBeanConstructorResultTransformer(DSPInfo.class.getConstructors()[1]))
                .setCacheRegion("api_users")
                .setCacheMode(CacheMode.NORMAL);
        return sqlQuery.list();



        
        /*String hql = "select user.id as userId, user.pingURL as pingURL from UserEntity user where user.userType = '" + UserType.dsp + "' and user.approved = true and user.deleted = false and user .denied = false";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setCacheable(true);
        query.setCacheRegion("api_users");
        query.setCacheMode(CacheMode.NORMAL);
        query.setResultTransformer(new AliasToBeanResultTransformer(DSPInfo.class));
        return query.list();*/

    }

    public AdBlockInfo getAdBlockInfo(Long pubId, Long adBlockId) {
        SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(PUBLISHER_INFO_QUERY);
        sqlQuery.setParameter("userId", pubId)
                .setParameter("adBlockId", adBlockId)
                .setResultTransformer(new AliasToBeanConstructorResultTransformer(AdBlockInfo.class.getConstructors()[0]))
                .setCacheRegion("ad_block")
                .setCacheMode(CacheMode.NORMAL);
        List<AdBlockInfo> results  = sqlQuery.list();
        return null != results && results.size()>0 ? results.get(0) : null;


       /* String hql = "select user.id as userId,user.name as userName, site.id as siteId, site.siteName as siteName,site.siteURL as siteURL, site.category as siteCat," +
                " adBlock.category as adBlockCat, adBlock.width as width, adBlock.height as height, adBlock.floorPrice as floorPrice,adBlock.adPosition as adPosition " +
                "from UserEntity user, SiteEntity site,AdBlockEntity adBlock where user.id = site.user.id " +
                " and site.id = adBlock.site.id and user.approved=true and user.deleted=false and user.denied= false" +
                " and site.approved=true and site.deleted=false and site.denied= false and adBlock.approved = true and adBlock.deleted = false and adBlock.denied = false" +
                " and user.userType = 'pub' and user.id=" + pubId + " and adBlock.id=" + adBlockId;
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setCacheable(true);
        //query.setCacheRegion("ad_block");
        query.setCacheMode(CacheMode.NORMAL);
        query.setResultTransformer(new AliasToBeanResultTransformer(AdBlockInfo.class));
        List result = query.list();
        return null != result && result.size() > 0 ? (AdBlockInfo) result.get(0) : null;*/
    }
}
