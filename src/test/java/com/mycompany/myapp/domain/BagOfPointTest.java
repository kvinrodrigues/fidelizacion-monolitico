package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BagOfPointTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BagOfPoint.class);
        BagOfPoint bagOfPoint1 = new BagOfPoint();
        bagOfPoint1.setId(1L);
        BagOfPoint bagOfPoint2 = new BagOfPoint();
        bagOfPoint2.setId(bagOfPoint1.getId());
        assertThat(bagOfPoint1).isEqualTo(bagOfPoint2);
        bagOfPoint2.setId(2L);
        assertThat(bagOfPoint1).isNotEqualTo(bagOfPoint2);
        bagOfPoint1.setId(null);
        assertThat(bagOfPoint1).isNotEqualTo(bagOfPoint2);
    }
}
