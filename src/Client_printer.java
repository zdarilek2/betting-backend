public class Client_printer {
    private static final Client_printer INSTANCE = new Client_printer();

    public static Client_printer getInstance() { return INSTANCE; }

    private Client_printer() { }

    public void print(Client customer) {
        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        System.out.print("id :          ");
        System.out.println(customer.getId());
        System.out.print("first name:   ");
        System.out.println(customer.getFirst_name());
        System.out.print("last name:    ");
        System.out.println(customer.getLast_name());
        System.out.print("birth number: ");
        System.out.println(customer.getBirth_number());
        System.out.print("email:      ");
        System.out.println(customer.getEmail());
        System.out.println();
    }
}
