package com.library.libraryapi.Controller;

import com.google.gson.Gson;
import com.library.libraryapi.DAO.BookItemRepository;
import com.library.libraryapi.DAO.CustomerRepository;
import com.library.libraryapi.DAO.DictionaryItemRepository;
import com.library.libraryapi.DAO.HireRepository;
import com.library.libraryapi.Model.BookItem;
import com.library.libraryapi.Model.Customer;
import com.library.libraryapi.Model.DictionaryItem;
import com.library.libraryapi.Model.Hire;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/action")
public class HireController {

    private final CustomerRepository customerRepository;
    private final BookItemRepository bookItemRepository;
    private final HireRepository hireRepository;
    private final DictionaryItemRepository dictionaryItemRepository;

    private HttpHeaders headers;
    private Gson gson;

    public HireController(CustomerRepository customerRepository,
                          BookItemRepository bookItemRepository,
                          HireRepository hireRepository,
                          DictionaryItemRepository dictionaryItemRepository) {

        this.bookItemRepository = bookItemRepository;
        this.customerRepository = customerRepository;
        this.hireRepository = hireRepository;
        this.dictionaryItemRepository = dictionaryItemRepository;
        this.headers = new HttpHeaders();
        this.headers.setContentType(MediaType.APPLICATION_JSON);
        this.gson = new Gson();
    }

    @RequestMapping(value = "/{me}/hire", method = RequestMethod.POST)
    public ResponseEntity<String> hireAction(@PathVariable(value = "me") String me,
                                             @RequestParam(name = "book") String book) {

        //Customer validation
        if (me == null || me.isEmpty()) {
            return new ResponseEntity<>(null, headers, HttpStatus.valueOf(" user id cant be empty"));
        }

        Optional<Customer> userOptional = customerRepository.findById(Long.parseLong(me));

        Customer user = new Customer();
        user.setLogin(userOptional.map(Customer::getLogin).orElse(null));

        user = customerRepository.findByLogin(user.getLogin());

        if (user == null) {
            return new ResponseEntity<>(null, headers, HttpStatus.valueOf(" account doesnt exist"));
        }

        if (user.isDeleted()) {
            return new ResponseEntity<>(null, headers, HttpStatus.valueOf(" account was deleted"));
        }

        //Book validation
        if (book == null || book.isEmpty()) {
            return new ResponseEntity<>(null, headers, HttpStatus.valueOf(" BookId is required"));
        }

        Optional<BookItem> bookItemOptional = bookItemRepository.findById(Long.parseLong(book));

        BookItem bookItem = new BookItem();
        bookItem.setAuthor(bookItemOptional.map(BookItem::getAuthor).orElse(null));
        bookItem.setTitle(bookItemOptional.map(BookItem::getTitle).orElse(null));

        bookItem = bookItemRepository.findByAuthorAndTitle(bookItem.getAuthor(), bookItem.getTitle());

        if (bookItem == null) {
            return new ResponseEntity<>(null, headers, HttpStatus.valueOf(" book doesny exist"));
        }

        if (!bookItem.isAvailable()) {
            return new ResponseEntity<>(null, headers, HttpStatus.valueOf(" book isnt available"));
        }

//
//        Hire hire = new Hire();
//
//        hire.setAvailableExtension(3);

        return null;
    }


    @RequestMapping(value = "/{me}/return", method = RequestMethod.POST)
    public ResponseEntity<String> returnAction(@PathVariable(value = "me") String me,
                                               @RequestParam(name = "book") String bookId) {

        return null;
    }


    @RequestMapping(value = "/{me}/extend", method = RequestMethod.POST)
    public ResponseEntity<String> extendAction(@PathVariable(value = "me") String me,
                                               @RequestParam(name = "book") String bookId) {

        return null;
    }
}