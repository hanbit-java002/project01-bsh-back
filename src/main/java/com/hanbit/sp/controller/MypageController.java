package com.hanbit.sp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class MypageController {
	@RequestMapping("/{menuId}")
	public String main(Model model, @PathVariable("menuId") String menuId) {
		model.addAttribute("menuId", menuId);
		return "admin/" + menuId;
	}

}
