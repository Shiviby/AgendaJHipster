package it.shinteck.agenda.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.shinteck.agenda.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PhoneContactTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PhoneContact.class);
        PhoneContact phoneContact1 = new PhoneContact();
        phoneContact1.setId(1L);
        PhoneContact phoneContact2 = new PhoneContact();
        phoneContact2.setId(phoneContact1.getId());
        assertThat(phoneContact1).isEqualTo(phoneContact2);
        phoneContact2.setId(2L);
        assertThat(phoneContact1).isNotEqualTo(phoneContact2);
        phoneContact1.setId(null);
        assertThat(phoneContact1).isNotEqualTo(phoneContact2);
    }
}
