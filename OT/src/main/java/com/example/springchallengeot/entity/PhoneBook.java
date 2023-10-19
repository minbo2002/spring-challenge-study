package com.example.springchallengeot.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PhoneBook {

    private List<Person> personList;

    @Builder
    public PhoneBook(List<Person> personList) {
        this.personList = personList;
    }
}
