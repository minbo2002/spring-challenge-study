package com.example.springchallengeot.entity;

import com.example.springchallengeot.enums.Gender;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Person {

    private String name;

    private String phone;

    private Gender gender;

    @Builder
    public Person(String name, String phone, Gender gender) {

        if(!name.matches("^[a-zA-Z가-힣\\s]*$")) {
            throw new IllegalArgumentException("이름에 특수문자가 포함되어 있습니다.");
        }

        // 전화번호 010-XXXX-XXXX 형태외는 에러
        if (!phone.matches("010-\\d{4}-\\d{4}")) {
            throw new IllegalArgumentException("전화번호 형식이 올바르지 않습니다.");
        }

        this.name = name;
        this.phone = phone;
        this.gender = gender;
    }

    /*
       equals()와 hashCode()는 동일성 비교를 위해 재정의한 메서드인데
       @EqualsAndHashCode 어노테이션을 사용하면 자동으로 재정의된다.
    */
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj==null || getClass()!= obj.getClass()) return false;
        Person person = (Person) obj;
        return Objects.equals(name, person.name) &&
                Objects.equals(phone, person.phone) &&
                gender == person.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, gender);
    }
}
