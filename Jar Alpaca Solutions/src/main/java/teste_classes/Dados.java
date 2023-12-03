package teste_classes;

import cliente.Cliente;
import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.sistema.Sistema;
import conexao_banco_dados.Conexao;
import conexao_banco_dados.ConexaoBancoDados;
import disco.Disco;
import maquina.Maquina;
import mensageria.Alertas;
import org.springframework.jdbc.core.JdbcTemplate;
import rede.Rede;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Dados {
    public static void main(String[] args) {
        // Inicializa a conexão com o banco de dados
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        Scanner leitor = new Scanner(System.in);
        String continuar;
        Timer tempo = new Timer();
        Alertas alerta = new Alertas();

        System.out.println("Olá Bem Vindo ao Sistema de Monitoramento Alpaca Solutions");

        System.out.println("Digite seu Email");
        String email = leitor.nextLine();

        System.out.println("Agora digite sua senha:");
        String senha = leitor.nextLine();

        Cliente cliente = new Cliente(email , senha);


        ConexaoBancoDados busca_cliente = new ConexaoBancoDados();


        Integer resultados = busca_cliente.Busca_Cliente(cliente);

        Integer resultado_select_cliente = busca_cliente.Busca_Cliente(cliente);


        if(resultado_select_cliente == 200) {
            System.out.println("  ___   _                              _____         _         _    _                    \n" +
                    " / _ \\ | |                            /  ___|       | |       | |  (_)                   \n" +
                    "/ /_\\ \\| | _ __    __ _   ___   __ _  \\ `--.   ___  | | _   _ | |_  _   ___   _ __   ___ \n" +
                    "|  _  || || '_ \\  / _` | / __| / _` |  `--. \\ / _ \\ | || | | || __|| | / _ \\ | '_ \\ / __|\n" +
                    "| | | || || |_) || (_| || (__ | (_| | /\\__/ /| (_) || || |_| || |_ | || (_) || | | |\\__ \\\n" +
                    "\\_| |_/|_|| .__/  \\__,_| \\___| \\__,_| \\____/  \\___/ |_| \\__,_| \\__||_| \\___/ |_| |_||___/\n" +
                    "          | |                                                                            \n" +
                    "          |_|                                                                            ");

            TimerTask task = new TimerTask() {
                AtomicBoolean insercaoRealizada = new AtomicBoolean(false);


                @Override
                public void run() {
                    Looca looca = new Looca();



                    // Funcão para capturar dados do processador e mandar para as classes
                    Memoria memoria = new Memoria();
                    Long memoria_em_uso = memoria.getEmUso();
                    Long memoria_diponivel = memoria.getDisponivel();
                    Long memoria_total = memoria.getTotal();
                    memoria.Memoria dados_memoria = new memoria.Memoria(memoria_em_uso, memoria_diponivel, memoria_total);

                    dados_memoria.setMemoria_em_uso(dados_memoria.calcular_memoria_em_uso(memoria_em_uso));
                    dados_memoria.setMemoria_Disponivel(dados_memoria.calcular_memoria_disponivel(memoria_diponivel));
                    dados_memoria.setMemoria_total(dados_memoria.calcular_memoria_em_uso(memoria_total));

                    System.out.println(dados_memoria);
                    // Ram não é preciso pq a api ja junta todos os dados alocados de memoria do sistema e mostra junto;

                    // Função Disco

                    Disco disco01 = new Disco();

                    disco01.setTamanho_Total_do_Disco(disco01.total_tamanho_disco());
                    disco01.setPorcentagem_de_Uso_do_Disco(disco01.percentual_uso_do_disco());
                    disco01.setTamanho_disponivel_do_disco(disco01.tamanho_disponivel_do_disco());

                    System.out.println(disco01);

                    // Disco Completo:)

                    // Função Rede


                    Rede rede = new Rede();


                    rede.setQuantidade_bytes_enviados(rede.quantidade_bytes_enviados_total());
                    rede.setQuantidade_bytes_recebidos(rede.quantidade_bytes_recebidos_total());


                    System.out.println(rede);
                    // função rede completa :)


                    // Dados Maquina

                    Sistema sisitema01 = new Sistema();
                    Maquina maquina01 = new Maquina();

                    maquina01.setSistema_Operacional(looca.getSistema().getSistemaOperacional());

                    try {
                        maquina01.setIpMaquina(maquina01.obterIP());
                    } catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                      maquina01.setNomeMaquina(maquina01.obterNomeMaquina());

                    }
                    catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    }


                    // maquina ativa ou não

                    Boolean statusMaquina;

                    try {
                        boolean temInternet = maquina01.statusMaquina();
                        if (temInternet) {
                            statusMaquina = true;
                        } else {
                            statusMaquina = false;
                        }
                    } catch (MalformedURLException e) {
                        statusMaquina = false;
                    } catch (IOException e) {
                        statusMaquina = false;
                    }

                    System.out.println("Status da Máquina " + statusMaquina);
                    maquina01.setStatusMaquina(statusMaquina);
                    System.out.println(statusMaquina);
                    System.out.println(maquina01);

                    // Cpu

                    Processador processador01 = new Processador();

                    processador.Processador processador = new processador.Processador(processador01.getUso());

                    List<processador.Processador> lista_processador = new ArrayList<>();

                    lista_processador.add(processador);


                    System.out.println(processador);

                    ConexaoBancoDados insertMaquina = new ConexaoBancoDados();

                    Integer fk_maquina = null;

                    Integer fk_maquinaNuvem = null;

                    if (insercaoRealizada.compareAndSet(false, true)) {
                        Map<String, Object> resultadoLocal = insertMaquina.buscar_empresa_e_unidade_local(cliente);
                        Integer idUnidadeLocal = resultadoLocal != null ? (Integer) resultadoLocal.get("idUnidade") : null;
                        Integer idEmpresaLocal = resultadoLocal != null ? (Integer) resultadoLocal.get("idEmpresa") : null;

                        insertMaquina.obterFksTipoComponente();
                        Map<String, Object> resultadoNuvem = insertMaquina.buscar_empresa_e_unidade_nuvem(cliente);
                        Integer idUnidadeNuvem = resultadoNuvem != null ? (Integer) resultadoNuvem.get("idUnidade") : null;
                        Integer idEmpresaNuvem = resultadoNuvem != null ? (Integer) resultadoNuvem.get("idEmpresa") : null;

                        insertMaquina.inserir_dados_maquina(
                                maquina01.getNomeMaquina(),
                                maquina01.getIpMaquina(),
                                maquina01.getSistema_Operacional(),
                                maquina01.getStatusMaquina(),
                                idUnidadeLocal,
                                idEmpresaLocal
                        );
                        Integer valorBancoNuvem;
                        if(maquina01.getStatusMaquina().equals(true)){
                            valorBancoNuvem = 1;
                        }
                        else{
                            valorBancoNuvem = 0;
                        }

                        insertMaquina.inserir_dados_maquinaNuvem(
                                maquina01.getNomeMaquina(),
                                maquina01.getIpMaquina(),
                                maquina01.getSistema_Operacional(),
                                valorBancoNuvem,
                                idEmpresaNuvem,
                                idUnidadeNuvem
                        );
                        List<Integer> idsMaquina = insertMaquina.buscar_fk_maquina(maquina01.getNomeMaquina());
                        fk_maquina = !idsMaquina.isEmpty() ? idsMaquina.get(0) : 0;
                        insertMaquina.atualizarFkUnidadeMedida(fk_maquina);

                        List<Integer> idsMaquinaNuvem = insertMaquina.buscar_fk_maquinaNuvem(maquina01.getNomeMaquina());

                        fk_maquinaNuvem = !idsMaquinaNuvem.isEmpty() ? idsMaquinaNuvem.get(0) : 0;
                        insertMaquina.atualizarFkUnidadeMedidaNuvem(fk_maquinaNuvem);

                    }
                    List<Integer> idsMaquinaNuvem = insertMaquina.buscar_fk_maquinaNuvem(maquina01.getNomeMaquina());
                    fk_maquinaNuvem = !idsMaquinaNuvem.isEmpty() ? idsMaquinaNuvem.get(0) : 0;
                    Map<String, Integer> valoresFk = insertMaquina.obterFksTipoComponente();

                    Integer fkMemoriaUsada = valoresFk.get("FkMemoriaUsada");
                    Integer fkMemoriaEmUso = valoresFk.get("fkMemoriaEmUso");
                    Integer fkMemoriaDipsonivel = valoresFk.get("fkMemoriaDisponivel");
                    Integer fkpercetDisco =valoresFk.get("fkPercentualDisco");
                    Integer fktamanhoDisco =valoresFk.get("fkTamanhoDisco");
                    Integer fkTamanhoDisponivelDisco =valoresFk.get("fkTamanhoDisponivelDisco");
                    Integer fkpercentualProcessador=valoresFk.get("fkPercentualProcessador");
                    Integer fkBytesRecebidos =valoresFk.get("fkBytesRecebidos");
                    Integer fkBytesEnviados=valoresFk.get("fkBytesEnviados");
                    insertMaquina.InserirTabelaConfiguracoes(fk_maquina , fkMemoriaUsada , fkMemoriaEmUso ,
                            fkMemoriaDipsonivel , fkpercetDisco , fktamanhoDisco , fkTamanhoDisponivelDisco , fkpercentualProcessador,
                            fkBytesRecebidos , fkBytesEnviados);




                    Map<String, Integer> valoresFkNuvem = insertMaquina.obterFksTipoComponenteNuvem();

                    Integer fkMemoriaUsadaNuvem = valoresFkNuvem.get("FkMemoriaUsada");
                    Integer fkMemoriaEmUsoNuvem = valoresFkNuvem.get("fkMemoriaEmUso");
                    Integer fkMemoriaDipsonivelNuvem = valoresFkNuvem.get("fkMemoriaDisponivel");
                    Integer fkpercetDiscoNuvem =valoresFkNuvem.get("fkPercentualDisco");
                    Integer fktamanhoDiscoNuvem =valoresFkNuvem.get("fkTamanhoDisco");
                    Integer fkTamanhoDisponivelDiscoNuvem =valoresFkNuvem.get("fkTamanhoDisponivelDisco");
                    Integer fkpercentualProcessadorNuvem=valoresFkNuvem.get("fkPercentualProcessador");
                    Integer fkBytesRecebidosNuvem =valoresFkNuvem.get("fkBytesRecebidos");
                    Integer fkBytesEnviadosNuvem=valoresFkNuvem.get("fkBytesEnviados");

                    insertMaquina.InserirTabelaConfiguracoesNuvem(fk_maquinaNuvem , fkMemoriaUsadaNuvem , fkMemoriaEmUsoNuvem ,
                            fkMemoriaDipsonivelNuvem , fkpercetDiscoNuvem , fktamanhoDiscoNuvem , fkTamanhoDisponivelDiscoNuvem , fkpercentualProcessadorNuvem,
                            fkBytesRecebidosNuvem , fkBytesEnviadosNuvem);



                    List<Integer> idsMaquina = insertMaquina.buscar_fk_maquina(maquina01.getNomeMaquina());



                    Integer fk = !idsMaquina.isEmpty() ? idsMaquina.get(0) : 0;
                    List<Integer> fkUnidadeMaquina = insertMaquina.buscarFkUnidade(fk);
                    Integer fkUnidade = !fkUnidadeMaquina.isEmpty() ? fkUnidadeMaquina.get(0) : 0;

                    List<Integer> fkUnidadeMaquinaNuvem = insertMaquina.buscar_fk_maquinaNuvem(maquina01.getNomeMaquina());
                    Integer fkUnidadeNuvem = !fkUnidadeMaquinaNuvem.isEmpty() ? fkUnidadeMaquinaNuvem.get(0) : 0;

                    Map<String, Integer> configuracoes = insertMaquina.buscarFksConfiguracao();


                    Integer fkConfigMemoriaUsada = configuracoes.get("ConfiguracaoMemoriaUsada");
                    Integer fkConfigMemoriaemUso = configuracoes.get("ConfiguracaoMemoriaEmUso");
                    Integer fkConfigMemoriaDisponivel = configuracoes.get("ConfiguracaoMemoriaDisponivel");
                    Integer fkConfigPercentDisco = configuracoes.get("ConfiguracaoPercentualUsoDisco");
                    Integer fkConfigTamanhoDisco = configuracoes.get("ConfiguracaoTamanhoDisco");
                    Integer fkConfigTamanhoDisponivelDisco = configuracoes.get("ConfiguracaoTamanhoDisponivel");
                    Integer fkconfigPercentualProcessador = configuracoes.get("ConfiguracaoPercentualUsoProcessador");
                    Integer fkConfigBytesRecebidos = configuracoes.get("ConfiguracaoBytesRecebidos");
                    Integer fkConfigBytesEnviados = configuracoes.get("ConfiguracaoBytesEnviados");


                    Map<String, Integer> configuracoesNuvem = insertMaquina.buscarFksConfiguracaoNuvem();


                    Integer fkConfigMemoriaUsadaNuvem  = configuracoesNuvem.get("ConfiguracaoMemoriaUsada");
                    Integer fkConfigMemoriaemUsoNuvem  = configuracoesNuvem.get("ConfiguracaoMemoriaEmUso");
                    Integer fkConfigMemoriaDisponivelNuvem  = configuracoesNuvem.get("ConfiguracaoMemoriaDisponivel");
                    Integer fkConfigPercentDiscoNuvem  = configuracoesNuvem.get("ConfiguracaoPercentualUsoDisco");
                    Integer fkConfigTamanhoDiscoNuvem  = configuracoesNuvem.get("ConfiguracaoTamanhoDisco");
                    Integer fkConfigTamanhoDisponivelDiscoNuvem  = configuracoesNuvem.get("ConfiguracaoTamanhoDisponivel");
                    Integer fkconfigPercentualProcessadorNuvem  = configuracoesNuvem.get("ConfiguracaoPercentualUsoProcessador");
                    Integer fkConfigBytesRecebidosNuvem  = configuracoesNuvem.get("ConfiguracaoBytesRecebidos");
                    Integer fkConfigBytesEnviadosNuvem  = configuracoesNuvem.get("ConfiguracaoBytesEnviados");

                    insertMaquina.inserirMedicoes(dados_memoria, rede, processador, disco01, fk, fkConfigMemoriaUsadaNuvem,
                            fkMemoriaEmUsoNuvem, fkMemoriaDipsonivelNuvem, fkpercetDiscoNuvem , fktamanhoDiscoNuvem , fkTamanhoDisponivelDiscoNuvem, fkpercentualProcessadorNuvem,
                            fkBytesRecebidosNuvem, fkBytesEnviadosNuvem, fkUnidade);

                    insertMaquina.inserirMedicoesNuvem(dados_memoria, rede, processador, disco01, fk_maquinaNuvem, fkMemoriaUsadaNuvem,
                            fkMemoriaEmUsoNuvem, fkMemoriaDipsonivelNuvem, fkpercetDiscoNuvem , fktamanhoDiscoNuvem , fkTamanhoDisponivelDiscoNuvem, fkpercentualProcessadorNuvem,
                            fkBytesRecebidosNuvem, fkBytesEnviadosNuvem, fkUnidadeNuvem);



                    insertMaquina.Alertas(
                            dados_memoria, disco01, rede, processador,fk, fkUnidade, fkMemoriaUsada, fkMemoriaEmUso,
                            fkMemoriaDipsonivel, fkpercetDisco, fktamanhoDisco, fkTamanhoDisponivelDisco, fkpercentualProcessador, fkBytesRecebidos, fkBytesEnviados, fkConfigMemoriaUsada,
                            fkConfigMemoriaemUso, fkConfigMemoriaDisponivel, fkConfigPercentDisco, fkConfigTamanhoDisco,
                            fkConfigTamanhoDisponivelDisco, fkconfigPercentualProcessador, fkConfigBytesRecebidos, fkConfigBytesEnviados
                    );

                    insertMaquina.AlertasNuvem(
                            dados_memoria, disco01, rede, processador,fk_maquinaNuvem, fkUnidadeNuvem, fkMemoriaUsadaNuvem, fkMemoriaEmUsoNuvem,
                            fkMemoriaDipsonivelNuvem, fkpercetDiscoNuvem, fktamanhoDiscoNuvem, fkTamanhoDisponivelDiscoNuvem, fkpercentualProcessadorNuvem,
                            fkBytesRecebidosNuvem, fkBytesEnviadosNuvem, fkConfigMemoriaUsadaNuvem,
                            fkConfigMemoriaemUsoNuvem, fkConfigMemoriaDisponivelNuvem, fkConfigPercentDiscoNuvem, fkConfigTamanhoDiscoNuvem,
                            fkConfigTamanhoDisponivelDiscoNuvem, fkconfigPercentualProcessadorNuvem, fkConfigBytesRecebidosNuvem, fkConfigBytesEnviadosNuvem
                    );
                    try {
                        alerta.Alerta(insertMaquina.AlertaMemoria(fk));
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            };
            long delay_de_tempo = 0;
            long tempo_carregar = 100;

            tempo.scheduleAtFixedRate(task, delay_de_tempo, tempo_carregar);

        }
        else{
            System.out.println("Você ainda não fez cadastro no nosso sistema :(");
        }
    }
}
