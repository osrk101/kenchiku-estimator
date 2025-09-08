package com.kenchiku_estimator;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BcryptGen {
  public static void main(String[] args) {
    args = new String[] {"password"}; // for test
    if (args.length == 0) {
      System.out.println("usage: BcryptGen <plain>");
      return;
    }
    PasswordEncoder pe = new BCryptPasswordEncoder(); // cost=10
    System.out.println(pe.encode(args[0]));
  }
}
