//package com.cgi.nikoniko.controllers.demo;
//
//import java.io.IOException;
//import java.util.List;
//
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.erwan.nikonikospring.dao.IGreetingCrudRepository;
//import com.erwan.nikonikospring.models.hello.Greeting;
//
//@RestController
//public class RestGreetingController {
//
//    @Autowired
//    private IGreetingCrudRepository repository;
//
//    @RequestMapping(path = "/demo/greeting", method = RequestMethod.GET)
//    public List<Greeting> listAction(
//    		HttpServletResponse response,
//    		@RequestParam int offset,
//    		@RequestParam int limit) throws IOException{
//    	if (limit<=0) {
//			response.sendError(HttpStatus.BAD_REQUEST.value(),"Invalid limit");
//			return null;
//		}
//    	Pageable pageable = new PageRequest(offset/limit, limit);
//
//    	return this.repository.findAll(pageable);
//    }
//
//    @RequestMapping(path = "/demo/greeting", method = RequestMethod.POST)
//    public Greeting createAction(@RequestParam String content,
//    		HttpServletResponse response) throws IOException{
//
//        return newOrEdit(response, content, new Greeting());
//    }
//
//    @RequestMapping(path = "/demo/greeting/{id}", method = RequestMethod.DELETE)
//    public void deleteAction(@PathVariable Long id,
//    		HttpServletResponse response) throws IOException {
//    	if (this.repository.exists(id)) {
//    		this.repository.delete(id);
//			response.setStatus(HttpStatus.NO_CONTENT.value());
//		} else {
//			response.setStatus(HttpStatus.NOT_FOUND.value());
//		}
//    }
//
//    @RequestMapping(path = "/demo/greeting/{id}/edit", method = RequestMethod.PUT)
//    public Greeting editAction(
//    		@PathVariable("id") Long id,
//    		@RequestParam(value = "content", required = true) String content,
//    		HttpServletResponse response) throws IOException{
//
//        return newOrEdit(response, content, this.repository.findOne(id));
//    }
//
//    @RequestMapping(path = "/demo/greeting/{id}/show", method = RequestMethod.GET)
//    public Greeting showAction(
//    		@PathVariable("id") Long id,
//    		HttpServletResponse response){
//    	Greeting greeting = this.repository.findOne(id);
//    	if (greeting == null) {
//			response.setStatus(HttpStatus.NOT_FOUND.value());//renvoie une erreur 404
//		}
//		return greeting;
//    }
//
//    private Greeting newOrEdit(	HttpServletResponse response,
//					    		String content,
//					    		Greeting greeting) throws IOException{
//
//    	if (content.equals("")) {
//			response.sendError(HttpStatus.BAD_REQUEST.value(),"Required name");
//			return null;
//		}
//    	if (!content.toLowerCase().contains("hello")) {
//			content = "Hello " + content + "!";
//		}
//    	greeting.setContent(content);
//    	if (greeting.getId() == null) {
//			response.setStatus(HttpStatus.CREATED.value());
//		}
//    	this.repository.save(greeting);
//
//        return greeting;
//    }
//
//    @RequestMapping(path = "/demo/greeting/addbourrin", method = RequestMethod.GET)
//    public Greeting addGreeting(
//    			@RequestParam(value = "name", defaultValue = "world") String name) {
//
//    	Greeting greeting = new Greeting(String.format("Hello %s!", name));
//
//    	this.repository.save(greeting);
//
//        return greeting;
//    }
//
//}