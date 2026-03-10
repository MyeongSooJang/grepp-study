package com.grepp.jms.greppstudy.product.infrastructure.acl;

import com.grepp.jms.greppstudy.product.application.acl.SellerAcl;
import com.grepp.jms.greppstudy.product.application.acl.SellerIdentity;
import com.grepp.jms.greppstudy.product.application.exception.InactiveSellerException;
import com.grepp.jms.greppstudy.product.application.exception.SellerNotFoundException;
import com.grepp.jms.greppstudy.product.infrastructure.acl.client.ExternalSellerClient;
import com.grepp.jms.greppstudy.product.infrastructure.acl.client.ExternalSellerPayload;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SellerAclAdapter implements SellerAcl {

    private static final String ACTIVE_STATUS = "ACTIVE";

    private final ExternalSellerClient externalSellerClient;

    @Override
    public SellerIdentity loadActiveSeller(UUID sellerId) {
        ExternalSellerPayload payload = externalSellerClient.findSeller(sellerId)
                .orElseThrow(() -> new SellerNotFoundException(sellerId));

        if (!ACTIVE_STATUS.equalsIgnoreCase(payload.sellerStatusCode())) {
            throw new InactiveSellerException(sellerId);
        }

        return new SellerIdentity(parseSellerId(payload.sellerNo()));
    }

    private UUID parseSellerId(String sellerNo) {
        try {
            return UUID.fromString(sellerNo);
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException("Invalid seller id from external system. sellerNo=" + sellerNo,
                    ex);
        }
    }
}
