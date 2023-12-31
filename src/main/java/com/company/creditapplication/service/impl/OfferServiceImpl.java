package com.company.creditapplication.service.impl;

import com.company.creditapplication.entity.Offer;
import com.company.creditapplication.entity.PaymentShedule;
import io.jmix.core.DataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.aspectj.runtime.internal.Conversions.doubleValue;

@Service
public class OfferServiceImpl {
    @Autowired
    protected DataManager dataManager;
    @PersistenceContext
    protected EntityManager entityManager;

    @Transactional
    public Offer saveOfferEntityManager(Offer offer) {
        return entityManager.merge(offer);

    }

    public List<PaymentShedule> generatePaymentList(Offer offer) {
        double p = offer.getPercent() / 12;

        double amount = doubleValue(offer.getAmount());
        long monthlyPayment = (long) Math.round(amount * (p + (p / (Math.pow((1 + p), offer.getNumberMonths()) - 1))));

        long summary = (long) doubleValue(offer.getAmount());
        List<PaymentShedule> paymentList = new ArrayList<>();

        for (int i = 0; i < offer.getNumberMonths(); i++) {
            long interestAmount = (long) Math.floor(summary * p);
            long principalAmount = monthlyPayment - interestAmount;
            summary -= principalAmount;
            PaymentShedule paymentShedule = dataManager.create(PaymentShedule.class);
            paymentShedule.setId(UUID.randomUUID());
            paymentShedule.setPaymantDate(LocalDate.now().plusMonths(i));
            paymentShedule.setPercent(BigDecimal.valueOf(principalAmount));
            paymentShedule.setLoanBody(BigDecimal.valueOf(interestAmount));
            paymentShedule.setOffer(offer);
            paymentList.add(paymentShedule);
        }
        return paymentList;
    }
}