package ro.fasttrackit.paymentservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ro.fasttrackit.paymentservice.model.Payment;
public interface PaymentRepository extends MongoRepository<Payment, String> {
}
