package it.shinteck.agenda.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailContactMapperTest {

    private EmailContactMapper emailContactMapper;

    @BeforeEach
    public void setUp() {
        emailContactMapper = new EmailContactMapperImpl();
    }
}
