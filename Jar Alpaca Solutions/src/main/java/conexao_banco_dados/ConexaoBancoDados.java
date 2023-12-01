package conexao_banco_dados;

import cliente.Cliente;
import disco.Disco;
import memoria.Memoria;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import processador.Processador;
import rede.Rede;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConexaoBancoDados extends Conexao{
        List<Cliente> clienteList;



    public Integer Busca_Cliente(Cliente cliente) {

        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        ConexaoNuvem conexaoNuvem = new ConexaoNuvem();
        JdbcTemplate conNuvem = conexaoNuvem.getConexaoDoBanco();



        Integer resultado = 0;
        List<Cliente> resultado_select_local;
        List<Cliente> resultado_select_nuvem;


        resultado_select_local = con.query(
                    "SELECT 'Empresa' AS TipoCliente\n" +
                            "FROM Empresa\n" +
                            "WHERE email = ? AND senha = ?\n" +
                            "UNION\n" +
                            "SELECT 'Empregado' AS TipoCliente\n" +
                            "FROM Usuario\n" +
                            "WHERE email = ? AND senha = ?;",
                    new Object[]{cliente.getEmail(), cliente.getSenha(), cliente.getEmail(), cliente.getSenha()},
                    new BeanPropertyRowMapper<>(Cliente.class));
        resultado_select_nuvem = conNuvem.query(
                "SELECT 'Empresa' AS TipoCliente\n" +
                        "FROM Empresa\n" +
                        "WHERE email = ? AND senha = ?\n" +
                        "UNION\n" +
                        "SELECT 'Empregado' AS TipoCliente\n" +
                        "FROM Usuario\n" +
                        "WHERE email = ? AND senha = ?",
                new Object[]{cliente.getEmail(), cliente.getSenha(), cliente.getEmail(), cliente.getSenha()},
                new BeanPropertyRowMapper<>(Cliente.class)
        );

        if (resultado_select_local.size() > 0 || resultado_select_nuvem.size() > 0){
                resultado = 200;
            }
            else{
                resultado = 404;
            }

            return resultado;
        }

    public Map<String, Object> buscar_empresa_e_unidade_local(Cliente cliente) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        List<Map<String, Object>> resultado_banco_local;
        try {
            resultado_banco_local = con.queryForList(
                    "SELECT Unidade.idUnidade , Empresa.idEmpresa  FROM Empresa " +
                            "JOIN endereco ON fk_endereco = idendereco " +
                            "JOIN Unidade ON unidade.fkEndereco = idendereco " +
                            "WHERE empresa.email = ? AND empresa.senha = ?",
                    cliente.getEmail(), cliente.getSenha());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        if (resultado_banco_local.isEmpty()) {
            return null;
        } else {
            return resultado_banco_local.get(0);
        }
    }

    public Map<String, Object> buscar_empresa_e_unidade_nuvem(Cliente cliente) {
        ConexaoNuvem conexaoNuvem = new ConexaoNuvem();
        JdbcTemplate conNuvem = conexaoNuvem.getConexaoDoBanco();

        List<Map<String, Object>> resultado_banco_nuvem;
        try {
            resultado_banco_nuvem = conNuvem.queryForList(
                    "SELECT TOP 1 Unidade.idUnidade, Empresa.idEmpresa\n" +
                            "FROM Empresa\n" +
                            "JOIN Endereco ON Empresa.fk_endereco = Endereco.idendereco\n" +
                            "JOIN Unidade ON Unidade.fkEndereco = Endereco.idendereco\n" +
                            "WHERE Empresa.email = ? AND Empresa.senha = ?;",
                    cliente.getEmail(), cliente.getSenha());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        if (resultado_banco_nuvem.isEmpty()) {
            return null;
        } else {
            return resultado_banco_nuvem.get(0);
        }
    }




    public void inserir_dados_maquina(String nome_maquina, String ipMaquina, String so, Boolean status, Integer fk_empresa, Integer fKUnidade) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();


        con.update("INSERT INTO Maquina (hostname, ipmaquina, sistemaOperacional, statusMaquina, FkEmpresa, fKUnidade ) VALUES (?, ?, ?, ?, ? , ?)",
                nome_maquina,    ipMaquina, so, status, fk_empresa, fKUnidade);
    }

    public void inserir_dados_maquinaNuvem(String nome_maquina, String ipMaquina, String so, Integer status, Integer fk_empresa, Integer fKUnidade) {
        ConexaoNuvem conexaoNuvem = new ConexaoNuvem();
        JdbcTemplate conNuvem = conexaoNuvem.getConexaoDoBanco();

        try {
            conNuvem.update("INSERT INTO Maquina (hostname, ipmaquina, sistemaOperacional, statusMaquina, FkEmpresa, fKUnidade) VALUES (?, ?, ?, ?, ?, ?)",
                    nome_maquina, ipMaquina, so, status, fk_empresa, fKUnidade);


        } catch (DataAccessException e) {
            System.out.println("Erro ao executar SQL: " + e.getMessage());
            Throwable cause = e.getCause();
            if (cause instanceof SQLException sqlException) {
                System.out.println("Código de erro SQL: " + sqlException.getErrorCode());
                System.out.println("SQLState: " + sqlException.getSQLState());
            }
        }
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

    public void inserir_tipo_componenteNuvem(){
        ConexaoNuvem conexaoNuvem = new ConexaoNuvem();
        JdbcTemplate conNuvem = conexaoNuvem.getConexaoDoBanco();


        conNuvem.update("INSERT INTO TipoComponente (nomeTipo, tipoComponente) values (? , ? )", "Memoria Usada" , "Memoria");
        conNuvem.update("INSERT INTO TipoComponente (nomeTipo, tipoComponente ) values (? , ? )", "Memoria em Uso" , "Memoria");
        conNuvem.update("INSERT INTO TipoComponente (nomeTipo, tipoComponente ) values (? , ? )", "Memoria Disponível" , "Memoria");

        // disco

        conNuvem.update("INSERT INTO TipoComponente (nomeTipo, tipoComponente) values (? , ? )", "Percentual de Uso do Disco" , "Disco");
        conNuvem.update("INSERT INTO TipoComponente (nomeTipo, tipoComponente) values (? , ? )", "Tamanho do Disco" , "Disco");
        conNuvem.update("INSERT INTO TipoComponente (nomeTipo, tipoComponente) values (? , ? )", "Tamanho Disponível" , "Disco");

        // processador

        conNuvem.update("INSERT INTO TipoComponente (nomeTipo, tipoComponente) values (? , ? )", "Percentual de Uso do Processador" , "Processador");

        // rede

        conNuvem.update("INSERT INTO TipoComponente (nomeTipo, tipoComponente) values (? , ? )", "Bytes Recebidos" , "Rede");
        conNuvem.update("INSERT INTO TipoComponente (nomeTipo, tipoComponente) values (? , ? )", "Bytes Enviados" , "Rede");

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

        public void InserirTabelaConfiguracoesNuvem(Integer fkMaquina){
            ConexaoNuvem conexaoNuvem = new ConexaoNuvem();
            JdbcTemplate conNuvem = conexaoNuvem.getConexaoDoBanco();


            // nuvem


            // Memória
            conNuvem.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoMemoriaUsada", fkMaquina, 1);

            conNuvem.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoMemoriaEmUso", fkMaquina, 2);

            conNuvem.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoMemoriaDisponivel", fkMaquina, 3);

// Disco
            conNuvem.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoPercentualUsoDisco", fkMaquina, 4);

            conNuvem.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoTamanhoDisco", fkMaquina, 5);

            conNuvem.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoTamanhoDisponivel", fkMaquina, 6);

            // Processador
            conNuvem.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoPercentualUsoProcessador", fkMaquina, 7);

            // Rede
            conNuvem.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoBytesRecebidos", fkMaquina, 8);

            conNuvem.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoBytesEnviados", fkMaquina, 9);
        }

    public Integer obterIdMaquina(String nomeMaquina) {
        try {
            Conexao conexaoLocal = new Conexao();
            JdbcTemplate conLocal = conexaoLocal.getConexaoDoBanco();

            String sqlLocal = "SELECT idMaquina FROM maquina WHERE nomeMaquina = ?";
            Integer valorLocal = conLocal.queryForObject(sqlLocal, Integer.class, nomeMaquina);

            // Conexão com o banco de dados na nuvem
            ConexaoNuvem conexaoNuvem = new ConexaoNuvem();
            JdbcTemplate conNuvem = conexaoNuvem.getConexaoDoBanco();

            String sqlNuvem = "SELECT idMaquina FROM maquina WHERE nomeMaquina = ?";
            Integer valorNuvem = conNuvem.queryForObject(sqlNuvem, Integer.class, nomeMaquina);

            if (valorLocal != null && valorNuvem != null) {
                return valorLocal;
            } else if (valorLocal != null) {
                return valorLocal;
            } else if (valorNuvem != null) {
                return valorNuvem;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Integer> buscar_fk_maquina() {
        Conexao conexao = new Conexao();
        JdbcTemplate jdbcTemplate = conexao.getConexaoDoBanco();
        ConexaoNuvem conexaoNuvem = new ConexaoNuvem();
        JdbcTemplate conNuvem = conexaoNuvem.getConexaoDoBanco();

        String sql = "SELECT idMaquina FROM maquina";
        List<Integer> idMaquinas = jdbcTemplate.queryForList(sql, Integer.class);
        return idMaquinas;
    }

    public List<Integer> buscar_fk_maquinaNuvem(){
        Conexao conexao = new Conexao();
        JdbcTemplate jdbcTemplate = conexao.getConexaoDoBanco();
        ConexaoNuvem conexaoNuvem = new ConexaoNuvem();
        JdbcTemplate conNuvem = conexaoNuvem.getConexaoDoBanco();

        String sql = "SELECT idMaquina FROM maquina";
        List<Integer> idMaquinasbancoNuvem = conNuvem.queryForList(sql , Integer.class);
        return idMaquinasbancoNuvem;
    }

        public void atualizarFkUnidadeMedida(Integer fk_maquina){
            Conexao conexao = new Conexao();
            JdbcTemplate con = conexao.getConexaoDoBanco();


            String sql = "UPDATE UnidadeMedida SET fkMaquina = ? WHERE idParametros = 1;\n";

            con.update(sql, fk_maquina);

        }

        public void atualizarFkUnidadeMedidaNuvem(Integer fk_maquina){
            ConexaoNuvem conexaoNuvem = new ConexaoNuvem();
            JdbcTemplate conNuvem = conexaoNuvem.getConexaoDoBanco();
            String sql = "UPDATE UnidadeMedida SET fkMaquina = ? WHERE idParametros = 1;\n";
            conNuvem.update(sql , fk_maquina);
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

        public void inserirMedicoesNuvem(Memoria memoria , Rede rede , Processador processador, Disco disco, Integer fk_computador){
            ConexaoNuvem conexaoNuvem = new ConexaoNuvem();
            JdbcTemplate conNuvem = conexaoNuvem.getConexaoDoBanco();
            Date dataHoraAtual = new Date();

            // nuvem

            // inserção de cpu
            conNuvem.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", processador.getPercentual_uso_do_processador(),dataHoraAtual, fk_computador, 7 , 1);

            // inserção de disco
            conNuvem.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", disco.getPorcentagem_de_Uso_do_Disco(),dataHoraAtual, fk_computador, 4 , 1);
            conNuvem.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", disco.getTamanho_Total_do_Disco(),dataHoraAtual, fk_computador, 5 , 1);
            conNuvem.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", disco.getTamanho_disponivel_do_disco(),dataHoraAtual, fk_computador, 6 , 1);


            // inserção de  Memoria

            conNuvem.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", memoria.getMemoria_Disponivel(),dataHoraAtual, fk_computador, 3 , 1);
            conNuvem.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", memoria.getMemoria_total(),dataHoraAtual, fk_computador, 2, 1);
            conNuvem.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", memoria.getMemoria_em_uso(),dataHoraAtual, fk_computador, 1 , 1);

            // inserção de rede

            conNuvem.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", rede.getQuantidade_bytes_enviados(),dataHoraAtual, fk_computador, 9 , 1);
            conNuvem.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", rede.getQuantidade_bytes_recebidos(),dataHoraAtual, fk_computador, 8 , 1);


        }

    public String AlertaMemoria(int idMaquina) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        String sql = "SELECT MA.mensagemAlerta " +
                "FROM MetricasAlertas MA " +
                "JOIN UnidadeMedida UM ON MA.fkUnidadeMedida = UM.idParametros " +
                "JOIN Maquina M ON UM.fkMaquina = M.idMaquina " +
                "WHERE M.idMaquina = ? " +
                "ORDER BY MA.dhHoraAlerta DESC";

        List<String> mensagensAlerta = con.queryForList(sql, String.class, idMaquina);

        if (!mensagensAlerta.isEmpty()) {
            return mensagensAlerta.get(0);
        }

        return null;
    }




    public void Alertas(Memoria memoria,Disco disco,Rede rede,Processador processador, Integer fk_computador) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        Date dataHoraAtual = new Date();
        Double percentualDeUso = (memoria.getMemoria_em_uso() / memoria.getMemoria_total()) * 100.0;
        ConexaoNuvem conexaoNuvem = new ConexaoNuvem();
        JdbcTemplate conNuvem = conexaoNuvem.getConexaoDoBanco();



        Double limiteAlerta = 80.0;
        Double limiteCritico = 90.0;
        String tipoComponenteM = "Memória";
        String tipoComponenteD = "Disco";
        String tipoComponenteR = "Rede";
        String tipoComponenteP = "Processador";
        String mensagemAlertaProcessador = "Atenção! O percentual de uso do processador está acima do normal. Recomenda-se verificar a causa desse aumento.";
        String mensagemAlertaCriticoProcessador = "Estado Crítico! O percentual de uso do processador atingiu um nível perigoso. Ações imediatas são necessárias para evitar problemas de desempenho";
        String mensagemAlertaRede = "Baixa atividade na rede.";
        String mensagemCriticoRede = "Alta atividade na rede.";
        String mensagemMemoriaCritico = "Estado crítico! O percentual de uso da memória ram está acima de " + limiteCritico + "%";
        String mensagemMemoriaAlerta = "Estado de alerta! O percentual de uso da memória ram está acima de " + limiteAlerta + "%";
        String mensagemDiscoAlerta = "Atenção! O espaço no disco está quase atingindo seu limite. Recomenda-se liberar espaço para evitar problemas de armazenamento.";
        String mensagemDiscoCritico = "Estado Crítico! O espaço no disco atingiu um nível perigoso. Ações imediatas são necessárias para liberar espaço e garantir o funcionamento adequado do sistema.";
        Double minimoMemoria = 0.0;
        Double maximoMemoria = 100.0;

        // rede

        Integer limiteInferiorRede = 1;
        Integer limiteSuperiorRede = 1000;
        Double mediaDeRede = (rede.getQuantidade_bytes_enviados() + rede.getQuantidade_bytes_enviados()) / 2.0;

        // alerta de memória
        if (percentualDeUso >= limiteCritico) {
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteM, maximoMemoria, mensagemMemoriaCritico, minimoMemoria, dataHoraAtual);
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteM, maximoMemoria, mensagemMemoriaCritico, minimoMemoria, dataHoraAtual);
        } else if (percentualDeUso >= limiteAlerta) {
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteM, maximoMemoria, mensagemMemoriaAlerta, minimoMemoria, dataHoraAtual);
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteM, maximoMemoria, mensagemMemoriaAlerta, minimoMemoria, dataHoraAtual);
        }

        // alerta de disco
        if(disco.getPorcentagem_de_Uso_do_Disco() >=limiteCritico){
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteD, maximoMemoria, mensagemDiscoCritico, minimoMemoria, dataHoraAtual);
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteD, maximoMemoria, mensagemDiscoCritico, minimoMemoria, dataHoraAtual);
        }
        else if(disco.getPorcentagem_de_Uso_do_Disco() >= limiteAlerta){
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteD, maximoMemoria, mensagemDiscoAlerta, minimoMemoria, dataHoraAtual);
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteD, maximoMemoria, mensagemDiscoAlerta, minimoMemoria, dataHoraAtual);
        }

        // alerta de rede
        if(mediaDeRede < limiteInferiorRede){
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteR, maximoMemoria, mensagemAlertaRede, minimoMemoria, dataHoraAtual);
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteR, maximoMemoria, mensagemAlertaRede, minimoMemoria, dataHoraAtual);
        }
        else if(mediaDeRede > limiteSuperiorRede){
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteR, maximoMemoria, mensagemCriticoRede, minimoMemoria, dataHoraAtual);
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteR, maximoMemoria, mensagemCriticoRede, minimoMemoria, dataHoraAtual);
        }

        // alerta de processador
        if(processador.getPercentual_uso_do_processador() >= limiteAlerta){
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteP, maximoMemoria, mensagemAlertaProcessador, minimoMemoria, dataHoraAtual);
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteP, maximoMemoria, mensagemAlertaProcessador, minimoMemoria, dataHoraAtual);
        }
        else if(processador.getPercentual_uso_do_processador() >= limiteCritico){
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteP, maximoMemoria, mensagemAlertaCriticoProcessador, minimoMemoria, dataHoraAtual);
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteP, maximoMemoria, mensagemAlertaCriticoProcessador, minimoMemoria, dataHoraAtual);
        }
    }

    public void AlertasNuvem(Memoria memoria,Disco disco,Rede rede,Processador processador, Integer fk_computador) {
        Date dataHoraAtual = new Date();
        Double percentualDeUso = (memoria.getMemoria_em_uso() / memoria.getMemoria_total()) * 100.0;
        ConexaoNuvem conexaoNuvem = new ConexaoNuvem();
        JdbcTemplate conNuvem = conexaoNuvem.getConexaoDoBanco();

        Double limiteAlerta = 80.0;
        Double limiteCritico = 90.0;
        String tipoComponenteM = "Memória";
        String tipoComponenteD = "Disco";
        String tipoComponenteR = "Rede";
        String tipoComponenteP = "Processador";
        String mensagemAlertaProcessador = "Atenção! O percentual de uso do processador está acima do normal. Recomenda-se verificar a causa desse aumento.";
        String mensagemAlertaCriticoProcessador = "Estado Crítico! O percentual de uso do processador atingiu um nível perigoso. Ações imediatas são necessárias para evitar problemas de desempenho";
        String mensagemAlertaRede = "Baixa atividade na rede.";
        String mensagemCriticoRede = "Alta atividade na rede.";
        String mensagemMemoriaCritico = "Estado crítico! O percentual de uso da memória ram está acima de " + limiteCritico + "%";
        String mensagemMemoriaAlerta = "Estado de alerta! O percentual de uso da memória ram está acima de " + limiteAlerta + "%";
        String mensagemDiscoAlerta = "Atenção! O espaço no disco está quase atingindo seu limite. Recomenda-se liberar espaço para evitar problemas de armazenamento.";
        String mensagemDiscoCritico = "Estado Crítico! O espaço no disco atingiu um nível perigoso. Ações imediatas são necessárias para liberar espaço e garantir o funcionamento adequado do sistema.";
        Double minimoMemoria = 0.0;
        Double maximoMemoria = 100.0;

        // rede

        Integer limiteInferiorRede = 1;
        Integer limiteSuperiorRede = 1000;
        Double mediaDeRede = (rede.getQuantidade_bytes_enviados() + rede.getQuantidade_bytes_enviados()) / 2.0;

        // alerta de memória
        if (percentualDeUso >= limiteCritico) {
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteM, maximoMemoria, mensagemMemoriaCritico, minimoMemoria, dataHoraAtual);
        } else if (percentualDeUso >= limiteAlerta) {
         conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteM, maximoMemoria, mensagemMemoriaAlerta, minimoMemoria, dataHoraAtual);
        }

        // alerta de disco
        if(disco.getPorcentagem_de_Uso_do_Disco() >=limiteCritico){
          conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteD, maximoMemoria, mensagemDiscoCritico, minimoMemoria, dataHoraAtual);
        }
        else if(disco.getPorcentagem_de_Uso_do_Disco() >= limiteAlerta){
        conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteD, maximoMemoria, mensagemDiscoAlerta, minimoMemoria, dataHoraAtual);
        }

        // alerta de rede
        if(mediaDeRede < limiteInferiorRede){
          conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteR, maximoMemoria, mensagemAlertaRede, minimoMemoria, dataHoraAtual);
        }
        else if(mediaDeRede > limiteSuperiorRede){
           conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteR, maximoMemoria, mensagemCriticoRede, minimoMemoria, dataHoraAtual);
        }

        // alerta de processador
        if(processador.getPercentual_uso_do_processador() >= limiteAlerta){
          conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteP, maximoMemoria, mensagemAlertaProcessador, minimoMemoria, dataHoraAtual);
        }
        else if(processador.getPercentual_uso_do_processador() >= limiteCritico){
           conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, 1, 1, 1);", tipoComponenteP, maximoMemoria, mensagemAlertaCriticoProcessador, minimoMemoria, dataHoraAtual);
        }
    }
}


