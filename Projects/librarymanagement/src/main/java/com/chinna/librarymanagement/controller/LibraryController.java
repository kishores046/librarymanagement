package com.chinna.librarymanagement.controller;

import java.util.List;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import com.chinna.librarymanagement.model.Book;
import com.chinna.librarymanagement.model.BorrowRecord;
import com.chinna.librarymanagement.model.Customer;
import com.chinna.librarymanagement.service.BookService;
import com.chinna.librarymanagement.service.BorrowService;
import com.chinna.librarymanagement.service.CustomerService;
import com.chinna.librarymanagement.forms.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class LibraryController {

    private final CustomerService customerService;
    private final BookService bookService;
    private final BorrowService borrowService;

    public LibraryController(CustomerService customerService, BookService bookService, BorrowService borrowService) {
        this.customerService = customerService;
        this.bookService = bookService;
        this.borrowService = borrowService;
    }

    // --- Login / Signup ---

    @GetMapping("customer/login")
    public String loginPage(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "customer-login";
    }

    @PostMapping("customer/login")
    public String loginSubmit(@Valid @ModelAttribute("loginForm") LoginForm form,
                              BindingResult br,
                              HttpSession session,
                              RedirectAttributes ra) {
        if (br.hasErrors()) return "customer-login";

        Customer c = customerService.getCustomerById(form.getCustomerId())
                .orElse(null);

        if (c == null) {
            br.rejectValue("customerId", "notfound", "Customer not found");
            return "customer-login";
        }

        // simple session set
        session.setAttribute("customerId", c.getCustomerId());
        session.setAttribute("customer", c);
        ra.addFlashAttribute("success", "Welcome " + c.getName());
        return "redirect:/home";
    }

    @GetMapping("customer/signup")
    public String signupPage(Model model) {
        model.addAttribute("signupForm", new SignUpForm());
        return "customer-signup";
    }

    @PostMapping("customer/signup")
    public String signupSubmit(@Valid @ModelAttribute("signupForm") SignUpForm form,
                               BindingResult br,
                               RedirectAttributes ra,
                               HttpSession session) {
        if (br.hasErrors()) return "customer-signup";

        Customer created = customerService.createFromForm(form);
        session.setAttribute("customerId", created.getCustomerId());
        session.setAttribute("customer", created);
        ra.addFlashAttribute("success", "Account created. Welcome " + created.getName());
        return "redirect:/home";
    }

    // --- Home / Dashboard ---

    @GetMapping({"", "home"})
    public String home(HttpSession session, Model model) {
        Customer c = (Customer) session.getAttribute("customer");
        if (c == null) return "redirect:/customer/login";
        // refresh customer (in case of updates)
        customerService.getCustomerById(c.getCustomerId()).ifPresent(ref -> {
            session.setAttribute("customer", ref);
        });
        model.addAttribute("customer", session.getAttribute("customer"));
        return "home";
    }

    // --- Borrowing flows ---

    @GetMapping("borrow-options")
    public String borrowOptions(HttpSession session) {
        if (session.getAttribute("customer") == null) return "redirect:/customer/login";
        return "borrow-options";
    }

    @GetMapping("search/genre")
    public String searchByGenre(@RequestParam("genre") String genre, Model model) {
        List<Book> books = bookService.findByGenre(genre);
        model.addAttribute("books", books);
        return "search-results";
    }

    @GetMapping("search/name")
    public String searchByName(@RequestParam("name") String name, Model model) {
        // bookService should offer search by name (or you can implement a simple contains filter)
        List<Book> books = bookService.findByName(name);
        model.addAttribute("books", books);
        return "search-results";
    }

    @GetMapping("search/author")
    public String searchByAuthor(@RequestParam("author") String author, Model model) {
        List<Book> books = bookService.findByAuthor(author);
        model.addAttribute("books", books);
        return "search-results";
    }

    @PostMapping("borrow")
    public String borrowBook(@RequestParam("bookId") long bookId,
                             HttpSession session,
                             RedirectAttributes ra) {
        Customer c = (Customer) session.getAttribute("customer");
        if (c == null) return "redirect:/customer/login";

        try {
            BorrowRecord br = borrowService.borrow(c.getCustomerId(), bookId);
            ra.addFlashAttribute("record", br);
            return "redirect:/borrow-success";
        } catch (Exception ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/borrow-options";
        }
    }

    @GetMapping("borrow-success")
    public String borrowSuccess(Model model) {
        // record is in flash attribute
        return "borrow-success";
    }

    // --- Return flows ---

    @GetMapping("return-book")
    public String returnPage(Model model, HttpSession session) {
        if (session.getAttribute("customer") == null) return "redirect:/customer/login";
        model.addAttribute("returnForm", new ReturnForm());
        return "return-book";
    }

    @PostMapping("return")
    public String returnSubmit(@Valid @ModelAttribute("returnForm") ReturnForm form,
                               BindingResult br,
                               RedirectAttributes ra,
                               HttpSession session) {
        if (br.hasErrors()) return "return-book";

        try {
            BorrowRecord record = borrowService.returnBook(form.getBorrowId());
            ra.addFlashAttribute("record", record);
            return "redirect:/return-success";
        } catch (Exception ex) {
            br.reject("return.failed", ex.getMessage());
            return "return-book";
        }
    }

    @GetMapping("return-success")
    public String returnSuccess() {
        return "return-success";
    }

    // --- Customer details & active borrows ---

    @GetMapping("customer/details")
    public String customerDetails(HttpSession session, Model model) {
        Customer c = (Customer) session.getAttribute("customer");
        if (c == null) return "redirect:/customer/login";

        customerService.getCustomerById(c.getCustomerId()).ifPresent(ref -> {
            model.addAttribute("customer", ref);
            List<BorrowRecord> active = borrowService.getActiveBorrowsOfCustomer(ref.getCustomerId());
            model.addAttribute("activeBorrows", active);
        });

        return "customer-details";
    }

    // --- Logout ---

    @GetMapping("logout")
    public String logout(HttpSession session, RedirectAttributes ra) {
        session.invalidate();
        ra.addFlashAttribute("info", "You have been logged out.");
        return "redirect:/customer/login";
    }
}
