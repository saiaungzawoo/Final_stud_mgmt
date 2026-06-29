package com.finalproject.Final.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    // GET USER BY ID
    public UserBean findById(int id) {
        return userRepo.findById(id);
    }

    // OPTIONAL: teacher only
    public UserBean findTeacherById(int id) {
        return userRepo.findTeacherById(id);
    }
}