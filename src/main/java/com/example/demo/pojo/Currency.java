package com.example.demo.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {

    private String id;
    private String name;
    @JsonProperty("min_size")
    private String minSize;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}
