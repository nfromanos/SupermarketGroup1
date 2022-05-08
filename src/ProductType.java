
public enum ProductType {
    ALIMENTO(1.2),
    BEBIDA(2.3),
    HIGIENE(1.5);

    public double getMarkup() {
        return markup;
    }

    private double markup;


    ProductType(double markup) {
        this.markup = markup;
    }
}



