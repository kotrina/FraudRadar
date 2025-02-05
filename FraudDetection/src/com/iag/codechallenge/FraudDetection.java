package com.iag.codechallenge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FraudDetection {

    public FraudDetection() {
    	
    }
	public List<FraudResult> check(String filePath) throws IOException {
        // READ FRAUD LINES
        List<Order> orders = new ArrayList<>();
        List<FraudResult> fraudResults = new ArrayList<>();

        List<String> lines = Files.readAllLines(Paths.get(filePath));

        for (String line : lines) {
            String[] items = line.split(",");

            Order order = new Order(
                Integer.parseInt(items[0]),
                Integer.parseInt(items[1]),
                items[2].toLowerCase(),
                items[3].toLowerCase(),
                items[4].toLowerCase(),
                items[5].toLowerCase(),
                items[6],
                items[7]
            );
            orders.add(order);
        }

        // NORMALIZE
        for (Order order : orders) {
            // Normalize email
            String[] aux = order.getEmail().split("@");
            int atIndex = aux[0].indexOf("+");
            if (atIndex >= 0) {
                aux[0] = aux[0].substring(0, atIndex);
            }
            aux[0] = aux[0].replace(".", "");
            order.setEmail(aux[0] + "@" + aux[1]);

            // Normalize street
            order.setStreet(order.getStreet().replace("st.", "street").replace("rd.", "road"));

            // Normalize state
            order.setState(order.getState().replace("il", "illinois")
                                        .replace("ca", "california")
                                        .replace("ny", "new york"));
        }

        // CHECK FRAUD
        for (int i = 0; i < orders.size(); i++) {
            Order current = orders.get(i);

            for (int j = i + 1; j < orders.size(); j++) {
                Order other = orders.get(j);
                boolean isFraudulent = false;

                if (current.getDealId() == other.getDealId()
                        && current.getEmail().equals(other.getEmail())
                        && !current.getCreditCard().equals(other.getCreditCard())) {
                    isFraudulent = true;
                }

                if (current.getDealId() == other.getDealId()
                        && current.getState().equals(other.getState())
                        && current.getZipCode().equals(other.getZipCode())
                        && current.getStreet().equals(other.getStreet())
                        && current.getCity().equals(other.getCity())
                        && !current.getCreditCard().equals(other.getCreditCard())) {
                    isFraudulent = true;
                }

                if (isFraudulent) {
                    fraudResults.add(new FraudResult(other.getOrderId(), true));
                }
            }
        }

        return fraudResults;
    }

    public static class FraudResult {
        private int orderId;
        private boolean isFraudulent;

        public FraudResult(int orderId, boolean isFraudulent) {
            this.orderId = orderId;
            this.isFraudulent = isFraudulent;
        }

        public int getOrderId() {
            return orderId;
        }

        public boolean isFraudulent() {
            return isFraudulent;
        }
    }

    public static class Order {
        private int orderId;
        private int dealId;
        private String email;
        private String street;
        private String city;
        private String state;
        private String zipCode;
        private String creditCard;

        public Order(int orderId, int dealId, String email, String street, String city, String state, String zipCode, String creditCard) {
            this.orderId = orderId;
            this.dealId = dealId;
            this.email = email;
            this.street = street;
            this.city = city;
            this.state = state;
            this.zipCode = zipCode;
            this.creditCard = creditCard;
        }

        public int getOrderId() {
            return orderId;
        }

        public int getDealId() {
            return dealId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCity() {
            return city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getZipCode() {
            return zipCode;
        }

        public String getCreditCard() {
            return creditCard;
        }
    }
}
