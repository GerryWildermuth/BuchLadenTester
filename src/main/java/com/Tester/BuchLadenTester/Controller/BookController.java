package com.Tester.BuchLadenTester.Controller;

import com.Tester.BuchLadenTester.Repository.AuthorRepository;
import com.Tester.BuchLadenTester.Repository.BookRepository;
import com.Tester.BuchLadenTester.Service.BookServiceImp;
import com.Tester.BuchLadenTester.Model.Book;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Calendar;
import java.util.Optional;

import static com.Tester.BuchLadenTester.BuchLadenTesterApplication.logger;

@Controller()
@RequestMapping("/books")
public class BookController {

    final
    BookRepository bookRepository;
    final
    AuthorRepository authorRepository;

    BookServiceImp bookService;

    public BookController(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @GetMapping()
    public ModelAndView BookOverview(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("books",bookRepository.findAll());
        modelAndView.setViewName("books");

        return modelAndView;
    }

    @GetMapping(value = "/newBook")
    public ModelAndView CreateBook() {
        ModelAndView modelAndView = new ModelAndView();
        Book book = new Book();
        Calendar calendar = Calendar.getInstance();
        java.sql.Date sqlDate = new java.sql.Date(calendar.getTime().getTime());

        book.setPublishedDate(sqlDate);
        modelAndView.addObject("book", book);
        modelAndView.addObject("authors",authorRepository.findAll());
        modelAndView.setViewName("newBook"); // resources/template/register.html
        return modelAndView;
    }

    @PostMapping(value="/newBook")
    public ModelAndView CreateBook(@Valid Book book, BindingResult bindingResult, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView();
        // Check for the validations
        if(bindingResult.hasErrors()) {
            modelAndView.addObject("successMessage", "Please correct the errors in form!");
            logger.info("Please correct the errors in form!");
            modelMap.addAttribute("bindingResult", bindingResult);
        }
        else if(bookRepository.findByName(book.getName()).isPresent()){
        //else if(bookService.isBookAlreadyPresent(book)){
            modelAndView.addObject("successMessage", "book already exists!");
            logger.info("book already exists!");
        }
        // we will save the book if, no binding errors
        else {
            bookRepository.save(book);
            //bookService.saveBook(book);
            modelAndView.addObject("successMessage", "Book is registered successfully!");
            logger.info("Book is registered successfully!");
        }
        modelAndView.addObject("book", book);
        modelAndView.setViewName("newBook");
        return modelAndView;
    }

    @DeleteMapping(value = "/deleteBook")
    public ModelAndView DeleteBook(@Valid int BookId, BindingResult bindingResult, ModelMap modelMap){
        ModelAndView modelAndView = new ModelAndView();
        Optional<Book> book = bookRepository.findById(BookId);
        if(bindingResult.hasErrors()) {
            modelAndView.addObject("successMessage", "Please correct the errors in form!");
            logger.info("Please correct the errors in form!");
            modelMap.addAttribute("bindingResult", bindingResult);
        }
        else if(book==null){
            modelAndView.addObject("successMessage", "There is no Book with this BookId");
            logger.info("There is no Book with this BookId");
        }
        // we will save the book if, no binding errors
        else {
            bookRepository.deleteById(BookId);
            modelAndView.addObject("successMessage", "Book with BookId"+BookId+" got removed!");
            logger.info("Book with BookId"+BookId+" got removed!");
        }
        modelAndView.setViewName("books");
        return modelAndView;
    }
/*
    @InitBinder
    protected void initBinder(WebDataBinder binder) throws Exception {
        binder.registerCustomEditor(Set.class, "authors", new CustomCollectionEditor(Set.class) {
            protected Object convertElement(Object element) {
                if (element instanceof Author) {
                    System.out.println("Converting from Author to Author: " + element);
                    return element;
                }
                if (element instanceof Integer) {
                    Author author = authorRepository.findOne((Integer) element);
                    System.out.println("Looking up author for id " + element + ": " + author);
                    return author;
                }
                System.out.println("Don't know what to do with: " + element);
                return null;
            }
        });
    }*/
}
