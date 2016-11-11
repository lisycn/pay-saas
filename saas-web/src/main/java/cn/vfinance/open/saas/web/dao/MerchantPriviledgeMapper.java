package cn.vfinance.open.saas.web.dao;

import cn.vfinance.open.saas.web.model.MerchantPriviledge;

import java.util.List;

public interface MerchantPriviledgeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_channel_priviledge
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_channel_priviledge
     *
     * @mbggenerated
     */
    int insert(MerchantPriviledge record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_channel_priviledge
     *
     * @mbggenerated
     */
    int insertSelective(MerchantPriviledge record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_channel_priviledge
     *
     * @mbggenerated
     */
    MerchantPriviledge selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_channel_priviledge
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(MerchantPriviledge record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_channel_priviledge
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(MerchantPriviledge record);

    List<MerchantPriviledge> selectPriviledgeByRole(Integer roleId);

    List<MerchantPriviledge> selectPriviledgeByMerchant(Integer merchantId);
}