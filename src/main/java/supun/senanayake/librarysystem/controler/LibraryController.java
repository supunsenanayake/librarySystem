package supun.senanayake.librarysystem.controler;

import supun.senanayake.librarysystem.model.Book;
import supun.senanayake.librarysystem.model.Borrower;
import supun.senanayake.librarysystem.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LibraryController {

    @Autowired
    private LibraryService libraryService;


    @PostMapping("/api/borrowers")
    public ResponseEntity<Borrower> registerBorrower(@RequestBody Borrower borrower) {
        Borrower newBorrower = libraryService.registerBorrower(borrower);
        return ResponseEntity.ok(newBorrower);
    }

    @PostMapping("/api/books")
    public ResponseEntity<Book> registerBook(@RequestBody Book book) {
        Book newBook = libraryService.registerBook(book);
        return ResponseEntity.ok(newBook);
    }

    @GetMapping("/api/books")
    public ResponseEntity<List<Book>> listBooks() {
        List<Book> books = libraryService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @PostMapping("/api/borrowers/{borrowerId}/borrow")
    public ResponseEntity<String> borrowBook(@PathVariable String borrowerId, @RequestBody String bookId) {
        libraryService.borrowBook(borrowerId, bookId);
        return ResponseEntity.ok("Book borrowed successfully.");
    }

    @PostMapping("/api/borrowers/{borrowerId}/return")
    public ResponseEntity<String> returnBook(@PathVariable String borrowerId, @RequestBody String bookId) {
        libraryService.returnBook(borrowerId, bookId);
        return ResponseEntity.ok("Book returned successfully.");
    }
}