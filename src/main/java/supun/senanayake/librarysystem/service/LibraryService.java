package supun.senanayake.librarysystem.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import supun.senanayake.librarysystem.model.Book;
import supun.senanayake.librarysystem.model.Borrower;
import supun.senanayake.librarysystem.repository.BookRepository;
import supun.senanayake.librarysystem.repository.BorrowerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BorrowerRepository borrowerRepository;

    // Registers a new borrower in the system
    @Transactional
    public Borrower registerBorrower(Borrower borrower) {
        return borrowerRepository.save(borrower);
    }

    // Registers a new book in the system
    @Transactional
    public Book registerBook(Book book) throws Exception {
        Optional<Book> result = bookRepository.findByIsbn(book.getIsbn());
        if (result.isPresent()) {
            if (result.get().getAuthor().equals(book.getAuthor()) && result.get().getTitle().equals(book.getTitle())) {
                return bookRepository.save(book);
            } else {
                throw new Exception("2 books with the same ISBN numbers must have the same title and same author");
            }
        }else {
            return bookRepository.save(book);
        }
    }

    // Retrieves a list of all books in the library
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Allows a borrower to borrow a book if it's not already borrowed
    @Transactional
    public void borrowBook(String borrowerId, String bookId) throws Exception {
        Optional<Borrower> borrower = borrowerRepository.findById(borrowerId);
        Optional<Book> book = bookRepository.findById(bookId);

        if (borrower.isPresent() && book.isPresent() && book.get().getBorrowedBy() == null) {
            book.get().setBorrowedBy(borrower.get());
            bookRepository.save(book.get());
        } else {
            throw new Exception("Book is not available for borrowing or borrower/book not found");
        }
    }

    // Allows a borrower to return a book they have borrowed
    @Transactional
    public void returnBook(String borrowerId, String bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);

        if (book.isPresent() && book.get().getBorrowedBy() != null && book.get().getBorrowedBy().getId().equals(borrowerId)) {
            book.get().setBorrowedBy(null);
            bookRepository.save(book.get());
        } else {
            throw new Exception("Book is not borrowed by this borrower or book not found");
        }
    }

}
