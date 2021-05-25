package br.com.bandtec.avaliacaocontinuadatres.exportacao;

import br.com.bandtec.avaliacaocontinuadatres.controller.AtletaController;
import br.com.bandtec.avaliacaocontinuadatres.controller.TipoController;
import br.com.bandtec.avaliacaocontinuadatres.interfaces.TipoCorredorInterface;
import br.com.bandtec.avaliacaocontinuadatres.interfaces.TipoNadadorInterface;
import br.com.bandtec.avaliacaocontinuadatres.model.Atleta;
import br.com.bandtec.avaliacaocontinuadatres.model.TipoCorredor;
import br.com.bandtec.avaliacaocontinuadatres.model.TipoNadador;
import br.com.bandtec.avaliacaocontinuadatres.repository.TipoCorredorRepository;
import br.com.bandtec.avaliacaocontinuadatres.repository.TipoNadadorRepository;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Exportacao {

    static ListaObj<Atleta> atletaListaObj;
    TipoCorredorRepository corredorRepository;
    TipoNadadorRepository tipoNadadorRepository;
    TipoController tipoController;

    public void gravaLista(ListaObj<Atleta> projetos, boolean isCSV, String nomeArquivo) {
        FileWriter arq = null;
        Formatter saida = null;
        boolean deuRuim = false;
        atletaListaObj = new ListaObj<>(projetos.getTamanho());

        if (isCSV) {
            nomeArquivo += ".csv";
            try {
                arq = new FileWriter(
                        "src/main/resources/static/"+nomeArquivo,
                        true);
                saida = new Formatter(arq);
            }
            catch (IOException erro) {
                System.err.println("Erro ao abrir arquivo");
                System.exit(1);
            }

            try {
                for (int i=0; i< projetos.getTamanho(); i++) {
                    Atleta projeto = projetos.getElemento(i);
                    if (isCSV) {
                        saida.format("%s;%s;%s;%s;%s;%s%n",projeto.getId(),projeto.getNomeAtleta(),
                                projeto.getTreinoPorDia(),projeto.getTipoDieta(),projeto.getTipoCorredor().getTipo(), projeto.getTipoNadador().getTipo());
                    }
//                    else {
//                        saida.format("%s %s %s %s%n",projeto.getNome(),
//                                projeto.getTitulo(),projeto.getTecnologia(),projeto.getSoftskills());
//                    }
                }
            }
            catch (FormatterClosedException erro) {
                System.err.println("Erro ao gravar no arquivo");
                deuRuim= true;
            }
            finally {
                saida.close();
                try {
                    arq.close();
                }
                catch (IOException erro) {
                    System.err.println("Erro ao fechar arquivo.");
                    deuRuim = true;
                }
                if (deuRuim) {
                    System.exit(1);
                }
            }
        }
        else {
            nomeArquivo += ".txt";
            String header = "";
            String detail = "";
            String trailer = "";
            int contRegister = 0;

            try {
                arq = new FileWriter(
                        "src/main/resources/static/"+nomeArquivo,
                        true);
                saida = new Formatter(arq);
            }
            catch (IOException erro) {
                System.err.println("Erro ao abrir arquivo");
                System.exit(1);
            }
            try {
                Date todayData = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                SimpleDateFormat formatterMonth = new SimpleDateFormat("MM");
                SimpleDateFormat formatterYear = new SimpleDateFormat("yyyy");
                int semester = (Integer.parseInt(formatterMonth.format(todayData)) > 6)? 02 : 01 ;

                header += "00PROJETO"+formatterYear.format(todayData)+semester;
                header += formatter.format(todayData)+"01\n";

                saida.format(header);

//                for (int i = 0; i < projetos.getTamanho(); i++ ) {
//                    Atleta projeto = projetos.getElemento(i);
//                    if(detail.isEmpty()) {
//                        detail = "10" + String.format("%02d", contRegister) + String.format("%-50s", projeto.getNomeAtleta())+"\n";
//                        saida.format(detail);
//                    }
//                    else{
//                        detail="";
//                        break;
//                    }
//                }
                for (int i = 0; i < projetos.getTamanho(); i++) {
                    Atleta projeto = projetos.getElemento(i);
                    detail = contRegister>0 ? "11" : detail+"11";
                    contRegister++;

                    detail += String.format("%-2s", projeto.getTipoNadador().getId());
                    detail += String.format("%-10s", projeto.getTipoNadador().getTipo());
                    detail += String.format("%-2s", projeto.getTipoCorredor().getId());
                    detail += String.format("%-10s", projeto.getTipoCorredor().getTipo())+"\n";
                    saida.format(detail);

                }

                for (int i = 0;i< projetos.getTamanho();i++) {
                    Atleta projeto = projetos.getElemento(i);
                    detail = contRegister>0 ? "12" : detail+"12";

                    contRegister++;
                    detail += String.format("%-15s", projeto.getId());
                    detail += String.format("%-15s", projeto.getNomeAtleta());
                    detail += String.format("%-15s", projeto.getTreinoPorDia());
                    detail += String.format("%-10s", projeto.getTipoDieta())+"\n";
                    saida.format(detail);
                }


                trailer += "02";
                trailer += String.format("%010d",contRegister);
                saida.format(trailer);
            }
            catch (FormatterClosedException erro) {
                System.err.println("Erro ao gravar no arquivo");
                deuRuim= true;
            }
            finally {
                saida.close();
                try {
                    arq.close();
                }
                catch (IOException erro) {
                    System.err.println("Erro ao fechar arquivo.");
                    deuRuim = true;
                }
                if (deuRuim) {
                    System.exit(1);
                }
            }
        }
    }

    public ListaObj<Atleta> leArquivo(String nomeArq) {
        ListaObj<Atleta> atletaUpload = new ListaObj<>(15);
        FilaObj<TipoNadador> tipoNadadorListaObj = new FilaObj<>(15);
        FilaObj<TipoCorredor> tipoCorredorListaObj = new FilaObj<>(15);
        BufferedReader entrada = null;
        String registro;
        String tipoRegistro;

        String nomeAtleta, tipoDieta,tipoCorredorTipo,tipoNadadorTipo;
        int id, treinoPorDia,tipoCorredorId,tipoNadadorId;

        int contRegistro=0;

        // Abre o arquivo
        try {
            entrada = new BufferedReader(new FileReader(nomeArq));
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }

        // Lê os registros do arquivo
        try {
            // Lê um registro
            registro = entrada.readLine();

            while (registro != null) {
                // Obtém o tipo do registro
                tipoRegistro = registro.substring(0, 2); // obtém os 2 primeiros caracteres do registro

                if (tipoRegistro.equals("11")) {
                    TipoCorredor tipo = new TipoCorredor();
                    TipoNadador tipoN = new TipoNadador();

                    tipoCorredorId = Integer.parseInt(registro.substring(2,4).trim());
                    tipoCorredorTipo= registro.substring(4,14);

                    tipoNadadorId = Integer.parseInt(registro.substring(14,16).trim());
                    tipoNadadorTipo = registro.substring(16,26).trim();

                    tipo.setId(tipoCorredorId);
                    tipo.setTipo(tipoCorredorTipo);

                    tipoN.setId(tipoNadadorId);
                    tipoN.setTipo(tipoNadadorTipo);
                    tipoNadadorListaObj.insert(tipoN);
                    tipoCorredorListaObj.insert(tipo);
                    System.out.printf(
                            "%-2d %-10s %-2d %-10s\n",
                            tipo.getId(),
                            tipo.getTipo(),
                            tipoN.getId(),
                            tipoN.getTipo()
                    );

                }
                if (tipoRegistro.equals("12")) {
                    id = Integer.parseInt(registro.substring(2,17).trim());
                    nomeAtleta = registro.substring(17,32).trim();
                    treinoPorDia = Integer.parseInt(registro.substring(32,47).trim());
                    tipoDieta = registro.substring(47,57).trim();

                    TipoCorredor tipoCorredorFila = tipoCorredorListaObj.poll();
                    TipoNadador tipoNadadorFila = tipoNadadorListaObj.poll();

                    System.out.printf(
                            "%-15d %-15s %-15d %-10s %-2d %-10s %-2d %-10s\n",
                            id,
                            nomeAtleta,
                            treinoPorDia,
                            tipoDieta,
                            tipoCorredorFila.getId(),
                            tipoCorredorFila.getTipo(),
                            tipoNadadorFila.getId(),
                            tipoNadadorFila.getTipo()
                    );

                    Atleta novoAtleta = new Atleta();

                    novoAtleta.setId(id);
                    novoAtleta.setNomeAtleta(nomeAtleta);
                    novoAtleta.setTreinoPorDia(treinoPorDia);
                    novoAtleta.setTipoDieta(tipoDieta);
                    novoAtleta.setTipoCorredor(tipoCorredorFila);
                    novoAtleta.setTipoNadador(tipoNadadorFila);
                    //add na lista
                    atletaUpload.adiciona(novoAtleta);

                    contRegistro++;
                }
                else {
                    System.out.println("Tipo de registro inválido");
                }

                // lê o próximo registro
                registro = entrada.readLine();
            }

            // Fecha o arquivo
            entrada.close();
        } catch (IOException e) {
            System.err.printf("Erro ao ler arquivo: %s.\n", e.getMessage());
        }

        return atletaUpload;
    }
}
