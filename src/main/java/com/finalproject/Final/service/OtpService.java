package com.finalproject.Final.service;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
@Service
public class OtpService {

    private static final String ATTR_OTP = "otp";
    private static final String ATTR_TIMESTAMP = "otpTimestamp";
    private static final String ATTR_ATTEMPTS = "otpAttempts";
    private static final String ATTR_EMAIL = "otpEmail";
    private static final String ATTR_VERIFIED = "otpVerified";

    private static final long VALID_DURATION_MILLIS = 5 * 60 * 1000; // 5 minutes
    private static final int MAX_ATTEMPTS = 5;
    private static final int OTP_BOUND = 1_000_000; // 6-digit range: 000000–999999

    private final SecureRandom secureRandom = new SecureRandom();
    public String issueOtp(HttpSession session, String email) {
        String otp = String.format("%06d", secureRandom.nextInt(OTP_BOUND));

        session.setAttribute(ATTR_OTP, otp);
        session.setAttribute(ATTR_EMAIL, email);
        session.setAttribute(ATTR_TIMESTAMP, System.currentTimeMillis());
        session.setAttribute(ATTR_ATTEMPTS, 0);
        session.removeAttribute(ATTR_VERIFIED);

        return otp;
    }

    public enum VerificationResult {
        SUCCESS, EXPIRED, TOO_MANY_ATTEMPTS, INCORRECT
    }
    public VerificationResult verify(HttpSession session, String submittedOtp) {
        String storedOtp = (String) session.getAttribute(ATTR_OTP);
        Long issuedAt = (Long) session.getAttribute(ATTR_TIMESTAMP);
        int attempts = getAttempts(session);

        if (storedOtp == null || issuedAt == null) {
            return VerificationResult.EXPIRED;
        }

        if (isExpired(issuedAt)) {
            clearOtp(session);
            return VerificationResult.EXPIRED;
        }

        if (attempts >= MAX_ATTEMPTS) {
            clearOtp(session);
            return VerificationResult.TOO_MANY_ATTEMPTS;
        }

        if (storedOtp.equals(submittedOtp)) {
            clearOtp(session);
            session.setAttribute(ATTR_VERIFIED, true);
            return VerificationResult.SUCCESS;
        }

        session.setAttribute(ATTR_ATTEMPTS, attempts + 1);
        return VerificationResult.INCORRECT;
    }

    public boolean isVerified(HttpSession session) {
        Boolean verified = (Boolean) session.getAttribute(ATTR_VERIFIED);
        return Boolean.TRUE.equals(verified);
    }

    public String getVerifiedEmail(HttpSession session) {
        return (String) session.getAttribute(ATTR_EMAIL);
    }

    public void clearVerification(HttpSession session) {
        session.removeAttribute(ATTR_VERIFIED);
        session.removeAttribute(ATTR_EMAIL);
    }

    private boolean isExpired(long issuedAt) {
        return System.currentTimeMillis() - issuedAt > VALID_DURATION_MILLIS;
    }

    private int getAttempts(HttpSession session) {
        Integer attempts = (Integer) session.getAttribute(ATTR_ATTEMPTS);
        return attempts == null ? 0 : attempts;
    }

    private void clearOtp(HttpSession session) {
        session.removeAttribute(ATTR_OTP);
        session.removeAttribute(ATTR_TIMESTAMP);
        session.removeAttribute(ATTR_ATTEMPTS);
    }
}
