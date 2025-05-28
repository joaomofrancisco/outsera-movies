# Avaliação Back-end

# Requirements

- JDK 21

# Build

Para compilar e rodar os testes de integração rode o seguinte comando:

```console
./mvnw clean package
```

# Exec

Rodar com a jdk
```console
java -jar target/outsera-movies-1.0.0-SNAPSHOT-runner.jar arquivo.csv
```

# Endpoint consume

Para obetnção dos dados processados pode se executar o seguinte commando:

```console
curl -X GET 'http://localhost:8080/api/reward-range'
```

Caso tenha o [jq](https://jqlang.org/) instalado para uma melhor visualização do resultado pode se executar o seguinte comando:

```console
curl -X GET 'http://localhost:8080/api/reward-range' | jq '.'
```
