package com.finalproject.Final.config;


import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.finalproject.Final.model.UserBean;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Component
public class LoginInterceptor implements HandlerInterceptor {


	@Override
	public boolean preHandle(
	        HttpServletRequest request,
	        HttpServletResponse response,
	        Object handler
	) throws Exception {


	    HttpSession session = request.getSession(false);


	    UserBean user = null;


	    if(session != null) {

	        user = (UserBean) session.getAttribute("loginUser");

	    }



	    String uri = request.getRequestURI();



	    // =========================
	    // PUBLIC URLS
	    // =========================

	    if(uri.equals("/")
	            || uri.startsWith("/login")
	            || uri.startsWith("/css")
	            || uri.startsWith("/js")
	            || uri.startsWith("/images")
	            || uri.startsWith("/upload")
	            || uri.startsWith("/courses/show")
	            || uri.startsWith("/courses/")
	            || uri.startsWith("/scholarship/")
	            || uri.startsWith("/forgot-password")) {

	        return true;

	    }




	    // =========================
	    // LOGIN CHECK
	    // =========================

	    if(user == null) {

	        response.sendRedirect("/login");

	        return false;

	    }



	    String role = user.getRoleName();



	    // =========================
	    // ADMIN AREA
	    // =========================

	    if(uri.startsWith("/admin")) {


	        if("Admin".equals(role)) {

	            return true;

	        }


	        response.sendRedirect("/");

	        return false;

	    }




	    // =========================
	    // TEACHER AREA
	    // =========================

	    if(uri.startsWith("/dashboard")
	            || uri.startsWith("/announce")
	            || uri.startsWith("/attendance")
	            || uri.startsWith("/teacher")) {


	        if("Teacher".equals(role)) {

	            return true;

	        }


	        response.sendRedirect("/");

	        return false;

	    }





	    // =========================
	    // STUDENT AREA
	    // =========================

	    if(uri.startsWith("/student")
	            || uri.startsWith("/enrollment")
	            || uri.startsWith("/payment")) {


	        if("Student".equals(role)) {

	            return true;

	        }


	        response.sendRedirect("/");

	        return false;

	    }



	    return true;

	}

}