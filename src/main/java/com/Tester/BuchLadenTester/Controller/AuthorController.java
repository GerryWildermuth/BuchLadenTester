package com.Tester.BuchLadenTester.Controller;

import com.Tester.BuchLadenTester.Repository.BookRepository;
import com.Tester.BuchLadenTester.Model.Author;
import com.Tester.BuchLadenTester.Repository.AuthorRepository;
import com.Tester.BuchLadenTester.Service.AuthorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import java.util.Optional;

import static com.Tester.BuchLadenTester.BuchLadenTesterApplication.logger;

@Controller()
@RequestMapping("/authors")
public class AuthorController {

    final
    BookRepository bookRepository;

    final
    AuthorRepository authorRepository;

    AuthorService authorService;

    public AuthorController(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @GetMapping()
    public ModelAndView AuthorOverview() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("authors",authorRepository.findAll());
        modelAndView.setViewName("authors");

        return modelAndView;
    }

    @PostMapping()
    public ModelAndView CreateAuthor(@Valid Author author, BindingResult bindingResult, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView();
        // Check for the validations
        if(bindingResult.hasErrors()) {
            modelAndView.addObject("successMessage", "Please correct the errors in form!");
            logger.info("Please correct the errors in form!");
            modelMap.addAttribute("bindingResult", bindingResult);
        }
        else if(authorService.isAuthorAlreadyPresent(author)){
            modelAndView.addObject("successMessage", "author already exists!");
            logger.info("author already exists!");
        }
        // we will save the book if, no binding errors
        else {
            authorRepository.save(author);
            modelAndView.addObject("successMessage", "author is registered successfully!");
            logger.info("author is registered successfully!");
        }
        modelAndView.addObject("author", new Author());
        modelAndView.setViewName("author");
        return modelAndView;
    }
    @DeleteMapping()
    public ModelAndView DeleteAuthor(@Valid int AuthorId, BindingResult bindingResult, ModelMap modelMap){
        ModelAndView modelAndView = new ModelAndView();
        Optional<Author> author = authorRepository.findById(AuthorId);
        if(bindingResult.hasErrors()) {
            modelAndView.addObject("successMessage", "Please correct the errors in form!");
            logger.info("Please correct the errors in form!");
            modelMap.addAttribute("bindingResult", bindingResult);
        }
        else if(author==null){
            modelAndView.addObject("successMessage", "There is no Author with this AuthorId");
            logger.info("There is no Author with this AuthorId");
        }
        else {
            authorRepository.deleteById(AuthorId);
            modelAndView.addObject("successMessage", "Book with AuthorId"+AuthorId+" got removed!");
            logger.info("Book with AuthorId"+AuthorId+" got removed!");
        }
        modelAndView.addObject("","");
        modelAndView.setViewName("author");
        return modelAndView;
    }

}
