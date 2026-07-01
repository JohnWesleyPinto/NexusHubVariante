package br.ufpb.dsc.nexushub.model.payments.repository;
import br.ufpb.dsc.nexushub.model.payments.domain.Product;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductRepository extends JpaRepository<Product,UUID>{List<Product> findByActiveTrue();}
