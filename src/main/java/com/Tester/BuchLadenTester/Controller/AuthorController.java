package com.Tester.BuchLadenTester.Controller;

import com.Tester.BuchLadenTester.Repository.BookRepository;
import com.Tester.BuchLadenTester.Model.Author;
import com.Tester.BuchLadenTester.Repository.AuthorRepository;
import com.Tester.BuchLadenTester.Service.AuthorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.Calendar;
import java.util.Optional;

import static com.Tester.BuchLadenTester.BuchLadenTesterApplication.logger;

@Controller()
@RequestMapping("/authors")
public class AuthorController {

    final
    BookRepository bookRepository;

    final
    AuthorRepository authorRepository;

    final
    AuthorService authorService;

    //Init of all services and repositories
    public AuthorController(BookRepository bookRepository, AuthorRepository authorRepository, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.authorService = authorService;
    }

    @GetMapping()
    public ModelAndView AuthorOverview() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("authors",authorRepository.findAll());
        modelAndView.setViewName("authors");

        return modelAndView;
    }

    @GetMapping(value = "/newAuthor")
    public ModelAndView CreateBook() {
        ModelAndView modelAndView = new ModelAndView();
        Author author = new Author();
        Calendar calendar = Calendar.getInstance();
        java.sql.Date sqlDate = new java.sql.Date(calendar.getTime().getTime());

        author.setBirthDate(sqlDate);
        modelAndView.addObject("author", author);
        modelAndView.setViewName("newAuthor");
        return modelAndView;
    }

    @PostMapping(value="/newAuthor")
    public ModelAndView CreateAuthor(@Valid Author author, BindingResult bindingResult, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView();
        //Setting correct full name for the author
        author.setName(author.getFirstName()+" "+author.getLastName());
        if(bindingResult.hasErrors()) {
            modelAndView.addObject("successMessage", "Please correct the errors in form!");
            logger.info("Please correct the errors in form!");
            modelMap.addAttribute("bindingResult", bindingResult);
        }
        else if(authorService.isAuthorWithNameAlreadyPresent(author)){
            modelAndView.addObject("successMessage", "author with the name: "+author.getName()+" already exists!");
            logger.info("author with this this name: "+author.getName()+" already exists!");
        }
        else {
            authorService.saveAuthor(author);
            modelAndView.addObject("successMessage", "author is registered successfully!");
            logger.info("author is registered successfully!");
        }
        modelAndView.addObject("authors",authorRepository.findAll());
        modelAndView.setViewName("authors");
        return modelAndView;
    }

    @PostMapping(value = "/deleteAuthor")
    public ModelAndView DeleteAuthor(@RequestParam int authorId, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        Optional<Author> author = authorRepository.findById(authorId);
        if(!author.isPresent()){
            modelAndView.addObject("successMessage", "There is no Author with this authorId");
            logger.info("There is no Author with this authorId");
        }
        else {
            authorRepository.deleteById(authorId);
            modelAndView.addObject("successMessage", "Book with authorId"+authorId+" got removed!");
            logger.info("Book with authorId"+authorId+" got removed!");
        }
        modelAndView.addObject("authors",authorRepository.findAll());
        modelAndView.setViewName("authors");
        return modelAndView;
    }
}