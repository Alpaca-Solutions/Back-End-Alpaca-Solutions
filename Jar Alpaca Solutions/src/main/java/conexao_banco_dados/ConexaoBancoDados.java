package conexao_banco_dados;

import cliente.Cliente;
import disco.Disco;
import memoria.Memoria;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import processador.Processador;
import rede.Rede;

import java.sql.SQLException;
import java.util.*;

// acabei o jar aaaaaaaaaaaa

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

        if (resultado_select_local.size() > 0 && resultado_select_nuvem.size() > 0){
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

        public void InserirTabelaConfiguracoes(Integer fkMaquina,  Integer FkMemoriaUsada, Integer fkmemoriaUso , Integer fkMemoriaDipsonivel , Integer fkpercetDisco ,
                                               Integer fktamanhoDisco , Integer fkTamanhoDisponivelDisco , Integer fkpercentualProcessador,
                                               Integer fkBytesRecebidos , Integer fkBytesEnviados){
            Conexao conexao = new Conexao();
            JdbcTemplate con = conexao.getConexaoDoBanco();


            // Memória
            // Memória
            con.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoMemoriaUsada", fkMaquina, FkMemoriaUsada);

            con.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoMemoriaEmUso", fkMaquina, fkmemoriaUso);

            con.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoMemoriaDisponivel", fkMaquina, fkMemoriaDipsonivel);

// Disco
            con.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoPercentualUsoDisco", fkMaquina, fkpercetDisco);

            con.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoTamanhoDisco", fkMaquina, fktamanhoDisco);

            con.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoTamanhoDisponivel", fkMaquina, fkTamanhoDisponivelDisco);

            // Processador
            con.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoPercentualUsoProcessador", fkMaquina, fkpercentualProcessador);

            // Rede
            con.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoBytesRecebidos", fkMaquina, fkBytesRecebidos);

            con.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoBytesEnviados", fkMaquina, fkBytesEnviados);


        }

        public void InserirTabelaConfiguracoesNuvem(Integer fkMaquina,  Integer FkMemoriaUsada, Integer fkmemoriaUso , Integer fkMemoriaDipsonivel , Integer fkpercetDisco ,
                                                    Integer fktamanhoDisco , Integer fkTamanhoDisponivelDisco , Integer fkpercentualProcessador,
                                                    Integer fkBytesRecebidos , Integer fkBytesEnviados){
            ConexaoNuvem conexaoNuvem = new ConexaoNuvem();
            JdbcTemplate conNuvem = conexaoNuvem.getConexaoDoBanco();


            // nuvem


            // Memória
            conNuvem.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoMemoriaUsada", fkMaquina, FkMemoriaUsada);

            conNuvem.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoMemoriaEmUso", fkMaquina, fkmemoriaUso);

            conNuvem.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoMemoriaDisponivel", fkMaquina, fkMemoriaDipsonivel);

// Disco
            conNuvem.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoPercentualUsoDisco", fkMaquina, fkpercetDisco);

            conNuvem.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoTamanhoDisco", fkMaquina, fktamanhoDisco);

            conNuvem.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoTamanhoDisponivel", fkMaquina, fkTamanhoDisponivelDisco);

            // Processador
            conNuvem.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoPercentualUsoProcessador", fkMaquina, fkpercentualProcessador);

            // Rede
            conNuvem.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoBytesRecebidos", fkMaquina, fkBytesRecebidos);

            conNuvem.update("INSERT INTO Config (ValorConfiguracao, fkMaquina, fkTipoComponenteID) VALUES (?, ?, ?)",
                    "ConfiguracaoBytesEnviados", fkMaquina, fkBytesEnviados);
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

    public Map<String, Integer> obterFksTipoComponenteNuvem(){

        ConexaoNuvem conexaoNuvem = new ConexaoNuvem();
        JdbcTemplate con = conexaoNuvem.getConexaoDoBanco();
        Map<String, Integer> valoresFk = new HashMap<>();

        // Select específico para 'Percentual de uso do Disco'
        List<Map<String, Object>> resultPercentualDisco = con.queryForList(
                "SELECT TOP 1 idTipoComponente, nomeTipo\n" +
                        "FROM TipoComponente\n" +
                        "WHERE nomeTipo = 'Percentual de uso do Disco';"
        );

        // Select geral para todos os tipos
        List<Map<String, Object>> results = con.queryForList(
                " WITH CTE AS (\n" +
                        "                SELECT\n" +
                        "                idTipoComponente,\n" +
                        "                nomeTipo,\n" +
                        "                ROW_NUMBER() OVER (PARTITION BY nomeTipo ORDER BY idTipoComponente) AS row_num\n" +
                        "                FROM TipoComponente\n" +
                        "                WHERE nomeTipo IN ('Memoria Usada', 'Memoria em Uso', 'Memoria Disponível', 'Percentual de uso do Disco', 'Tamanho do Disco', 'Tamanho Disponível', 'Percentual de Uso do Processador', 'Bytes Recebidos', 'Bytes Enviados')\n" +
                        "        )\n" +
                        "        SELECT idTipoComponente, nomeTipo\n" +
                        "        FROM CTE\n" +
                        "        WHERE row_num = 1;");

        if (!resultPercentualDisco.isEmpty()) {
            // Se houver resultado para 'Percentual de uso do Disco', imprima as informações
            Map<String, Object> resultado = resultPercentualDisco.get(0);
            int idTipoComponentePercentualDisco = (int) resultado.get("idTipoComponente");
            String nomeTipoPercentualDisco = (String) resultado.get("nomeTipo");
            valoresFk.put("fkPercentualDisco", idTipoComponentePercentualDisco);

        } else {
            System.out.println("Nenhum resultado encontrado para 'Percentual de uso do Disco'.");
        }

        if (!results.isEmpty()) {
            Integer FkMemoriaUsada = null, fkMemoriaEmUso = null, fkMemoriaDisponivel = null, fkPercentualDisco = null, fkTamanhoDisco = null, fkTamanhoDisponivelDisco = null, fkPercentualProcessador = null, fkBytesRecebidos = null, fkBytesEnviados = null;

            // Se houver resultados para todos os tipos, processe-os
            for (Map<String, Object> coluna : results) {
                String nomeTipo = (String) coluna.get("nomeTipo");
                int idTipoComponente = (int) coluna.get("idTipoComponente");


                // os valores às variáveis correspondentes
                switch (nomeTipo) {
                    case "Memoria Usada":
                        FkMemoriaUsada = idTipoComponente;
                        break;
                    case "Memoria em Uso":
                        fkMemoriaEmUso = idTipoComponente;
                        break;
                    case "Memoria Disponível":
                        fkMemoriaDisponivel = idTipoComponente;
                        break;
                    case "Percentual de uso do Disco":
                        fkPercentualDisco = idTipoComponente;
                        break;
                    case "Tamanho do Disco":
                        fkTamanhoDisco = idTipoComponente;
                        break;
                    case "Tamanho Disponível":
                        fkTamanhoDisponivelDisco = idTipoComponente;
                        break;
                    case "Percentual de Uso do Processador":
                        fkPercentualProcessador = idTipoComponente;
                        break;
                    case "Bytes Recebidos":
                        fkBytesRecebidos = idTipoComponente;
                        break;
                    case "Bytes Enviados":
                        fkBytesEnviados = idTipoComponente;
                        break;
                    default:
                        System.out.println("Tipo desconhecido: " + nomeTipo);
                        break;
                }
            }

            valoresFk.put("FkMemoriaUsada", FkMemoriaUsada);
            valoresFk.put("fkMemoriaEmUso", fkMemoriaEmUso);
            valoresFk.put("fkMemoriaDisponivel", fkMemoriaDisponivel);
            valoresFk.put("fkTamanhoDisco", fkTamanhoDisco);
            valoresFk.put("fkTamanhoDisponivelDisco", fkTamanhoDisponivelDisco);
            valoresFk.put("fkPercentualProcessador", fkPercentualProcessador);
            valoresFk.put("fkBytesRecebidos", fkBytesRecebidos);
            valoresFk.put("fkBytesEnviados", fkBytesEnviados);


        } else {
            System.out.println("Nenhum resultado encontrado para todos os tipos.");
        }

        return valoresFk;



    }
    public Map<String, Integer> obterFksTipoComponente() {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        Map<String, Integer> valoresFk = new HashMap<>();

        // Select específico para 'Percentual de uso do Disco'
        List<Map<String, Object>> resultPercentualDisco = con.queryForList(
                "SELECT idTipoComponente, nomeTipo \n" +
                        "                        FROM TipoComponente \n" +
                        "                        WHERE nomeTipo = 'Percentual de uso do Disco' limit 1;"
        );

        // Select geral para todos os tipos
        List<Map<String, Object>> results = con.queryForList(
                " SELECT idTipoComponente, nomeTipo\n" +
                        "FROM TipoComponente\n" +
                        "WHERE (nomeTipo, idTipoComponente) IN (\n" +
                        "    SELECT nomeTipo, MIN(idTipoComponente) AS idTipoComponente\n" +
                        "    FROM TipoComponente\n" +
                        "    WHERE nomeTipo IN ('Memoria Usada', 'Memoria em Uso', 'Memoria Disponível', 'Percentual de uso do Disco', 'Tamanho do Disco', 'Tamanho Disponível', 'Percentual de Uso do Processador', 'Bytes Recebidos', 'Bytes Enviados')\n" +
                        "    GROUP BY nomeTipo\n" +
                        ");");

        if (!resultPercentualDisco.isEmpty()) {
            // Se houver resultado para 'Percentual de uso do Disco', imprima as informações
            Map<String, Object> resultado = resultPercentualDisco.get(0);
            int idTipoComponentePercentualDisco = (int) resultado.get("idTipoComponente");
            String nomeTipoPercentualDisco = (String) resultado.get("nomeTipo");
            valoresFk.put("fkPercentualDisco", idTipoComponentePercentualDisco);

        } else {
            System.out.println("Nenhum resultado encontrado para 'Percentual de uso do Disco'.");
        }

        if (!results.isEmpty()) {
            Integer FkMemoriaUsada = null, fkMemoriaEmUso = null, fkMemoriaDisponivel = null, fkPercentualDisco = null, fkTamanhoDisco = null, fkTamanhoDisponivelDisco = null, fkPercentualProcessador = null, fkBytesRecebidos = null, fkBytesEnviados = null;

            // Se houver resultados para todos os tipos, processe-os
            for (Map<String, Object> coluna : results) {
                String nomeTipo = (String) coluna.get("nomeTipo");
                int idTipoComponente = (int) coluna.get("idTipoComponente");


                // os valores às variáveis correspondentes
                switch (nomeTipo) {
                    case "Memoria Usada":
                        FkMemoriaUsada = idTipoComponente;
                        break;
                    case "Memoria em Uso":
                        fkMemoriaEmUso = idTipoComponente;
                        break;
                    case "Memoria Disponível":
                        fkMemoriaDisponivel = idTipoComponente;
                        break;
                    case "Percentual de uso do Disco":
                        fkPercentualDisco = idTipoComponente;
                        break;
                    case "Tamanho do Disco":
                        fkTamanhoDisco = idTipoComponente;
                        break;
                    case "Tamanho Disponível":
                        fkTamanhoDisponivelDisco = idTipoComponente;
                        break;
                    case "Percentual de Uso do Processador":
                        fkPercentualProcessador = idTipoComponente;
                        break;
                    case "Bytes Recebidos":
                        fkBytesRecebidos = idTipoComponente;
                        break;
                    case "Bytes Enviados":
                        fkBytesEnviados = idTipoComponente;
                        break;
                    default:
                        System.out.println("Tipo desconhecido: " + nomeTipo);
                        break;
                }
            }

            valoresFk.put("FkMemoriaUsada", FkMemoriaUsada);
            valoresFk.put("fkMemoriaEmUso", fkMemoriaEmUso);
            valoresFk.put("fkMemoriaDisponivel", fkMemoriaDisponivel);
            valoresFk.put("fkTamanhoDisco", fkTamanhoDisco);
            valoresFk.put("fkTamanhoDisponivelDisco", fkTamanhoDisponivelDisco);
            valoresFk.put("fkPercentualProcessador", fkPercentualProcessador);
            valoresFk.put("fkBytesRecebidos", fkBytesRecebidos);
            valoresFk.put("fkBytesEnviados", fkBytesEnviados);


        } else {
            System.out.println("Nenhum resultado encontrado para todos os tipos.");
        }

        return valoresFk;
    }
    public List<Integer> buscar_fk_maquina(String hostname) {
        Conexao conexao = new Conexao();
        JdbcTemplate jdbcTemplate = conexao.getConexaoDoBanco();

        String sql = "SELECT idMaquina FROM maquina WHERE hostname = ? LIMIT 1;";
        Integer idMaquina = jdbcTemplate.queryForObject(sql, Integer.class, hostname);

        List<Integer> idMaquinas = new ArrayList<>();
        if (idMaquina != null) {
            idMaquinas.add(idMaquina);
        }

        return idMaquinas;
    }


    public List<Integer> buscar_fk_maquinaNuvem(String hostname){
        ConexaoNuvem conexaoNuvem = new ConexaoNuvem();
        JdbcTemplate jdbcTemplate = conexaoNuvem.getConexaoDoBanco();

        String sql = "SELECT Top 1 idMaquina FROM maquina WHERE hostname = ?;";
        Integer idMaquina = jdbcTemplate.queryForObject(sql, Integer.class, hostname);

        List<Integer> idMaquinas = new ArrayList<>();
        if (idMaquina != null) {
            idMaquinas.add(idMaquina);
        }

        return idMaquinas;
    }

    public List<Integer> buscarFkUnidade(Integer fkMaquina){
        Conexao conexao = new Conexao();
        JdbcTemplate jdbcTemplate = conexao.getConexaoDoBanco();

        String sql = "SELECT m.idmaquina\n" +
                "FROM UnidadeMedida um\n" +
                "JOIN Maquina m ON um.fkMaquina = m.idMaquina\n" +
                "WHERE m.idmaquina = " + fkMaquina;

        List<Integer> idMaquinas = jdbcTemplate.queryForList(sql, Integer.class);

        System.out.println("Fk unidade é " + idMaquinas);
        return idMaquinas;
    }

    public List<Integer> buscarFkUnidadeNuvem(Integer fkMaquina){
        Conexao conexao = new Conexao();
        JdbcTemplate jdbcTemplate = conexao.getConexaoDoBanco();

        String sql = "SELECT m.idmaquina\n" +
                "FROM UnidadeMedida um\n" +
                "JOIN Maquina m ON um.fkMaquina = m.idMaquina\n" +
                "WHERE m.idmaquina = " + fkMaquina;

        List<Integer> idMaquinas = jdbcTemplate.queryForList(sql, Integer.class);

        System.out.println("Fk unidade é " + idMaquinas);
        return idMaquinas;
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


        public void inserirMedicoes(Memoria memoria , Rede rede , Processador processador, Disco disco, Integer fk_computador,
                                    Integer FkMemoriaUsada, Integer fkmemoriaUso , Integer fkMemoriaDipsonivel , Integer fkpercetDisco ,
                                    Integer fktamanhoDisco , Integer fkTamanhoDisponivelDisco , Integer fkpercentualProcessador,
                                    Integer fkBytesRecebidos , Integer fkBytesEnviados, Integer fkUnidadeMedida){
            Conexao conexao = new Conexao();
            JdbcTemplate con = conexao.getConexaoDoBanco();
            Date dataHoraAtual = new Date();


            // inserção de cpu
           con.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", processador.getPercentual_uso_do_processador(),dataHoraAtual, fk_computador, fkpercentualProcessador , fkUnidadeMedida);

           // inserção de disco
            con.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", disco.getPorcentagem_de_Uso_do_Disco(),dataHoraAtual, fk_computador, fkpercetDisco , fkUnidadeMedida);
            con.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", disco.getTamanho_Total_do_Disco(),dataHoraAtual, fk_computador, fktamanhoDisco, fkUnidadeMedida);
            con.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", disco.getTamanho_disponivel_do_disco(),dataHoraAtual, fk_computador, fkTamanhoDisponivelDisco , fkUnidadeMedida);


            // inserção de  Memoria

            con.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", memoria.getMemoria_Disponivel(),dataHoraAtual, fk_computador, fkMemoriaDipsonivel , fkUnidadeMedida);
            con.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", memoria.getMemoria_total(),dataHoraAtual, fk_computador,FkMemoriaUsada , fkUnidadeMedida);
            con.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", memoria.getMemoria_em_uso(),dataHoraAtual, fk_computador, fkmemoriaUso , fkUnidadeMedida);

            // inserção de rede

            con.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", rede.getQuantidade_bytes_enviados(),dataHoraAtual, fk_computador, fkBytesEnviados , fkUnidadeMedida);
            con.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", rede.getQuantidade_bytes_recebidos(),dataHoraAtual, fk_computador, fkBytesRecebidos , fkUnidadeMedida);



        }

        public void inserirMedicoesNuvem(Memoria memoria , Rede rede , Processador processador, Disco disco, Integer fk_computador,
                                         Integer FkMemoriaUsada, Integer fkmemoriaUso , Integer fkMemoriaDipsonivel , Integer fkpercetDisco ,
                                         Integer fktamanhoDisco , Integer fkTamanhoDisponivelDisco , Integer fkpercentualProcessador,
                                         Integer fkBytesRecebidos , Integer fkBytesEnviados, Integer fkUnidadeMedida){
            ConexaoNuvem conexaoNuvem = new ConexaoNuvem();
            JdbcTemplate conNuvem = conexaoNuvem.getConexaoDoBanco();
            Date dataHoraAtual = new Date();

            // nuvem

            // inserção de cpu
            conNuvem.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", processador.getPercentual_uso_do_processador(),dataHoraAtual, fk_computador, fkpercentualProcessador , fkUnidadeMedida);

            // inserção de disco
            conNuvem.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", disco.getPorcentagem_de_Uso_do_Disco(),dataHoraAtual, fk_computador, fkpercetDisco , fkUnidadeMedida);
            conNuvem.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", disco.getTamanho_Total_do_Disco(),dataHoraAtual, fk_computador, fktamanhoDisco, fkUnidadeMedida);
            conNuvem.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", disco.getTamanho_disponivel_do_disco(),dataHoraAtual, fk_computador, fkTamanhoDisponivelDisco , fkUnidadeMedida);


            // inserção de  Memoria

            conNuvem.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", memoria.getMemoria_Disponivel(),dataHoraAtual, fk_computador, fkMemoriaDipsonivel , fkUnidadeMedida);
            conNuvem.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", memoria.getMemoria_total(),dataHoraAtual, fk_computador,FkMemoriaUsada , fkUnidadeMedida);
            conNuvem.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", memoria.getMemoria_em_uso(),dataHoraAtual, fk_computador, fkmemoriaUso , fkUnidadeMedida);

            // inserção de rede

            conNuvem.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", rede.getQuantidade_bytes_enviados(),dataHoraAtual, fk_computador, fkBytesEnviados , fkUnidadeMedida);
            conNuvem.update("insert into Medicoes (valor, data_hora_leitura, id_computador, fkTipoComponenteID, fkUnidadeMedidaID) values (? ,?, ? , ? , ?)", rede.getQuantidade_bytes_recebidos(),dataHoraAtual, fk_computador, fkBytesRecebidos , fkUnidadeMedida);


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

    public Map<String , Integer> buscarFksConfiguracaoNuvem(){

        ConexaoNuvem conexaoNuvem = new ConexaoNuvem();
        JdbcTemplate con = conexaoNuvem.getConexaoDoBanco();


        List<Map<String, Object>> resultado = con.queryForList(
                "  SELECT MIN(idComponentes) AS idComponentes, ValorConfiguracao\n" +
                        "        FROM Config\n" +
                        "        WHERE ValorConfiguracao IN (\n" +
                        "                'ConfiguracaoMemoriaUsada',\n" +
                        "                'ConfiguracaoMemoriaEmUso',\n" +
                        "                'ConfiguracaoMemoriaDisponivel',\n" +
                        "                'ConfiguracaoPercentualUsoDisco',\n" +
                        "                'ConfiguracaoTamanhoDisco',\n" +
                        "                'ConfiguracaoTamanhoDisponivel',\n" +
                        "                'ConfiguracaoPercentualUsoProcessador',\n" +
                        "                'ConfiguracaoBytesRecebidos',\n" +
                        "                'ConfiguracaoBytesEnviados'\n" +
                        "        )\n" +
                        "        GROUP BY ValorConfiguracao;"
        );

        Map<String, Integer> configuracoes = new HashMap<>();

        for (Map<String, Object> row : resultado) {
            int idComponente = (int) row.get("idComponentes");
            String nomeConfiguracao = (String) row.get("ValorConfiguracao");

            configuracoes.put(nomeConfiguracao, idComponente);
        }

        return configuracoes;


    }
    public Map<String, Integer> buscarFksConfiguracao() {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        List<Map<String, Object>> resultado = con.queryForList(
                "SELECT MIN(idComponentes) AS idComponentes, ValorConfiguracao\n" +
                        "FROM Config\n" +
                        "WHERE ValorConfiguracao IN (\n" +
                        "    'ConfiguracaoMemoriaUsada',\n" +
                        "    'ConfiguracaoMemoriaEmUso',\n" +
                        "    'ConfiguracaoMemoriaDisponivel',\n" +
                        "    'ConfiguracaoPercentualUsoDisco',\n" +
                        "    'ConfiguracaoTamanhoDisco',\n" +
                        "    'ConfiguracaoTamanhoDisponivel',\n" +
                        "    'ConfiguracaoPercentualUsoProcessador',\n" +
                        "    'ConfiguracaoBytesRecebidos',\n" +
                        "    'ConfiguracaoBytesEnviados'\n" +
                        ")\n" +
                        "GROUP BY ValorConfiguracao;"
        );

        Map<String, Integer> configuracoes = new HashMap<>();

        for (Map<String, Object> row : resultado) {
            int idComponente = (int) row.get("idComponentes");
            String nomeConfiguracao = (String) row.get("ValorConfiguracao");

            configuracoes.put(nomeConfiguracao, idComponente);
        }

        return configuracoes;
    }




    public void Alertas(Memoria memoria,Disco disco,Rede rede,Processador processador, Integer fk_computador, Integer fkUnidade,
                        Integer FkMemoriaUsada, Integer fkmemoriaUso , Integer fkMemoriaDipsonivel , Integer fkpercetDisco ,
                        Integer fktamanhoDisco , Integer fkTamanhoDisponivelDisco , Integer fkpercentualProcessador,
                        Integer fkBytesRecebidos , Integer fkBytesEnviados,
                        Integer fkConfigMemoriaUsada, Integer fkConfigMemoriaemUso, Integer fkConfigMemoriaDisponivel, Integer fkConfigPercentDisco, Integer fkConfigTamanhoDisco,
                        Integer fkConfigTamanhoDisponivelDisco, Integer fkconfigPercentualProcessador, Integer fkConfigBytesRecebidos,
                        Integer fkConfigBytesEnviados) {

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
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteM, maximoMemoria, mensagemMemoriaCritico, minimoMemoria, dataHoraAtual, fkUnidade, fkMemoriaDipsonivel, fkConfigMemoriaDisponivel);
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteM, maximoMemoria, mensagemMemoriaCritico, minimoMemoria, dataHoraAtual, fkUnidade, fkmemoriaUso, fkmemoriaUso);
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteM, maximoMemoria, mensagemMemoriaCritico, minimoMemoria, dataHoraAtual, fkUnidade, FkMemoriaUsada, fkConfigMemoriaUsada);
        } else if (percentualDeUso >= limiteAlerta) {
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteM, maximoMemoria, mensagemMemoriaCritico, minimoMemoria, dataHoraAtual, fkUnidade, fkMemoriaDipsonivel, fkConfigMemoriaDisponivel);
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteM, maximoMemoria, mensagemMemoriaCritico, minimoMemoria, dataHoraAtual, fkUnidade, fkmemoriaUso, fkmemoriaUso);
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteM, maximoMemoria, mensagemMemoriaCritico, minimoMemoria, dataHoraAtual, fkUnidade, FkMemoriaUsada, fkConfigMemoriaUsada);
        }
        // alerta de disco
        if(disco.getPorcentagem_de_Uso_do_Disco() >=limiteCritico){
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteD, maximoMemoria, mensagemDiscoCritico, minimoMemoria, dataHoraAtual, fkUnidade, fkUnidade, fkConfigPercentDisco, fkConfigPercentDisco);
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteD, maximoMemoria, mensagemDiscoCritico, minimoMemoria, dataHoraAtual, fkUnidade, fkConfigPercentDisco, fkConfigPercentDisco);
        }
        else if(disco.getPorcentagem_de_Uso_do_Disco() >= limiteAlerta){
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteD, maximoMemoria, mensagemDiscoAlerta, minimoMemoria, dataHoraAtual, fkUnidade, fkUnidade, fkConfigPercentDisco, fkConfigPercentDisco);
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteD, maximoMemoria, mensagemDiscoAlerta, minimoMemoria, dataHoraAtual, fkUnidade, fkUnidade, fkConfigPercentDisco, fkConfigPercentDisco);
        }
        // alerta de rede
        if(mediaDeRede < limiteInferiorRede){
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteR, maximoMemoria, mensagemAlertaRede, minimoMemoria, dataHoraAtual, fkBytesRecebidos , fkConfigBytesRecebidos);
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteR, maximoMemoria, mensagemAlertaRede, minimoMemoria, dataHoraAtual, fkBytesEnviados, fkConfigBytesEnviados);
        }
        else if(mediaDeRede > limiteSuperiorRede){
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteR, maximoMemoria, mensagemCriticoRede, minimoMemoria, dataHoraAtual, fkUnidade, fkBytesRecebidos,fkConfigBytesRecebidos);
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteR, maximoMemoria, mensagemCriticoRede, minimoMemoria, dataHoraAtual, fkUnidade, fkBytesEnviados,fkConfigBytesEnviados);
        }

        // alerta de processador
        if(processador.getPercentual_uso_do_processador() >= limiteAlerta){
            con.update("INSERT INTO MetricasAlertas " +
                        "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteP, maximoMemoria, mensagemAlertaProcessador, minimoMemoria, dataHoraAtual , fkUnidade, fkpercentualProcessador, fkconfigPercentualProcessador);
       }
        else if(processador.getPercentual_uso_do_processador() >= limiteCritico){
            con.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteP, maximoMemoria, mensagemAlertaCriticoProcessador, minimoMemoria, dataHoraAtual, fkUnidade, fkpercentualProcessador, fkconfigPercentualProcessador);
        }
    }
    public void AlertasNuvem(Memoria memoria,Disco disco,Rede rede,Processador processador, Integer fk_computador, Integer fkUnidade,
                             Integer FkMemoriaUsada, Integer fkmemoriaUso , Integer fkMemoriaDipsonivel , Integer fkpercetDisco ,
                             Integer fktamanhoDisco , Integer fkTamanhoDisponivelDisco , Integer fkpercentualProcessador,
                             Integer fkBytesRecebidos , Integer fkBytesEnviados,
                             Integer fkConfigMemoriaUsada, Integer fkConfigMemoriaemUso, Integer fkConfigMemoriaDisponivel, Integer fkConfigPercentDisco, Integer fkConfigTamanhoDisco,
                             Integer fkConfigTamanhoDisponivelDisco, Integer fkconfigPercentualProcessador, Integer fkConfigBytesRecebidos,
                             Integer fkConfigBytesEnviados) {
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
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteM, maximoMemoria, mensagemMemoriaCritico, minimoMemoria, dataHoraAtual, fkUnidade, fkMemoriaDipsonivel, fkConfigMemoriaDisponivel);
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteM, maximoMemoria, mensagemMemoriaCritico, minimoMemoria, dataHoraAtual, fkUnidade, fkmemoriaUso, fkmemoriaUso);
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteM, maximoMemoria, mensagemMemoriaCritico, minimoMemoria, dataHoraAtual, fkUnidade, FkMemoriaUsada, fkConfigMemoriaUsada);
        } else if (percentualDeUso >= limiteAlerta) {
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteM, maximoMemoria, mensagemMemoriaCritico, minimoMemoria, dataHoraAtual, fkUnidade, fkMemoriaDipsonivel, fkConfigMemoriaDisponivel);
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteM, maximoMemoria, mensagemMemoriaCritico, minimoMemoria, dataHoraAtual, fkUnidade, fkmemoriaUso, fkmemoriaUso);
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteM, maximoMemoria, mensagemMemoriaCritico, minimoMemoria, dataHoraAtual, fkUnidade, FkMemoriaUsada, fkConfigMemoriaUsada);
        }
        // alerta de disco
        if(disco.getPorcentagem_de_Uso_do_Disco() >=limiteCritico){
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteD, maximoMemoria, mensagemDiscoCritico, minimoMemoria, dataHoraAtual, fkUnidade, fkUnidade, fkConfigPercentDisco, fkConfigPercentDisco);
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteD, maximoMemoria, mensagemDiscoCritico, minimoMemoria, dataHoraAtual, fkUnidade, fkConfigPercentDisco, fkConfigPercentDisco);
        }
        else if(disco.getPorcentagem_de_Uso_do_Disco() >= limiteAlerta){
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteD, maximoMemoria, mensagemDiscoAlerta, minimoMemoria, dataHoraAtual, fkUnidade, fkUnidade, fkConfigPercentDisco, fkConfigPercentDisco);
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteD, maximoMemoria, mensagemDiscoAlerta, minimoMemoria, dataHoraAtual, fkUnidade, fkUnidade, fkConfigPercentDisco, fkConfigPercentDisco);
        }
        // alerta de rede
        if(mediaDeRede < limiteInferiorRede){
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteR, maximoMemoria, mensagemAlertaRede, minimoMemoria, dataHoraAtual, fkBytesRecebidos , fkConfigBytesRecebidos);
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteR, maximoMemoria, mensagemAlertaRede, minimoMemoria, dataHoraAtual, fkBytesEnviados, fkConfigBytesEnviados);
        }
        else if(mediaDeRede > limiteSuperiorRede){
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteR, maximoMemoria, mensagemCriticoRede, minimoMemoria, dataHoraAtual, fkUnidade, fkBytesRecebidos,fkConfigBytesRecebidos);
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteR, maximoMemoria, mensagemCriticoRede, minimoMemoria, dataHoraAtual, fkUnidade, fkBytesEnviados,fkConfigBytesEnviados);
        }

        // alerta de processador
        if(processador.getPercentual_uso_do_processador() >= limiteAlerta){
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteP, maximoMemoria, mensagemAlertaProcessador, minimoMemoria, dataHoraAtual , fkUnidade, fkpercentualProcessador, fkconfigPercentualProcessador);
        }
        else if(processador.getPercentual_uso_do_processador() >= limiteCritico){
            conNuvem.update("INSERT INTO MetricasAlertas " +
                    "(TipoComponente, maximo, mensagemAlerta, minimo, dhHoraAlerta, fkUnidadeMedida, fkTipoComponente, fkConfiguracao) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?);", tipoComponenteP, maximoMemoria, mensagemAlertaCriticoProcessador, minimoMemoria, dataHoraAtual, fkUnidade, fkpercentualProcessador, fkconfigPercentualProcessador);
        }
    }
}


