Requisitos do Sistema
- Java JDK 8 ou superior
- Microsoft SQL Server 2019 ou superior
- Apache NetBeans 25
- Driver JDBC para SQL Server (incluído no projeto)

Configuração do Banco de Dados
1. Abra o SQL Server Management Studio ou outra ferramenta de administração do SQL Server
2. Execute o script `database_script.sql` //pode abrir diretamente da pasta, dando 2 cliques
3. Verifique se o banco de dados `AntiGolpeDB` foi criado corretamente

No SQL CONFIGURATION
1. Procure por SQL Browser e o ative
2. Procure por TCP/IP, Habilite-o e veja se a porta esta em 1433

Configuração da Conexão
Antes de executar o aplicativo, configure as informações de conexão com o banco de dados:

1. Abra o arquivo `src/com/bradescoa3/util/ConexaoDb.java`
2. Modifique as seguintes constantes:
```java
   private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=AntiGolpeDB;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

Importando o Projeto no NetBeans
1. Abra o Apache NetBeans 25
2. Selecione "File" > "Open Project"
3. Navegue até a pasta do projeto e selecione-a
4. Clique em "Open Project"

Executando o Aplicativo
1. No NetBeans, clique com o botão direito no projeto
2. Selecione "Clean and Build"
3. Após a compilação, clique com o botão direito novamente e selecione "Run"
4. O aplicativo será iniciado com a tela de login

Usuário Administrador Padrão
O sistema cria automaticamente um usuário administrador na primeira execução:
- CPF/CNPJ: 12345678909
- Senha: 501515
