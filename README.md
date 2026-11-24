# Ludoteca API

Projeto acadêmico da disciplina de Back-End Frameworks. A API REST permite catalogar jogos de tabuleiro. Cada jogo tem título, categoria, faixa de jogadores e duração estimada. O projeto usa Java 21, Spring Boot, Spring Data JPA e PostgreSQL 16.

## Compilar e testar

Na raiz do projeto, com JDK 21:

```bash
./mvnw clean package
```

No Windows, use `mvnw.cmd clean package`. Com Maven 3.9+ já instalado, `mvn clean package` também funciona. O Maven Wrapper baixa o Maven na primeira execução, se necessário.

O comando executa os testes e gera `target/ludoteca-api.jar`. Para rodar somente os testes, use `./mvnw test`. Os testes de Service e Controller usam objetos simulados e podem rodar sem banco. Os resultados detalhados ficam em `target/surefire-reports/`.

## Executar com Docker Compose

Com o Docker Engine ativo e o Docker Compose instalado, compile o projeto, copie o modelo e defina uma senha local em `.env`.

```bash
cp -n .env.example .env
```

Depois de preencher `POSTGRES_PASSWORD`, execute:

```bash
docker compose up --build -d
docker compose ps
docker compose logs --tail=30 api
curl --fail --show-error http://127.0.0.1:8080/actuator/health
```

Se alterar `API_PORT` em `.env`, substitua 8080 nos exemplos pela porta escolhida. A resposta esperada do Actuator é HTTP `200` com o campo `"status":"UP"`. Se a API ainda estiver iniciando, acompanhe `docker compose logs -f api` e tente novamente.

Use `docker compose stop` para parar os serviços e `docker compose start` para iniciar os mesmos containers. `docker compose down` remove os containers e a rede do projeto e mantém o volume nomeado. `docker compose down -v` também remove o volume e apaga os dados armazenados nele.

## Executar o JAR fora da IDE

Com um PostgreSQL acessível, crie um banco e um usuário com permissão para criar a tabela no esquema utilizado. O exemplo usa PostgreSQL local na porta 5432. Ajuste a URL e o usuário conforme a instalação. Use o perfil `local` para limitar a escuta HTTP ao próprio computador.

```bash
export DB_URL=jdbc:postgresql://127.0.0.1:5432/ludoteca
export DB_USER=ludoteca
read -rs -p 'Senha do PostgreSQL: ' DB_PASSWORD
printf '\n'
export DB_PASSWORD

SPRING_PROFILES_ACTIVE=local java -jar target/ludoteca-api.jar
```

O arquivo `.env` é lido pelo Compose. Para a execução direta do JAR, use as variáveis exportadas acima. O esquema inicial está em `src/main/resources/schema.sql`. A aplicação tenta criar a tabela quando ela ainda não existe e o Hibernate valida seu mapeamento na inicialização.

## Usar a API

| Operação | Rota | Sucesso |
| --- | --- | --- |
| Cadastrar | `POST /jogos` | `201 Created`, JSON e cabeçalho `Location` |
| Listar | `GET /jogos` | `200 OK`, lista ordenada por ID |
| Buscar | `GET /jogos/{id}` | `200 OK`, JSON do jogo |
| Atualizar | `PUT /jogos/{id}` | `200 OK`, JSON atualizado |
| Excluir | `DELETE /jogos/{id}` | `204 No Content` |

```bash
BASE=http://127.0.0.1:8080

curl -i -X POST "$BASE/jogos" -H 'Content-Type: application/json' \
  -d '{"titulo":"Trilha das Ilhas","categoria":"Estratégia","minimoJogadores":2,"maximoJogadores":4,"duracaoMinutos":45}'

curl -i "$BASE/jogos"
```

Substitua `1` pelo identificador devolvido no cadastro. O `PUT` recebe os cinco campos.

```bash
ID=1
curl -i "$BASE/jogos/$ID"

curl -i -X PUT "$BASE/jogos/$ID" -H 'Content-Type: application/json' \
  -d '{"titulo":"Trilha das Ilhas","categoria":"Estratégia","minimoJogadores":2,"maximoJogadores":5,"duracaoMinutos":60}'

curl -i -X DELETE "$BASE/jogos/$ID"
curl -i "$BASE/jogos/$ID"
```

Os textos são obrigatórios e limitados a 120 caracteres no título e 80 na categoria. Os três números devem ser inteiros positivos e o máximo de jogadores deve ser igual ou superior ao mínimo. Entradas inválidas resultam em `400`. Busca, atualização ou exclusão de um ID inexistente retorna `404`.

## Evidências de execução

- [Build e testes](evidencias/01-build-e-testes.txt)
- [JAR, PostgreSQL, CRUD e Actuator](evidencias/02-jar-e-http.txt)
- [Compose, Actuator e persistência após recriação dos containers](evidencias/03-compose-e-volume.txt)
