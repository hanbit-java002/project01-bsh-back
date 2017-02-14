package com.hanbit.sp.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hanbit.sp.dao.MemberDAO;

@Service
public class MemberService {

	private static final String SECRET_KEY = "sooplace";
	private PasswordEncoder passwordEncoder = new StandardPasswordEncoder(SECRET_KEY);
	
	@Autowired
	private MemberDAO memberDAO;
	
	private String generateKey(String prefix) {
		String key = prefix + StringUtils.leftPad(
				String.valueOf(System.nanoTime()), 30, "0");
		
		key += StringUtils.leftPad(
				String.valueOf((int) (Math.random() * 1000) % 100), 2, "0");
		
		return key;
	}
	
	public String addMember(String userId, String userPw) {
		String uid = generateKey("UID");
		String encryptedUserPw = passwordEncoder.encode(userPw); 
		
		memberDAO.insertMember(uid, userId, encryptedUserPw);
		
		return uid;
	}

	public boolean isValidMember(String userId, String userPw) {
		String encryptedUserPw = memberDAO.selectUserPw(userId);
		
		return passwordEncoder.matches(userPw, encryptedUserPw);
	}

	public void modifyMember(String uid, String userPw) {
		String encryptedUserPw = passwordEncoder.encode(userPw);
		
		memberDAO.updateMember(uid, encryptedUserPw);
	}

	public String getUid(String userId) {
		return memberDAO.selectUid(userId);
	}
	
}

