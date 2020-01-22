package com.Tester.BuchLadenTester.Controller;

import com.Tester.BuchLadenTester.Model.Author;
import com.Tester.BuchLadenTester.Model.Role;
import com.Tester.BuchLadenTester.Model.Shoppingcart;
import com.Tester.BuchLadenTester.Repository.AuthorRepository;
import com.Tester.BuchLadenTester.Repository.BookRepository;
import com.Tester.BuchLadenTester.Repository.ShoppingcartRepository;
import com.Tester.BuchLadenTester.Service.BookServiceImp;
import com.Tester.BuchLadenTester.Model.Book;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

import static com.Tester.BuchLadenTester.BuchLadenTesterApplication.getPreviousPageByRequest;
import static com.Tester.BuchLadenTester.BuchLadenTesterApplication.logger;

@Controller()
@RequestMapping("/books")
public class BookController {

    final
    BookRepository bookRepository;
    final
    AuthorRepository authorRepository;

    final
    ShoppingcartRepository shoppingcartRepository;

    BookServiceImp bookService;
    private Map<String, Author> authorCache;


    public BookController(BookRepository bookRepository, AuthorRepository authorRepository, ShoppingcartRepository shoppingcartRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.shoppingcartRepository = shoppingcartRepository;
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
        List<Author> authorList = authorRepository.findAll();
        authorCache = new HashMap<String, Author>();
        for (Author author : authorList) {
            authorCache.put(author.getIdAsString(), author);
        }
        Calendar calendar = Calendar.getInstance();
        java.sql.Date sqlDate = new java.sql.Date(calendar.getTime().getTime());
        book.setPublishedDate(sqlDate);
        modelAndView.addObject("book", book);
        modelAndView.addObject("bookAuthors",authorList);
        modelAndView.setViewName("newBook");
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
            Set<Author> bookAuthors = book.getBookAuthors();
            for(Author author : bookAuthors)
            {
                author.addBookAuthors(book);
                authorRepository.save(author);
            }
            modelAndView.addObject("successMessage", "Book is registered successfully!");
            logger.info("Book is registered successfully!");
        }
        modelAndView.addObject("book", book);
        List<Author> authorList = authorRepository.findAll();
        modelAndView.addObject("bookAuthors",authorList);
        modelAndView.setViewName("newBook");
        return modelAndView;
    }

    @PostMapping(value = "/deleteBook")
    public String DeleteBook(@RequestParam int bookId, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        Book book = bookRepository.getOne(bookId);
        if(book==null){
            modelAndView.addObject("successMessage", "There is no Book with this bookId");
            logger.info("There is no Book with this bookId");
        }
        else {
            for (Shoppingcart shoppingcart : shoppingcartRepository.findAll()) {
                shoppingcart.getBooks().remove(book);
            }
            bookRepository.deleteById(bookId);
            modelAndView.addObject("successMessage", "Book with bookId"+bookId+" got removed!");
            logger.info("Book with bookId"+bookId+" got removed!");
        }
        return getPreviousPageByRequest(request).orElse("/");
    }
    private Map<Integer, String> getAuthorStrings() {
        //List<String> authorStrings = new ArrayList<>();
        Map<Integer,String> authorStringIntegerMap = new HashMap<>();
        for (Author author :  authorRepository.findAll()) {
            //authorStrings.add(author.getFirstName() + " " + author.getLastName());
            authorStringIntegerMap.put(author.getAuthor_id(),author.getName());
        }
        return authorStringIntegerMap;
    }

   @InitBinder
    protected void initBinder(WebDataBinder binder) throws Exception {
        binder.registerCustomEditor(Set.class, "bookAuthors", new CustomCollectionEditor(Set.class) {
            protected Object convertElement(Object element) {
                if (element instanceof Author) {
                    System.out.println("Converting from Author to Author: " + element);
                    return element;
                }
                if (element instanceof String) {
                    Author author = authorCache.get(element);
                       System.out.println("Looking up author for id " + element + ": " + author);
                       return author;
                }
                System.out.println("Don't know what to do with: " + element);
                return null;
            }
        });
    }
}
