import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Deal from './deal';
import DealDetail from './deal-detail';
import DealUpdate from './deal-update';
import DealDeleteDialog from './deal-delete-dialog';

const DealRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Deal />} />
    <Route path="new" element={<DealUpdate />} />
    <Route path=":id">
      <Route index element={<DealDetail />} />
      <Route path="edit" element={<DealUpdate />} />
      <Route path="delete" element={<DealDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DealRoutes;
