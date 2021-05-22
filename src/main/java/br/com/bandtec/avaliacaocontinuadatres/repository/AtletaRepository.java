package br.com.bandtec.avaliacaocontinuadatres.repository;

import br.com.bandtec.avaliacaocontinuadatres.model.Atleta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtletaRepository extends JpaRepository<Atleta, Integer> {
}
