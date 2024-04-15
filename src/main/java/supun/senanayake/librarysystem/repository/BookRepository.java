package supun.senanayake.librarysystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import supun.senanayake.librarysystem.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
}
