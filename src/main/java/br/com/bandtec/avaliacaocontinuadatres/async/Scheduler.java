package br.com.bandtec.avaliacaocontinuadatres.async;

import br.com.bandtec.avaliacaocontinuadatres.controller.AtletaController;
import br.com.bandtec.avaliacaocontinuadatres.exportacao.FilaObj;
import br.com.bandtec.avaliacaocontinuadatres.model.Atleta;
import br.com.bandtec.avaliacaocontinuadatres.repository.AtletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Scheduler {

    public FilaObj<Atleta> atletaFilaObj = new FilaObj<>(30);

    @Autowired
    private AtletaRepository repository;

    @Scheduled(fixedRate = 1000)
    public void asyncNewAtleta(){
        if (atletaFilaObj.isEmpty()){
            System.out.println("nada a se fazer");
        }else{
            repository.save(atletaFilaObj.poll());
        }
//        System.out.println(atletaFilaObj.poll());
    }

}
