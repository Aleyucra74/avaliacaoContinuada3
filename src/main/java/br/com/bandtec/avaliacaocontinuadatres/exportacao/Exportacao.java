package br.com.bandtec.avaliacaocontinuadatres.exportacao;

import br.com.bandtec.avaliacaocontinuadatres.controller.AtletaController;
import br.com.bandtec.avaliacaocontinuadatres.model.Atleta;
import br.com.bandtec.avaliacaocontinuadatres.model.TipoCorredor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Exportacao {

    static ListaObj<Atleta> atletaListaObj;

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

                for (int i = 0;i< projetos.getTamanho();i++) {
                    Atleta projeto = projetos.getElemento(i);
                    detail = contRegister>0 ? "11" : detail+"11";

                    contRegister++;
                    detail += String.format("%-15s", projeto.getId());
                    detail += String.format("%-15s", projeto.getNomeAtleta());
                    detail += String.format("%-15s", projeto.getTreinoPorDia());
                    detail += String.format("%-10s", projeto.getTipoDieta());
                    detail += String.format("%-15s", projeto.getTipoNadador().getTipo());
                    detail += String.format("%-15s", projeto.getTipoCorredor().getTipo())+"\n";

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

    public static void leExibeArquivo(boolean isCSV, String nomeArquivo) {
        FileReader arq= null;
        Scanner entrada = null;
        boolean deuRuim= false;

        if (isCSV) {
            nomeArquivo += ".csv";
        }
        else {
            nomeArquivo += ".txt";
        }

        try {
            arq = new FileReader(nomeArquivo);
            if (isCSV) {
                entrada = new Scanner(arq).useDelimiter(";|\\r\\n");
            }
            else {
                entrada = new Scanner(arq);
            }
        }
        catch (FileNotFoundException erro) {
            System.err.println("Arquivo não encontrado");
            System.exit(1);
        }

        try {

            // Enquanto tem registro a ser lido
            while (entrada.hasNext()) {
                Integer id = entrada.nextInt();			// Lê o id
                String nomeAtleta = entrada.next();			// Lê o nome
                Integer treinoPorDia = entrada.nextInt();          // Lê o porte
                String tipoDieta = entrada.next();		// Lê o peso
                String tipoCorredor = entrada.next();		// Lê o peso
                String tipoNadador = entrada.next();		// Lê o peso
//                atletaListaObj.adiciona(new Atleta(id,nomeAtleta,treinoPorDia,tipoDieta,tipoCorredor,tipoNadador));
            }
        }
        catch (NoSuchElementException erro)
        {
            System.err.println("Arquivo com problemas.");
            deuRuim = true;
        }
        catch (IllegalStateException erro)
        {
            System.err.println("Erro na leitura do arquivo.");
            deuRuim = true;
        }
        finally {
            entrada.close();
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
