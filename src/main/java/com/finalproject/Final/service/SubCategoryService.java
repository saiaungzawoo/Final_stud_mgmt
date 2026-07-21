package com.finalproject.Final.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.Final.model.SubCategoryBean;
import com.finalproject.Final.repository.SubCategoryRepository;

@Service
	public class SubCategoryService {

	    @Autowired
	    private SubCategoryRepository repository;

	    public List<SubCategoryBean> getAll() {
	        return repository.findAll();
	    }

	}


