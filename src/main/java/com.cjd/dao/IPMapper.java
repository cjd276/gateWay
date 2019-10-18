package com.cjd.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface IPMapper {

    @Select(value="select ip_addr from ip_black")
    Set<String> getIpBlackList();

    @Select(value="select ip_addr from ip_white")
    Set<String> getIpWhiteList();

}
