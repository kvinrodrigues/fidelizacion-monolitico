package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NacionalityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Nacionality.class);
        Nacionality nacionality1 = new Nacionality();
        nacionality1.setId(1L);
        Nacionality nacionality2 = new Nacionality();
        nacionality2.setId(nacionality1.getId());
        assertThat(nacionality1).isEqualTo(nacionality2);
        nacionality2.setId(2L);
        assertThat(nacionality1).isNotEqualTo(nacionality2);
        nacionality1.setId(null);
        assertThat(nacionality1).isNotEqualTo(nacionality2);
    }
}
