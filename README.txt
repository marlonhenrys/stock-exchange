** Toda a aplicação é executada por meio das interfaces gráficas: StockGUI e BrokerGUI.

1 - Executar a bolsa de valores:
* É necessário abrir a interface StockGUI.

1.1 - Na raiz do projeto, clique duas vezes no arquivo 'StockExchange.jar';
1.2 - [Sistema] Deverá abrir uma interface gráfica no canto direito da tela;
1.3 - Digite o endereço do servidor ou mantenha como 'localhost';
1.4 - Clique em 'Abrir Negociações';
1.5 - [Sistema] Uma thread StockReceive será iniciada para ouvir todas as mensagens publicadas
no canal 'BROKER' no servidor informado na interface;
1.6 - [Sistema] Se não ocorrer algum erro, aparecerá o texto 'Negociações abertas...' na interface.

2 - Executar uma corretora:
* É necessário abrir a interface BrokerGUI.

2.1 - Na raiz do projeto, clique duas vezes no arquivo 'Broker.jar';
2.2 - [Sistema] Deverá abrir uma interface gráfica no centro da tela;
2.3 - Digite o endereço do servidor ou mantenha como 'localhost';
2.4 - Digite o nome do Broker ou mantenha como 'BKR1' (tamanho obrigatório de 4 caracteres);
2.5 - Clique no ComboBox e selecione um ativo (ação da bovespa) ou mantenha na primeira opção;
* Os códigos dos ativos são carregados do arquivo 'acoes_bovespa.csv';
2.6 - Digite a quantidade de lotes a serem negociados para esse ativo;
2.7 - Digite o preço de cada lote a ser negociado para este ativo;
2.8 - Clique no botão 'Comprar' para abrir uma oferta de compra ou clique no botão 'Vender' para
abrir uma oferta de venda desse ativo;
2.9 - [Sistema] Uma thread BrokerEmit será iniciada para enviar a oferta (mensagem) para o canal
'BROKER' no servidor informado na interface;
2.10 - [Sistema] Os campos de quantidade e preço terão seus valores apagados após o envio.

3 - Acompanhar ativos:
* É necessário estar com a interface BrokerGUI aberta.

3.1 - Clique no botão 'Abrir Visualizador';
3.2 - [Sistema] uma nova interface será aberta ao lado do painel;
3.4 - 2.2 - Digite o endereço do servidor ou mantenha como 'localhost';
3.3 - Clique no ComboBox e selecione um ativo (ação da bovespa) ou mantenha na primeira opção;
* Os códigos dos ativos são carregados do arquivo 'acoes_bovespa.csv';
3.4 - Clique no botão 'Acompanhar' para ver todas as negociações (mensagens) desse ativo a
partir desse momento;
3.5 - [Sistema] O nome do ativo será adicionado na interface dentro de colchetes [];
3.6 - [Sistema] Uma thread BrokerReceive será iniciada para ouvir todas as mensagens do ativo
selecionado que forem publicadas no canal 'BOLSADEVALORES' no servidor informado na interface;
** É possível acompanhar vários ativos simultaneamente, basta repetir o processo de selecionar
e adicionar um novo ativo na lista. (Será iniciada uma nova thread para cada ativo).

4 - Encaminhar mensagens:
* É realizado automaticamente pelo sistema.

[Info] Quando a bolsa de valores recebe qualquer mensagem, realiza-se o encaminhamento
da mensagem para todos os Brokers que estão acompanhando o ativo envolvido;

4.1 - [Sistema] Uma thread StockEmit é iniciada para cada mensagem recebida no canal 'BROKER'
para que ela seja publicada também no canal 'BOLSADEVALORES';
4.2 - [Sistema] Para as mensagens do tipo oferta (compra e venda), realiza-se o armazenamento da
mensagem no livro de ofertas (Offer Book). 

5 - Realizar transação:
* É realizada automaticamente pelo sistema.

5.1 - [Sistema] Quando uma nova oferta é publicada no livro de ofertas (Offer Book) executa-se 
um processo de 'combinação' para encontrar uma oferta correspondente e realizar uma transação
de compra-venda;
5.2 - [Sistema] Se houver alguma correspondência gera-se uma transação;
5.3 - [Sistema] A transação é salva e emitida para a bolsa de valores por meio de uma nova 
thread BrokerEmit que publica no canal 'BROKER';
5.4 - [Sistema] A bolsa de valores recebe a transação, exibe na interface e encaminha para os Brokers
que estão acompanhando o ativo envolvido.
