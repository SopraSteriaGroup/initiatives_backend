package com.soprasteria.initiatives.commons.api.clients;

/**
 * URLs for microservices
 *
 * @author jntakpe
 */
final class ServicesUrls {

    static final String ACCOUNT_VIEWING_SERVICE = "account-viewing-service";

    static final String ACCOUNT_VIEWING_SERVICE_TEST_URL = "${account-viewing.test.url:}";

    //TODO cba : think about naming
    static final String CONNECTOR_CORE_BANKING_A = "connector-core-banking-a";

    //TODO cba : think about naming
    static final String CONNECTOR_CORE_BANKING_A_TEST_URL = "${connector-core-banking-a.test.url:http://localhost:9080/connector-core-banking-a}";

    //TODO cba : think about naming
    static final String CORE_BANKING_A = "core-banking-a";

    //TODO cba : think about naming
    static final String CORE_BANKING_A_TEST_URL = "${core-banking-a.test.url:http://localhost:9080/core-banking-a}";
}
