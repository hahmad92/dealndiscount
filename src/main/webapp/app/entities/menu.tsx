import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/bank">
        <Translate contentKey="global.menu.entities.bank" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/card">
        <Translate contentKey="global.menu.entities.card" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/store">
        <Translate contentKey="global.menu.entities.store" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/deal">
        <Translate contentKey="global.menu.entities.deal" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/city">
        <Translate contentKey="global.menu.entities.city" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/favorite">
        <Translate contentKey="global.menu.entities.favorite" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/extended-user">
        <Translate contentKey="global.menu.entities.extendedUser" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
