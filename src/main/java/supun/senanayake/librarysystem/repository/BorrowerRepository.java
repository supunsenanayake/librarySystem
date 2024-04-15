package supun.senanayake.librarysystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import supun.senanayake.librarysystem.model.Borrower;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, String> {
}
