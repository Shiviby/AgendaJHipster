package it.shinteck.agenda.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PhoneContactMapperTest {

    private PhoneContactMapper phoneContactMapper;

    @BeforeEach
    public void setUp() {
        phoneContactMapper = new PhoneContactMapperImpl();
    }
}
