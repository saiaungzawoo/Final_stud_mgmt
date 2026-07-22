//package com.finalproject.Final.config;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import com.finalproject.Final.model.UserBean;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
//
//@Component
//public class RoleInterceptor implements HandlerInterceptor {
//
//
//	
//    @Override
//    public boolean preHandle(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            Object handler
//    ) throws Exception {
//
//    	//test
//    	System.out.println("INTERCEPTOR RUNNING");
//
//        HttpSession session = request.getSession(false);
//
//
//        // Check login
//        if(session == null ||
//           session.getAttribute("loginUser") == null) {
//
//
//            response.sendRedirect("/login");
//
//            return false;
//        }
//
//
//
//        UserBean user =
//                (UserBean) session.getAttribute("loginUser");
//
//
//
//        String role =
//                user.getRoleName().trim();
//       
//               
//
//
//
//        String url =
//                request.getRequestURI();
//        
//        //test
//        System.out.println("URL = " + url);
//        System.out.println("ROLE = [" + role + "]");
//        System.out.println(role.length());
//
//
//
//        /*
//         * ADMIN AREA
//         */
//        if(url.startsWith("/admin")) {
//
//
//            if(!role.equals("Admin")) {
//
//
//            	System.out.println("BEFORE REDIRECT");
//                redirectByRole(
//                        role,
//                        response
//                );
//                System.out.println("AFTER REDIRECT");
//
//
//                return false;
//            }
//
//        }
//
//
//
//        /*
//         * TEACHER AREA
//         */
//        if(url.startsWith("/dashboard") ||
//           url.startsWith("/attendance") ||
//           url.startsWith("/announce")) {
//
//
//
//            if(!role.equals("Teacher")) {
//
//
//                redirectByRole(
//                        role,
//                        response
//                );
//
//
//                return false;
//            }
//
//        }
//
//
//
//        /*
//         * STUDENT AREA
//         */
//        if(url.startsWith("/student")) {
//
//
//            if(!role.equals("Student")) {
//
//
//                redirectByRole(
//                        role,
//                        response
//                );
//
//
//                return false;
//            }
//
//        }
//
//
//
//        return true;
//
//    }
//
//
//
//
//
//    private void redirectByRole(
//            String role,
//            HttpServletResponse response
//    ) throws Exception {
//
//
//    	  System.out.println("Redirecting role = " + role);
//
//
//        if("Admin".equalsIgnoreCase(role.trim())) {
//
//
//            System.out.println("GOING TO ADMIN");
//
//            response.sendRedirect(
//                    "/admin/dashboard"
//            );
//
//
//        } else if ("Teacher".equalsIgnoreCase(role.trim())) {
//
//
//            System.out.println("GOING TO TEACHER");
//
//            response.sendRedirect(
//                    "/dashboard/dashboard-teacher"
//            );
//
//
//        } else if ("Student".equalsIgnoreCase(role.trim())){
//
//
//            System.out.println("GOING TO PORTAL");
//            
//
//            response.sendRedirect(
//                    "/student/portal"
//            );
//
//        }
//
//    }
//
//}