//package com.cgi.nikoniko.models.hello;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//
//import com.fasterxml.jackson.annotation.JsonInclude;
////import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonProperty;
//
//@Entity
//@Table(name="demo_greeting")
//public class Greeting {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@JsonInclude
//	@JsonProperty(value = "id")
//    private Long id = null;
//
//	@JsonProperty(value = "my super content")
//    private String content;
//
//    public Greeting(String content) {
//        this.content = content;
//    }
//
//    public Greeting() {
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//	/**
//	 * @param content the content to set
//	 */
//	public void setContent(String content) {
//		this.content = content;
//	}
//
//}