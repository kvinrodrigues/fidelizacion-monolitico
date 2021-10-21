package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PointUsageConceptTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PointUsageConcept.class);
        PointUsageConcept pointUsageConcept1 = new PointUsageConcept();
        pointUsageConcept1.setId(1L);
        PointUsageConcept pointUsageConcept2 = new PointUsageConcept();
        pointUsageConcept2.setId(pointUsageConcept1.getId());
        assertThat(pointUsageConcept1).isEqualTo(pointUsageConcept2);
        pointUsageConcept2.setId(2L);
        assertThat(pointUsageConcept1).isNotEqualTo(pointUsageConcept2);
        pointUsageConcept1.setId(null);
        assertThat(pointUsageConcept1).isNotEqualTo(pointUsageConcept2);
    }
}
