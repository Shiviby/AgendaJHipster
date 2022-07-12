import contact from 'app/entities/contact/contact.reducer';
import phoneContact from 'app/entities/phone-contact/phone-contact.reducer';
import emailContact from 'app/entities/email-contact/email-contact.reducer';
import appointment from 'app/entities/appointment/appointment.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  contact,
  phoneContact,
  emailContact,
  appointment,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
