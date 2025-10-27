package com.example.blog2.service;

import com.example.blog2.po.PersonInfo;

import java.util.List;

public interface PersonInfoService {
    List<PersonInfo> listPersonInfo();

    void deletePersonInfo(Long id);

    PersonInfo savePersonInfo(PersonInfo PersonInfo);

    PersonInfo updatePersonInfo(Long id,PersonInfo PersonInfo);
}
