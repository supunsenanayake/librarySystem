package supun.senanayake.librarysystem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import supun.senanayake.librarysystem.model.Book;
import supun.senanayake.librarysystem.model.Borrower;
import supun.senanayake.librarysystem.repository.BookRepository;
import supun.senanayake.librarysystem.repository.BorrowerRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {
    @InjectMocks
    private LibraryService libraryService;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BorrowerRepository borrowerRepository;

    @Test
    public void registerBorrowerSavesAndReturnsBorrower() {
        // Arrange
        Borrower newBorrower = new Borrower();
        newBorrower.setName("John Doe");
        newBorrower.setEmail("johndoe@example.com");
        when(borrowerRepository.save(any(Borrower.class))).thenReturn(newBorrower);

        // Act
        Borrower savedBorrower = libraryService.registerBorrower(newBorrower);

        // Assert
        assertNotNull(savedBorrower, "The saved borrower should not be null");
        assertEquals("John Doe", savedBorrower.getName(), "The name of the borrower should match the input");
        assertEquals("johndoe@example.com", savedBorrower.getEmail(), "The email of the borrower should match the input");
        verify(borrowerRepository).save(newBorrower);  // Verifies the repository was called with the new borrower
    }

    @Test
    public void registerNewBookNotExisting() throws Exception {
        Book newBook = new Book("id1","1234567890", "New Title", "New Author");
        when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(newBook);

        Book savedBook = libraryService.registerBook(newBook);

        assertNotNull(savedBook);
        assertEquals("New Title", savedBook.getTitle());
        assertEquals("New Author", savedBook.getAuthor());
        verify(bookRepository).save(newBook);
    }

    @Test
    public void registerBookWithSameISBNDifferentAuthorThrowsException() {
        Book existingBook = new Book("id1","1234567890", "Old Title", "Old Author");
        Book newBook = new Book("id2","1234567890", "Old Title", "New Author");
        when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.of(existingBook));

        Exception exception = assertThrows(Exception.class, () -> {
            libraryService.registerBook(newBook);
        });

        assertEquals("2 books with the same ISBN numbers must have the same title and same author", exception.getMessage());
    }

    @Test
    public void registerBookWithSameISBNSameDetails() throws Exception {
        Book existingBook = new Book("id1","1234567890", "Same Title", "Same Author");
        when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        Book savedBook = libraryService.registerBook(existingBook);

        assertEquals("Same Title", savedBook.getTitle());
        assertEquals("Same Author", savedBook.getAuthor());
        verify(bookRepository).save(existingBook);
    }
    @Test
    public void getAllBooksReturnsAllBooks() {
        // Arrange
        Book book1 = new Book("b1","isbn1", "1984", "George Orwell");
        Book book2 = new Book("b2","isbn2", "Brave New World", "Aldous Huxley");
        List<Book> books = Arrays.asList(book1, book2);
        when(bookRepository.findAll()).thenReturn(books);

        // Act
        List<Book> retrievedBooks = libraryService.getAllBooks();

        // Assert
        assertNotNull(retrievedBooks);
        assertEquals(2, retrievedBooks.size(), "Should return all books");
        assertSame(books, retrievedBooks, "The retrieved books should be the same as the ones returned by the repository");
    }
    @Test
    public void borrowBookSuccess() throws Exception {
        // Arrange
        Borrower borrower = new Borrower();
        borrower.setId("1");

        Book book = new Book();
        book.setId("101");
        book.setBorrowedBy(null);  // Ensuring the book is not currently borrowed

        when(borrowerRepository.findById("1")).thenReturn(Optional.of(borrower));
        when(bookRepository.findById("101")).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // Act
        libraryService.borrowBook("1", "101");

        // Assert
        assertNotNull(book.getBorrowedBy(), "The book should be borrowed now");
        assertEquals(borrower, book.getBorrowedBy(), "The book should be borrowed by the correct borrower");
    }

    @Test
    public void borrowBookFailureAlreadyBorrowed() {
        // Arrange
        Borrower borrower = new Borrower();
        borrower.setId("1");

        Borrower anotherBorrower = new Borrower();
        anotherBorrower.setId("2");

        Book book = new Book();
        book.setId("101");
        book.setBorrowedBy(anotherBorrower);  // The book is already borrowed

        when(borrowerRepository.findById("1")).thenReturn(Optional.of(borrower));
        when(bookRepository.findById("101")).thenReturn(Optional.of(book));

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> libraryService.borrowBook("1", "101"));
        assertEquals("Book is not available for borrowing or borrower/book not found", exception.getMessage());
    }

    @Test
    public void borrowBookFailureBookOrBorrowerNotFound() {
        // Arrange
        when(borrowerRepository.findById("1")).thenReturn(Optional.empty());
        when(bookRepository.findById("101")).thenReturn(Optional.empty());

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> libraryService.borrowBook("1", "101"));
        assertEquals("Book is not available for borrowing or borrower/book not found", exception.getMessage());
    }
    @Test
    public void returnBookSuccess() throws Exception {
        // Arrange
        Borrower borrower = new Borrower();
        borrower.setId("1");

        Book book = new Book();
        book.setId("101");
        book.setBorrowedBy(borrower);  // Set up as currently borrowed by this borrower

        when(bookRepository.findById("101")).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // Act
        libraryService.returnBook("1", "101");

        // Assert
        assertNull(book.getBorrowedBy(), "The book should no longer be borrowed");
    }

    @Test
    public void returnBookFailureNotBorrowed() {
        // Arrange
        Book book = new Book();
        book.setId("101");
        book.setBorrowedBy(null);  // The book is not currently borrowed

        when(bookRepository.findById("101")).thenReturn(Optional.of(book));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> libraryService.returnBook("1", "101"));
        assertEquals("Book is not borrowed by this borrower or book not found", exception.getMessage());
    }

    @Test
    public void returnBookFailureWrongBorrower() {
        // Arrange
        Borrower borrower = new Borrower();
        borrower.setId("2");  // Not the current borrower

        Borrower currentBorrower = new Borrower();
        currentBorrower.setId("1");  // Current borrower

        Book book = new Book();
        book.setId("101");
        book.setBorrowedBy(currentBorrower);

        when(bookRepository.findById("101")).thenReturn(Optional.of(book));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> libraryService.returnBook("2", "101"));
        assertEquals("Book is not borrowed by this borrower or book not found", exception.getMessage());
    }

    @Test
    public void returnBookFailureBookNotFound() {
        // Arrange
        when(bookRepository.findById("101")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> libraryService.returnBook("1", "101"));
        assertEquals("Book is not borrowed by this borrower or book not found", exception.getMessage());
    }
}