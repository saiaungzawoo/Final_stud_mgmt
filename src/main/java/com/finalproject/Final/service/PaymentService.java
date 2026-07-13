package com.finalproject.Final.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.Final.dto.PaymentDTO;
import com.finalproject.Final.model.EnrollmentBean;
import com.finalproject.Final.model.InstallmentPlanBean;
import com.finalproject.Final.model.InstallmentRuleItemBean;
import com.finalproject.Final.model.PaymentBean;
import com.finalproject.Final.model.PaymentTypeBean;
import com.finalproject.Final.repository.EnrollmentRepository;
import com.finalproject.Final.repository.PaymentRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private InstallmentPlanService installmentPlanService;

    @Autowired
    private PaymentTypeService paymentTypeService;

    @Autowired
    private InstallmentRuleItemService installmentRuleItemService;

    public String processPayment(PaymentDTO dto) {

        // Save payment type into enrollment
        enrollmentRepository.updatePaymentType(
                dto.getEnrollmentId(),
                dto.getPaymentTypeId()
        );
        
        if(dto.getInstallmentRuleId() != null) {

            enrollmentRepository.updateInstallmentRule(
                    dto.getEnrollmentId(),
                    dto.getInstallmentRuleId()
            );

        }
        

        PaymentTypeBean paymentType =
                paymentTypeService.getById(
                        dto.getPaymentTypeId()
                );

        // ===========================
        // INSTALLMENT PAYMENT
        // ===========================
        if (paymentType != null &&
                "INSTALLMENT".equals(paymentType.getName())) {

            EnrollmentBean enrollment =
                    enrollmentRepository.findById(
                            dto.getEnrollmentId()
                    );

            // Create installment plan only once
            if (installmentPlanService
                    .getByEnrollmentId(dto.getEnrollmentId())
                    .isEmpty()) {

                for (InstallmentRuleItemBean item :
                        installmentRuleItemService.getByRuleId(
                                enrollment.getInstallmentRuleId())) {

                    installmentPlanService.createPlan(
                            dto.getEnrollmentId(),
                            item
                    );
                }
            }

            InstallmentPlanBean plan =
                    installmentPlanService.getFirstPending(
                            dto.getEnrollmentId()
                    );

            if (plan == null) {
                throw new RuntimeException(
                        "All installments have already been paid."
                );
            }

            String paymentId =
                    paymentRepository.savePayment(
                            dto.getEnrollmentId(),
                            plan.getInstallmentPlanId(),
                            dto.getPaymentMethodId(),
                            plan.getAmountDue()
                    );

            installmentPlanService.markPaid(
                    plan.getInstallmentPlanId()
            );

            if (installmentPlanService
                    .getFirstPending(dto.getEnrollmentId()) == null) {

                enrollmentRepository.updatePaymentStatus(
                        dto.getEnrollmentId()
                );

            } else {

                enrollmentRepository.updatePartialPaymentStatus(
                        dto.getEnrollmentId()
                );
            }

            return paymentId;
        }

        // ===========================
        // FULL PAYMENT
        // ===========================

        String paymentId =
                paymentRepository.savePayment(
                        dto.getEnrollmentId(),
                        dto.getPaymentMethodId(),
                        dto.getAmount()
                );

        enrollmentRepository.updatePaymentStatus(
                dto.getEnrollmentId()
        );

        return paymentId;
    }

    public PaymentBean getById(String paymentId) {

        return paymentRepository.getById(paymentId);

    }

}