package supun.senanayake.librarysystem.controler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import supun.senanayake.librarysystem.model.Book;
import supun.senanayake.librarysystem.model.Borrower;
import supun.senanayake.librarysystem.service.LibraryService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LibraryControllerTest {


    private MockMvc mockMvc;

    @Mock
    private LibraryService libraryService;

    @InjectMocks
    private LibraryController controller;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testRegisterBorrower() throws Exception {
        Borrower savedBorrower = new Borrower();  // Set fields that reflect the saved state

        when(libraryService.registerBorrower(any(Borrower.class))).thenReturn(savedBorrower);

        mockMvc.perform(post("/api/borrowers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"John Doe\", \"email\": \"johndoe@example.com\" }"))  // JSON content to match the Borrower class structure
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(savedBorrower.getName()));  // Assert based on expected results
    }
    @Test
    public void testRegisterBook() throws Exception {
        Book book = new Book();  // Set necessary fields as required, like 'title', 'author'
        book.setTitle("1984");
        book.setAuthor("George Orwell");

        Book savedBook = new Book();  // Set fields that reflect the saved state
        savedBook.setTitle("1984");
        savedBook.setAuthor("George Orwell");

        when(libraryService.registerBook(any(Book.class))).thenReturn(savedBook);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"1984\", \"author\": \"George Orwell\" }"))  // JSON content to match the Book class structure
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("1984"))
                .andExpect(jsonPath("$.author").value("George Orwell"));
    }
    @Test
    public void testListBooks() throws Exception {

        Book book1 = new Book("b1","isbn1","1984", "George Orwell");
        Book book2 = new Book("b2","isbn2","Brave New World", "Aldous Huxley");

        List<Book> books = Arrays.asList(book1, book2);
        when(libraryService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("1984")))
                .andExpect(jsonPath("$[0].author", is("George Orwell")))
                .andExpect(jsonPath("$[1].title", is("Brave New World")))
                .andExpect(jsonPath("$[1].author", is("Aldous Huxley")));
    }
    @Test
    public void testBorrowBook() throws Exception {
        String borrowerId = "1";
        String bookId = "101";

        doNothing().when(libraryService).borrowBook(anyString(), anyString());

        mockMvc.perform(put("/api/borrowers/{borrowerId}/books/{bookId}/borrow", borrowerId, bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Book borrowed successfully."));
    }

    @Test
    public void testReturnBook() throws Exception {
        String borrowerId = "1";
        String bookId = "101";

        // Assuming returnBook method performs some action and does not return a value
        doNothing().when(libraryService).returnBook(anyString(), anyString());

        mockMvc.perform(put("/api/borrowers/{borrowerId}/books/{bookId}/return", borrowerId, bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Book returned successfully."));
    }
}