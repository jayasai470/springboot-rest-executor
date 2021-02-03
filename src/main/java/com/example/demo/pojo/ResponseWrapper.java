package com.example.demo.pojo;

import lombok.Data;

import java.util.List;

@Data
public class ResponseWrapper<T> {

    private List<T> data;

}
