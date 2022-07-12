package it.shinteck.agenda.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.shinteck.agenda.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PhoneContactDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PhoneContactDTO.class);
        PhoneContactDTO phoneContactDTO1 = new PhoneContactDTO();
        phoneContactDTO1.setId(1L);
        PhoneContactDTO phoneContactDTO2 = new PhoneContactDTO();
        assertThat(phoneContactDTO1).isNotEqualTo(phoneContactDTO2);
        phoneContactDTO2.setId(phoneContactDTO1.getId());
        assertThat(phoneContactDTO1).isEqualTo(phoneContactDTO2);
        phoneContactDTO2.setId(2L);
        assertThat(phoneContactDTO1).isNotEqualTo(phoneContactDTO2);
        phoneContactDTO1.setId(null);
        assertThat(phoneContactDTO1).isNotEqualTo(phoneContactDTO2);
    }
}
