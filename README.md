# Giovani Nota Simões
# Lucas Brisch
# Livia Rosembach

# Análise de Dados de Comércio com MapReduce

Este projeto implementa várias tarefas de MapReduce do Hadoop para analisar dados de comércio internacional. A análise inclui contagem de transações, cálculo de médias e busca de valores mínimos/máximos para diversos critérios (País, Ano, Categoria e Fluxo).

## Estrutura do Projeto

- `src/main/java`: Contém as implementações dos jobs MapReduce e as classes `Writable` customizadas.
- `input_data/dataset.csv`: O conjunto de dados de entrada para a análise.
- `output_data/`: Diretório onde os resultados de cada tarefa são armazenados.

## Tarefas Disponíveis

1. **Número de transações envolvendo o Brasil.**
2. **Número de transações por ano.**
3. **Número de transações por categoria.**
4. **Número de transações por tipo de fluxo (flow).**
5. **Valor médio das transações por ano somente no Brasil.** (Uso de `AvgWritable`)
6. **Transação mais cara e mais barata no Brasil em 2016.** (Uso de `MinMaxWritable` e Combiner)
7. **Valor médio das transações por ano, considerando somente as transações do tipo exportação (Export) realizadas no Brasil.** (Uso de Combiner)
8. **Transação com o maior e menor preço (com base na coluna amount), por ano e país.** (Uso de `CountryYearWritable` como Comparable Writable)

## Como Executar

### Pré-requisitos

- Java 8 ou superior (Java 11+ recomendado)
- Apache Maven

### Execução

O projeto está configurado para rodar no modo local do Hadoop, portanto, você não precisa de um cluster Hadoop completo instalado.

#### Usando Maven

Você pode executar uma tarefa específica usando o seguinte comando:

```bash
mvn compile exec:java -Dexec.mainClass="Main" -Dexec.args="<TASK_ID>"
```

Substitua `<TASK_ID>` por um número de 1 a 8.

Exemplo (Tarefa 7):
```bash
mvn compile exec:java -Dexec.mainClass="Main" -Dexec.args="7"
```

#### Executando por uma IDE (IntelliJ/VS Code)

1. Abra `src/main/java/Main.java`.
2. Localize a variável `taskId` (linha 17):
   ```java
   String taskId = "8"; // Altere para o ID da tarefa desejada
   ```
3. Execute a classe `Main`.

### Saída (Output)

Os resultados serão armazenados em `output_data/task_<TASK_ID>/part-r-00000`. O projeto exclui automaticamente a pasta de saída existente antes de executar uma tarefa para evitar o erro "Output directory already exists" do Hadoop.

## Tipos de Dados e Visibilidade

- **Dados Numéricos**: Grandes somas e médias são processadas usando `double` e `DoubleWritable` para manter a precisão.
- **Formatação de Saída**: As médias são formatadas com 2 casas decimais e enviadas como `Text` para evitar a notação científica em números grandes, garantindo melhor legibilidade.
- **Mínimo/Máximo**: Os valores nas tarefas de Mínimo/Máximo são convertidos para `long` na saída para uma visualização mais limpa.
