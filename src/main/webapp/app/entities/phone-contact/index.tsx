import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PhoneContact from './phone-contact';
import PhoneContactDetail from './phone-contact-detail';
import PhoneContactUpdate from './phone-contact-update';
import PhoneContactDeleteDialog from './phone-contact-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PhoneContactUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PhoneContactUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PhoneContactDetail} />
      <ErrorBoundaryRoute path={match.url} component={PhoneContact} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PhoneContactDeleteDialog} />
  </>
);

export default Routes;
