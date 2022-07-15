import React, { useEffect, useState } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';


import { IContact } from 'app/shared/model/contact.model';
import axios from 'axios';
import { IPhoneContact } from 'app/shared/model/phone-contact.model';
import { IEmailContact } from 'app/shared/model/email-contact.model';
import { IAppointment } from 'app/shared/model/appointment.model';
import { convertDateTimeFromServerReadable } from 'app/shared/util/date-utils';

export const ContactDetail = (props: RouteComponentProps<{ id: string }>) => {
  const [isEdit, setIsEdit] = useState(false);

  const [contactEntity, setContactEntity] = useState({} as IContact);
  const [contactPhones, setContactPhones] = useState([] as IPhoneContact[]);
  const [contactEmails, setContactEmails] = useState([] as IEmailContact[]);
  const [contactAppointments, setContactAppointments] = useState([] as IAppointment[]);

  // eslint-disable-next-line no-console
  console.log("contactAppointments", contactAppointments);
  const reloadEntity = () => {
    if (props?.location?.search) {
      const searchParams = new URLSearchParams(props.location.search);
      const editValue = searchParams.get("edit");
      if (editValue) {
        setIsEdit("true" === editValue.toLowerCase());
      }
    }
    axios.get<IContact>(`api/contacts/${props.match.params.id}`).then(data => {
      setContactEntity(data?.data);
    });
    axios.get<IPhoneContact[]>(`api/phone-contacts?contactId.equals=${props.match.params.id}`).then(data => {
      setContactPhones(data?.data);
    });
    axios.get<IEmailContact[]>(`api/email-contacts?contactId.equals=${props.match.params.id}`).then(data => {
      setContactEmails(data?.data);
    });
    axios.get<IAppointment[]>(`api/appointments?contactId.equals=${props.match.params.id}`).then(data => {
      setContactAppointments(data?.data);
    });
  }

  useEffect(() => {
    reloadEntity();
  }, []);

  return (
    <div className='contact-detail'>
      <Button className="back-button" tag={Link} to="/contacts">
        <FontAwesomeIcon icon="arrow-left" />{' '}
      </Button>
      <div className='contact-personal-detail'>
        <div className='avatar'>
          {contactEntity.avatar ? (
            <div>
              {contactEntity.avatarContentType ? (
                <img src={`data:${contactEntity.avatarContentType};base64,${contactEntity.avatar}`} />
              ) : null}
            </div>
          ) : null}
        </div>
        <div className='description'>{contactEntity.title}</div>
      </div>
      {contactPhones?.map((phone, index) =>
        <div className='card' key={`phone-${index}`}>
          <div className='contact-personal-detail contact-item'>
            <FontAwesomeIcon icon="phone" />
            <div className='description'>{phone.phone}</div>
            {/* <FontAwesomeIcon icon="pencil-alt" /> */}
          </div>
        </div>
      )}
      {contactEmails?.map((email, index) =>
        <div className='card' key={`email-${index}`}>
          <div className='contact-personal-detail contact-item'>
            <FontAwesomeIcon icon="envelope" />
            <div className='description'>{email.email}</div>
          </div>
        </div>
      )}

      {contactAppointments?.map((appointment, index) =>
        <div className='card' key={`appointment-${index}`}>
          <div className='contact-personal-detail contact-item'>
            <FontAwesomeIcon icon="bell" />
            <div className='description'>{convertDateTimeFromServerReadable(appointment.startDate)}</div>
          </div>
          <div className='contact-personal-detail contact-item'>
            <FontAwesomeIcon icon="book" />
            <div className='description'>{appointment.title}</div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ContactDetail;
