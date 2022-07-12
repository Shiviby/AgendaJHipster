import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './email-contact.reducer';

export const EmailContactDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const emailContactEntity = useAppSelector(state => state.emailContact.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="emailContactDetailsHeading">
          <Translate contentKey="agendaApp.emailContact.detail.title">EmailContact</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{emailContactEntity.id}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="agendaApp.emailContact.type">Type</Translate>
            </span>
          </dt>
          <dd>{emailContactEntity.type}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="agendaApp.emailContact.email">Email</Translate>
            </span>
          </dt>
          <dd>{emailContactEntity.email}</dd>
          <dt>
            <Translate contentKey="agendaApp.emailContact.contact">Contact</Translate>
          </dt>
          <dd>{emailContactEntity.contact ? emailContactEntity.contact.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/email-contact" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/email-contact/${emailContactEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EmailContactDetail;
