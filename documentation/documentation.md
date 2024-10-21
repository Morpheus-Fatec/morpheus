# Documentação

## Backlog do Produto
- [Sprint 1](#sprint-1)
   - [Gerenciar Portais de Notícias](#gerenciar-portais-de-notícias)
   - [Gerenciar Tags](#gerenciar-tags)

- [Sprint 2:](#sprint-2)
  - [Registrar dados provenientes de Portais de Notícias](#registrar-dados-de-portais-de-noticias)
   - [Tratar sinônimos de tags](#tratar-sinonimos-de-tags)


## Entidade Relacionamento
![entidade-relacionamento](./diagrams/morpheus-bd.jpg)

# Sprint 1:

## <a id="gerenciar-portais-de-notícias">Gerenciar Portais de Notícias por uma interface que me permita cadastrar endereços e possíveis autores das notícias.</a>

### Critérios de Aceitação:

1. **Cadastro de Portais**:
   - O sistema deve permitir o cadastro de portais de notícias, incluindo o URL e o nome do portal.

2. **Relacionamento de Tags**:
   - O sistema deve permitir relacionar várias tags a cada portal cadastrado.

3. **Edição e Remoção**:
   - O sistema deve permitir a edição e remoção de portais e suas tags já cadastradas.

4. **Interface de Usuário**:
   - A interface deve ser intuitiva, permitindo que os usuários visualizem a lista de portais e suas respectivas tags de forma clara.

5. **Performance**:
   - O sistema deve ser capaz de cadastrar, editar e remover portais e tags de forma rápida e eficiente.

6. **Documentação**:
   - A documentação deve ser atualizada para refletir as funcionalidades relacionadas ao gerenciamento de portais de notícias.

## <a id="gerenciar-tags"> Gerenciar Tags, sendo essas conteúdos textuais livres, considerando regionalismo e palavras polissêmicas. </a>

### Critérios de Aceitação
1. **Cadastro de Tags**:
   - O sistema deve permitir o cadastro de tags como conteúdos textuais livres.

2. **Edição e Remoção**:
   - O sistema deve permitir a edição e remoção de tags já cadastradas.

3. **Interface de Usuário**:
   - A interface deve ser intuitiva, permitindo que os usuários visualizem e gerenciem as tags de forma clara.

4. **Performance**:
   - O sistema deve ser capaz de cadastrar, editar e remover tags de forma rápida e eficiente.

5. **Documentação**:
   - A documentação deve ser atualizada para refletir as funcionalidades relacionadas ao gerenciamento de tags.

# Sprint 2:

## <a id="registrar-dados-de-portais-de-noticias">Registrar dados provenientes de portais de notícias vinculando uma notícia ao seu devido autor e a data de sua publicação e registro </a>

### Critérios de aceitação:

1. **Coleta de Dados**:
   - O sistema deve extrair os dados pertinentes da notícia a partir do conteúdo da página, incluindo título, conteúdo principal, autor e data de publicação, assegurando que apenas as informações relevantes sejam coletadas.

2. **Armazenamento**:
   - Os dados coletados devem ser armazenados em um banco de dados estruturado, com campos separados para título, conteúdo, autor e data.

3. **Validação de Dados**:
   - O sistema deve validar que o autor e a data estão no formato correto antes de armazenar as informações.

4. **Performance**:
   - O sistema deve ser capaz de registrar e acessar os dados de forma rápida e eficiente, mesmo com um grande volume de notícias.

5. **Documentação**:
   - A documentação deve ser atualizada para refletir as funcionalidades do sistema.

## <a id="tratar-sinonimos-de-tags">Tratar sinônimos de tags, para que a aplicação possa contemplar regionalismo nos textos das tags</a>

1. **Cadastro de Palavras**:
   - O sistema deve permitir o cadastro de palavras que serão utilizadas como tags.

2. **Relação de Sinônimos**:
   - O sistema deve possibilitar a relação de cada palavra a uma lista de sinônimos, permitindo a inclusão de várias palavras associadas a uma única tag.

3. **Geração de Tags**:
   - Ao cadastrar uma tag, o sistema deve gerar automaticamente as tags correspondentes, substituindo as palavras pelos sinônimos cadastrados.

4. **Interface de Usuário**:
   - A interface deve permitir que os usuários visualizem e editem as relações de sinônimos de forma intuitiva.

5. **Validação**:
   - O sistema deve validar que as palavras e sinônimos cadastrados não sejam duplicados.

6. **Performance**:
   - O sistema deve gerar as tags sinônimas de forma rápida, mesmo com um grande volume de palavras e sinônimos cadastrados.

7. **Documentação**:
   - A documentação deve ser atualizada para refletir as funcionalidades relacionadas ao tratamento de sinônimos de tags.

# Sprint 3:

## <a id="tratar-sinonimos-de-tags">Tratar sinônimos de tags, para que a aplicação possa contemplar regionalismo nos textos das tags</a>

### Critérios de aceitação:

1. **Cadastro de Palavras**:
   - O sistema deve permitir o cadastro de palavras que serão utilizadas como tags.

2. **Relação de Sinônimos**:
   - O sistema deve possibilitar a relação de cada palavra a uma lista de sinônimos, permitindo a inclusão de várias palavras associadas a uma única tag.

3. **Geração de Tags**:
   - Ao cadastrar uma tag, o sistema deve gerar automaticamente as tags correspondentes, substituindo as palavras pelos sinônimos cadastrados.

4. **Interface de Usuário**:
   - A interface deve permitir que os usuários visualizem e editem as relações de sinônimos de forma intuitiva.

5. **Validação**:
   - O sistema deve validar que as palavras e sinônimos cadastrados não sejam duplicados.

6. **Performance**:
   - O sistema deve gerar as tags sinônimas de forma rápida, mesmo com um grande volume de palavras e sinônimos cadastrados.

7. **Documentação**:
   - A documentação deve ser atualizada para refletir as funcionalidades relacionadas ao tratamento de sinônimos de tags.

## <a id="registrar-dados-de-portais-de-noticias">Registrar dados provenientes de portais de notícias vinculando uma notícia ao seu devido autor e a data de sua publicação e registro</a>

### Critérios de aceitação:

1. **Coleta de Dados**:
   - O sistema deve extrair os dados pertinentes da notícia a partir do conteúdo da página, incluindo título, conteúdo principal, autor e data de publicação, assegurando que apenas as informações relevantes sejam coletadas.

2. **Armazenamento**:
   - Os dados coletados devem ser armazenados em um banco de dados estruturado, com campos separados para título, conteúdo, autor e data.

3. **Validação de Dados**:
   - O sistema deve validar que o autor e a data estão no formato correto antes de armazenar as informações.

4. **Performance**:
   - O sistema deve ser capaz de registrar e acessar os dados de forma rápida e eficiente, mesmo com um grande volume de notícias.

5. **Documentação**:
   - A documentação deve ser atualizada para refletir as funcionalidades do sistema.
