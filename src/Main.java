import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    private static Object[][] produtos = new Object[10][10];
    private static Object[][] carrinho = new Object[10][5];
    private static Object[][] logVenda = new Object[10][5];


    private static int contadorItens = 0;
    private static int contadorCarrinho = 0;
    private static int logLinha = 0;

    public static void main(String[] args) {

        do {
            menu();
        } while (true);
    }

    public static void menu() {

        Scanner sc = new Scanner(System.in);
        System.out.print("Menu\n" +
                "\t1 - Cadastrar/Comprar produtos\n" +
                "\t2 - Imprimir estoque\n" +
                "\t3 - Listar os produto pelo Tipo\n" +
                "\t4 - Pesquisar um produto pelo codigo\n" +
                "\t5 - Pesquisar um produto pelo nome usando like\n" +
                "\t6 - Vendas\n" +
                "\t7 - Relatorio de vendas analitico, todas as vendas\n" +
                "\t8 - Relatorios de vendas sintetico, consolidado por CPF\n" +
                "\t9 - Sair\n");



        switch (sc.next()) {
            case "1":
                cadastrar();
                break;
            case "2":
                imprimirItens();
                break;
            case "3":
                listarPorTipos(2,3);
                break;
            case "4":
                pesquisaProdutoPeloCodigo(1,3,7,8);
                return;
            case "5":
                likeProduto();
                break;
            case "6":
                carrinho();
                break;
            case "7":
                relatorioAnaliticoVendas(logVenda);
                break;
            case "8":
                relatorioSinteticoVendas(logVenda);
                break;
            case "9":
                System.out.println("ASTALAVISTA BABY");
                System.exit(1);
                break;

            default:
                System.out.println("Opção inválida, informe novamente.");
        }
    }

    public static void cadastrar() {
//        Variaveis
        Scanner sc = new Scanner(System.in);
        int posicaoItens;
        String identificador;
        String marca;
        final int IDENTIFICADOR = 0;
        final int MARCA= 1;
        final int TIPO = 2;
        final int NOME = 3;
        final int PRECO_CUSTO = 4;
        final int QUANTIDADE = 5;
        final int DATA_COMPRA = 6;
        final int PRECO = 7;
        final int ESTOQUE= 8;
        final int FLUXO_DE_CAIXA = 9;

        System.out.println("Bem vindo ao Cadastro!\n");

        System.out.println("Digite o Identificador do Produto");
        identificador = sc.nextLine();

        System.out.println("Digite a Marca do Produto");
        marca = sc.nextLine();

//        validação de cadastro ou compra
        posicaoItens = validador(produtos, identificador, contadorItens);
        if(posicaoItens <0){
            posicaoItens = contadorItens;
            produtos[posicaoItens][ESTOQUE] = 0;
        }else{
            contadorItens--;
        }
        produtos[posicaoItens][IDENTIFICADOR] = identificador;
        produtos[posicaoItens][MARCA] = marca;

        System.out.println("Digite o Tipo do Produto(ALIMENTO, BEBIDA OU HIGIENE)");
        ProductType produto = null;
        do {
            try {
                String tipo = sc.nextLine().toUpperCase();
                produto = ProductType.valueOf(tipo);
                produtos[posicaoItens][TIPO] = produto.toString();
            }catch(Exception exception){
                System.out.println("Erro, Digite novamente!");
            }
        }while(produto == null);

        System.out.println("Digite o Nome do Produto (ex.: Coca Cola)");
        produtos[posicaoItens][NOME] = sc.nextLine();

        System.out.println("Digite o Preço Custo do Produto (ex.: 2,6)");
        do{
            produtos[posicaoItens][PRECO_CUSTO] = sc.nextDouble();
            if ((double) produtos[posicaoItens][PRECO_CUSTO] < 0.0){
                System.out.println("Preço inválido, entre novamente.");
            }
        }while((double)produtos[posicaoItens][PRECO_CUSTO]<=0.0);

        System.out.println("Digite a Quantidade do Produto (ex.: 2)");
        do {
            produtos[posicaoItens][QUANTIDADE] = sc.nextInt();
            if ((int)produtos[posicaoItens][QUANTIDADE]<0){
                System.out.println("Quantidade negativa, informe novamente");
            }
        }while ((int)produtos[posicaoItens][QUANTIDADE]<0);

        produtos[posicaoItens][DATA_COMPRA] = LocalDateTime.now();

        String tipoProduto = String.valueOf(produtos[posicaoItens][TIPO]);
        produtos[posicaoItens][PRECO] = (Double) produtos[posicaoItens][PRECO_CUSTO] * ProductType.valueOf(tipoProduto).getMarkup();

        atualizarEstoque(posicaoItens, QUANTIDADE, ESTOQUE);
        fluxoDeCaixa(posicaoItens, PRECO_CUSTO, QUANTIDADE, FLUXO_DE_CAIXA);

        contadorItens++;

        if (contadorItens == produtos.length) {
            produtos = (Object[][]) aumentarMatriz(produtos);
        }

    }

    private static void fluxoDeCaixa(int posicaoItens, int PRECO_CUSTO, int QUANTIDADE, int FLUXO_DE_CAIXA) {
        produtos[posicaoItens][FLUXO_DE_CAIXA] = (int)produtos[posicaoItens][QUANTIDADE] * (double)produtos[posicaoItens][PRECO_CUSTO]*-1.0;
    }

    private static void atualizarEstoque(int posicaoItens, int QUANTIDADE, int ESTOQUE) {
        nullViraZero(produtos, posicaoItens, ESTOQUE);
        produtos[posicaoItens][ESTOQUE] = (int)produtos[posicaoItens][ESTOQUE] + (int) produtos[posicaoItens][QUANTIDADE];
    }

    private static void nullViraZero(Object[][] lista, int linha, int coluna) {
        if (lista[linha][coluna] == null){
            lista[linha][coluna] = 0.0;
        }
    }

    public static void imprimirItens() {
        System.out.println("Bem vindo ao Relatorio!");
        System.out.println(contadorItens);

        for (int i = 0; i < contadorItens; i++) {
            for (int j = 0; j < 9; j++) {
                if(j==6){
                    imprimirTempoFormatado(produtos[i][j]);
                }
                else {
                    System.out.print(produtos[i][j] + "\t ");
                }
            }
            System.out.println();
        }
    }

    private static int validador(Object[][] lista, String validado, int contador) {
        for (int i = 0; i < contador; i++) {
            if (lista[i][0].equals(validado)){
                return i;
            }
        }
        return -1;
    }

    private static void likeProduto() {
        boolean produtoEncontrado = false;
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite o nome ou parte do nome do produto:");
        String nome = sc.nextLine();
        for (int i = 0; i < contadorItens; i++) {
            if (((String) produtos[i][3]).contains(nome)) {
                System.out.printf("%nMarca: %s \t Nome:%s\t Preço: R$%s\t Estoque: %s \t", produtos[i][1], produtos[i][3], produtos[i][7], produtos[i][8]);
                produtoEncontrado = true;
            }
        }
        if (!produtoEncontrado) System.out.println("Nenhum produto encontrado");
    }

    private static void pesquisaProdutoPeloCodigo(int MARCA, int NOME, int PRECO, int ESTOQUE){
        int a = getPosicaoCodigo(produtos, contadorItens);
        if(a==-1){
            System.out.println("O Produto não existe");
        }else{
            System.out.printf("%nMarca: %s \t Nome:%s\t Preço: R$%s\t Estoque: %s \t%n", produtos[a][MARCA], produtos[a][NOME], produtos[a][PRECO], produtos[a][ESTOQUE]);
            }
        }

    private static int getPosicaoCodigo(Object[][]lista, int contador) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite o codigo identificador:");
        String identificador = sc.nextLine().toUpperCase();
        if(identificador.equals("FIM")){
            return -2;
        }
        return validador(lista, identificador, contador );
    }

    private static void carrinho(){
        final int CPF = 0;
        final int TIPO_CLIENTE = 1;
        final int QUANTIDADE_PRODUTOS = 2;
        final int PRECO = 3;
        final int VALOR_A_PAGAR = 4;
        Scanner sc = new Scanner(System.in);
        String cpf;
        int posicaoProdutoComprado = 0;
        int quantidadeProdutoComprado;
        int posicaoProdutoCompradoImprimir;
        System.out.println("Deseja cadastrar o CPF?\n" +
                " (1 - Sim/2 - Não)");
        String opcaoDoCpf = sc.nextLine();

        if(opcaoDoCpf.equals("1")){
            System.out.println("Por favor, entre o CPF:");
            cpf = sc.nextLine();
        }else{
            cpf = "00000000191";
        }

        ClientType tipoCliente = null;

        do {
            try {
                System.out.println("Digite o tipo de cliente: PJ, PF, VIP");
                String tipo = sc.nextLine().toUpperCase();
                tipoCliente = ClientType.valueOf(tipo);
            }catch(Exception exception){
                System.out.println("Erro, Digite novamente!");
            }
        }while(tipoCliente == null);


        do{
            posicaoProdutoCompradoImprimir = posicaoProdutoComprado;
            posicaoProdutoComprado = getPosicaoCodigo(produtos,contadorItens);
            if(posicaoProdutoComprado==-2){
                break;
            }
            System.out.println("Quantos itens deseja comprar?");
            quantidadeProdutoComprado = sc.nextInt();

            if((int) produtos[posicaoProdutoComprado][8] >= quantidadeProdutoComprado) {
                carrinho[contadorCarrinho][TIPO_CLIENTE] = tipoCliente;
                carrinho[contadorCarrinho][CPF] = cpf;
                nullViraZero(carrinho, contadorCarrinho, QUANTIDADE_PRODUTOS);
                carrinho[contadorCarrinho][QUANTIDADE_PRODUTOS] = (Double)carrinho[contadorCarrinho][QUANTIDADE_PRODUTOS] + quantidadeProdutoComprado ;
                produtos[posicaoProdutoComprado][8] = (int)produtos[posicaoProdutoComprado][8] - quantidadeProdutoComprado;
                nullViraZero(carrinho, contadorCarrinho, PRECO);
                carrinho[contadorCarrinho][PRECO] = (Double) carrinho[contadorCarrinho][PRECO] +((Double)produtos[posicaoProdutoComprado][7]*quantidadeProdutoComprado);
                carrinho[contadorCarrinho][VALOR_A_PAGAR] = (Double)carrinho[contadorCarrinho][PRECO] * tipoCliente.getDiscount();
            }
            else{
                System.out.println("Quantidade insuficiente do produto");
                continue;
            }

            contadorCarrinho++;
            if (contadorCarrinho == carrinho.length) {
                carrinho = (Object[][]) aumentarMatriz(carrinho);
            }

        }while(posicaoProdutoComprado!=-2);


        for(int i=0;i<contadorCarrinho;i++) {
            System.out.printf("%nCodigo: %s | Nome: %s | Quantidade: %s | Preco: %s | ValorPagar: %s%n", produtos[posicaoProdutoCompradoImprimir][0], produtos[posicaoProdutoCompradoImprimir][3], carrinho[i][QUANTIDADE_PRODUTOS], carrinho[i][PRECO], carrinho[i][VALOR_A_PAGAR]);
        }

        for (int i = 0; i < carrinho.length; i++) {
            for (int j = 0; j < carrinho[contadorCarrinho].length; j++) {
                logVenda[logLinha][j] = carrinho[i][j];
            }
            logLinha++;
            if (logLinha == logVenda.length) {
                logVenda = (Object[][]) aumentarMatriz(logVenda);
            }
        }
        contadorCarrinho = 0;

    }

    public static void listarPorTipos(int TIPO, int NOME){
        Scanner sc = new Scanner(System.in);

        System.out.println("Bem vindo a Listagem!");
        System.out.print("Escolhe o tipo de produto a ser filtrado - Opções: ");
        for (ProductType itens : ProductType.values()) {
            System.out.print(itens + " ");
        }
        String tipo = sc.nextLine().toUpperCase();
        System.out.printf("Listando produtos do tipo %s%n", tipo );
        for (int i = 0; i < contadorItens; i++) {
            if (produtos[i][TIPO].equals(tipo)) {
                System.out.print(produtos[i][NOME] + " ");
            }
        }
        System.out.println();
    }

    private static Object aumentarMatriz(Object[][] lista) {
        Object[][] novoArray = new Object[lista.length + 10][lista[0].length];
        for (int i = 0; i < lista.length; i++) {
            for (int j = 0; j < lista[i].length; j++) {
                novoArray[i][j] = lista[i][j];
            }
        }
        return novoArray;
    }

    private static void imprimirTempoFormatado(Object lista){
        LocalDateTime batata = (LocalDateTime) lista;
        DateTimeFormatter tempoFormato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        System.out.print(batata.format(tempoFormato)+ "\t ");
    }

    private static void relatorioAnaliticoVendas(Object[][] listaVendas){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Entre o CPF da pessoa para o relatório analítico:");
        String cpfProcurado = scanner.nextLine();
        for (int i = 0; i < listaVendas.length; i++) {
            if (cpfProcurado.equals(listaVendas[i][0])){
                System.out.printf("%nCPF: %s | Tipo Cliente: %s | Quantidade de Produtos: %s | Valor Pago: %s%n", cpfProcurado, listaVendas[i][1], listaVendas[i][2],listaVendas[i][4]);
            }
        }

    }

    private static void relatorioSinteticoVendas(Object[][] listaVendas){
        Scanner scanner = new Scanner(System.in);
        int quantidadeTotal = 0;
        double valorPagoTotal = 0.0;
        System.out.println("Entre o CPF da pessoa para o relatório sintético:");
        String cpfProcurado = scanner.nextLine();
        for (int i = 0; i < listaVendas.length; i++) {
            if (cpfProcurado.equals(listaVendas[i][0])){
                quantidadeTotal += (Double)listaVendas[i][2];
                valorPagoTotal += (Double)listaVendas[i][4];
            }
        }
        System.out.printf("%nCPF: %s | Quantidade de Produtos Total: %s | Valor Pago Total: %.2f%n", cpfProcurado, quantidadeTotal, valorPagoTotal);
    }
}


//        Supermercado
//
//FASE 1
//        ________________________________________________________________________________________________________________________
//        PRODUTOS
//        - O sistema deve permitir a compra/cadastro de produtos
//        - Deve solicitar os seguintes dados
//        Tipo
//        Marca
//        Identificador
//        Nome
//        Preco Custo
//        Quantidade
//
//        `- O sistema deve verificar se o produto ja existe, se sim atualizar os dados, se não armazenar um novo produto
//
//        - O cadastro de produto devera armazenar também os seguintes dados
//        Data Compra
//        Preco de venda
//        Estoque
//
//        - O preco de venda deve ser calculado a partir do preco de custo, usando o markup de cada tipo de produto
//
//        - O estoque deve ser atualizado a cada compra.
//
//        - Armazenar os produtos em uma matriz
//
//        - Caractetriscas do produto:
//
//        Tipo: (Enum) ALIMENTOS - markup 1.2 , BEBIDA - markup 2.3, HIGIENTE - markup 1.5
//        Marca: (String)
//        Identificador: (String)
//        Nome: (String)
//        Preco Custo: (Double)
//        Quantidade: (int)
//        Data Compra: (LocalDatetime)
//        Preco: (Double) deve ser calculado a patir do preco de custo, markup cada tipo de produto tem o seu markup
//        Estoque: (int)
//
//        Criar um menu de acesso ao sistema
//
//        1 - Cadastrar/Comprar produtos
//        2 - Imprimir estoque
//        3 - Listar os produto pelo Tipo
//
//        OBS:
//        - Quatidade de protutos nao pode ser negativa, e os precos de custo também
//        - Usar o redimensionamento...
//
//        +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//FASE 2
//        ________________________________________________________________________________________________________________________
//        TIPOS DE CLIENTES
//        - Criar um enum para os tipos de clientes
//
//        Tipo de Clientes: (Enum) PF, PJ, VIP
//        Clientes PF: 0% desconto
//        PJ: 5% desconto
//        VIP: 15% desconto
//
//        ________________________________________________________________________________________________________________________
//        VENDAS
//
//
//        - O sistema deve permitir a venda de produtos
//
//        - O sistema deve perguntar ao usuario se ele deseja inserir CPF ou não
//        se sim, solicitar o CPF, se não a compra ira usar um CPF valido padrão para venda ao consumidor 000 000 001 91
//
//        - Se o CPF for digitado, o sistema deve pedir o tipo de cliente.
//
//        - O sistema deve solicitar os codigos dos produtos comprados e suas quantidades
//        O sistema deve continuar solicitando os produtos e suas quantidades até que a palavra FIM seja digitada no lugar do codigo do produto
//
//        - ao final o sistema devera mostrar um resumo com os produtos comprados
//        Codigo | Nome | Quantidade | Preco | ValorPagar
//
//        apos a exibiçao do resumo, mostrar o valor total a pagar Armazenar a venda em uma matriz mostrar e uma opçao para voltar ao menu principal
//
//        Vendas
//        CPF Cliente
//        Tipo Cliente
//        Quantidade de produtos
//        Valor total Pago
//
//
//        OBS, 	se um prodtuto informado não existir, mostra msg de produto invalido
//        se a quantidade um produto não tiver estoque, mostrar a msg sem estoque suficiente e mostrar o estoque do produto
//
//        Ao terminar de inseir os produtos, o sistema deve baixar o estoque de cada produto vendido no cadastro
//        Deve ser aplicado o desconto no total do valor a pagar de acordo com o desconto de cada tipo de cliente
//
//
//        1 - Cadastrar/Comprar produtos
//        2 - Imprimir estoque
//        3 - Listar os produtos Por Tipo
//        4 - Pesquisar um produto pelo codigo
//        5 - Pesquisar um produto pelo nome usando like
//        6 - Vendas
//        7 - Relatorio de vendas analitico, todas as vendas
//        CPF | Tipo Cliente | Quantidade de Produtos | Valor Pago
//        8 - Relatorios de vendas sintetico, consolidado por CPF
//        CPF | Quantidade de Produtos | Valor Pago
