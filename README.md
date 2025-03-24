<h2>Estrutura e tecnologias utilizadas no projeto:</h2>

O projeto foi feito utilizando os plugins kotlin multiplatform para
compartilhamento das camadas de data e model e compose multiplatform
para compartilhamento da camada de UI, configrando-se assim em um projeto com uma 
unica base de código, mas apto a rodar nas diferentes plataformas abaixo:

Android [testado e rodando de acordo com o esperado]
Desktop [testado e rodando de acordo com o esperado]
IOS [em teoria deveria funcionar, mas ñ testei pois desenvolvi via windows]

<h2>Features:</h2>
Listagem de personagens de ricky and morthy via API e cache local desses personagens
permitindo acesso msm offline
Busca via nome
Busca via filtro por estado do personagem (alive, dead, unknown)
Tela de detalhes (ao clical em um personagem) com algumas informaçoes extras
Testes

<h2>Arquitetura do projeto:</h2>

Como forma de deixar o projeto organizado facilitando sua legibilidade, testabilidade,
manutenbilidade e escalabilidade foram usadas as melhores práticas em seu desenvolvimento.

Como a clean archirterture para organizar os arquivos do projeto nas layers
data, model e presentation facilitando sua legibilidade. 

E a escolha de uma arquitetura para separação das layers a nivel de código 
permitindo uma melhor manutenbilidade.

Devido ao compose ser um framework de UI declarativa, optei por usar o MVI
em vez do MVVM, devido ao MVI ter uma melhor adaptabilidade ao gerenciamento de
estados algo necessário no compose.

Para reforçar a separação de responsabilidades buscada pela arquitetura tbm usei
os seguintes design patterns:
repository (para separação da logica de dados)
usecase (para separação de regras)
mapper (para conversão de objetos)
dependency injection (para otmização de código e testabilidade)
State (para gerenciamento da UI)
ORM (para mapeamento dos dados de cache em objetos)
Interpreter (para adaptar comportamentos especificos para cada plataforma)

<h2>Tecnologias utilizadas:</h2>
Kotlin Multiplatform (linguagem para escrito do codigo)
Compose multiplatform (framework para escrita da parte visual)
kotlinx-coroutines & flow (para requests e operaçoes asincronas)
kotlinx-serialization (para serialização dos jsons)
Ktor (para realização de requests)
SQLDelight (para armazenar dados localmente)
Coil (para exibição e cache de imagens vindas de API)
voyager-navigator (para fluxo de navegaçao de telas)
Koin (para injeçao de dependencias)
napier (para logs em KMP)



