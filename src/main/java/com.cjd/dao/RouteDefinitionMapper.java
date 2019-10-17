package com.cjd.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.List;
import java.util.Map;

@Mapper
public interface RouteDefinitionMapper {

    @Select(value="select * from route_definition where enabled = #{enabled,jdbcType=INTEGER}")
    List<Map> getRouteDefinition(Integer enabled);


    @Select(value = "select * from predicate_definition where enabled = #{enabled,jdbcType=INTEGER} and rd_id=#{routeDefinitionId,jdbcType=INTEGER}")
    List<Map> getPredicate(Integer enabled,Integer routeDefinitionId);

    @Select(value = "select * from filter_definition where enabled = #{enabled,jdbcType=INTEGER} and rd_id=#{routeDefinitionId,jdbcType=INTEGER}")
    List<Map> getFilterDefinition(Integer enabled,Integer routeDefinitionId);
}
