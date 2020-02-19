package com.Tester.BuchLadenTester.Controller;

import com.Tester.BuchLadenTester.Repository.BookRepository;
import com.Tester.BuchLadenTester.Repository.ShoppingcartRepository;
import com.Tester.BuchLadenTester.Model.Book;
import com.Tester.BuchLadenTester.Model.Shoppingcart;
import com.Tester.BuchLadenTester.Model.User;
import com.Tester.BuchLadenTester.Repository.UserRepository;
import com.Tester.BuchLadenTester.Service.ShoppingCartServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;
import java.util.Set;

import static com.Tester.BuchLadenTester.config.MyAuthenticationSuccessHandler.getPreviousPageByRequest;
import static com.Tester.BuchLadenTester.BuchLadenTesterApplication.logger;

@Controller()
@RequestMapping("/shoppingcart")
public class  ShoppingCartController {

    final
    BookRepository bookRepository;

    final
    UserRepository userRepository;

    final
    ShoppingcartRepository shoppingcartRepository;

    @Autowired
    ShoppingCartServiceImp shoppingCartService;

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
        if(shoppingCartService.isBookAlreadyPresentInShoppingCart(book,user))
        {
            modelAndView.addObject("successMessage", "The requested book with id: "+book.getBook_id()+" is already in the cart!");
            logger.info("The requested book is already in the cart!");
        }
        else {
            shoppingcart.getBooks().add(book);
            shoppingcartRepository.save(shoppingcart);
            //userRepository.save(user);
            modelAndView.addObject("successMessage", "Book: "+book.getName()+" get added to shoppingcart successfully!");
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
            try
            {
                Thread.sleep(4000);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
            double totalPrice = 0;
            for(Book book: shoppingCartBooks)
            {

                totalPrice += book.getPrice();
            }
            //Remove all Books from cart and add them to the user
            modelAndView.addObject("successMessage", "Books are now bought for a total of "+totalPrice+" successfully!");
            logger.info("Books are now bought for a total of "+totalPrice+" successfully!");
            user.getUserBooks().addAll(shoppingCartBooks);
            shoppingCartBooks.removeAll(shoppingCartBooks);
            userRepository.save(user);

        }
        modelAndView.setViewName("shoppingcart");
        return modelAndView;
    }

    //Buch von shoppingcart löschen
    @PostMapping(value = "deleteBook")
    public String DeleteBookFromCart(@RequestParam int bookId,Principal principal, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        Optional<Book> book = bookRepository.findById(bookId);
        User user = userRepository.findByEmail(principal.getName());
        Shoppingcart shoppingCart = user.getShoppingcart();
        if(book.isPresent()) {
            Book saveBook = book.get();
        if (!shoppingCart.getBooks().contains(saveBook)) {
                modelAndView.addObject("successMessage", "No Book with "+bookId+" in the Cart");
                logger.info("No such book in the Cart");
            }
            // we will save the book if, no binding errors
            else {
                shoppingCart.getBooks().remove(saveBook);
                shoppingcartRepository.save(shoppingCart);
                modelAndView.addObject("successMessage", "Book: "+saveBook.getName()+" got removed from shoppingCart!");
                logger.info("Book got removed from shoppingCart!");
            }
        }
        else {
            modelAndView.addObject("successMessage", "There is no book with the id: "+bookId);
            logger.info("There is no book with the id: "+bookId);
        }
        return getPreviousPageByRequest(request).orElse("/");
    }
}
