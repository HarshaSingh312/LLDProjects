package org.example.model;

import java.util.List;

public class Seller {
    private String id;

    public List<String> getPincodes() {
        return pincodes;
    }

    public List<String> getPaymentModes() {
        return paymentModes;
    }


    private List<String> pincodes;
    private List<String> paymentModes;

    public Seller(String id, List<String> pincodes, List<String> paymentModes) {
        this.id = id;
        this.pincodes = pincodes;
        this.paymentModes = paymentModes;
    }
}
