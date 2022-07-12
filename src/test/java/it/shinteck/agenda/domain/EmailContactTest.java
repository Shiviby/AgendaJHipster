package it.shinteck.agenda.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.shinteck.agenda.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmailContactTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailContact.class);
        EmailContact emailContact1 = new EmailContact();
        emailContact1.setId(1L);
        EmailContact emailContact2 = new EmailContact();
        emailContact2.setId(emailContact1.getId());
        assertThat(emailContact1).isEqualTo(emailContact2);
        emailContact2.setId(2L);
        assertThat(emailContact1).isNotEqualTo(emailContact2);
        emailContact1.setId(null);
        assertThat(emailContact1).isNotEqualTo(emailContact2);
    }
}
