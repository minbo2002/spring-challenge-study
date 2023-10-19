package com.example.springchallengeot.controller;

import com.example.springchallengeot.entity.Person;
import com.example.springchallengeot.entity.PhoneBook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.springchallengeot.enums.Gender.FEMALE;
import static com.example.springchallengeot.enums.Gender.MALE;

@Slf4j
@RestController
@RequestMapping("/api")
public class TestController {

    /*
        전화번호부를 일단 미리 초기화
        전화번호부 내부 사람들 --> name, phone, gender
        name에는 특수문자가 들어가면 안됨. phone은 010-xxxx-xxxx 형식. 똑같은 사람은 추가할수 없음.
     */

    private static final PhoneBook phoneBook = new PhoneBook(new ArrayList<>());

    // 초기화
    @GetMapping("/reset")
    public PhoneBook reset() {

        Person person1 = Person.builder()
                .name("AAA")
                .phone("010-1111-1111")
                .gender(FEMALE)
                .build();

        Person person2 = Person.builder()
                .name("BBB")
                .phone("010-2222-2222")
                .gender(MALE)
                .build();

        Person person3 = Person.builder()
                .name("CCC")
                .phone("010-3333-3333")
                .gender(MALE)
                .build();

        Person person4 = Person.builder()
                .name("DDD")
                .phone("010-4444-4444")
                .gender(MALE)
                .build();

        // Set<Person> 안에 기존에 있는 person1,2,3,4에 추가
        phoneBook.getPersonList().addAll(List.of(person1, person2, person3, person4));

        return phoneBook;
    }

    // 전화번호부 조회
    @GetMapping("/phoneBook")
    public PhoneBook getPhoneBook() {

        List<Person> personList = phoneBook.getPersonList().stream()
                .sorted(Comparator.comparing(Person::getName))
                .filter(person -> person.getGender() == MALE)
                .collect(Collectors.toList());

        log.info("personList : {}", personList);

        return new PhoneBook(personList);
    }

    // 전화번호부에 사람추가
    @PostMapping("/person")
    public PhoneBook createPerson(@RequestBody Person person) {

        if(phoneBook.getPersonList().contains(person)) {
            throw new IllegalArgumentException("이미 존재하는 사람입니다.");
        }else {
            log.info("person : {}", person);
            phoneBook.getPersonList().add(person);
        }

        return phoneBook;
    }
}
