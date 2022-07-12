import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './phone-contact.reducer';

export const PhoneContactDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const phoneContactEntity = useAppSelector(state => state.phoneContact.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="phoneContactDetailsHeading">
          <Translate contentKey="agendaApp.phoneContact.detail.title">PhoneContact</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{phoneContactEntity.id}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="agendaApp.phoneContact.type">Type</Translate>
            </span>
          </dt>
          <dd>{phoneContactEntity.type}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="agendaApp.phoneContact.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{phoneContactEntity.phone}</dd>
          <dt>
            <Translate contentKey="agendaApp.phoneContact.contact">Contact</Translate>
          </dt>
          <dd>{phoneContactEntity.contact ? phoneContactEntity.contact.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/phone-contact" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/phone-contact/${phoneContactEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PhoneContactDetail;
