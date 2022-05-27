package ro.fasttrackit.paymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Payment {
    @MongoId
    private String id;
    private String cardNumber;
    private Integer transactionId;
}
