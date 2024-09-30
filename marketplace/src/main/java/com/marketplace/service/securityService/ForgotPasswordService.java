package com.marketplace.service.securityService;

import com.marketplace.model.User;
import com.marketplace.repository.UserRepository;
import com.marketplace.service.email.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();

    public boolean sendOtpToEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String otp = generateOtp();
            otpStore.put(email, otp);
            emailService.sendOtpEmail(email, otp);
            return true;
        }
        return false;
    }

    public boolean verifyOtp(String email, String otp) {
        String storedOtp = otpStore.get(email);
        return storedOtp != null && storedOtp.equals(otp);
    }

    public boolean resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null && otpStore.containsKey(email)) {
            user.setPassword(encodePassword(newPassword));
            userRepository.save(user);
            otpStore.remove(email); // Remove the OTP after successful password reset
            return true;
        }
        return false;
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 10000 + random.nextInt(90000);
        return String.valueOf(otp);
    }

    private String encodePassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }



}
