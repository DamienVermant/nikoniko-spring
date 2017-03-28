package com.cgi.nikoniko.models.hello;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "demo_greeting")
public class Greetings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty(value = "id")
	private Long id = null;

	@JsonProperty(value = "my_content")
    private String content;

    public Greetings(String content) {
        this.content = content;
    }

    public Greetings() {
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
