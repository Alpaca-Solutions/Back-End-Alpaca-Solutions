package conexao_banco_dados;

import cliente.Cliente;
import disco.Disco;
import memoria.Memoria;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import processador.Processador;
import rede.Rede;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ConexaoBancoDados {
        List<Cliente> clienteList;

        public Integer Busca_Cliente(Cliente cliente){
            Conexao conexao = new Conexao();
            JdbcTemplate con = conexao.getConexaoDoBanco();
            Integer resultado =0;

            List<Cliente> resultado_select = con.query("SELECT 'Empresa' AS TipoCliente\n" +
                            "FROM Empresa\n" +
                            "WHERE email = ? AND senha = ?\n" +
                            "UNION\n" +
                            "SELECT 'Empregado' AS TipoCliente\n" +
                            "FROM Usuario\n" +
                            "WHERE email = ? AND senha = ?;",
                    new Object[]{cliente.getEmail(), cliente.getSenha(), cliente.getEmail(), cliente.getSenha()},
                    new BeanPropertyRowMapper<>(Cliente.class));


            if (resultado_select.size() > 0){
                resultado = 200;
            }
            else{
                resultado = 404;
            }

            return resultado;
        }

    public Map<String, Object> buscar_empresa_e_unidade(Cliente cliente) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        try {
            List<Map<String, Object>> resultados = con.queryForList(
                    "SELECT unidade.idUnidade, empresa.idEmpresa FROM empresa " +
                            "JOIN endereco ON fk_endereco = idendereco " +
                            "JOIN Unidade ON unidade.fkEndereco = idendereco " +
                            "WHERE empresa.email = ? AND empresa.senha = ?",
                    cliente.getEmail(), cliente.getSenha());

            if (resultados.isEmpty()) {
                return null;
            } else if (resultados.size() > 1) {
                return null;
            } else {
                return resultados.get(0);
            }
        } catch (IncorrectResultSizeDataAccessException ex) {
            System.out.println("Erro: " + ex.getMessage());
            return null;
        }
    }

    public void inserir_dados_maquina(String nome_maquina ,String ipMaquina, String so , Boolean status, Integer fk_empresa , Integer fKUnidade){
            Conexao conexao = new Conexao();
            JdbcTemplate con = conexao.getConexaoDoBanco();
            con.update("INSERT INTO Maquina (nomeMaquina, ipmaquina, sistemaOperacional, statusMaquina,FkEmpresa , fKUnidade ) VALUES (?, ?, ?, ?, ? , ?)", nome_maquina, ipMaquina, so, status, fk_empresa , fKUnidade);
        }

        public void inserir_tipo_componente(){
            Conexao conexao = new Conexao();
            JdbcTemplate con = conexao.getConexaoDoBanco();

            // memoria
            con.update("INSERT INTO TipoComponente (nomeTipo, tipoComponente) values (? , ? )", "Memoria Usada" , "Memoria");
            con.update("INSERT INTO TipoComponente (nomeTipo, tipoComponente) values (? , ? )", "Memoria em Uso" , "Memoria");
            con.update("INSERT INTO TipoComponente (nomeTipo, tipoComponente) values (? , ? )", "Memoria Disponível" , "Memoria");

            // disco

            con.update("INSERT INTO TipoComponente (nomeTipo, tipoComponente) values (? , ? )", "Percentual de Uso do Disco" , "Disco");
            con.update("INSERT INTO TipoComponente (nomeTipo, tipoComponente) values (? , ? )", "Tamanho do Disco" , "Disco");
            con.update("INSERT INTO TipoComponente (nomeTipo, tipoComponente) values (? , ? )", "Tamanho Disponível" , "Disco");

            // processador

            con.update("INSERT INTO TipoComponente (nomeTipo, tipoComponente) values (? , ? )", "Percentual de Uso do Processador" , "Processador");

            // rede

            con.update("INSERT INTO TipoComponente (nomeTipo, tipoComponente) values (? , ? )", "Bytes Recebidos" , "Rede");
            con.update("INSERT INTO TipoComponente (nomeTipo, tipoComponente) values (? , ? )", "Bytes Enviados" , "Rede");


        }

        public void InserirTabelaConfiguracoes(){
            Conexao conexao = new Conexao();
            JdbcTemplate con = conexao.getConexaoDoBanco();

            // Memória
            con.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoMemoriaUsada", 1, 1);

            con.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoMemoriaEmUso", 1, 2);

            con.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoMemoriaDisponivel", 1, 3);

// Disco
            con.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoPercentualUsoDisco", 1, 4);

            con.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoTamanhoDisco", 1, 5);

            con.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoTamanhoDisponivel", 1, 6);

                // Processador
            con.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoPercentualUsoProcessador", 1, 7);

             // Rede
            con.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoBytesRecebidos", 1, 8);

            con.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoBytesEnviados", 1, 9);

        }

    public Integer obterIdMaquina(String nomeMaquina) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        try {
            String sql = "SELECT idMaquina FROM maquina WHERE nomeMaquina = '?'";

            return con.queryForObject(sql, Integer.class, nomeMaquina);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<Integer> buscar_fk_maquina() {
        Conexao conexao = new Conexao();
        JdbcTemplate jdbcTemplate = conexao.getConexaoDoBanco();

        String sql = "SELECT idMaquina FROM maquina";
        List<Integer> idMaquinas = jdbcTemplate.queryForList(sql, Integer.class);

        return idMaquinas;
    }
        public void atualizarFkUnidadeMedida(Integer fk_maquina){
            Conexao conexao = new Conexao();
            JdbcTemplate con = conexao.getConexaoDoBanco();

            String sql = "UPDATE UnidadeMedida SET fkMaquina = ? WHERE idParametros = 1;\n";

            con.update(sql, fk_maquina);
        }

        public void inserirMedicoes(Memoria memoria , Rede rede , Processador processador, Disco disco, Integer fk_computador){
            Conexao conexao = new Conexao();
            JdbcTemplate con = conexao.getConexaoDoBanco();
            Date dataHoraAtual = new Date();

            // inserção de cpu
           con.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", processador.getPercentual_uso_do_processador(),dataHoraAtual, fk_computador, 7 , 1);

           // inserção de disco
            con.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", disco.getPorcentagem_de_Uso_do_Disco(),dataHoraAtual, fk_computador, 4 , 1);
            con.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", disco.getTamanho_Total_do_Disco(),dataHoraAtual, fk_computador, 5 , 1);
            con.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", disco.getTamanho_disponivel_do_disco(),dataHoraAtual, fk_computador, 6 , 1);


            // inserção de  Memoria

            con.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", memoria.getMemoria_Disponivel(),dataHoraAtual, fk_computador, 3 , 1);
            con.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", memoria.getMemoria_total(),dataHoraAtual, fk_computador, 2, 1);
            con.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", memoria.getMemoria_em_uso(),dataHoraAtual, fk_computador, 1 , 1);

            // inserção de rede

            con.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", rede.getQuantidade_bytes_enviados(),dataHoraAtual, fk_computador, 9 , 1);
            con.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", rede.getQuantidade_bytes_recebidos(),dataHoraAtual, fk_computador, 8 , 1);

        }

    public String AlertaMemoria(int idMaquina) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        String sql = "SELECT MA.mensagemAlerta " +
                "FROM MetricasAlertas MA " +
                "JOIN UnidadeMedida UM ON MA.fkUnidadeMedida = UM.idParametros " +
                "JOIN Maquina M ON UM.fkMaquina = M.idMaquina " +
                "WHERE M.idMaquina = ? " +
                "ORDER BY MA.dhHoraAlerta DESC " +
                "LIMIT 1";

        return con.query(sql, new Object[]{idMaquina}, resultSet -> {
            if (resultSet.next()) {
                return resultSet.getString("mensagemAlerta");
            }

            return null;
        });
    }



    public void Alertas(Memoria memoria,Disco disco,Rede rede, Integer fk_computador) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        Date dataHoraAtual = new Date();
        Double percentualDeUso = (memoria.getMemoria_em_uso() / memoria.getMemoria_total()) * 100.0;



        double limiteAlerta = 80.0;
        double limiteCritico = 90.0;
        String tipoComponenteM = "Memória";
        String tipoComponenteD = "Disco";
        String tipoComponenteR = "Rede";
        String mensagemAlertaRede = "Baixa atividade na rede.";
        String mensagemCriticoRede = "Alta atividade na rede.";
        String mensagemMemoriaCritico = "Estado crítico! O percentual de uso da memória está acima de " + limiteCritico + "%";
        String mensagemMemoriaAlerta = "Estado de alerta! O percentual de uso da memória está acima de " + limiteAlerta + "%";
        String mensagemDiscoAlerta = "Atenção! O espaço no disco está quase atingindo seu limite. Recomenda-se liberar espaço para evitar problemas de armazenamento.";
        String mensagemDiscoCritico = "Estado Crítico! O espaço no disco atingiu um nível perigoso. Ações imediatas são necessárias para liberar espaço e garantir o funcionamento adequado do sistema.";
        double minimoMemoria = 0.0;
        double maximoMemoria = 100.0;

        // rede




        // Calcula a média entre bytes recebidos e bytes enviados
        double limiteInferiorRede = 1000000;
        double limiteSuperiorRede = 1000000000;
        Double mediaDeRede = (rede.getQuantidade_bytes_enviados() + rede.getQuantidade_bytes_enviados()) / 2.0;

        // alerta de memória
        if (percentualDeUso >= limiteCritico) {
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteM, maximoMemoria, mensagemMemoriaCritico, minimoMemoria, dataHoraAtual);
        } else if (percentualDeUso >= limiteAlerta) {
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteM, maximoMemoria, mensagemMemoriaAlerta, minimoMemoria, dataHoraAtual);
        }

        if(disco.getPorcentagem_de_Uso_do_Disco() >=limiteCritico){
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteD, maximoMemoria, mensagemDiscoCritico, minimoMemoria, dataHoraAtual);
        }
        else if(disco.getPorcentagem_de_Uso_do_Disco() >= limiteAlerta){
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteD, maximoMemoria, mensagemDiscoAlerta, minimoMemoria, dataHoraAtual);
        }
        if(mediaDeRede < limiteInferiorRede){
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteR, maximoMemoria, mensagemAlertaRede, minimoMemoria, dataHoraAtual);
        }
        else if(mediaDeRede > limiteSuperiorRede){
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteR, maximoMemoria, mensagemCriticoRede, minimoMemoria, dataHoraAtual);
        }
    }

}


