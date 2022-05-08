public enum ClientType {
    PF(1),
    PJ(0.95),
    VIP(0.85);

    private final double discount;

    ClientType(double discount){
        this.discount = discount;
    }

    public double getDiscount() {
        return discount;
    }


}
