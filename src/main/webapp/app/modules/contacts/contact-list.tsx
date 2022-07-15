import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IContact } from 'app/shared/model/contact.model';
import axios from 'axios';

export const ContactList = (props: RouteComponentProps<{ url: string }>) => {
  const [contactList, setContactList] = useState([]);
  const [searchString, setSearchString] = useState(null);
  const [loading, setLoading] = useState(true);

  const reloadEntities = () => {
    setLoading(true);
    axios.get<IContact[]>(searchString ? `api/contacts?title.contains=${searchString}` : 'api/contacts').then(data => {
      setContactList(data?.data);
      setLoading(false);
    });
  }

  useEffect(() => {
    reloadEntities();
  }, []);

  const handleSyncList = () => {
    reloadEntities();
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    reloadEntities();
  }

  return (
    <div className='contact-list'>
      <form onSubmit={handleSubmit}>
        <input className='search-field' placeholder='Cerca...' type="text" value={searchString} onChange={(e) => setSearchString(e.target.value)} />
      </form>

      <div className='list-container'>
        {contactList && contactList.length > 0 ? (
          contactList.map((contact, i) => (
            <Link className='entity-cell' key={`entity-${i}`} to={`/contacts/${contact.id}`}>
              <div className='avatar'>
                {contact.avatar ? (
                  <div>
                    {contact.avatarContentType ? (
                      <img src={`data:${contact.avatarContentType};base64,${contact.avatar}`} />
                    ) : null}
                  </div>
                ) : null}
              </div>
              <div className='description'>{contact.title}</div>
            </Link>
          ))
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="agendaApp.contact.home.notFound">No Contacts found</Translate>
            </div>
          )
        )}
        <Link to="/contacts/new" className="btn add-button" id="jh-create-entity" data-cy="entityCreateButton">
          <FontAwesomeIcon icon="plus" />
        </Link>
      </div>
    </div>
  );
};

export default ContactList;
