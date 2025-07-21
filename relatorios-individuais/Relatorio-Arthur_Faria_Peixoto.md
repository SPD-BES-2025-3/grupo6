## **Relatório individual etapa 1**   
Arthur Faria Peixoto \- 201910873

Para a primeira entrega do projeto final, o Grupo 6 (composto pelos alunos Álvaro Veloso Lisboa, Arthur Faria Peixoto e Thâmara Cordeiro de Castro) definiu uma reunião inicial em que discutimos o problema a ser resolvido, avaliamos as tecnologias disponíveis e dividimos as responsabilidades. Nossa arquitetura prevê três frentes principais: uma aplicação desktop, uma API RESTful e, futuramente, um integrador de entidades.  
Na fase de planejamento, a Thâmara ficou responsável por toda a documentação. O Álvaro deu início à camada de persistência ORM, criando os mapeamentos de entidades para as operações básicas de CRUD. Eu me encarreguei da parte de interface, optando por uma solução inovadora e diferente: desenvolvi o front-end em Next.js e o empacotei com Electron para atender ao requisito de desktop.  
O Next.js nos trouxe renderização híbrida (servidor e cliente), roteamento automático e otimizações de performance, enquanto o Electron me permitiu “embrulhar” nossa aplicação web em uma janela nativa, com acesso a APIs de sistema. Para acelerar o desenvolvimento da interface, instalei bibliotecas como “react‑hook‑form” (para formulários mais simples e performáticos), Material UI para componentes estilizados e “react‑icons” para ícones vetoriais. Em paralelo, implementei a lógica de autenticação: o usuário faz login e é redirecionado conforme seu perfil, com tratamento de erros exibido por um componente global de alertas já integrado ao “theme” personalizado que fiz para o Material UI.  
Essa base de theming garante que cores, tipografia e espaçamentos sigam um padrão moderno e consistente, possibilitando a criação rápida de novas telas de CRUD e testes de usabilidade. Com o localhost:3000 rodando, basta iniciar o Electron para testar o sistema como uma aplicação desktop eliminando a dependência de navegadores durante o desenvolvimento. Uma vez finalizado o projeto, basta fazer o build da interface com o Next.js e utilizar o resultado do build para acessar pelo Electron  
Com isso tudo feito, o grupo se dispõe de uma praticidade enorme, facilitando o desenvolvimento futuro com base na documentação.

## **Imagens da aplicação:**
Aplicação Next rodando em desktop com Electron  
<img width="708" height="541" alt="image" src="https://github.com/user-attachments/assets/49139818-e23d-40a7-9faf-acce9563c2c6" />

Tela em “Fullscreen”  
<img width="1110" height="621" alt="image" src="https://github.com/user-attachments/assets/fbb39aba-14c1-41f1-b43a-efdd9b00ef98" />


Teste com dados errados  
<img width="425" height="352" alt="image" src="https://github.com/user-attachments/assets/3a7b458e-3332-4958-9919-3692fa352d50" />


Resultado do login inválido  
<img width="506" height="398" alt="image" src="https://github.com/user-attachments/assets/77fa1b1c-01ee-4d1f-acec-ca9fba71091e" />


