package com.example.ToDo.Helper;

import com.example.ToDo.DTO.LoginRequest;

public class UserHelper {

    private final MyAnalyzer myAnalyzer;

    public UserHelper(MyAnalyzer myAnalyzer) {
        this.myAnalyzer = myAnalyzer;
    }

   public  boolean isValid(String email, String password, LoginRequest loginRequest) {
        String processedEmail = myAnalyzer.stem(email);
        if(processedEmail.equals(email) && password.equals(loginRequest.getPassword())) return true;
        return false;
    }
}
