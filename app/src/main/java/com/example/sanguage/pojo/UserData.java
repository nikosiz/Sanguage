package com.example.sanguage.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.Set;
@JsonPropertyOrder({
        "userID",
        "username",
        "email",
        "registrationDate",
        "secondLanguage",
        "knownTranslations"
})
public class UserData implements Serializable {

    @JsonProperty("userID")
    private Long userID;
    @JsonProperty("username")
    private String username;
    @JsonProperty("email")
    private String email;
    @JsonProperty("registrationDate")
    private String registrationDate;
    @JsonProperty("secondLanguage")
    private Set<String> secondLanguage = null;
    @JsonProperty("knownTranslations")
    private KnownTranslations knownTranslations;
}
