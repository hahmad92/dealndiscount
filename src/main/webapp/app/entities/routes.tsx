import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Bank from './bank';
import Card from './card';
import Store from './store';
import Deal from './deal';
import City from './city';
import Favorite from './favorite';
import ExtendedUser from './extended-user';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="bank/*" element={<Bank />} />
        <Route path="card/*" element={<Card />} />
        <Route path="store/*" element={<Store />} />
        <Route path="deal/*" element={<Deal />} />
        <Route path="city/*" element={<City />} />
        <Route path="favorite/*" element={<Favorite />} />
        <Route path="extended-user/*" element={<ExtendedUser />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
