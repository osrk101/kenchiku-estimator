package com.kenchiku_estimator.model;

import lombok.Data;

@Data
public class MAccount {

    private int id;

    private String username;

    private String password;

    private String role;

    private String fullname;
}
