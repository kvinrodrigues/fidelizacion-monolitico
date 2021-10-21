package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PointUseDetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PointUseDet.class);
        PointUseDet pointUseDet1 = new PointUseDet();
        pointUseDet1.setId(1L);
        PointUseDet pointUseDet2 = new PointUseDet();
        pointUseDet2.setId(pointUseDet1.getId());
        assertThat(pointUseDet1).isEqualTo(pointUseDet2);
        pointUseDet2.setId(2L);
        assertThat(pointUseDet1).isNotEqualTo(pointUseDet2);
        pointUseDet1.setId(null);
        assertThat(pointUseDet1).isNotEqualTo(pointUseDet2);
    }
}
