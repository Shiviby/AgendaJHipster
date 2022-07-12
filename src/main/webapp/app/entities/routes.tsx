import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Contact from './contact';
import PhoneContact from './phone-contact';
import EmailContact from './email-contact';
import Appointment from './appointment';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}contact`} component={Contact} />
        <ErrorBoundaryRoute path={`${match.url}phone-contact`} component={PhoneContact} />
        <ErrorBoundaryRoute path={`${match.url}email-contact`} component={EmailContact} />
        <ErrorBoundaryRoute path={`${match.url}appointment`} component={Appointment} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
