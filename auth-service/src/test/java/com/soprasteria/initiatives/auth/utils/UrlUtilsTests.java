package com.soprasteria.initiatives.auth.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * URL utilities testing
 *
 * @author jntakpe
 */
public class UrlUtilsTests {

    @Test
    public void getServerAddressFromRequest_shouldGetAddressFromLocalhost() {
        assertThat(UrlUtils.getServerAdressFromRequest("http://localhost:9081/api/tokens")).isEqualTo("http://localhost:9081");
    }

    @Test
    public void getServerAddressFromRequest_shouldGetAddressFromServer() {
        assertThat(UrlUtils.getServerAdressFromRequest("http://my-server/api/tokens")).isEqualTo("http://my-server");
    }

}
