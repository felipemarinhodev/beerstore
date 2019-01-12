package br.com.felipemarinho.beerstore.service;

import br.com.felipemarinho.beerstore.model.Beer;
import br.com.felipemarinho.beerstore.repository.Beers;
import br.com.felipemarinho.beerstore.service.exception.BeerAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class BeerService {

    private Beers beers;

    public BeerService(@Autowired Beers beers) {
        this.beers = beers;
    }

    public Beer save(final Beer beer) {
        verifyIfBeerExists(beer);
        return beers.save(beer);
    }

    public void delete(Long id) {
        verifyIfBeerExists(id);
        beers.deleteById(id);
    }

    private void verifyIfBeerExists(Long id) {
        Optional<Beer> beerById = beers.findById(id);
        if (!beerById.isPresent()) {
            throw new EntityNotFoundException();
        }
    }

    private void verifyIfBeerExists(final Beer beer) {
        Optional<Beer> beerByNameAndType = beers.findByNameAndType(beer.getName(), beer.getType());
        if (beerByNameAndType.isPresent() && (beer.isNew() ||
                isUpdatingToDifferentBeer(beer, beerByNameAndType))) {
            throw new BeerAlreadyExistException();
        }
    }

    private boolean isUpdatingToDifferentBeer(Beer beer, Optional<Beer> beerByNameAndType) {
        return beer.alreadyExist() && !beerByNameAndType.get().equals(beer);
    }
}
