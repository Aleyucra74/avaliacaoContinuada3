package br.com.bandtec.avaliacaocontinuadatres.controller;

import br.com.bandtec.avaliacaocontinuadatres.exportacao.Exportacao;
import br.com.bandtec.avaliacaocontinuadatres.exportacao.ListaObj;
import br.com.bandtec.avaliacaocontinuadatres.exportacao.PilhaObj;
import br.com.bandtec.avaliacaocontinuadatres.model.Atleta;
import br.com.bandtec.avaliacaocontinuadatres.model.TipoCorredor;
import br.com.bandtec.avaliacaocontinuadatres.model.TipoDefault;
import br.com.bandtec.avaliacaocontinuadatres.model.TipoNadador;
import br.com.bandtec.avaliacaocontinuadatres.repository.AtletaRepository;
import br.com.bandtec.avaliacaocontinuadatres.repository.TipoCorredorRepository;
import br.com.bandtec.avaliacaocontinuadatres.repository.TipoNadadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@RequestMapping("/atletas")
@RestController
public class AtletaController {

    Exportacao exportacao = new Exportacao();
    PilhaObj<Integer> atletaPilhaObj = new PilhaObj<>(1);
    TipoController tipoController = new TipoController();

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
            atletaPilhaObj.push(atleta.getId());
            atletaPilhaObj.exibe();
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

    @GetMapping("/download/{isCsv}")
    public ResponseEntity getDownloadAtletas(HttpServletResponse response, @PathVariable("isCsv") boolean isCsv) throws IOException {
        List<Atleta> atletaList = repository.findAll();
        ListaObj<Atleta> atletaListaObj = new ListaObj<>(atletaList.size());

        for (int i = 0; i < atletaList.size(); i++) {
            Atleta atleta = atletaList.get(i);
            atletaListaObj.adiciona(atleta);
        }
        exportacao.gravaLista(atletaListaObj,isCsv,"Atletas");

        String downloadFolder = "src/main/resources/static/";

        if (isCsv) {
            Path file = Paths.get(downloadFolder, "Atletas.csv");
            response.setContentType("text/csv");
            response.addHeader("Content-Disposition","attachment; filename=Atletas.csv");
            Files.copy(file, response.getOutputStream());
            response.getOutputStream().flush();
        }else {
            Path file = Paths.get(downloadFolder, "Atletas.txt");
            response.setContentType("text/plain");
            response.addHeader("Content-Disposition","attachment; filename=Atletas.txt");
            Files.copy(file,response.getOutputStream());
            response.getOutputStream().flush();
        }
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/desfazer")
    public ResponseEntity deleteDesfazer(){
        Integer pilha = atletaPilhaObj.pop();
        repository.deleteById(pilha);
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/async/novo-atleta")
    public ResponseEntity postAsyncMethod(){
        LocalDateTime previsao = LocalDateTime.now().plusSeconds(16);
        Integer numero = ThreadLocalRandom.current().nextInt(0,100);


        Thread sorteador = new Thread(() -> {
            try {

                Thread.sleep(10000);

//                ListaObj<TipoDefault> lista = (ListaObj<TipoDefault>) tipoController.getTipo().getBody();

//                Integer random = ThreadLocalRandom.current().nextInt(0, lista.getTamanho());

//                TipoCorredor randomCorredor = new TipoCorredor(123,"plano");
//                TipoNadador randomNadador = new TipoNadador(234,"costas");
////            tipocorredor getcorredor = tipoController.getTipoNadador().getBody();
                TipoCorredor getCorredor = new TipoCorredor();
                getCorredor.setId(1);
                getCorredor.setTipo("borboleta");

                TipoNadador getNadador = new TipoNadador();
                getNadador.setId(1);
                getNadador.setTipo("2km");

                Atleta novoAtleta = new Atleta();
                novoAtleta.setId(numero);
                novoAtleta.setNomeAtleta("Felps");
                novoAtleta.setTreinoPorDia(8);
                novoAtleta.setTipoDieta("lowcarb");
                novoAtleta.setTipoCorredor(getCorredor);
                novoAtleta.setTipoNadador(getNadador);

                repository.save(novoAtleta);

            }catch (InterruptedException e){
                e.printStackTrace();
            }
        });
        sorteador.start();
        return ResponseEntity.status(202)
                .header("protocolo", String.valueOf(numero))
                .header("previsao", previsao.toString())
                .body("Em 1minuto estara um novo atleta adicionado");
    }

    @GetMapping("/{protocolo}")
    public ResponseEntity getProtocolo(@PathVariable Integer numero){
        Optional<Atleta> optionalAtleta = repository.findById(numero);

        if (optionalAtleta.isPresent()){
            return ResponseEntity.status(200).body("Novo Atleta:"+
                    optionalAtleta.get().getNomeAtleta());
        }else{
            LocalDateTime previsao = LocalDateTime.now().plusSeconds(15);
            return ResponseEntity.status(404).header("previsao",previsao.toString()).build();
        }

    }

}



