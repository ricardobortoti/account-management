# Account Management

Exemplo simples de sistema de gerenciamento de contas com deploy num cluster ECS

## 🚀 Começando

Rodar o projeto dentro do container docker :
```
docker build -t account-management . && docker run -p 80:80 account-management
```

Rodar o projeto diretamente com gradle :
```
gradlew bootRun
```

## ⚙️ Executando os testes
```
gradlew build test
```


### 🔩 Coleção de Testes no postman


Na pasta postman.collection existe um arquivo de coleção chamado : 
```
Account Management - Integration Test.postman_collection.json
```
ele pode ser importado e contém tanto requests quanto testes integrados.

### 📋 Swagger

Quando a aplicação estiver rodando uma descrição da Api no swagger pode ser vista em :

```
http://localhost:80/swagger-ui.html
```

## Diagrama de Fluxo de Deploy
[![](https://mermaid.ink/img/eyJjb2RlIjoiZ3JhcGggTFJcbiAgICBBW0NvZGVdXG4gICAgQSAtLT58UHVzaCB0byBtYXN0ZXJ8IEIoR2l0SHViKVxuICAgICAgICBzdWJncmFwaCBHaXRIdWIgQWN0aW9uc1xuICAgIEIgLS0-IHxCdWlsZHwgQyhEb2NrZXIgSW1hZ2UpXG4gICAgQyAtLT4gfFVwbG9hZCB0b3wgRChBV1MgRUNSKVxuICAgIEQgLS0-IHxEZXBsb3l8IEUoRUNTIENsdXN0ZXIpXG4gICAgZW5kIiwibWVybWFpZCI6eyJ0aGVtZSI6ImRlZmF1bHQifSwidXBkYXRlRWRpdG9yIjpmYWxzZX0)](https://mermaid-js.github.io/mermaid-live-editor/#/edit/eyJjb2RlIjoiZ3JhcGggTFJcbiAgICBBW0NvZGVdXG4gICAgQSAtLT58UHVzaCB0byBtYXN0ZXJ8IEIoR2l0SHViKVxuICAgICAgICBzdWJncmFwaCBHaXRIdWIgQWN0aW9uc1xuICAgIEIgLS0-IHxCdWlsZHwgQyhEb2NrZXIgSW1hZ2UpXG4gICAgQyAtLT4gfFVwbG9hZCB0b3wgRChBV1MgRUNSKVxuICAgIEQgLS0-IHxEZXBsb3l8IEUoRUNTIENsdXN0ZXIpXG4gICAgZW5kIiwibWVybWFpZCI6eyJ0aGVtZSI6ImRlZmF1bHQifSwidXBkYXRlRWRpdG9yIjpmYWxzZX0)