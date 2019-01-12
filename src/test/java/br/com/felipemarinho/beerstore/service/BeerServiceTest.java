package br.com.felipemarinho.beerstore.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

import br.com.felipemarinho.beerstore.model.Beer;
import br.com.felipemarinho.beerstore.model.BeerType;
import br.com.felipemarinho.beerstore.repository.Beers;
import br.com.felipemarinho.beerstore.service.exception.BeerAlreadyExistException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;

public class BeerServiceTest {

    private BeerService beerService;

    @Mock
    private Beers beersMocked;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        beerService = new BeerService(beersMocked);
    }

    @Test(expected = BeerAlreadyExistException.class)
    public void should_deny_creation_of_beer_that_exists() {
        Beer beerInDatabase = new Beer();
        beerInDatabase.setId(10L);
        beerInDatabase.setName("Heineken");
        beerInDatabase.setVolume(new BigDecimal("355"));
        beerInDatabase.setType(BeerType.LAGER);

        when(beersMocked.findByNameAndType("Heineken", BeerType.LAGER)).thenReturn(Optional.of(beerInDatabase));

        Beer newBeer = new Beer();
        newBeer.setName("Heineken");
        newBeer.setType(BeerType.LAGER);
        newBeer.setVolume(new BigDecimal("355"));
        beerService.save(newBeer);
    }

    @Test
    public void should_create_new_beer() {
        Beer newBeer = new Beer();
        newBeer.setName("Heineken");
        newBeer.setType(BeerType.LAGER);
        newBeer.setVolume(new BigDecimal("355"));

        Beer newBeerInDatabase = new Beer();
        newBeerInDatabase.setId(10L);
        newBeerInDatabase.setType(BeerType.LAGER);
//        newBeerInDatabase.setVolume(new BigDecimal("355"));
        newBeerInDatabase.setName("Heineken");

        when(beersMocked.save(newBeer)).thenReturn(newBeerInDatabase);
        Beer beerSaved = beerService.save(newBeer);

        assertThat(beerSaved.getId(), equalTo(10L));
        assertThat(beerSaved.getName(), equalTo("Heineken"));
        assertThat(beerSaved.getType(), equalTo(BeerType.LAGER));

    }

    @Test
    public void should_update_beer_volume() {
        final Beer beerInDatabase = new Beer();
        beerInDatabase.setId(10L);
        beerInDatabase.setName("Devassa");
        beerInDatabase.setType(BeerType.PILSE);
        beerInDatabase.setVolume(new BigDecimal("300"));

        when(beersMocked.findByNameAndType("Devassa", BeerType.PILSE))
                .thenReturn(Optional.of(beerInDatabase));

        final Beer beerMocked = new Beer();
        beerMocked.setId(10L);
        beerMocked.setName("Devassa");
        beerMocked.setType(BeerType.PILSE);
        beerMocked.setVolume(new BigDecimal("200"));

        when(beersMocked.save(beerMocked)).thenReturn(beerMocked);

        final Beer beerUpdated = beerService.save(beerMocked);
        assertThat(beerUpdated.getId(), equalTo(10L));
        assertThat(beerUpdated.getName(), equalTo("Devassa"));
        assertThat(beerUpdated.getType(), equalTo(BeerType.PILSE));
        assertThat(beerUpdated.getVolume(), equalTo(new BigDecimal("200")));
    }

    @Test(expected = BeerAlreadyExistException.class)
    public void should_deny_update_of_an_existing_beer_that_already_exists() {
        final Beer beerInDatabase = new Beer();
        beerInDatabase.setId(10L);
        beerInDatabase.setName("Heineken");
        beerInDatabase.setType(BeerType.LAGER);
        beerInDatabase.setVolume(new BigDecimal("355"));

        when(beersMocked.findByNameAndType("Heineken", BeerType.LAGER))
                .thenReturn(Optional.of(beerInDatabase));

        final Beer beerToUpdate = new Beer();
        beerToUpdate.setId(5L);
        beerToUpdate.setName("Heineken");
        beerToUpdate.setType(BeerType.LAGER);
        beerToUpdate.setVolume(new BigDecimal("355"));

        beerService.save(beerToUpdate);
    }

    @Test(expected = EntityNotFoundException.class)
    public void should_deny_delete_of_a_not_existing_beer() {
        when(beersMocked.findById(50L)).thenReturn(Optional.empty());
        beerService.delete(50L);
    }

    @Test
    public void should_delete_existing_beer() {
        final Beer beerInDatabase = new Beer();
        beerInDatabase.setId(10L);
        beerInDatabase.setName("Heineken");
        beerInDatabase.setType(BeerType.LAGER);
        beerInDatabase.setVolume(new BigDecimal("355"));
        when(beersMocked.findById(10L)).thenReturn(Optional.of(beerInDatabase));

        beerService.delete(10L);
    }

}
