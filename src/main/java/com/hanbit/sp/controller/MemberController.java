package com.hanbit.sp.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hanbit.sp.exception.SooException;
import com.hanbit.sp.service.MemberService;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	//회원가입//
	@RequestMapping(value="/api2/member/signup", method=RequestMethod.POST)
	@ResponseBody
	public Map signup(@RequestParam("userId") String userId,
			@RequestParam("userPw") String userPw) {
		if(StringUtils.isBlank(userId)) {
			throw new SooException("아이디가 잘못 입력되었습니다.", "WRONG_ID");
		}
		else if(StringUtils.isBlank(userPw)) {
			throw new SooException("비밀번호가 잘못 입력되었습니다.", "WRONG_PW");
		}
		else if(memberService.countUserId(userId) != 0) {
			throw new SooException("이미 가입된 이메일 주소입니다.", "ALREADY_SIGNUP");
		}
		
		String uid = memberService.addMember(userId, userPw);
		
		Map result = new HashMap();
		result.put("result", "ok");
		result.put("uid", uid);
		
		System.out.println("check controller signup -");

		return result;
	}
	
	//로그인//
	@RequestMapping(value="/api2/member/signin", method=RequestMethod.POST)
	@ResponseBody
	public Map signin(@RequestParam("userId") String userId,
			@RequestParam("userPw") String userPw,
			HttpServletRequest request) {
		
		try {
			if(!memberService.isValidMember(userId, userPw)) {
				throw new SooException("비밀번호가 일치하지 않습니다.", "WRONG_PW");
			}
		}
		catch (NullPointerException e) {
			throw new SooException("가입된 이메일 주소가 아닙니다.", "NULL_ID");
		}
		
		HttpSession session = request.getSession();
		
		String uid = memberService.getUid(userId);
		
		session.setAttribute("signedIn", true);
		session.setAttribute("uid", uid);
		session.setAttribute("userId", userId);
		
		Map result = new HashMap();
		result.put("result", "ok");
		
		return result;
	}
	
	//비밀번호 찾기//
	@RequestMapping(value="/api2/member/findpw", method=RequestMethod.POST)
	@ResponseBody
	public Map findpw(@RequestParam("userId") String userId) {
		
		if(memberService.countUserId(userId) != 0) {
			throw new SooException("가입된 이메일 주소가 아닙니다.", "NULL_ID");
		}
		else {
			Map result = new HashMap();
			result.put("result", "ok");
			
			return result;
		}	
	}
	
	//로그인상태//
	@RequestMapping("/api2/member/signedin")
	@ResponseBody
	public Map signedin(HttpSession session) {
		Map result = new HashMap();
		String signedIn = "no";		
		
		if (session.getAttribute("signedIn") != null &&
				(Boolean) session.getAttribute("signedIn")) {
			
			signedIn = "yes";
			
			result.put("userId", session.getAttribute("userId"));
		}
		
		result.put("result", signedIn);
		
		return result;
	}
	
	//업데이트//
	@RequestMapping(value="/api2/member/update", method=RequestMethod.POST)
	@ResponseBody
	public Map update(@RequestParam("userPw") String userPw,
			HttpSession session) {
		
		String uid = (String) session.getAttribute("uid");
		
		if (StringUtils.isBlank(uid)) {
			throw new RuntimeException("로그인이 필요합니다.");
		}
		
		memberService.modifyMember(uid, userPw);
		
		Map result = new HashMap();
		result.put("result", "ok");
		
		return result;
	}
	
	//로그아웃//
	@RequestMapping("/api2/member/signout")
	@ResponseBody
	public Map signout(HttpSession session) {
		
		session.invalidate();
		
		Map result = new HashMap();
		result.put("result", "ok");
		
		return result;
	}
}
