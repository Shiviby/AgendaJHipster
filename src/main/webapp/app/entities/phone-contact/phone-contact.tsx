import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPhoneContact } from 'app/shared/model/phone-contact.model';
import { getEntities } from './phone-contact.reducer';

export const PhoneContact = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const phoneContactList = useAppSelector(state => state.phoneContact.entities);
  const loading = useAppSelector(state => state.phoneContact.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="phone-contact-heading" data-cy="PhoneContactHeading">
        <Translate contentKey="agendaApp.phoneContact.home.title">Phone Contacts</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="agendaApp.phoneContact.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/phone-contact/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="agendaApp.phoneContact.home.createLabel">Create new Phone Contact</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {phoneContactList && phoneContactList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="agendaApp.phoneContact.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="agendaApp.phoneContact.type">Type</Translate>
                </th>
                <th>
                  <Translate contentKey="agendaApp.phoneContact.phone">Phone</Translate>
                </th>
                <th>
                  <Translate contentKey="agendaApp.phoneContact.contact">Contact</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {phoneContactList.map((phoneContact, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/phone-contact/${phoneContact.id}`} color="link" size="sm">
                      {phoneContact.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`agendaApp.PhoneContactType.${phoneContact.type}`} />
                  </td>
                  <td>{phoneContact.phone}</td>
                  <td>
                    {phoneContact.contact ? <Link to={`/contact/${phoneContact.contact.id}`}>{phoneContact.contact.title}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/phone-contact/${phoneContact.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/phone-contact/${phoneContact.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/phone-contact/${phoneContact.id}/delete`}
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
              <Translate contentKey="agendaApp.phoneContact.home.notFound">No Phone Contacts found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default PhoneContact;
