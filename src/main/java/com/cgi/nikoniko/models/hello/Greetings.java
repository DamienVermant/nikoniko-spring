package com.cgi.nikoniko.models.hello;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="demo")
public class Greetings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonInclude
	@JsonProperty(value="id")
    private Long id = null;
    
    @JsonProperty(value = "content")
    private String content;

    public Greetings(Long id, String content) {
        this.id = id;
        this.content = content;
    }
    
	public Greetings(String content) {
		super();
		this.content = content;
	}

	public Greetings() {
		super();
	}

	public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}

