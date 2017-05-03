package com.yo.friendis.common.common.mapper;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ExportMapper {
	@Select("${sql}")
	List<LinkedHashMap<String, Object>> selecltBySql(@Param("sql") String sql);
}
