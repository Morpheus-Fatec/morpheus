# <p align = "center"> Morpheus - API 3º Semestre - BD 2024



<p align="center">
  <a href="#integrantes-da-equipe">Integrantes</a> •
  <a href="#descrição-do-desafio">Desafio</a> •
  <a href="#objetivo">Objetivo</a> •
  <a href="#requisitos-funcionais">Requisitos Funcionais</a> •
  <a href="#requisitos-não-funcionais">Requisitos Não Funcionais</a> •
  <a href="#cronograma">Cronograma</a> •
  <a href="#product-backlog">Product Backlog</a> •
  <a href="#documentação">Documentação</a> •
  <a href="#tecnologias-utilizadas">Tecnologias Utilizadas</a>
</p>




## :mortar_board: Integrantes:

| **Nome**                   | **Função**            | **LinkedIn**                                                  |
|:----------------------:|:-----------------:|:----------------------------------------------------------:|
| César Truyts           | Scrum Master      | [![LinkedIn](https://img.shields.io/badge/LinkedIn-Profile-blue?style=flat-square&logo=linkedin&labelColor=blue)](https://www.linkedin.com/in/cesar-augusto-anselmo-pelogia-truyts-94a08a268/) |
| Ricardo Campos         | Product Owner     | [![LinkedIn](https://img.shields.io/badge/LinkedIn-Profile-blue?style=flat-square&logo=linkedin&labelColor=blue)](https://www.linkedin.com/in/ricardo-campos-ba56091b5/) |
| Elbert Jean         | Desenvolvedor     | [![LinkedIn](https://img.shields.io/badge/LinkedIn-Profile-blue?style=flat-square&logo=linkedin&labelColor=blue)](https://www.linkedin.com/in/elbertjean/) |
| Gabriel Souza           | Desenvolvedor     | [![LinkedIn](https://img.shields.io/badge/LinkedIn-Profile-blue?style=flat-square&logo=linkedin&labelColor=blue)](https://www.linkedin.com/in/gabriel-alves-de-souza-5b7747267/) |
| Isaque de Souza           | Desenvolvedor     | [![LinkedIn](https://img.shields.io/badge/LinkedIn-Profile-blue?style=flat-square&logo=linkedin&labelColor=blue)](https://www.linkedin.com/in/isaque-souza-6760b8270/) |
| Julio Araujo           | Desenvolvedor     | [![LinkedIn](https://img.shields.io/badge/LinkedIn-Profile-blue?style=flat-square&logo=linkedin&labelColor=blue)](https://www.linkedin.com/in/julio-cesar-da-silva-araujo-65182911b/) |
| Mateus Marques          | Desenvolvedor     | [![LinkedIn](https://img.shields.io/badge/LinkedIn-Profile-blue?style=flat-square&logo=linkedin&labelColor=blue)](https://www.linkedin.com/in/mateus-soares-4983681a0) |
| Paloma Soares  | Desenvolvedor     | [![LinkedIn](https://img.shields.io/badge/LinkedIn-Profile-blue?style=flat-square&logo=linkedin&labelColor=blue)](https://www.linkedin.com/in/paloma-soares-rocha/) |
| Vinícius Monteiro  | Desenvolvedor     | [![LinkedIn](https://img.shields.io/badge/LinkedIn-Profile-blue?style=flat-square&logo=linkedin&labelColor=blue)](https://www.linkedin.com/in/viniciusvasm/) |


## :anger: Descrição do Desafio

Desenvolver uma ferramenta para captura e armazenamento de notícias estratégicas e dados relevantes associados.

## :dart: Objetivo

Criar um mecanismo para mapeamento de portais de notícias estratégicas, com captura rotineira para geração de histórico. Em um estágio futuro, aplicar análises baseadas em IA e/ou machine learning para cruzamento de dados, visando identificar ações estratégicas para o negócio. Essa estrutura e conceito também devem ser aplicados a APIs que fornecem dados estratégicos, como, por exemplo, previsão do tempo.

## :page_facing_up: Requisitos Funcionais
* Cadastro de Portais de notícias
* Cadastro de APIs
* Cadastro de Tags
* Cadastro de Jornalistas
* Processo de web scraping (capturar os dados de notícias e apis e armazenar em banco de dados)
* Indicação de tags que estão relacionadas
* Tela de consulta de notícias, com filtros de pesquisa
* Tela de APIs, com filtros de pesquisa

## :page_with_curl: Requisitos Não Funcionais

* Prever um grande volume de notícias armazenas
* Utilizar softwares livres
* Manual do Usuário
* Guia de instalação
* Java (linguagem de programação, frameworks e APIs)
* Documentação API – Application Programming Interface com OpenID
* Modelagem de Banco de Dados ou Arquivo de dados.
* Deverá ser uma aplicação web.
* O front-end deve ser desenvolvido de forma minimalista.

## :calendar: Cronograma

| Sprint  | Nome | Data inicio  | Data Fim | Status |
| ------------- | ------------- | ------------- | ------------- | ------------- |
| --  | KickOff   | 26/08   | 30/08 | Ok |
|  1  | Sprint 1   | 09/09   | 29/09 |  |
|  2  | Sprint 2   | 30/09   | 20/10 |  |
|  3  | Sprint 3   | 21/10   | 10/11 |  |
|  4  | Sprint 4   | 11/11   | 30/11 |  |
|  5  | Feira de Soluções  | 12/12     |


## :date: Product BackLog
<table>
    <thead>
    <tr>
        <th>Identificador</th>
        <th>Como</th>
        <th>User Storie</th>
        <th>Sprint</th>
        <th>Prioridade</th>
        <th>Dependência</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>A</td>
        <td>Analista</td>
        <td>Gerenciar portais de notícias por uma interface que me permita cadastrar endereços e possíveis autores das notícias</td>
        <td>1</td>
        <td>Alta</td>
        <td>-</td>
    </tr>
    <tr>
        <td>B</td>
        <td>Analista</td>
        <td>Gerenciar fontes de dados provenientes de APIs (Interface de Programação de Aplicações) por meio de uma interface que me permita cadastrar endereços</td>
        <td>1</td>
        <td>Alta</td>
        <td>-</td>
    </tr>
    <tr>
        <td>C</td>
        <td>Analista</td>
        <td>Registrar dados provenientes de portais de notícias vinculando uma notícia ao seu devido autor e a data de sua publicação e registro</td>
        <td>2</td>
        <td>Alta</td>
        <td>A</td>
    </tr>
    <tr>
        <td>D</td>
        <td>Analista</td>
        <td>Registrar dados provenientes de fontes de dados provenientes de APIs vinculadas a sua devida fonte e a sua data de registro</td>
        <td>2</td>
        <td>Alta</td>
        <td>B</td>
    </tr>
    <tr>
        <td>E</td>
        <td>Analista</td>
        <td>Consultar dados provenientes de portais de notícias e de fontes de dados provenientes de APIs</td>
        <td>3</td>
        <td>Média</td>
        <td>-</td>
    </tr>
    <tr>
        <td>F</td>
        <td>Analista</td>
        <td>Filtrar os dados de uma consulta de dados provenientes de portais de notícias e de fontes de dados provenientes de APIs utilizando tags, mediante uma análise do conteúdo registrado</td>
        <td>3</td>
        <td>Média</td>
        <td>C, D e E</td>
    </tr>
    <tr>
        <td>G</td>
        <td>Analista</td>
        <td>Filtrar os dados de uma consulta de dados provenientes de portais de notícias e de fontes de dados utilizando filtros referente aos dados de uma fonte de dados ou dados de um portal de notícias</td>
        <td>4</td>
        <td>Baixa</td>
        <td>F</td>
    </tr>
    <tr>
        <td>H</td>
        <td>Analista</td>
        <td>Filtrar os dados de uma consulta de dados provenientes de portais de notícias e de fontes de dados utilizando filtros referente aos dados de uma fonte de dados ou dados de um portal de notícias</td>
        <td>4</td>
        <td>Baixa</td>
        <td>F</td>
    </tr>
    <tr>
        <td>I</td>
        <td>Analista</td>
        <td>Filtrar os dados de uma consulta de dados provenientes de portais de notícias e de fontes de dados utilizando filtros vinculados aos dados de um registro, seja ele proveniente de uma fonte de dados de APIs ou de um portal de notícias.</td>
        <td>4</td>
        <td>Baixa</td>
        <td>F</td>
    </tr>
    </tbody>
</table>

## :chart_with_upwards_trend: Gráfico Burndown
<div align="center">
    <img src="./documentation/burndown" alt="Gráfico Burndown" alt="Gráfico Burndown" width="75%">
</div>


## :page_with_curl: Documentação

Todo o material relacionado à documentação do projeto pode ser encontrado no diretório [docs](./documentation/).

### Estrutura da Documentação:
- **[Diagrams](./documentation/diagrams/)**: Contém os diagramas e esquemas do projeto, incluindo o Diagrama Entidade-Relacionamento (DER) e diagramas de arquitetura.
- **[API](./documentation/api/)**: Contém a especificação da API, exemplos de requisições e respostas, e guias de autenticação.
- **[Manuais](./documentation/manuals/)**: Contém manuais do usuário, guias de instalação, configuração e resolução de problemas.


## :bookmark: Tecnologias Utilizadas
> * [Java](https://www.java.com/pt-BR/) - Versão 21
> * [JavaScript](https://developer.mozilla.org/pt-BR/docs/Web/JavaScript) - Versão ES6+
> * [MySQL](https://www.mysql.com/) - Versão 8.0
> * [JDBC MySQL](https://dev.mysql.com/downloads/connector/j/) - Versão 8.0.26
> * [Maven](https://maven.apache.org/) - Versão 3.8.1
> * [Spring Boot](https://spring.io/projects/spring-boot) - Versão 3.3.3
> * [Vue.js](https://vuejs.org/) - Versão 3
> * [Git](https://git-scm.com/)
> * [GitHub](https://github.com/)
> * [Itellij](https://www.jetbrains.com/pt-br/idea/)
> * [VisualStudioCode](https://visualstudio.microsoft.com/pt-br/)
> * [Discord](https://discord.com/)
> * [Slack](https://slack.com/)
> * [Google Docs](https://docs.google.com/)
