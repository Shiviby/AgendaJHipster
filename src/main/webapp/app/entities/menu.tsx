import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/contact">
        <Translate contentKey="global.menu.entities.contact" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/phone-contact">
        <Translate contentKey="global.menu.entities.phoneContact" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/email-contact">
        <Translate contentKey="global.menu.entities.emailContact" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/appointment">
        <Translate contentKey="global.menu.entities.appointment" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
