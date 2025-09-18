# ReportApp

## Visão Geral do Projeto

O **ReportApp** é um aplicativo Android nativo, desenvolvido em Java, que permite aos usuários reportar problemas urbanos de forma simples e eficiente. A aplicação foi projetada como um portfólio para demonstrar habilidades em desenvolvimento Android nativo, incluindo a implementação de uma arquitetura robusta, gerenciamento de dados locais e integração com serviços de geolocalização.

## Funcionalidades Principais

*   **Autenticação de Usuário:** Sistema seguro de login e registro.
*   **Relato de Problemas:** Usuários podem criar relatórios detalhados, anexando informações e a localização exata do problema.
*   **Visualização em Mapa:** Todos os relatórios são exibidos em um mapa interativo, permitindo uma visualização clara da situação da cidade.
*   **Histórico de Relatórios:** Os usuários podem visualizar e gerenciar os relatórios que criaram.
*   **Perfil de Usuário:** Gerenciamento das informações do usuário.

## Arquitetura e Tecnologias

O projeto foi estruturado para ser escalável e de fácil manutenção, seguindo as melhores práticas de desenvolvimento Android.

*   **Linguagem:** **Java**
*   **Arquitetura:**
    *   **Single-Activity Architecture:** Utiliza uma única `MainActivity` que gerencia múltiplos Fragments.
    *   **Android Navigation Component:** Para gerenciar a navegação entre as telas (Fragments), garantindo um fluxo de usuário consistente e desacoplado.
*   **Interface de Usuário (UI):**
    *   Layouts definidos em **XML**.
    *   Uso de `RecyclerView` para listas eficientes (ex: histórico de relatórios).
*   **Persistência de Dados:**
    *   **SQLite:** Para armazenamento local dos dados de usuários e relatórios, gerenciado pela classe `DatabaseHelper`.
*   **Utils:**
    *   O projeto conta com classes utilitárias para gerenciar permissões (`PermissionUtils`), sessões de usuário (`SessionManager`), e localização (`LocationHelper`), promovendo a reutilização de código.

## Estrutura do Projeto

O código-fonte está organizado nos seguintes pacotes:

```
app/src/main/java/com/example/report/
|
├── data/
│   ├── models/         # Modelos de dados (User, Problem, etc.)
│   └── DatabaseHelper.java # Gerenciador do banco de dados SQLite
│
├── ui/
│   ├── auth/           # Telas de Login e Registro
│   ├── history/        # Fragmento e Adapter para o histórico
│   ├── map/            # Fragmento para o mapa
│   ├── profile/        # Fragmento para o perfil do usuário
│   └── report/         # Fragmento para a criação de novos relatórios
│
├── utils/              # Classes utilitárias (Permissões, Sessão, etc.)
│
└── MainActivity.java   # Ponto de entrada e container dos Fragments
```

## Como Executar

1.  Clone o repositório.
2.  Abra o projeto no Android Studio.
3.  Compile e execute em um emulador ou dispositivo físico com Android.
