package br.com.bandtec.avaliacaocontinuadatres.repository;

import br.com.bandtec.avaliacaocontinuadatres.interfaces.TipoCorredorInterface;
import br.com.bandtec.avaliacaocontinuadatres.model.TipoCorredor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TipoCorredorRepository extends JpaRepository<TipoCorredor, Integer> {

    @Query(value = "select * from tipo_corredor where tipo=:corredor", nativeQuery = true)
    TipoCorredor encontrarPorId(@Param("corredor") String corredor);

}
