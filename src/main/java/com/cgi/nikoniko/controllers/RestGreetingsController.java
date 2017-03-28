package com.cgi.nikoniko.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import javassist.tools.web.BadHttpRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cgi.nikoniko.dao.GreetingsCrudRepository;
import com.cgi.nikoniko.models.hello.Greetings;

@RestController
public class RestGreetingsController {
	
	@Autowired
	private GreetingsCrudRepository repository;
	
	// Rest actions.

    @RequestMapping(path="/demo/greeting", method=RequestMethod.GET)
    public Iterable<Greetings> listAction() {
        return this.repository.findAll();
    }

    @RequestMapping(path="/demo/greeting", method=RequestMethod.POST)
    public Greetings createAction(
            HttpServletResponse response,
            @RequestParam String content) throws BadHttpRequest, IOException {
        if (content.equals(null) || content.equals("")) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Pas Content");
        }

        if (!content.toLowerCase().contains("hello")) {
            content = "Hello " + content;
        }

        response.setStatus(201);
        Greetings greeting = new Greetings(content);

        this.repository.save(greeting);

        return greeting;
    }

    // Delete, create, edit, show

    // Tools.

    @RequestMapping(path="/demo/greeting/add", method=RequestMethod.GET)
    public Greetings addGreeting(
            @RequestParam(value="name", defaultValue="world")
            String name) {
        Greetings greeting = new Greetings(
                String.format("Hello %s!", name));

        this.repository.save(greeting);

        return greeting;
    }
    
    // "?name="value", va récupérer par la valeur de name grâce au @RequestParam
    
    @RequestMapping(path="/demo/greeting/{id}/edit", method=RequestMethod.PUT)
    public Greetings editAction(@PathVariable("id") Long id) {
        
    	Greetings greetings = this.repository.findOne(id);
    	System.out.println(greetings);

        return null;
    }
    
    private Greetings newOrEdit(HttpServletResponse response, String content, Greetings greeting) throws IOException{
    	
    	if (content.equals("")) {
			response.sendError(HttpStatus.BAD_REQUEST.value(), "Required");
			return null;
		}
    	
    	if (!content.toLowerCase().contains("hello")) {
			content = "Hello " + content;
		}
    	    	
    	response.setStatus(HttpStatus.CREATED.value());
    	
    	return null;
    		
    }

	

}
