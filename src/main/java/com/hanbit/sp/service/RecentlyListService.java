package com.hanbit.sp.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hanbit.sp.dao.RecentlyListDAO;


@Service
public class RecentlyListService {

	@Autowired
	private RecentlyListDAO recentlyListDAO;
	
	public List<Map<String, Object>> getAll() {
		return recentlyListDAO.selectAll();
	}
}
