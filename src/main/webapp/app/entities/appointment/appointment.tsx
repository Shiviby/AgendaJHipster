import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAppointment } from 'app/shared/model/appointment.model';
import { getEntities } from './appointment.reducer';

export const Appointment = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const appointmentList = useAppSelector(state => state.appointment.entities);
  const loading = useAppSelector(state => state.appointment.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="appointment-heading" data-cy="AppointmentHeading">
        <Translate contentKey="agendaApp.appointment.home.title">Appointments</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="agendaApp.appointment.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/appointment/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="agendaApp.appointment.home.createLabel">Create new Appointment</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {appointmentList && appointmentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="agendaApp.appointment.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="agendaApp.appointment.title">Title</Translate>
                </th>
                <th>
                  <Translate contentKey="agendaApp.appointment.startDate">Start Date</Translate>
                </th>
                <th>
                  <Translate contentKey="agendaApp.appointment.contact">Contact</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {appointmentList.map((appointment, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/appointment/${appointment.id}`} color="link" size="sm">
                      {appointment.id}
                    </Button>
                  </td>
                  <td>{appointment.title}</td>
                  <td>
                    {appointment.startDate ? <TextFormat type="date" value={appointment.startDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {appointment.contacts
                      ? appointment.contacts.map((val, j) => (
                          <span key={j}>
                            <Link to={`/contact/${val.id}`}>{val.title}</Link>
                            {j === appointment.contacts.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/appointment/${appointment.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/appointment/${appointment.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/appointment/${appointment.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="agendaApp.appointment.home.notFound">No Appointments found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Appointment;
