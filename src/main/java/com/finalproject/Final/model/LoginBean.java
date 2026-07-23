package com.finalproject.Final.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public class LoginBean {
	private Integer id;
	private String email;
	private String password;
	}