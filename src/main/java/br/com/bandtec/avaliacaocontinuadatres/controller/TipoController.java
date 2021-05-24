package br.com.bandtec.avaliacaocontinuadatres.controller;

import br.com.bandtec.avaliacaocontinuadatres.model.TipoCorredor;
import br.com.bandtec.avaliacaocontinuadatres.model.TipoNadador;
import br.com.bandtec.avaliacaocontinuadatres.repository.TipoCorredorRepository;
import br.com.bandtec.avaliacaocontinuadatres.repository.TipoNadadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/tipo")
public class TipoController {

    @Autowired
    private TipoNadadorRepository tipoNadadorRepository;

    @Autowired
    private TipoCorredorRepository corredorRepository;

    @GetMapping("/corredor/{id}")
    public TipoCorredor getTipoCorredor(@PathVariable Integer id){
        Optional<TipoCorredor> tipoCorredor = corredorRepository.findById(id);

        return tipoCorredor.orElse(null);
    }

    @GetMapping("/nadador/{id}")
    public TipoNadador getTipoNadador(@PathVariable Integer id){
        Optional<TipoNadador> tipoNadador = tipoNadadorRepository.findById(id);
        return tipoNadador.orElse(null);
    }

    @GetMapping("/corredor-id/{tipo}")
    public TipoCorredor getTipoCorredorPorId(@PathVariable String tipo){
        TipoCorredor tipoNadador = corredorRepository.encontrarPorId(tipo);
        return tipoNadador;
    }

    @GetMapping("/nadador-id/{tipo}")
    public TipoNadador getTipoNadadorPorId(@PathVariable String tipo){
        TipoNadador tipoNadador = tipoNadadorRepository.encontrarPorId(tipo);
        return tipoNadador;
    }

}
