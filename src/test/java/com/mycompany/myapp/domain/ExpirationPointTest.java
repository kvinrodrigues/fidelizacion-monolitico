package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExpirationPointTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpirationPoint.class);
        ExpirationPoint expirationPoint1 = new ExpirationPoint();
        expirationPoint1.setId(1L);
        ExpirationPoint expirationPoint2 = new ExpirationPoint();
        expirationPoint2.setId(expirationPoint1.getId());
        assertThat(expirationPoint1).isEqualTo(expirationPoint2);
        expirationPoint2.setId(2L);
        assertThat(expirationPoint1).isNotEqualTo(expirationPoint2);
        expirationPoint1.setId(null);
        assertThat(expirationPoint1).isNotEqualTo(expirationPoint2);
    }
}
