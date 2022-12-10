package vn.ifa.study.infi.model;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
public class Invoice {
    @Setter
    @Getter
    @Builder
    @ToString
    public static class Address {
        private String line1;
        private String line2;
        private String city;
        private String state;
        private String country;
        private String zipCode;
    }

    @Setter
    @Getter
    @Builder
    @ToString
    public static class CompanyDetail {
        private String logoFile;
        private String website;
        private String name;
        private String email;
        private Address address;
    }

    @Setter
    @Getter
    @Builder
    @ToString
    public static class GuestDetail {
        private Address address;
        private String email;
        private String name;
    }

    @Setter
    @Getter
    @Builder
    @ToString
    public static class InvoiceItem {
        private String itemName;
        private int quantity;
        private float unitPrice;
        private String description;

        public float getTotalPrice() {

            return quantity * unitPrice;
        }
    }

    private String id;
    private LocalDate date;
    private LocalDate due;
    private double taxRate;
    private String notice;

    private List<InvoiceItem> items;
    private CompanyDetail from;
    private GuestDetail to;

    public double getSubTotal() {

        return items.stream()
                .mapToDouble(InvoiceItem::getTotalPrice)
                .sum();
    }

    public double getTaxAmount() {

        return taxRate * getSubTotal();
    }

    public double getTotal() {

        double subTotal = getSubTotal();
        return subTotal *= (1 + taxRate);
    }

}
