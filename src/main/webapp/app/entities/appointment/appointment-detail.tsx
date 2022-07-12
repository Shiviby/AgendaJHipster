import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './appointment.reducer';

export const AppointmentDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const appointmentEntity = useAppSelector(state => state.appointment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="appointmentDetailsHeading">
          <Translate contentKey="agendaApp.appointment.detail.title">Appointment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{appointmentEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="agendaApp.appointment.title">Title</Translate>
            </span>
          </dt>
          <dd>{appointmentEntity.title}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="agendaApp.appointment.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {appointmentEntity.startDate ? <TextFormat value={appointmentEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="agendaApp.appointment.contact">Contact</Translate>
          </dt>
          <dd>
            {appointmentEntity.contacts
              ? appointmentEntity.contacts.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.title}</a>
                    {appointmentEntity.contacts && i === appointmentEntity.contacts.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/appointment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/appointment/${appointmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AppointmentDetail;
