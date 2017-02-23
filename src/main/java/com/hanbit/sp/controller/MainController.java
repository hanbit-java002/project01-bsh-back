package com.hanbit.sp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanbit.sp.service.RecentlyListService;

@RestController
public class MainController {
	
	@Autowired
	private RecentlyListService recentlyListService;
	
	@RequestMapping("/api2/main/section/{sectionCode}/items")
	public List<Map<String, Object>> getSectionItems(
			@PathVariable("sectionCode") String sectionCode) {
		if ("01".equals(sectionCode)) {
			return recentlyListService.getAll(9);	
		}
		
		return new ArrayList<>();
	}
}
