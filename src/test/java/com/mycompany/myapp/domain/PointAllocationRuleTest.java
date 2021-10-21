package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PointAllocationRuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PointAllocationRule.class);
        PointAllocationRule pointAllocationRule1 = new PointAllocationRule();
        pointAllocationRule1.setId(1L);
        PointAllocationRule pointAllocationRule2 = new PointAllocationRule();
        pointAllocationRule2.setId(pointAllocationRule1.getId());
        assertThat(pointAllocationRule1).isEqualTo(pointAllocationRule2);
        pointAllocationRule2.setId(2L);
        assertThat(pointAllocationRule1).isNotEqualTo(pointAllocationRule2);
        pointAllocationRule1.setId(null);
        assertThat(pointAllocationRule1).isNotEqualTo(pointAllocationRule2);
    }
}
