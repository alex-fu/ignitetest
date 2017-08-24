package com.github.fyfhust.model;

import lombok.Data;
import lombok.Setter;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

@Data
public class Student {
    @QuerySqlField(index=true)
    String id;
    @QuerySqlField
    String name;
    @QuerySqlField
    Integer grade;
    @QuerySqlField
    String address;
}
