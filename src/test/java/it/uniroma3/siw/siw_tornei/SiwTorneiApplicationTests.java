package it.uniroma3.siw.siw_tornei;

import it.uniroma3.siw.siw_tornei.model.Giocatore;
import it.uniroma3.siw.siw_tornei.model.Squadra;
import it.uniroma3.siw.siw_tornei.repository.GiocatoreRepository;
import it.uniroma3.siw.siw_tornei.service.SquadraService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SiwTorneiApplicationTests {

	@Autowired
	private SquadraService squadraService;

	@Autowired
	private GiocatoreRepository giocatoreRepository;

	@Test
	void contextLoads() {
	}

	@Test
	@Transactional
	void testDeleteSquadraDeletesPlayers() {
		Squadra squadra = new Squadra();
		squadra.setNome("Test FC");
		squadra.setCitta("Rome");
		squadra.setAnnoFondazione(2000);
		squadra.setGiocatori(new ArrayList<>());
		squadraService.saveSquadra(squadra);

		Giocatore giocatore = new Giocatore();
		giocatore.setNome("Mario");
		giocatore.setCognome("Rossi");
		giocatore.setRuolo("Attaccante");
		giocatore.setDataNascita(LocalDate.of(1995, 1, 1));
		giocatore.setSquadra(squadra);
		giocatoreRepository.save(giocatore);

		squadra.getGiocatori().add(giocatore);
		squadraService.saveSquadra(squadra);

		Long squadraId = squadra.getId();
		Long giocatoreId = giocatore.getId();

		assertNotNull(squadraId);
		assertNotNull(giocatoreId);

		// Delete the team
		squadraService.deleteSquadra(squadraId);

		// Verify the team is deleted
		assertNull(squadraService.findById(squadraId));

		// Verify the player is also deleted
		assertFalse(giocatoreRepository.existsById(giocatoreId));
	}

}
