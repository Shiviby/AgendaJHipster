import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const CustomMenu = () => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.custom.main')}
    id="custom-menu"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <MenuItem id="contacts-item" icon="th-list" to="/contacts" >
      <Translate contentKey="global.menu.custom.contacts">Contacts</Translate>
    </MenuItem>
  </NavDropdown>
);

export default CustomMenu;
