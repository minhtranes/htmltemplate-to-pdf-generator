package vn.ifa.study.infi.controller;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import vn.ifa.study.infi.model.Invoice;
import vn.ifa.study.infi.model.Invoice.Address;
import vn.ifa.study.infi.model.Invoice.CompanyDetail;
import vn.ifa.study.infi.model.Invoice.GuestDetail;
import vn.ifa.study.infi.model.Invoice.InvoiceItem;

@Controller
public class InvoiceController {

    @GetMapping("invoice")
    public String invoice(final Model model) {

        Invoice invoice = Invoice.builder()
                .id("12345678")
                .date(LocalDate.now())
                .due(LocalDate.now()
                        .plusDays(5))
                .taxRate(0.2)
                .from(CompanyDetail.builder()
                        .name("Ben Tre Garden Farmstay")
                        .email("bentregardenfarmstay@gmail.com")
                        .website("https://bentregardenfarmstay.wordpress.com")
                        .logoFile("https://cdn-icons-png.flaticon.com/128/7892/7892754.png")
                        .address(Address.builder()
                                .line1("Group 10, Phuoc Xuan Hamlet, An Khanh Commune")
                                .state("Ben Tre")
                                .city("Chau Thanh")
                                .country("Vietnam")
                                .zipCode("70000")
                                .build())
                        .build())
                .to(GuestDetail.builder()
                        .name("minh tran")
                        .build())
                .items(Arrays.asList(InvoiceItem.builder()
                        .description("Bungalow")
                        .quantity(1)
                        .unitPrice(499000l)
                        .build()))
                .build();

        model.addAttribute("invoice", invoice);
        return "invoice";
    }
}
