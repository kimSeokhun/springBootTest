package com.flexink.config.web.security.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.flexink.domain.sec.LoginUserDetails;
import com.flexink.security.SessionUserDetail;

import lombok.extern.slf4j.Slf4j;

/**************************************************************
 * @FileName	: UserDetailsHelper.java
 * @Project		: fxBoot
 * @Package_Name: com.flexink.config.web.security.user
 * @Date		: 2017. 3. 23. 
 * @작성자		: KIMSEOKHOON
 * @변경이력		:
 * @프로그램 설명 	: Security 인증정보 Helper
 **************************************************************/
@Slf4j
public class UserDetailsHelper {

	/*public static LoginUserDetails getLoginUserDetails() {
		Object obj = getAuthenticatedUser();
		if(obj == null) {
			return new LoginUserDetails();
		} else if (obj instanceof LoginUserDetails) {
			return (LoginUserDetails) obj;
		} else {
			return new LoginUserDetails();
		}
	}*/
	public static SessionUserDetail getLoginUserDetails() {
		Object obj = getAuthenticatedUser();
		if(obj == null) {
			return null;
		} else if (obj instanceof LoginUserDetails) {
			return (SessionUserDetail) obj;
		} else {
			return null;
		}
	}
    /********************************************************************
     * @메소드명	: getAuthenticatedUser
     * @작성자	: KIMSEOKHOON
     * @메소드 내용	: 인증된 사용자객체를 VO형식으로 가져온다.
     * @return	: 사용자 ValueObject
     ********************************************************************/
    public static Object getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication == null) {
        	log.debug("## authentication object is null!!");
            return null;
        }

        /*if (authentication.getPrincipal() instanceof LoginUserDetails) {
        	LoginUserDetails details = (LoginUserDetails) authentication.getPrincipal();


        	log.debug("## LoginUserDetailsHelper.getAuthenticatedUser : AuthenticatedUser is {}", details.getUsername());

	        return details;
        } else {
        	return authentication.getPrincipal();
        }*/
        if (authentication.getPrincipal() instanceof SessionUserDetail) {
        	SessionUserDetail details = (SessionUserDetail) authentication.getPrincipal();


        	log.debug("## LoginUserDetailsHelper.getAuthenticatedUser : AuthenticatedUser is {}", details.getUsername());

	        return details;
        } else {
        	return authentication.getPrincipal();
        }
    }

    /********************************************************************
     * @메소드명	: getAuthorities
     * @작성자	: KIMSEOKHOON
     * @메소드 내용	: 인증된 사용자의 권한 정보 반환
     * @return	: 사용자 권한정보 목록
     * 예) [ROLE_ADMIN, ROLE_USER,
     * ROLE_A, ROLE_B, ROLE_RESTRICTED,
     * IS_AUTHENTICATED_FULLY,
     * IS_AUTHENTICATED_REMEMBERED,
     * IS_AUTHENTICATED_ANONYMOUSLY]
     ********************************************************************/
    public static List<String> getAuthorities() {
        List<String> listAuth = new ArrayList<String>();

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication == null) {
        	log.debug("## authentication object is null!!");
            return null;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        Iterator<? extends GrantedAuthority> iter = authorities.iterator();

        while(iter.hasNext()) {
        	GrantedAuthority auth = iter.next();
        	listAuth.add(auth.getAuthority());

        	log.debug("## LoginUserDetailsHelper.getAuthorities : Authority is {}", auth.getAuthority());

        }

        return listAuth;
    }
    
    /********************************************************************
     * @메소드명	: containsAuthority
     * @작성자	: KIMSEOKHOON
     * @메소드 내용	: 입력한 권한을 가지고 있는지 확인
     ********************************************************************/
    public static boolean containsAuthority(String role) {
        List<String> listAuth = new ArrayList<String>();

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication == null) {
        	log.debug("## authentication object is null!!");
            return Boolean.FALSE;
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        while(iter.hasNext()) {
        	GrantedAuthority auth = iter.next();
        	listAuth.add(auth.getAuthority());

        	log.debug("## LoginUserDetailsHelper.getAuthorities : Authority is {}", auth.getAuthority());
        }
        return listAuth.contains(role);
    }
    
    /********************************************************************
     * @메소드명	: getRoleType
     * @작성자	: KIMSEOKHOON
     * @메소드 내용	: 최상위 Role 타입 반환
     ********************************************************************/
    public static String getRoleType() {
    	SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication == null) {
        	log.debug("## authentication object is null!!");
            return null;
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        Object[] arr = authorities.toArray();
        GrantedAuthority auth = (GrantedAuthority) arr[0];
        
        /*while(iter.hasNext()) {
        	GrantedAuthority auth = iter.next();
        	return auth.getAuthority();
        }*/
		return auth.getAuthority();
    }

    /********************************************************************
     * @메소드명	: isAuthenticated
     * @작성자	: KIMSEOKHOON
     * @메소드 내용	: 인증된 사용자 여부
     ********************************************************************/
    public static Boolean isAuthenticated() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication == null) {
        	log.debug("## authentication object is null!!");
            return Boolean.FALSE;
        }

        String username = authentication.getName();
        if (username.equals("anonymousUser")) {
        	log.debug("## username is {}", username);
            return Boolean.FALSE;
        }

        Object principal = authentication.getPrincipal();

        return (Boolean.valueOf(principal != null));
    }

    public static String getEncodedPassword(String password) {
    	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String hashed = encoder.encode(password);

		return hashed;
    }
}
