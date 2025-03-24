<h2>Estrutura e tecnologias utilizadas no projeto:</h2>

O projeto foi feito utilizando os plugins kotlin multiplatform para
compartilhamento das camadas de data e model e compose multiplatform
para compartilhamento da camada de UI, configrando-se assim em um projeto com uma 
unica base de código, mas apto a rodar nas diferentes plataformas abaixo:

Android [testado e rodando de acordo com o esperado]<br>
Desktop [testado e rodando de acordo com o esperado]<br>
IOS [em teoria deveria funcionar, mas ñ testei pois desenvolvi via windows]<br>

<h2>Features:</h2>
Listagem de personagens de ricky and morthy via API e cache local desses personagens
permitindo acesso msm offline<br>
Busca via nome<br>
Busca via filtro por estado do personagem (alive, dead, unknown)<br>
Tela de detalhes (ao clical em um personagem) com algumas informaçoes extras<br>
Testes unitários completos<br>

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
os seguintes design patterns:<br>
repository (para separação da logica de dados)<br>
usecase (para separação de regras)<br>
mapper (para conversão de objetos)<br>
dependency injection (para otmização de código e testabilidade)<br>
State (para gerenciamento dos ciclos da UI)<br>
ORM (para mapeamento dos dados de cache em objetos)<br>
Interpreter (para adaptar comportamentos especificos para cada plataforma)<br>

<h2>Tecnologias utilizadas:</h2>
Kotlin Multiplatform (linguagem e framework para escrita do codigo)<br>
Compose multiplatform (framework para escrita da parte visual[UI])<br>
kotlinx-coroutines & flow (para requests e operaçoes asincronas)<br>
kotlinx-serialization (para serialização dos jsons)<br>
Ktor (para realização de requests)<br>
SQLDelight (para armazenar dados localmente)<br>
Coil (para exibição e cache de imagens vindas de API)<br>
voyager-navigator (para fluxo de navegaçao de telas)<br>
Koin (para injeçao de dependencias)<br>
napier (para logs em KMP)<br>

<h2>Pontos de melhoria:</h2>
Testes de UI similares ao Espresso e Apium do android (Já atuei com ambos os FW mas acabei n tendo tempo de pesquisar algo semelhante no KMP [esse foi meu segundo projeto])<br>
modularização<br>


<h2>Prints:</h2>
![image](https://github.com/user-attachments/assets/09cb8071-7c71-41ed-89b9-5966cba32632)

![image](https://github.com/user-attachments/assets/053080ed-f8ef-4988-b5d5-1de8c76d04de)





