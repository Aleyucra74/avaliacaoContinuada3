package br.com.bandtec.avaliacaocontinuadatres.controller;

import br.com.bandtec.avaliacaocontinuadatres.model.Atleta;
import br.com.bandtec.avaliacaocontinuadatres.model.TipoDefault;
import br.com.bandtec.avaliacaocontinuadatres.repository.AtletaRepository;
import br.com.bandtec.avaliacaocontinuadatres.repository.TipoCorredorRepository;
import br.com.bandtec.avaliacaocontinuadatres.repository.TipoNadadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RequestMapping("/atletas")
@RestController
public class AtletaController {

    @Autowired
    private AtletaRepository repository;

    @Autowired
    private TipoCorredorRepository corredorRepository;

    @Autowired
    private TipoNadadorRepository tipoNadadorRepository;

    @GetMapping
    public ResponseEntity getAtletas(){ return ResponseEntity.status(200).body(repository.findAll());}

    @GetMapping("/{id}")
    public ResponseEntity getAtletasById(@PathVariable Integer id){
        return ResponseEntity.status(200).body(repository.findAllById(Collections.singleton(id)));
    }

    @PostMapping
    public ResponseEntity postAtleta(@RequestBody Atleta atleta){
        if (tipoNadadorRepository.existsById(atleta.getTipoNadador().getId()) || corredorRepository.existsById(atleta.getTipoCorredor().getId())) {
            repository.save(atleta);
            return ResponseEntity.status(201).build();
        }else{
            return ResponseEntity.status(404).body("Tipo nao encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAtleta(@PathVariable Integer id){
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.status(200).build();
        }else{
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping("/{id}")
    public Atleta updateAtleta(@RequestBody Atleta atleta, @PathVariable Integer id) {
        return repository.findById(id).map(atleta1 -> {
            atleta1.setNomeAtleta(atleta.getNomeAtleta());
            atleta1.setTreinoPorDia(atleta.getTreinoPorDia());
            atleta1.setTipoDieta(atleta.getTipoDieta());
            atleta1.setTipoCorredor(atleta.getTipoCorredor());
            atleta1.setTipoNadador(atleta.getTipoNadador());
            return repository.save(atleta1);
        }).orElseGet(() -> {
            atleta.setId(id);
            return repository.save(atleta);
        });
    }

}



