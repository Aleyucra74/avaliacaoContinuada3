package br.com.bandtec.avaliacaocontinuadatres.repository;

import br.com.bandtec.avaliacaocontinuadatres.interfaces.TipoNadadorInterface;
import br.com.bandtec.avaliacaocontinuadatres.model.TipoCorredor;
import br.com.bandtec.avaliacaocontinuadatres.model.TipoNadador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TipoNadadorRepository extends JpaRepository<TipoNadador, Integer> {

    @Query(value = "select * from tipo_nadador where tipo=:nadador", nativeQuery = true)
    TipoNadador encontrarPorId(@Param("nadador") String nadador);

}
