//package com.cgi.nikoniko.controllers.demo;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.cgi.nikoniko.dao.demo.GreetingCrudRepository;
//import com.cgi.nikoniko.models.hello.Greetings;
//
//@RestController
//public class RestGreetingController {
//
//	@Autowired
//	private GreetingCrudRepository repository;
//
//	@RequestMapping(path="demo/greeting", method=RequestMethod.GET)
//	public List<Greetings> listaction(){
//
//		return (List<Greetings>) this.repository.findAll();
//
//	}
//
//	@RequestMapping(path="demo/greeting", method=RequestMethod.POST)
//	public Greetings createAction(@RequestParam String content){
//		Greetings greeting = new Greetings(content);
//
//		this.repository.save(greeting);
//		return greeting;
//	}
//
//
//	@RequestMapping(path = "demo/greeting/addbourrin", method=RequestMethod.GET)
//	public Greetings greeting(@RequestParam(value="name", defaultValue="world") String name) {
//		Greetings greeting = new Greetings(String.format("Hello, %s !", name));
//
//		this.repository.save(greeting);
//
//		return greeting;
//	}
//}
