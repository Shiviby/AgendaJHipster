import './contacts.scss';

import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ContactList from './contact-list';
import ContactDetail from './contact-detail';

export default ({ match }) => {
  return (
    <div className='contacts'>
      <Switch>
        <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ContactDetail} />
        <ErrorBoundaryRoute path={`${match.url}`} component={ContactList} />
      </Switch>
    </div>
  );
};