package kr.co.ibk.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.ibk.common.utils.CustomMap;

@Repository
public interface CommonRepository {
	public List<CustomMap> getMenuTree(CustomMap param);
}
