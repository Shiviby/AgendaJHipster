import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IContact } from 'app/shared/model/contact.model';
import { getEntities as getContacts } from 'app/entities/contact/contact.reducer';
import { IPhoneContact } from 'app/shared/model/phone-contact.model';
import { PhoneContactType } from 'app/shared/model/enumerations/phone-contact-type.model';
import { getEntity, updateEntity, createEntity, reset } from './phone-contact.reducer';

export const PhoneContactUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const contacts = useAppSelector(state => state.contact.entities);
  const phoneContactEntity = useAppSelector(state => state.phoneContact.entity);
  const loading = useAppSelector(state => state.phoneContact.loading);
  const updating = useAppSelector(state => state.phoneContact.updating);
  const updateSuccess = useAppSelector(state => state.phoneContact.updateSuccess);
  const phoneContactTypeValues = Object.keys(PhoneContactType);
  const handleClose = () => {
    props.history.push('/phone-contact');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getContacts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...phoneContactEntity,
      ...values,
      contact: contacts.find(it => it.id.toString() === values.contact.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          type: 'HOME',
          ...phoneContactEntity,
          contact: phoneContactEntity?.contact?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="agendaApp.phoneContact.home.createOrEditLabel" data-cy="PhoneContactCreateUpdateHeading">
            <Translate contentKey="agendaApp.phoneContact.home.createOrEditLabel">Create or edit a PhoneContact</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="phone-contact-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('agendaApp.phoneContact.type')}
                id="phone-contact-type"
                name="type"
                data-cy="type"
                type="select"
              >
                {phoneContactTypeValues.map(phoneContactType => (
                  <option value={phoneContactType} key={phoneContactType}>
                    {translate('agendaApp.PhoneContactType.' + phoneContactType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('agendaApp.phoneContact.phone')}
                id="phone-contact-phone"
                name="phone"
                data-cy="phone"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="phone-contact-contact"
                name="contact"
                data-cy="contact"
                label={translate('agendaApp.phoneContact.contact')}
                type="select"
                required
              >
                <option value="" key="0" />
                {contacts
                  ? contacts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/phone-contact" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PhoneContactUpdate;
