import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ExtendedUser from './extended-user';
import ExtendedUserDetail from './extended-user-detail';
import ExtendedUserUpdate from './extended-user-update';
import ExtendedUserDeleteDialog from './extended-user-delete-dialog';

const ExtendedUserRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ExtendedUser />} />
    <Route path="new" element={<ExtendedUserUpdate />} />
    <Route path=":id">
      <Route index element={<ExtendedUserDetail />} />
      <Route path="edit" element={<ExtendedUserUpdate />} />
      <Route path="delete" element={<ExtendedUserDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ExtendedUserRoutes;
