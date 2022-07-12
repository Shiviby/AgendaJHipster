package it.shinteck.agenda.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.shinteck.agenda.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmailContactDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailContactDTO.class);
        EmailContactDTO emailContactDTO1 = new EmailContactDTO();
        emailContactDTO1.setId(1L);
        EmailContactDTO emailContactDTO2 = new EmailContactDTO();
        assertThat(emailContactDTO1).isNotEqualTo(emailContactDTO2);
        emailContactDTO2.setId(emailContactDTO1.getId());
        assertThat(emailContactDTO1).isEqualTo(emailContactDTO2);
        emailContactDTO2.setId(2L);
        assertThat(emailContactDTO1).isNotEqualTo(emailContactDTO2);
        emailContactDTO1.setId(null);
        assertThat(emailContactDTO1).isNotEqualTo(emailContactDTO2);
    }
}
