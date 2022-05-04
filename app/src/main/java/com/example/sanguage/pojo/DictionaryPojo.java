package com.example.sanguage.pojo;


import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "vocabulary",
        "grammaticalCategory",
        "collocations",
        "topic",
        "vocabularyTranslated",
        "grammaticalCategoryTranslated",
        "definitionTranslated",
        "collocationsTranslated",
        "topicTranslated",
        "language",
        "level"
})
public class DictionaryPojo {

    @JsonProperty("vocabulary")
    private String vocabulary;
    @JsonProperty("grammaticalCategory")
    private String grammaticalCategory;
    @JsonProperty("collocations")
    private String collocations;
    @JsonProperty("topic")
    private String topic;
    @JsonProperty("vocabularyTranslated")
    private String vocabularyTranslated;
    @JsonProperty("grammaticalCategoryTranslated")
    private String grammaticalCategoryTranslated;
    @JsonProperty("definitionTranslated")
    private String definitionTranslated;
    @JsonProperty("collocationsTranslated")
    private String collocationsTranslated;
    @JsonProperty("topicTranslated")
    private String topicTranslated;
    @JsonProperty("language")
    private String language;
    @JsonProperty("level")
    private String level;

    public DictionaryPojo(String vocabulary) {
        this.vocabulary = vocabulary;
    }

    public DictionaryPojo() {
    }

    public DictionaryPojo(String vocabulary, String grammaticalCategory, String collocations, String topic, String vocabularyTranslated, String grammaticalCategoryTranslated, String definitionTranslated, String collocationsTranslated, String topicTranslated, String language, String level) {
        this.vocabulary = vocabulary;
        this.grammaticalCategory = grammaticalCategory;
        this.collocations = collocations;
        this.topic = topic;
        this.vocabularyTranslated = vocabularyTranslated;
        this.grammaticalCategoryTranslated = grammaticalCategoryTranslated;
        this.definitionTranslated = definitionTranslated;
        this.collocationsTranslated = collocationsTranslated;
        this.topicTranslated = topicTranslated;
        this.language = language;
        this.level = level;
    }

    @JsonProperty("vocabulary")
    public String getVocabulary() {
        return vocabulary;
    }

    @JsonProperty("vocabulary")
    public void setVocabulary(String vocabulary) {
        this.vocabulary = vocabulary;
    }

    @JsonProperty("grammaticalCategory")
    public String getGrammaticalCategory() {
        return grammaticalCategory;
    }

    @JsonProperty("grammaticalCategory")
    public void setGrammaticalCategory(String grammaticalCategory) {
        this.grammaticalCategory = grammaticalCategory;
    }

    @JsonProperty("collocations")
    public String getCollocations() {
        return collocations;
    }

    @JsonProperty("collocations")
    public void setCollocations(String collocations) {
        this.collocations = collocations;
    }

    @JsonProperty("topic")
    public String getTopic() {
        return topic;
    }

    @JsonProperty("topic")
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @JsonProperty("vocabularyTranslated")
    public String getVocabularyTranslated() {
        return vocabularyTranslated;
    }

    @JsonProperty("vocabularyTranslated")
    public void setVocabularyTranslated(String vocabularyTranslated) {
        this.vocabularyTranslated = vocabularyTranslated;
    }

    @JsonProperty("grammaticalCategoryTranslated")
    public String getGrammaticalCategoryTranslated() {
        return grammaticalCategoryTranslated;
    }

    @JsonProperty("grammaticalCategoryTranslated")
    public void setGrammaticalCategoryTranslated(String grammaticalCategoryTranslated) {
        this.grammaticalCategoryTranslated = grammaticalCategoryTranslated;
    }

    @JsonProperty("definitionTranslated")
    public String getDefinitionTranslated() {
        return definitionTranslated;
    }

    @JsonProperty("definitionTranslated")
    public void setDefinitionTranslated(String definitionTranslated) {
        this.definitionTranslated = definitionTranslated;
    }

    @JsonProperty("collocationsTranslated")
    public String getCollocationsTranslated() {
        return collocationsTranslated;
    }

    @JsonProperty("collocationsTranslated")
    public void setCollocationsTranslated(String collocationsTranslated) {
        this.collocationsTranslated = collocationsTranslated;
    }

    @JsonProperty("topicTranslated")
    public String getTopicTranslated() {
        return topicTranslated;
    }

    @JsonProperty("topicTranslated")
    public void setTopicTranslated(String topicTranslated) {
        this.topicTranslated = topicTranslated;
    }

    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonProperty("level")
    public String getLevel() {
        return level;
    }

    @JsonProperty("level")
    public void setLevel(String level) {
        this.level = level;
    }


}