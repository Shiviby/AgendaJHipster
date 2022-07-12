import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import EmailContact from './email-contact';
import EmailContactDetail from './email-contact-detail';
import EmailContactUpdate from './email-contact-update';
import EmailContactDeleteDialog from './email-contact-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EmailContactUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EmailContactUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EmailContactDetail} />
      <ErrorBoundaryRoute path={match.url} component={EmailContact} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={EmailContactDeleteDialog} />
  </>
);

export default Routes;
