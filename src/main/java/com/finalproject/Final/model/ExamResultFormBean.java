package com.finalproject.Final.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamResultFormBean {

    private String examID;

    private List<ExamResultBean> results;

}