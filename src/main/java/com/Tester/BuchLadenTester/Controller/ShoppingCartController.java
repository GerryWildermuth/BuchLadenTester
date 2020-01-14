package com.Tester.BuchLadenTester.Controller;

import com.Tester.BuchLadenTester.Repository.BookRepository;
import com.Tester.BuchLadenTester.Repository.ShoppingcartRepository;
import com.Tester.BuchLadenTester.Model.Book;
import com.Tester.BuchLadenTester.Model.Shoppingcart;
import com.Tester.BuchLadenTester.Model.User;
import com.Tester.BuchLadenTester.Repository.UserRepository;
import com.Tester.BuchLadenTester.Service.ShoppingCartImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Set;

import static com.Tester.BuchLadenTester.BuchLadenTesterApplication.getPreviousPageByRequest;
import static com.Tester.BuchLadenTester.BuchLadenTesterApplication.logger;

@Controller()
@RequestMapping("/shoppingcart")
public class ShoppingCartController {

    final
    BookRepository bookRepository;

    final
    UserRepository userRepository;

    final
    ShoppingcartRepository shoppingcartRepository;

    @Autowired
    ShoppingCartImp shoppingCartService;

    public ShoppingCartController(BookRepository bookRepository, UserRepository userRepository, ShoppingcartRepository shoppingcartRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.shoppingcartRepository = shoppingcartRepository;
    }

    @GetMapping()
    public ModelAndView Shoppingcart(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        String name = principal.getName();
        User currentUser = userRepository.findByEmail(principal.getName());
        Set<Book> books = currentUser.getShoppingcart().getBooks();
        double fullPrice=0;
        for(Book book: books) fullPrice = book.getPrice() + fullPrice;
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        modelAndView.addObject("books",books);
        modelAndView.addObject("fullPrice",fullPrice);
        modelAndView.setViewName("shoppingcart");
        return modelAndView;
    }

    @PostMapping()//add Book to Cart
    public String AddBookToUserCart(@RequestParam Integer bookId, Principal principal, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        User user = userRepository.findByEmail(principal.getName());
        Book book = bookRepository.getOne(bookId);
        Shoppingcart shoppingcart = user.getShoppingcart();
        if(shoppingCartService.isBookAlreadyPresentinShoppingCart(book,user))
        {
            modelAndView.addObject("successMessage", "The requested book is already in the cart!");
            logger.info("The requested book is already in the cart!");
        }
        else {
            shoppingcart.getBooks().add(book);
            shoppingcartRepository.save(shoppingcart);
            //userRepository.save(user);
            modelAndView.addObject("successMessage", "Book is registered successfully!");
            logger.info("Book is registered successfully!");
        }
        return getPreviousPageByRequest(request).orElse("/");
    }

    @PostMapping(value="/buyBooks")//Bücher des shoppingcarts kaufen
    public ModelAndView BuyBooksInCart( Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userRepository.findByEmail(principal.getName());
        Shoppingcart shoppingcart = user.getShoppingcart();
        Set<Book> shoppingCartBooks = shoppingcart.getBooks();
        if(shoppingCartBooks.isEmpty()){
            modelAndView.addObject("successMessage","No Books in Possesion");
            logger.info("No Books in Possesion");
        }
        else {
            /*try
            {
                Thread.sleep(4000);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
            */double totalPrice = 0;
            for(Book book: shoppingCartBooks)
            {
                totalPrice += book.getPrice();
            }

            user.getUserBooks().addAll(shoppingCartBooks);
            shoppingCartBooks.removeAll(shoppingCartBooks);
            //shoppingcartRepository.save(shoppingcart);
            userRepository.save(user);
            modelAndView.addObject("successMessage", "Books are now bought for a total of "+totalPrice+" successfully!");
            logger.info("Books are now bought for a total of \"+totalPrice+\" successfully!");
        }
        modelAndView.setViewName("shoppingcart");
        return modelAndView;
    }

    //Buch von shoppingcart löschen
    @PostMapping(value = "deleteBook")
    public String DeleteBookFromCart(@RequestParam int bookId,Principal principal, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        Book book = bookRepository.getOne(bookId);
        User user = userRepository.findByEmail(principal.getName());
        Shoppingcart shoppingCart = user.getShoppingcart();
       if(!shoppingCart.getBooks().contains(book)){
            modelAndView.addObject("successMessage", "No such book in the Cart");
            logger.info("No such book in the Cart");
        }
        // we will save the book if, no binding errors
        else {
            shoppingCart.getBooks().remove(book);
            shoppingcartRepository.save(shoppingCart);
            modelAndView.addObject("successMessage", "Book got removed from shoppingCart!");
            logger.info("Book got removed from shoppingCart!");
        }
        return getPreviousPageByRequest(request).orElse("/");
    }
}
