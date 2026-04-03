package com.nguyenquyen.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VNPayQueryResponse {

    @JsonProperty("vnp_ResponseId")
    private String responseId;

    @JsonProperty("vnp_Command")
    private String command;

    @JsonProperty("vnp_ResponseCode")
    private String rspCode; // Jackson sẽ tự động gán giá trị của vnp_ResponseCode vào biến này

    @JsonProperty("vnp_Message")
    private String message;

    @JsonProperty("vnp_TmnCode")
    private String tmnCode;

    @JsonProperty("vnp_TxnRef")
    private String txnRef;

    @JsonProperty("vnp_Amount")
    private String amount;

    @JsonProperty("vnp_BankCode")
    private String bankCode;

    @JsonProperty("vnp_PayDate")
    private String payDate;

    @JsonProperty("vnp_TransactionNo")
    private String transactionNo;

    @JsonProperty("vnp_TransactionType")
    private String transactionType;

    @JsonProperty("vnp_TransactionStatus")
    private String transactionStatus;

    @JsonProperty("vnp_SecureHash")
    private String secureHash;
}