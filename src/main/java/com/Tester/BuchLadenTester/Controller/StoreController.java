package com.Tester.BuchLadenTester.Controller;

import com.Tester.BuchLadenTester.Repository.BookRepository;
import com.Tester.BuchLadenTester.Repository.ShoppingcartRepository;
import com.Tester.BuchLadenTester.Model.Book;
import com.Tester.BuchLadenTester.Model.Shoppingcart;
import com.Tester.BuchLadenTester.Model.User;
import com.Tester.BuchLadenTester.Repository.UserRepository;
import com.Tester.BuchLadenTester.Service.ShoppingCartImp;
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
import java.util.Set;

import static com.Tester.BuchLadenTester.BuchLadenTesterApplication.logger;

@Controller()
@RequestMapping("/shoppingcart")
public class StoreController {

    final
    BookRepository bookRepository;

    final
    UserRepository userRepository;

    final
    ShoppingcartRepository shoppingcartRepository;

    ShoppingCartImp shoppingCartService;

    public StoreController(BookRepository bookRepository, UserRepository userRepository, ShoppingcartRepository shoppingcartRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.shoppingcartRepository = shoppingcartRepository;
    }

    @GetMapping()
    public ModelAndView Shoppingcart(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        String name = principal.getName();
        User currentUser = userRepository.findByEmail(principal.getName());
        Set<Book> books = currentUser.getBooks();
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
    public ModelAndView AddBookToUserCart(@Valid Book book, Principal principal, BindingResult bindingResult, ModelMap modelMap){
        ModelAndView modelAndView = new ModelAndView();
        User user = userRepository.findByEmail(principal.getName());
        Book mybook = bookRepository.getOne(book.getBook_id());

        if(bindingResult.hasErrors()) {
            modelAndView.addObject("successMessage", "Please correct the errors in form!");
            logger.info("Please correct the errors in form!");
            modelMap.addAttribute("bindingResult", bindingResult);
        }
        else if(shoppingCartService.isBookAlreadyPresentinShoppingCart(book,user))
        {
            modelAndView.addObject("successMessage", "The requested book is already in the cart!");
            logger.info("The requested book is already in the cart!");
        }
        else {
            user.getShoppingcart().getBooks().add(book);
            userRepository.save(user);
            modelAndView.addObject("successMessage", "Book is registered successfully!");
            logger.info("Book is registered successfully!");
        }
        modelAndView.setViewName("shoppingcart");
        return modelAndView;
    }

    @PostMapping(value="/buyBooks")//Bücher des shoppingcarts kaufen
    public ModelAndView BuyBooksInCart(@Valid int CartId, Principal principal, BindingResult bindingResult, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userRepository.findByEmail(principal.getName());
        Shoppingcart shoppingCart = shoppingcartRepository.getOne(CartId);
        Set<Book> shoppingCartBooks = shoppingCart.getBooks();
        // Check for the validations
        if(bindingResult.hasErrors()) {
            modelAndView.addObject("successMessage", "Please correct the errors in form!");
            logger.info("Please correct the errors in form!");
            modelMap.addAttribute("bindingResult", bindingResult);
        }
        else if(shoppingCartBooks.containsAll(user.getBooks())){
            StringBuilder errorString = new StringBuilder("The Books ");
            for(Book book: shoppingCartBooks)
            {
                if(user.getBooks().contains(book))
                errorString.append(book.getName());
            }
            errorString.append(" are allready owned!");
            modelAndView.addObject("successMessage", errorString.toString());
            logger.info(errorString.toString());
        }
        // we will save the book if, no binding errors
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

            user.getBooks().addAll(shoppingCartBooks);
            modelAndView.addObject("successMessage", "Books are now bought for a total of "+totalPrice+" successfully!");
            logger.info("Books are now bought for a total of \"+totalPrice+\" successfully!");
        }
        modelAndView.setViewName("shoppingcart");
        return modelAndView;
    }

    //Buch von shoppingcart löschen
    @DeleteMapping(value = "deleteBook")
    public ModelAndView DeleteBookFromCart(@Valid int BookId, Principal principal, BindingResult bindingResult, ModelMap modelMap){
        ModelAndView modelAndView = new ModelAndView();
        Book book = bookRepository.getOne(BookId);
        User user = userRepository.findByEmail(principal.getName());
        Shoppingcart shoppingCart = user.getShoppingcart();
        if(bindingResult.hasErrors()) {
            modelAndView.addObject("successMessage", "Please correct the errors in form!");
            logger.info("Please correct the errors in form!");
            modelMap.addAttribute("bindingResult", bindingResult);
        }
        else if(!shoppingCart.getBooks().contains(book)){
            modelAndView.addObject("successMessage", "No such book in the Cart");
            logger.info("No such book in the Cart");
        }
        // we will save the book if, no binding errors
        else {
            shoppingCart.getBooks().remove(book);
            modelAndView.addObject("successMessage", "Book got removed from shoppingCart!");
            logger.info("Book got removed from shoppingCart!");
        }
        modelAndView.setViewName("shoppingcart");
        return modelAndView;
    }
}
