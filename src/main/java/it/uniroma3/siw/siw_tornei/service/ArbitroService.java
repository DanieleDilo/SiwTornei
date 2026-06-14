package it.uniroma3.siw.siw_tornei.service;

import it.uniroma3.siw.siw_tornei.model.Arbitro;
import it.uniroma3.siw.siw_tornei.repository.ArbitroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ArbitroService {
    @Autowired
    private ArbitroRepository arbitroRepository;

    @Transactional(readOnly = true)
    public List<Arbitro> findAll() {
        return (List<Arbitro>) this.arbitroRepository.findAll();
    }

    @Transactional
    public void saveArbitro(Arbitro arbitro) {
        this.arbitroRepository.save(arbitro);
    }
}