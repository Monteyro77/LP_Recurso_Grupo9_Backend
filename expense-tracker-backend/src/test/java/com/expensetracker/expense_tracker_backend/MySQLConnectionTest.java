package com.expensetracker.expense_tracker_backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Cliente.Main.class)
public class MySQLConnectionTest {

    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testMySQLConnection() {
        assertNotNull(dataSource, "DataSource não deve ser nulo");
        
        try (Connection connection = dataSource.getConnection()) {
            // Teste básico de conexão
            assertNotNull(connection, "Conexão não deve ser nula");
            assertFalse(connection.isClosed(), "Conexão deve estar aberta");
            
            // Obter metadados do MySQL
            DatabaseMetaData metaData = connection.getMetaData();
            
            System.out.println("✅ Conexão MySQL estabelecida com sucesso!");
            System.out.println("==========================================");
            System.out.println("URL: " + metaData.getURL());
            System.out.println("Usuário: " + metaData.getUserName());
            System.out.println("Driver: " + metaData.getDriverName());
            System.out.println("Versão do Driver: " + metaData.getDriverVersion());
            System.out.println("Versão do MySQL: " + metaData.getDatabaseProductVersion());
            System.out.println("Nome do Banco: " + connection.getCatalog());
            
            // Testar versão do MySQL
            assertTrue(metaData.getDatabaseProductName().contains("MySQL"), 
                    "Deve ser uma conexão MySQL");
            
        } catch (SQLException e) {
            fail("Falha ao conectar ao MySQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Test
    public void testMySQLQuery() {
        assertNotNull(jdbcTemplate, "JdbcTemplate não deve ser nulo");
        
        try {
            // Query específica do MySQL para testar a versão
            String version = jdbcTemplate.queryForObject("SELECT VERSION()", String.class);
            assertNotNull(version, "Versão do MySQL deve ser retornada");
            assertTrue(version.contains("MySQL") || version.contains("MariaDB"), 
                    "Deve retornar versão do MySQL/MariaDB");
            
            System.out.println("✅ Versão do MySQL: " + version);
            
        } catch (Exception e) {
            fail("Falha ao executar query no MySQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Test
    public void testMySQLFeatures() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            System.out.println("\n📊 Recursos do MySQL suportados:");
            System.out.println("==========================================");
            System.out.println("Suporta transações: " + metaData.supportsTransactions());
            System.out.println("Suporta batch updates: " + metaData.supportsBatchUpdates());
            System.out.println("Máximo de conexões: " + metaData.getMaxConnections());
            System.out.println("Default transaction isolation: " + metaData.getDefaultTransactionIsolation());
            
        } catch (SQLException e) {
            fail("Erro ao obter metadados do MySQL: " + e.getMessage());
        }
    }
    
    @Test
    public void testDatabaseTables() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            // Listar tabelas do banco
            System.out.println("\n📋 Tabelas disponíveis no banco:");
            System.out.println("==========================================");
            
            try (ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"})) {
                int tableCount = 0;
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    String tableType = tables.getString("TABLE_TYPE");
                    System.out.println(tableCount + 1 + ". " + tableName + " (" + tableType + ")");
                    tableCount++;
                }
                System.out.println("Total de tabelas: " + tableCount);
                
                if (tableCount == 0) {
                    System.out.println("⚠️  Banco de dados está vazio (sem tabelas)");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar tabelas: " + e.getMessage());
            // Não falha o teste, apenas informa
        }
    }
    
    @Test
    public void testConnectionPerformance() {
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 5; i++) {
            try (Connection connection = dataSource.getConnection()) {
                // Executar query simples para testar performance
                jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            } catch (SQLException e) {
                fail("Falha na conexão de performance: " + e.getMessage());
            }
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        System.out.println("\n⚡ Performance Test:");
        System.out.println("==========================================");
        System.out.println("5 conexões/consultas em: " + duration + "ms");
        System.out.println("Média por conexão: " + (duration / 5.0) + "ms");
        
        assertTrue(duration < 5000, "Conexões devem ser rápidas (< 5 segundos)");
    }
}