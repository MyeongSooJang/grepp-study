package com.grepp.jms.greppstudy.product.application.acl;

import java.util.UUID;

public interface SellerAcl {

    SellerIdentity loadActiveSeller(UUID sellerId);
}
