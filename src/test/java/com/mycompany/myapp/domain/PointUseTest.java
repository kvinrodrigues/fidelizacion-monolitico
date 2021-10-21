package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PointUseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PointUse.class);
        PointUse pointUse1 = new PointUse();
        pointUse1.setId(1L);
        PointUse pointUse2 = new PointUse();
        pointUse2.setId(pointUse1.getId());
        assertThat(pointUse1).isEqualTo(pointUse2);
        pointUse2.setId(2L);
        assertThat(pointUse1).isNotEqualTo(pointUse2);
        pointUse1.setId(null);
        assertThat(pointUse1).isNotEqualTo(pointUse2);
    }
}
