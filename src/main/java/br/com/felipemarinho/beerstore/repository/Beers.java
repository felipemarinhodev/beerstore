package br.com.felipemarinho.beerstore.repository;

import br.com.felipemarinho.beerstore.model.Beer;
import br.com.felipemarinho.beerstore.model.BeerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Beers extends JpaRepository<Beer, Long> {

    Optional<Beer> findByNameAndType(String name, BeerType type);
}
