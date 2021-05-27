package br.com.bandtec.avaliacaocontinuadatres.controller;

import br.com.bandtec.avaliacaocontinuadatres.model.Atleta;
import br.com.bandtec.avaliacaocontinuadatres.model.TipoCorredor;
import br.com.bandtec.avaliacaocontinuadatres.model.TipoNadador;
import br.com.bandtec.avaliacaocontinuadatres.repository.AtletaRepository;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintValidator;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AtletaControllerTest {

    @Autowired
    AtletaController atletaController;

    @MockBean
    AtletaRepository repository;

    @Test
    void getAtletas() {
        List<Atleta> atletaList = Arrays.asList(new Atleta(),new Atleta(),new Atleta());

        Mockito.when(repository.findAll()).thenReturn(atletaList);

        ResponseEntity<List<Atleta>> responseEntity = atletaController.getAtletas();

        assertEquals(200,responseEntity.getStatusCodeValue());
    }

    @Test
    void getAtletasById() {
        Atleta atleta = new Atleta();
        atleta.setId(1);
        atleta.setNomeAtleta("joao");
        atleta.setTreinoPorDia(2);
        atleta.setTipoDieta("diet");
        atleta.setTipoNadador(new TipoNadador());
        atleta.setTipoCorredor(new TipoCorredor());

        Mockito.when(repository.findById(1)).thenReturn(java.util.Optional.of(atleta));

        ResponseEntity responseEntity = atletaController.getAtletasById(1);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void getByName(){
        Optional<Atleta> atleta = repository.findById(1);
        assertTrue(atleta.get().getNomeAtleta().equals("jose"));
        assertNotNull(atleta);
    }

    @Test
    void getListAtleta(){
        List<Atleta> atletaList = repository.findAll();
        assertTrue(atletaList.isEmpty());
    }

    @Test
    void updateAtleta(){
        Atleta atleta = new Atleta();
        atleta.setId(1);
        atleta.setNomeAtleta("joao");
        atleta.setTreinoPorDia(-2);
        atleta.setTipoDieta("diet");
        atleta.setTipoNadador(new TipoNadador());
        atleta.setTipoCorredor(new TipoCorredor());
        repository.save(atleta);
        Atleta responseEntity = atletaController.updateAtleta(atleta,1);
        assertEquals(1,atleta.getId());
    }

    @Test
    void deleteAtleta() {
        Atleta atleta = new Atleta();
        atleta.setId(1);
        Mockito.when(repository.deleteById(atleta.getId())).then("delete");

    }

    @Test
    void postAtleta(){
        Atleta atleta = new Atleta();

        ResponseEntity resposta = atletaController.postAtleta(atleta);

        assertEquals(200, resposta.getStatusCodeValue());
    }


}