import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEmailContact } from 'app/shared/model/email-contact.model';
import { getEntities } from './email-contact.reducer';

export const EmailContact = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const emailContactList = useAppSelector(state => state.emailContact.entities);
  const loading = useAppSelector(state => state.emailContact.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="email-contact-heading" data-cy="EmailContactHeading">
        <Translate contentKey="agendaApp.emailContact.home.title">Email Contacts</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="agendaApp.emailContact.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/email-contact/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="agendaApp.emailContact.home.createLabel">Create new Email Contact</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {emailContactList && emailContactList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="agendaApp.emailContact.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="agendaApp.emailContact.type">Type</Translate>
                </th>
                <th>
                  <Translate contentKey="agendaApp.emailContact.email">Email</Translate>
                </th>
                <th>
                  <Translate contentKey="agendaApp.emailContact.contact">Contact</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {emailContactList.map((emailContact, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/email-contact/${emailContact.id}`} color="link" size="sm">
                      {emailContact.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`agendaApp.EmailContactType.${emailContact.type}`} />
                  </td>
                  <td>{emailContact.email}</td>
                  <td>
                    {emailContact.contact ? <Link to={`/contact/${emailContact.contact.id}`}>{emailContact.contact.title}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/email-contact/${emailContact.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/email-contact/${emailContact.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/email-contact/${emailContact.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
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
              <Translate contentKey="agendaApp.emailContact.home.notFound">No Email Contacts found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default EmailContact;
