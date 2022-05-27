package ro.fasttrackit.paymentservice.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.fasttrackit.paymentservice.model.Payment;
import ro.fasttrackit.paymentservice.repository.PaymentRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class PaymentService {

    private PaymentRepository paymentRepository;

    public Payment createPayment(Payment payment) {
        paymentRepository.save(payment);
        return payment;
    }

    public List<Payment> getPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPayment(String id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public Payment deletePayment(String id) {
        return paymentRepository.findById(id)
                .map(payment -> {
                    paymentRepository.deleteById(id);
                    return payment;
                }).orElseThrow(() -> new IllegalArgumentException("Payment not found"));
    }

    public Payment updatePayment(String id, Payment payment) {
        return paymentRepository.findById(id).map(paymentToUpdate -> {
            paymentToUpdate.setCardNumber(payment.getCardNumber());
            paymentToUpdate.setTransactionId(payment.getTransactionId());
            return paymentRepository.save(paymentToUpdate);
        }).orElseThrow(() -> new IllegalArgumentException("Payment not found"));
    }
}
